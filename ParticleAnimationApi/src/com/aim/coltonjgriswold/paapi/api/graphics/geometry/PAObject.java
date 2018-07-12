package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveRelativeEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectRotateEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public abstract class PAObject {
    
    private Location a;
    private Set<PANode> b;
    private UUID c;
    private PAAction d;
    private Vector e;
    private Vector f;
    private Vector g;
    private Vector h;
    //private Vector i;
    private double j;
    private double k;
    //private double l;
    
    /**
     * Where to spawn the center of this new PAObject with a scale of (0.5, 0.5, 0.5)
     * 
     * @param location The location
     */
    public PAObject(Location location) {
	this(location, new HashSet<PANode>(), new Vector(0.5, 0.5, 0.5));
    }
    
    /**
     * Where to spawn the center of this new PAObject with a scale
     * 
     * @param location The location
     * @param scale
     */
    public PAObject(Location location, Vector scale) {
	this(location, new HashSet<PANode>(), scale);
    }
    
    /**
     * Where to spawn the center of this new PAObject with a scale and nodes
     * 
     * @param location The location
     * @param nodes
     * @param scale
     */
    public PAObject(Location location, Set<PANode> nodes, Vector scale) {
	a = location;
	b = nodes;
	c = UUID.randomUUID();
	e = new Vector(0, 0, 0);
	f = scale;
	g = new Vector(0, 0, 0);
	h = location.toVector();
	//i = new Vector(0, 0, 0);
	j = scale.length();
	k = 0;
	//l = 0;
	update();
    }
    
    /**
     * Draw all the nodes once per call
     */
    public void draw() {
	for (PANode node : b) {
	    PANode[] nodes = node.getConnectedNodes().toArray(new PANode[0]);
	    if (nodes.length > 0) {
		for (int A = 0; A <= nodes.length - 1; A++) {
		    for (double B = 0.0; B < (j * b.size()); B++) {
			Vector v = lerp(node.getOffset(), nodes[(A + 1) % nodes.length].getOffset(), B / (j * b.size()));
			if (node.isColorable() && node.hasColor()) {
			    double red = ((node.getColor().getRed() + 1.0) / 255.0);
			    double green = node.getColor().getGreen() / 255.0;
			    double blue = node.getColor().getBlue() / 255.0;
			    if (node.canSetData() && node.hasData())
				a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, red, green, blue, 1.0, node.getData());
			    else
				a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, red, green, blue, 1.0);
			} else {
			    if (node.canSetData() && node.hasData())
				a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, 0.0, node.getData());
			    else
				a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, 0.0);
			}
		    }
		}
	    } else {
		if (node.isColorable() && node.hasColor()) {
		    double red = ((node.getColor().getRed() + 1.0) / 255.0);
		    double green = node.getColor().getGreen() / 255.0;
		    double blue = node.getColor().getBlue() / 255.0;
		    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(node.getOffset()), 0, red, green, blue, 1.0);
		} else {
		    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(node.getOffset()), 0, 0, 0, 0, 1.0);
		}
	    }
	}
	if (k < 1 && !h.equals(new Vector(0, 0, 0))) {
	    Vector start = new Vector(0, 0, 0);
	    Vector end = h.clone().subtract(a.toVector());
	    a.add(start.clone().add(end.clone().subtract(start.clone()).multiply(k)));
	    k += 1.0 / start.distance(end);
	} else {
	    if (k != 0) {
		k = 0;
	    	h = new Vector(0, 0, 0);
	    }
	}
    }
    
    /**
     * Gets the true center of this objects nodes
     * 
     * @return Vector
     */
    public Vector getCenter() {
	Vector v = new Vector(0, 0, 0);
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
     * @return Vector
     */
    public Vector getscale() {
	return f.clone();
    }
    
    /**
     * Adds nodes to the object
     * 
     * @param nodes One or more node
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
     * @param node A node
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
    protected Set<PANode> getNodes() {
	return b;
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
     * NLerp
     * 
     * @param start
     * @param end
     * @param percent
     * @return Vector
     */
    protected Vector nLerp(Vector start, Vector end, double percent) {
	return lerp(start, end, percent).normalize();
    }
    
    /**
     * Remove a node
     * 
     * @param index The index of the node
     */
    protected void removeNode(int index) {
	if (index < b.size())
	    b.remove(index);
	update();
    }
    
    /**
     * Remove nodes
     * 
     * @param nodes One or more nodes
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
     * Gets the target location of this object
     * 
     * @return Vector
     */
    public Vector getTarget() {
	return h.clone();
    }
    
    /**
     * Move this object
     * 
     * @param x Xaxis
     * @param y Yaxis
     * @param z Zaxis
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
     * @param amount The amount as a Vector
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
     * @param x X amount
     * @param y Y amount
     * @param z	Z amount
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
     * @param relative The relative amount
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
     * @param relative The amount as a location
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
     * Rotate this object (degrees)
     * 
     * @param rotation
     */
    public void rotate(Vector rotation) {
	rotate(rotation.getX(), rotation.getY(), rotation.getZ());
    }
    
    /**
     * Rotate this object
     * 
     * @param degreesX
     * @param degreesY
     * @param degreesZ
     */
    public void rotate(double degreesX, double degreesY, double degreesZ) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().add(new Vector(degreesX, degreesY, degreesZ)));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(degreesX);
	    rotY(degreesY);
	    rotZ(degreesZ);
	}
    }
    
    /**
     * Rotates on the X-axis
     * 
     * @param degrees
     */
    public void rotateX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().add(new Vector(degrees, 0, 0)));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(degrees);
	}
    }
    
    /**
     * Rotates on the Y-axis
     * 
     * @param degrees
     */
    public void rotateY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().add(new Vector(0, degrees, 0)));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotY(degrees);
	}
    }
    
    /**
     * Rotates on the Z-axis
     * 
     * @param degrees
     */
    public void rotateZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().add(new Vector(0, 0, degrees)));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotZ(degrees);
	}
    }
    
    /**
     * Set the rotation of this object (degrees)
     * 
     * @param rotation
     */
    public void setRotation(Vector rotation) {
	setRotation(rotation.getX(), rotation.getY(), rotation.getZ());
    }
    
    /**
     * Set the rotation of this object
     * 
     * @param degreesX
     * @param degreesY
     * @param degreesZ
     */
    public void setRotation(double degreesX, double degreesY, double degreesZ) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), new Vector(degreesX, degreesY, degreesZ));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(-e.getX());
	    rotX(degreesX);
	    rotY(-e.getY());
	    rotY(degreesY);
	    rotZ(-e.getZ());
	    rotZ(degreesZ);
	}
    }
    
    /**
     * Set rotation on the X-axis
     * 
     * @param degrees
     */
    public void setRotationX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().setX(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(-e.getX());
	    rotX(degrees);
	}
    }
    
    /**
     * Set rotation on the Y-axis
     * 
     * @param degrees
     */
    public void setRotationY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().setY(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotY(-e.getY());
	    rotY(degrees);
	}
    }
    
    /**
     * Set rotation on the Z-axis
     * 
     * @param degrees
     */
    public void setRotationZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e.clone(), e.clone().setZ(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotZ(-e.getZ());
	    rotZ(degrees);
	}
    }
    
    /**
     * Sets the target location of this object
     * 
     * @param vector A vector
     */
    public void setTarget(Vector vector) {
	h = vector;
	k = 0;
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
    public Vector getHitbox() {
	return g.clone();
    }
    
    /**
     * Gets the Width of this objects hitbox
     * 
     * @return double
     */
    public double getWidth() {
	return g.getX();
    }
    
    /**
     * Gets the Height of this objects hitbox
     * 
     * @return double
     */
    public double getHeight() {
	return g.getY();
    }
    
    /**
     * Gets the Length of this objects hitbox
     * 
     * @return double
     */
    public double getLength() {
	return g.getZ();
    }
    
    /**
     * Get if a point is within the bounds of this objects hitbox
     * 
     * @param vector A Vector
     * @return boolean
     */
    public boolean inHitbox(Vector vector) {
	Vector max = new Vector(0, 0, 0);
	Vector min = new Vector(0, 0, 0);
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	Vector l = a.toVector();
	return vector.isInAABB(min.add(l), max.add(l));
    }
    
    /**
     * Gets entities within the bounds of this objects hitbox
     * 
     * @return Set<Entity>
     */
    public List<Entity> getEntities() {
	Vector h = g.clone().divide(new Vector(2, 2, 2));
	return (List<Entity>) a.getWorld().getNearbyEntities(a, h.getX(), h.getY(), h.getZ());
    }
    
    /**
     * Gets blocks within the bounds of this objects hitbox
     * 
     * @return Set<Block>
     */
    public List<Block> getBlocks() {
	List<Block> blocks = new ArrayList<Block>();
	Vector max = new Vector(0, 0, 0);
	Vector min = new Vector(0, 0, 0);
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	Vector l = a.toVector();
	min.add(l);
	max.add(l);
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
     * @return Vector
     */
    public Vector getRotation() {
	return e.clone();
    }
    
    /**
     * Gets the X-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationX() {
	return e.getX();
    }
    
    /**
     * Gets the Y-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationY() {
	return e.getY();
    }
    
    /**
     * Gets the Z-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationZ() {
	return e.getZ();
    }
    
    /**
     * Set the actions to be performed per update on this object
     * 
     * @param action The actions
     */
    public void setAction(PAAction action) {
	d = action;
    }
    
    /**
     * Sets the location of the object
     * 
     * @param location The location
     */
    public void setLocation(Location location) {
	a = location;
    }
    
    /**
     * Sets the scale of the object
     * 
     * @param scale The scale
     */
    public void setScale(Vector scale) {
	normalize();
	for (PANode node : b) {
	    node.setOffset(node.getOffset().multiply(scale.divide(new Vector(2, 2, 2))));
	}
	f = scale;
	j = scale.length();
	update();
    }
    
    private void rotX(double deg) {
	e.setX(e.getX() + deg);
	double theta = Math.toRadians(deg);
	double sin = Math.sin(theta);
	double cos = Math.cos(theta);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    double x = v.getX();
	    double y = v.getY();
	    double z = v.getZ();
	    v.setX(x);
	    v.setY((y * cos) - (z * sin));
	    v.setZ((z * cos) + (y * sin));
	    node.setOffset(v);
	}
	update();
    }
    
    private void rotY(double deg) {
	e.setY(e.getY() + deg);
	double theta = Math.toRadians(deg);
	double sin = Math.sin(theta);
	double cos = Math.cos(theta);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    double x = v.getX();
	    double y = v.getY();
	    double z = v.getZ();
	    v.setX((x * cos) - (z * sin));
	    v.setY(y);
	    v.setZ((z * cos) + (x * sin));
	    node.setOffset(v);
	}
	update();
    }
    
    private void rotZ(double deg) {
	e.setZ(e.getZ() + deg);
	double theta = Math.toRadians(deg);
	double sin = Math.sin(theta);
	double cos = Math.cos(theta);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    double x = v.getX();
	    double y = v.getY();
	    double z = v.getZ();
	    v.setX((x * cos) - (y * sin));
	    v.setY((y * cos) + (x * sin));
	    v.setZ(z);
	    node.setOffset(v);
	}
	update();
    }
    
    private void update() {
	Vector max = new Vector(0, 0, 0);
	Vector min = new Vector(0, 0, 0);
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	g = max.subtract(min);
    }
}
