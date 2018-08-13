package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.ParticleAnimationApi;
import com.aim.coltonjgriswold.paapi.api.graphics.enums.PAButton;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAEntityInteractEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PALivingEntityInteractEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectCollideEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveRelativeEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectRotateEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectVelocityEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAPlayerClickEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAQuaternion;

@SerializableAs("PAObject")
public class PAObject implements Listener, ConfigurationSerializable {

    private Location a;
    private Set<PANode> b;
    private UUID c;
    private PAAction d;
    private Vector[] e;
    private double[] f;
    private boolean g;
    private PAQuaternion h;
    private static Map<UUID, PAObject> objects;

    static {
	objects = new HashMap<UUID, PAObject>();
    }

    /**
     * Contructor for deserializing
     * 
     * @param object
     *            The PAObject to deserialize
     */
    public PAObject(Map<String, Object> object) {
	if (object.containsKey("location")) {
	    Object raw = object.get("location");
	    if (raw instanceof Map) {
		Map<?, ?> rawmap = (Map<?, ?>) raw;
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
		    map.put(entry.getKey().toString(), entry.getValue());
		}
		a = Location.deserialize(map);
	    }
	}
	b = new HashSet<PANode>();
	if (object.containsKey("nodes")) {
	    Object raw = object.get("nodes");
	    if (raw instanceof List) {
		List<?> list = (List<?>) raw;
		if (!list.isEmpty()) {
		    for (Object o : list) {
			if (o instanceof Map) {
			    Map<?, ?> rawmap = (Map<?, ?>) o;
			    Map<String, Object> map = new HashMap<String, Object>();
			    for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue());
			    }
			    PANode node = PANode.deserialize(map);
			    if (!b.contains(node))
				b.add(node);
			}
		    }
		}
	    }
	}
	c = UUID.fromString((String) object.get("uuid"));
	e = new Vector[] { new Vector(), new Vector(), new Vector(), new Vector() };
	if (object.containsKey("rotation")) {
	    Object raw = object.get("rotation");
	    if (raw instanceof Map) {
		Map<?, ?> rawmap = (Map<?, ?>) raw;
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
		    map.put(entry.getKey().toString(), entry.getValue());
		}
		h = PAQuaternion.deserialize(map);
	    }
	}
	if (object.containsKey("velocity")) {
	    Object raw = object.get("velocity");
	    if (raw instanceof Map) {
		Map<?, ?> rawmap = (Map<?, ?>) raw;
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
		    map.put(entry.getKey().toString(), entry.getValue());
		}
		e[3] = Vector.deserialize(map);
	    }
	}
	f = new double[] { 0.0, 0.0 };
	f[0] = (double) object.get("scale");
	f[1] = Math.sqrt(3 * (f[0] * f[0]));
	g = (boolean) object.get("visible");
	update();
	if (!objects.containsKey(c))
	    objects.put(c, this);
    }

    /**
     * Where to spawn the center of this new PAObject with a scale of (0.5, 0.5,
     * 0.5)
     * 
     * @param location
     *            The location
     */
    public PAObject(Location location) {
	this(location, new HashSet<PANode>(), 1.0);
    }

    /**
     * Where to spawn the center of this new PAObject with a scale
     * 
     * @param location
     *            The location
     * @param scale
     */
    public PAObject(Location location, double scale) {
	this(location, new HashSet<PANode>(), scale);
    }

    /**
     * Where to spawn the center of this new PAObject with a scale and nodes
     * 
     * @param location
     *            The location
     * @param nodes
     * @param scale
     */
    public PAObject(Location location, Set<PANode> nodes, double scale) {
	a = location;
	b = nodes;
	c = UUID.randomUUID();
	e = new Vector[] { new Vector(), new Vector(), new Vector(), new Vector() };
	f = new double[] { scale, Math.sqrt(3 * (scale * scale)) };
	g = false;
	h = new PAQuaternion();
	update();
	objects.put(c, this);
	Bukkit.getServer().getPluginManager().registerEvents(this, ParticleAnimationApi.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onEntityInteract(PlayerInteractEvent event) {
	Player player = event.getPlayer();
	Action action = event.getAction();
	if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
	    if (!a.getWorld().equals(player.getWorld()))
		return;
	    if (!g)
		return;
	    BlockIterator iter = new BlockIterator(player, (int) player.getEyeLocation().subtract(a).length());
	    Set<Vector> vecs = vectors();
	    while (iter.hasNext()) {
		Vector vec = iter.next().getLocation().toVector();
		for (Vector v : vecs) {
		    if (vec.clone().subtract(v).length() < 1.0) {
			PAButton button = null;
			if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))
			    button = PAButton.LEFT;
			if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
			    button = PAButton.RIGHT;
			PAPlayerClickEvent e = new PAPlayerClickEvent(this, player, button);
			Bukkit.getServer().getPluginManager().callEvent(e);
			if (!e.isCancelled()) {
			    event.setCancelled(true);
			    return;
			}
		    }
		}
	    }
	}
    }

    /**
     * Draw all the nodes once per call
     */
    public void draw() {
	if (!g)
	    return;
	for (PANode node : b) {
	    PANode[] nodes = node.getConnectedNodes().toArray(new PANode[0]);
	    if (nodes.length > 0) {
		for (int A = 0; A <= nodes.length - 1; A++) {
		    for (double B = 0.0; B < (f[1] * b.size()); B++) {
			Vector v = lerp(node.getOffset(), nodes[(A + 1) % nodes.length].getOffset(), B / (f[1] * b.size()));
			if (node.isColorable() && node.hasColor()) {
			    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, new Particle.DustOptions(node.getColor(), 0.5f));
			} else if (node.canSetData() && node.hasData()) {
			    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, node.getData());
			} else {
			    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0);
			}
		    }
		}
	    } else {
		Vector v = node.getOffset().multiply(f[0]);
		if (node.isColorable() && node.hasColor()) {
		    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, new Particle.DustOptions(node.getColor(), 0.5f));
		} else if (node.canSetData() && node.hasData()) {
		    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, node.getData());
		} else {
		    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0);
		}
	    }
	}
	if (hasVelocity()) {
	    PAObjectVelocityEvent event = new PAObjectVelocityEvent(this, e[3].clone());
	    Bukkit.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		a.add(e[3]);
		e[3] = e[3].add(new Vector().subtract(e[3]).multiply(0.1635));
	    }
	}
	List<Entity> entities = getEntities();
	if (!entities.isEmpty()) {
	    for (Entity entity : entities) {
		{
		    PAEntityInteractEvent event = new PAEntityInteractEvent(this, entity);
		    Bukkit.getServer().getPluginManager().callEvent(event);
		}
		if (entity instanceof LivingEntity) {
		    PALivingEntityInteractEvent event = new PALivingEntityInteractEvent(this, (LivingEntity) entity);
		    Bukkit.getServer().getPluginManager().callEvent(event);
		}
		if (entity instanceof Player) {
		    PALivingEntityInteractEvent event = new PALivingEntityInteractEvent(this, (Player) entity);
		    Bukkit.getServer().getPluginManager().callEvent(event);
		}
	    }
	}
	for (PAObject obj : objects.values()) {
	    if (!equals(obj) && obj.g && isColliding(obj)) {
		PAObjectCollideEvent event = new PAObjectCollideEvent(this, obj);
		Bukkit.getServer().getPluginManager().callEvent(event);
	    }
	}
    }

    /**
     * Gets the true center of this objects nodes
     * 
     * @return Vector
     */
    public Vector getCenter() {
	Vector v = new Vector();
	for (PANode node : b) {
	    v.add(node.getOffset());
	}
	double n = b.size();
	return v.divide(new Vector(n, n, n));
    }

    /**
     * Gets the location of this object
     * 
     * @return The location
     */
    public Location getLocation() {
	return a;
    }

    /**
     * Gets the current scale of this object
     * 
     * @return double
     */
    public double getscale() {
	return f[0];
    }

    /**
     * Adds nodes to the object
     * 
     * @param nodes
     *            One or more node
     */
    protected void addNodes(PANode... nodes) {
	for (PANode node : nodes) {
	    if (!b.contains(node))
		b.add(node);
	}
	update();
    }

    /**
     * Adds a node to the object
     * 
     * @param node
     *            A node
     */
    protected void addNode(PANode node) {
	if (!b.contains(node))
	    b.add(node);
	update();
    }

    /**
     * Gets the nodes of this object
     * 
     * @return Set<PANode>
     */
    public Set<PANode> getNodes() {
	return b;
    }

    /**
     * Sets this object to be visible or not
     * 
     * @param visible
     *            Is this object visible?
     */
    public void setVisible(boolean visible) {
	g = visible;
    }

    /**
     * Gets if this object is visible
     * 
     * @return boolean
     */
    public boolean isVisible() {
	return g;
    }

    /**
     * Lerp
     * 
     * @param start
     * @param end
     * @param percent
     * @return Vector
     */
    protected Vector lerp(Vector start, Vector end, double percent) {
	return start.clone().add(end.clone().subtract(start).multiply(percent));
    }

    /**
     * Remove a node
     * 
     * @param index
     *            The index of the node
     */
    protected void removeNode(int index) {
	if (index < b.size())
	    b.remove(index);
	update();
    }

    /**
     * Remove nodes
     * 
     * @param nodes
     *            One or more nodes
     */
    protected void removeNodes(PANode... nodes) {
	for (PANode node : nodes) {
	    if (b.contains(node))
		b.remove(node);
	}
	update();
    }

    /**
     * Remove a node
     * 
     * @param node
     */
    protected void removeNode(PANode node) {
	if (b.contains(node))
	    b.remove(node);
	update();
    }

    /**
     * Gets the Unique id of this object
     * 
     * @return UUID
     */
    public UUID getUuid() {
	return c;
    }

    /**
     * Move this object
     * 
     * @param x
     *            Xaxis
     * @param y
     *            Yaxis
     * @param z
     *            Zaxis
     */
    public void move(double x, double y, double z) {
	PAObjectMoveEvent event = new PAObjectMoveEvent(this, a.toVector(), a.clone().add(x, y, z).toVector());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled())
	    a.add(x, y, z);
    }

    /**
     * Move this object
     * 
     * @param amount
     *            The amount as a Vector
     */
    public void move(Vector amount) {
	PAObjectMoveEvent event = new PAObjectMoveEvent(this, a.toVector(), a.clone().add(amount).toVector());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled())
	    a.add(amount);
    }

    /**
     * Moves this object relative its location
     * 
     * @param x
     *            X amount
     * @param y
     *            Y amount
     * @param z
     *            Z amount
     */
    public void moveRelative(double x, double y, double z) {
	Vector amount = new Vector(x, y, z);
	PAObjectMoveRelativeEvent event = new PAObjectMoveRelativeEvent(this, a.toVector(), a.clone().add(amount).toVector());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    for (PANode node : b) {
		node.setOffset(node.getOffset().add(amount));
	    }
	    update();
	}
    }

    /**
     * Moves this object relative its location
     * 
     * @param relative
     *            The relative amount
     */
    public void moveRelative(Vector relative) {
	PAObjectMoveRelativeEvent event = new PAObjectMoveRelativeEvent(this, a.toVector(), a.clone().add(relative).toVector());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    for (PANode node : b) {
		node.setOffset(node.getOffset().add(relative));
	    }
	    update();
	}
    }

    /**
     * Moves this object relative its location
     * 
     * @param relative
     *            The amount as a location
     */
    public void moveRelative(Location relative) {
	Vector amount = relative.toVector();
	PAObjectMoveRelativeEvent event = new PAObjectMoveRelativeEvent(this, a.toVector(), a.clone().add(amount).toVector());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    for (PANode node : b) {
		node.setOffset(node.getOffset().add(amount));
	    }
	    update();
	}
    }

    /**
     * Normalize this object
     */
    public void normalize() {
	for (PANode node : b) {
	    node.setOffset(node.getOffset().normalize());
	}
	update();
    }

    /**
     * Rotate this object
     * 
     * @param rotation
     */
    public void rotate(PAQuaternion rotation) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), rotation.clone());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rot(rotation);
	}
    }

    /**
     * Rotate this object (degrees)
     * 
     * @param axis
     *            The axis to rotate around
     * @param degrees
     *            Amount to rotate
     */
    public void rotate(Vector axis, double degrees) {
	rotate(axis.getX(), axis.getY(), axis.getZ(), degrees);
    }

    /**
     * Rotate this object around an axis
     * 
     * @param axisX
     * @param axisY
     * @param axisZ
     * @param degrees
     */
    public void rotate(double axisX, double axisY, double axisZ, double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(axisX, axisY, axisZ, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rot(new Vector(axisX, axisY, axisZ), degrees);
	}
    }

    /**
     * Rotate around the X-axis
     * 
     * @param degrees
     */
    public void rotateX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(1, 0, 0, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(degrees);
	}
    }

    /**
     * Rotate around the Y-axis
     * 
     * @param degrees
     */
    public void rotateY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(0, 1, 0, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotY(degrees);
	}
    }

    /**
     * Rotate around the Z-axis
     * 
     * @param degrees
     */
    public void rotateZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(0, 0, 1, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotZ(degrees);
	}
    }

    /**
     * Set the rotation of this object
     * 
     * @param rotation
     */
    public void setRotation(PAQuaternion rotation) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), rotation.clone());
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    setRot(rotation);
	}
    }

    /**
     * Set the rotation of this object (degrees)
     * 
     * @param axis
     *            The axis to set the rotation on
     * @param degrees
     *            The amount to set
     */
    public void setRotation(Vector axis, double degrees) {
	setRotation(axis.getX(), axis.getY(), axis.getZ(), degrees);
    }

    /**
     * Set the rotation of this object
     * 
     * @param axisX
     * @param axisY
     * @param axisZ
     * @param degrees
     */
    public void setRotation(double axisX, double axisY, double axisZ, double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(axisX, axisY, axisZ, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    setRot(new Vector(axisX, axisY, axisZ), degrees);
	}
    }

    /**
     * Set rotation on the X-axis
     * 
     * @param degrees
     */
    public void setRotationX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(1, 0, 0, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    setRotX(degrees);
	}
    }

    /**
     * Set rotation on the Y-axis
     * 
     * @param degrees
     */
    public void setRotationY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(0, 1, 0, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    setRotY(degrees);
	}
    }

    /**
     * Set rotation on the Z-axis
     * 
     * @param degrees
     */
    public void setRotationZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, h.clone(), PAQuaternion.fromAxisAngles(0, 0, 1, degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    setRotZ(degrees);
	}
    }

    /**
     * Get the action for this object
     * 
     * @return PAAction
     */
    public PAAction getAction() {
	return d;
    }

    /**
     * Gets this objects hitbox as a vector ( X, Y, Z = Width, Height, Length)
     * 
     * @return Vector
     */
    public Vector getAABB() {
	return e[0].clone();
    }

    /**
     * Get this objects hitbox minimum
     * 
     * @return Vector
     */
    public Vector getAABBMin() {
	return e[1].clone();
    }

    /**
     * Get this objects hitbox maximum
     * 
     * @return Vector
     */
    public Vector getAABBMax() {
	return e[2].clone();
    }

    /**
     * Gets the Width of this objects hitbox
     * 
     * @return double
     */
    public double getWidth() {
	return e[0].getX();
    }

    /**
     * Gets the Height of this objects hitbox
     * 
     * @return double
     */
    public double getHeight() {
	return e[0].getY();
    }

    /**
     * Gets the Length of this objects hitbox
     * 
     * @return double
     */
    public double getLength() {
	return e[0].getZ();
    }

    /**
     * Gets this objects velocity
     * 
     * @return Vector
     */
    public Vector getVelocity() {
	return e[3].clone();
    }

    /**
     * Sets this objects velocity
     * 
     * @param velocity
     *            A Vector
     */
    public void setVelocity(Vector velocity) {
	e[3] = velocity;
    }

    /**
     * Sets this objects velocity
     * 
     * @param x
     *            X axis velocity
     * @param y
     *            Y axis velocity
     * @param z
     *            Z axis velocity
     */
    public void setVelocity(double x, double y, double z) {
	e[3] = new Vector(x, y, z);
    }

    /**
     * Adds velocity to this object
     * 
     * @param velocity
     *            A Vector
     */
    public void addVelocity(Vector velocity) {
	e[3].add(velocity);
    }

    /**
     * Adds velocity to this object
     * 
     * @param x
     *            X axis velocity
     * @param y
     *            Y axis velocity
     * @param z
     *            Z axis velocity
     */
    public void addVelocity(double x, double y, double z) {
	e[3].add(new Vector(x, y, z));
    }

    /**
     * Gets if this object has a velocity
     * 
     * @return boolean
     */
    public boolean hasVelocity() {
	return e[3].lengthSquared() >= 0.000001;
    }

    /**
     * Get if a point is within the bounds of this objects hitbox
     * 
     * @param vector
     *            A Vector
     * @return boolean
     */
    public boolean inHitbox(Vector vector) {
	Vector l = a.toVector();
	Vector min = e[1].clone();
	Vector max = e[2].clone();
	return vector.isInAABB(min.add(l), max.add(l));
    }

    /**
     * Check if another object is colliding with this object
     * 
     * @param object
     *            The other object
     * @return boolean
     */
    public boolean isColliding(PAObject object) {
	Location A = a;
	Location B = object.a;
	if (!A.getWorld().equals(B.getWorld()))
	    return false;
	Vector locA = A.toVector();
	Vector locB = B.toVector();
	Vector minA = e[1].clone().add(locA);
	Vector maxA = e[2].clone().add(locA);
	Vector minB = object.e[1].clone().add(locB);
	Vector maxB = object.e[2].clone().add(locB);
	return (minA.getX() < maxB.getX() && maxA.getX() > minB.getX()) && (minA.getY() < maxB.getY() && maxA.getY() > minB.getY()) && (minA.getZ() < maxB.getZ() && maxA.getZ() > minB.getZ());
    }

    /**
     * Gets entities within the bounds of this objects hitbox
     * 
     * @return Set<Entity>
     */
    public List<Entity> getEntities() {
	Vector v = e[0].clone().divide(new Vector(2, 2, 2));
	return (List<Entity>) a.getWorld().getNearbyEntities(a, v.getX(), v.getY(), v.getZ());
    }

    /**
     * Gets blocks within the bounds of this objects hitbox
     * 
     * @return Set<Block>
     */
    public List<Block> getBlocks() {
	List<Block> blocks = new ArrayList<Block>();
	Vector max = new Vector();
	Vector min = new Vector();
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	Vector v = a.toVector();
	min.add(v);
	max.add(v);
	for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
	    for (int y = min.getBlockY(); y < max.getBlockY(); y++) {
		for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
		    Block block = a.getWorld().getBlockAt(x, y, z);
		    if (!block.isEmpty())
			blocks.add(block);
		}
	    }
	}
	return blocks;
    }

    /**
     * Gets the rotation of this object
     * 
     * @return PAQuaternion
     */
    public PAQuaternion getRotation() {
	return h.clone();
    }

    /**
     * Set the actions to be performed per update on this object
     * 
     * @param action
     *            The actions
     */
    public void setAction(PAAction action) {
	d = action;
    }

    /**
     * Sets the location of the object
     * 
     * @param location
     *            The location
     */
    public void setLocation(Location location) {
	a = location;
    }

    /**
     * Sets the scale of the object
     * 
     * @param scale
     *            The scale
     */
    public void setScale(double scale) {
	normalize();
	for (PANode node : b) {
	    node.setOffset(node.getOffset().multiply(scale));
	}
	f = new double[] { scale, Math.sqrt(3 * (scale * scale)) };
	update();
    }

    /**
     * Gets a PAObject by id
     * 
     * @param id
     *            The Uuid of the object
     * @return PAObject
     */
    public static PAObject getByUuid(UUID id) {
	if (objects.containsKey(id))
	    return objects.get(id);
	return null;
    }

    private void setRot(PAQuaternion rotation) {
	h = rotation;
	for (PANode node : b) {
	    node.resetOffset();
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	update();
    }

    private void setRot(Vector axis, double deg) {
	h = PAQuaternion.fromAxisAngles(axis, deg);
	for (PANode node : b) {
	    node.resetOffset();
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	update();
    }

    private void setRotX(double deg) {
	setRot(new Vector(1, 0, 0), deg);
    }

    private void setRotY(double deg) {
	setRot(new Vector(0, 1, 0), deg);
    }

    private void setRotZ(double deg) {
	setRot(new Vector(0, 0, 1), deg);
    }

    private void rot(PAQuaternion rotation) {
	h = rotation;
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	update();
    }

    private void rot(Vector axis, double deg) {
	h = PAQuaternion.fromAxisAngles(axis, deg);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	update();
    }

    private void rotX(double deg) {
	rot(new Vector(1, 0, 0), deg);
    }

    private void rotY(double deg) {
	rot(new Vector(0, 1, 0), deg);
    }

    private void rotZ(double deg) {
	rot(new Vector(0, 0, 1), deg);
    }

    private Set<Vector> vectors() {
	Set<Vector> blocks = new HashSet<Vector>();
	Vector min = e[1].clone();
	Vector max = e[2].clone();
	Vector v = a.toVector();
	min.add(v);
	max.add(v);
	for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
	    for (int y = min.getBlockY(); y < max.getBlockY(); y++) {
		for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
		    blocks.add(new Vector(x, y, z));
		}
	    }
	}
	blocks.add(a.toVector());
	return blocks;
    }

    private void update() {
	Vector max = new Vector();
	Vector min = new Vector();
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	e[0] = max.subtract(min);
	e[1] = min;
	e[2] = max;
    }

    @Override
    public Map<String, Object> serialize() {
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
	result.put("location", a.serialize());
	for (PANode node : getNodes()) {
	    nodes.add(node.serialize());
	}
	result.put("nodes", nodes);
	result.put("uuid", c.toString());
	result.put("rotation", h.serialize());
	result.put("velocity", e[3].serialize());
	result.put("scale", f[0]);
	result.put("visible", g);
	return result;
    }

    /**
     * Deserialize a PAObject
     * 
     * @param object
     *            The PAObject to deserialize
     * 
     * @return PAObject
     */
    public static PAObject deserialize(Map<String, Object> object) {
	return new PAObject(object);
    }
}
