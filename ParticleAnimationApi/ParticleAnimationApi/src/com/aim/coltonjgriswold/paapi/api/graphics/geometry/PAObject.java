package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
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
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh.PAFace;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh.PAHalfEdge;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh.PANode;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAQuaternion;

public class PAObject implements Listener {

    private Location a;
    private Set<PANode> b;
    private UUID c;
    private PAAction d;
    private Vector[] e;
    private double[] f;
    private boolean g;
    private PAQuaternion h;
    private Set<PAFace> i;
    private static Map<UUID, PAObject> objects;

    static {
	objects = new HashMap<UUID, PAObject>();
    }

    /**
     * Where to spawn the center of this new PAObject with a scale of (0.5, 0.5,
     * 0.5)
     * 
     * @param location
     *            The location
     */
    public PAObject(Location location) {
	this(location, new HashSet<PAFace>(), 1.0);
    }

    /**
     * Where to spawn the center of this new PAObject with a scale
     * 
     * @param location
     *            The location
     * @param scale
     */
    public PAObject(Location location, double scale) {
	this(location, new HashSet<PAFace>(), scale);
    }

    /**
     * Where to spawn the center of this new PAObject with a scale and nodes
     * 
     * @param location
     *            The location
     * @param faces
     * @param scale
     */
    public PAObject(Location location, Set<PAFace> faces, double scale) {
	a = location.clone();
	b = new HashSet<PANode>();
	c = UUID.randomUUID();
	e = new Vector[] { new Vector(), new Vector(), new Vector(), new Vector() };
	f = new double[] { scale, Math.sqrt(3 * (scale * scale)) };
	g = false;
	h = new PAQuaternion();
	i = faces;
	updateVars();
	updateHitbox();
	objects.put(c, this);
	Bukkit.getServer().getPluginManager().registerEvents(this, ParticleAnimationApi.instance());
	if (scale != 1.0)
	    setScale(scale);
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
     * Draw all the faces once per call
     */
    public void draw() {
	if (!g)
	    return;
	for (PAFace face : i) {
	    PAHalfEdge start = face.getStartEdge();
	    double s = face.getHalfEdges().size();
	    for (PAHalfEdge edge = start; edge != start.prev(); edge = edge.next()) {
		PANode nodeA = edge.head();
		PANode nodeB = edge.next().head();
		for (double n = 0.0; n < f[1] * s; n++) {
		    Vector v = lerp(nodeA.getOffset(), nodeB.getOffset(), n / (f[1] * s));
		    if (nodeA.isColorable() && nodeA.hasColor()) {
			a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0, new Particle.DustOptions(nodeA.getColor(), nodeA.getSize()));
		    } else if (nodeA.canSetData() && nodeA.hasData()) {
			a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0, nodeA.getData());
		    } else {
			a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0);
		    }
		}
	    }
	    PANode nodeA = start.prev().head();
	    PANode nodeB = start.head();
	    for (double n = 0.0; n < f[1] * s; n++) {
		Vector v = lerp(nodeA.getOffset(), nodeB.getOffset(), n / (f[1] * s));
		if (nodeA.isColorable() && nodeA.hasColor()) {
		    a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0, new Particle.DustOptions(nodeA.getColor(), nodeA.getSize()));
		} else if (nodeA.canSetData() && nodeA.hasData()) {
		    a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0, nodeA.getData());
		} else {
		    a.getWorld().spawnParticle(nodeA.getParticle(), a.clone().add(v), 0, 0, 0, 0);
		}
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
	return a.clone();
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
     * Addes new faces to this object
     * 
     * @param faces
     */
    protected void addFaces(PAFace... faces) {
	for (PAFace face : faces) {
	    if (!i.contains(face))
		i.add(face);
	}
	updateVars();
    }

    /**
     * Addes new a face to this object
     * 
     * @param face
     */
    protected void addFace(PAFace face) {
	if (!i.contains(face)) {
	    i.add(face);
	    updateVars();
	}
    }

    /**
     * Removes faces from this object
     * 
     * @param faces
     */
    protected void removeFaces(PAFace... faces) {
	for (PAFace face : faces) {
	    if (i.contains(face))
		i.remove(face);
	}
	updateVars();
    }

    /**
     * Removes a face from this object
     * 
     * @param face
     */
    protected void removeFace(PAFace face) {
	if (!i.contains(face)) {
	    i.remove(face);
	    updateVars();
	}
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
     * Gets the faces of this object
     * 
     * @return Set<PAFace>
     */
    public Set<PAFace> getFaces() {
	return i;
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
	    updateVars();
	    updateHitbox();
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
	    updateVars();
	    updateHitbox();
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
	    updateVars();
	    updateHitbox();
	}
    }

    /**
     * Normalize this object
     */
    public void normalize() {
	for (PANode node : b) {
	    node.setOffset(node.getOffset().normalize());
	}
	updateVars();
	updateHitbox();
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
	return e[3].lengthSquared() >= 0.0001;
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
     * @return List<Entity>
     */
    public List<Entity> getEntities() {
	Vector v = e[0].clone().multiply(0.5);
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
	updateHitbox();
    }

    /**
     * Rotates this object so if faces a location
     * 
     * @param target
     *            The target location to point at
     */
    public void lookAt(Vector target) {
	Vector delta = target.clone().subtract(a.toVector());
	Vector up1 = new Vector(0, 1, 0);
	PAQuaternion rot1 = PAQuaternion.fromToRotation(new Vector(0, 0, 1), delta);
	Vector right = delta.getCrossProduct(up1);
	up1 = right.getCrossProduct(delta);
	Vector up2 = new Vector(0, 1, 0);
	rot1.apply(up2);
	PAQuaternion rot2 = PAQuaternion.fromToRotation(up2, up1);
	setRot(rot2.multiply(rot1));
    }

    /**
     * Method used to check and update variables
     */
    public void update() {
	if (hasVelocity()) {
	    PAObjectVelocityEvent event = new PAObjectVelocityEvent(this, e[3].clone());
	    Bukkit.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		a.add(e[3]);
		e[3].add(new Vector().subtract(e[3]).multiply(0.1635));
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

    /*public void triangulate() {
	Set<PAFace> faces = new HashSet<PAFace>();
	for (PAFace face : i) {
	    if (face.getHalfEdges().size() == 3) {
		faces.add(face);
		continue;
	    }
	    for (PAHalfEdge edge = face.getStartEdge(); edge != face.getStartEdge().prev(); edge = edge.next()) {
		Vector p1 = edge.head().getOffset();
		Vector p2 = edge.next().head().getOffset();
		Vector p3 = edge.next().next().head().getOffset();
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		double area = v1.getCrossProduct(v2).lengthSquared();
		System.out.println(area);
	    }
	}
    }*/

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
	updateVars();
	updateHitbox();
    }

    private void setRot(Vector axis, double deg) {
	h = PAQuaternion.fromAxisAngles(axis, deg);
	for (PANode node : b) {
	    node.resetOffset();
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	updateVars();
	updateHitbox();
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
	updateVars();
	updateHitbox();
    }

    private void rot(Vector axis, double deg) {
	h = PAQuaternion.fromAxisAngles(axis, deg);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    h.apply(v);
	    node.setOffset(v);
	}
	updateVars();
	updateHitbox();
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

    private void updateVars() {
	Set<PANode> nodes = new HashSet<PANode>();
	for (PAFace face : i) {
	    nodes.addAll(face.getNodes());
	}
	b = nodes;
    }

    private void updateHitbox() {
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
}
