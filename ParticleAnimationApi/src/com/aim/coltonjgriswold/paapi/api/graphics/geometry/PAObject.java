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
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectVelocityEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public abstract class PAObject {
    
    private Location a;
    private Set<PANode> b;
    private UUID c;
    private PAAction d;
    private Vector[] e;
    private double[] f;
    
    /**
     * Where to spawn the center of this new PAObject with a scale of (0.5, 0.5, 0.5)
     * 
     * @param location The location
     */
    public PAObject(Location location) {
	this(location, new HashSet<PANode>(), 1.0);
    }
    
    /**
     * Where to spawn the center of this new PAObject with a scale
     * 
     * @param location The location
     * @param scale
     */
    public PAObject(Location location, double scale) {
	this(location, new HashSet<PANode>(), scale);
    }
    
    /**
     * Where to spawn the center of this new PAObject with a scale and nodes
     * 
     * @param location The location
     * @param nodes
     * @param scale
     */
    public PAObject(Location location, Set<PANode> nodes, double scale) {
	a = location;
	b = nodes;
	c = UUID.randomUUID();
	e = new Vector[] { new Vector(), new Vector(), new Vector() };
	f = new double[] { scale, Math.sqrt(3 * (scale * scale)) };
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
		    for (double B = 0.0; B < (f[1] * b.size()); B++) {
			Vector v = lerp(node.getOffset(), nodes[(A + 1) % nodes.length].getOffset(), B / (f[1] * b.size()));
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
	if (hasVelocity()) {
	    PAObjectVelocityEvent event = new PAObjectVelocityEvent(this, e[2].clone());
	    Bukkit.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		a.add(e[2]);
		e[2] = e[2].add(new Vector().subtract(e[2]).multiply(0.196));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().add(new Vector(degreesX, degreesY, degreesZ)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().add(new Vector(degrees, 0, 0)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().add(new Vector(0, degrees, 0)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().add(new Vector(0, 0, degrees)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), new Vector(degreesX, degreesY, degreesZ));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    Vector r = e[0];
	    rotX(-r.getX());
	    rotX(degreesX);
	    rotY(-r.getY());
	    rotY(degreesY);
	    rotZ(-r.getZ());
	    rotZ(degreesZ);
	}
    }
    
    /**
     * Set rotation on the X-axis
     * 
     * @param degrees
     */
    public void setRotationX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().setX(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(-e[0].getX());
	    rotX(degrees);
	}
    }
    
    /**
     * Set rotation on the Y-axis
     * 
     * @param degrees
     */
    public void setRotationY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().setY(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotY(-e[0].getY());
	    rotY(degrees);
	}
    }
    
    /**
     * Set rotation on the Z-axis
     * 
     * @param degrees
     */
    public void setRotationZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, e[0].clone(), e[0].clone().setZ(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotZ(-e[0].getZ());
	    rotZ(degrees);
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
    public Vector getHitbox() {
	return e[1].clone();
    }
    
    /**
     * Gets the Width of this objects hitbox
     * 
     * @return double
     */
    public double getWidth() {
	return e[1].getX();
    }
    
    /**
     * Gets the Height of this objects hitbox
     * 
     * @return double
     */
    public double getHeight() {
	return e[1].getY();
    }
    
    /**
     * Gets the Length of this objects hitbox
     * 
     * @return double
     */
    public double getLength() {
	return e[1].getZ();
    }
    
    public Vector getVelocity() {
	return e[2].clone();
    }
    
    /**
     * Sets this objects velocity
     * 
     * @param velocity A Vector
     */
    public void setVelocity(Vector velocity) {
	e[2] = velocity;
    }
    
    /**
     * Sets this objects velocity
     * 
     * @param x X axis velocity
     * @param y Y axis velocity
     * @param z Z axis velocity
     */
    public void setVelocity(double x, double y, double z) {
	e[2] = new Vector(x, y, z);
    }
    
    /**
     * Adds velocity to this object
     * 
     * @param velocity A Vector
     */
    public void addVelocity(Vector velocity) {
	e[2].add(velocity);
    }
    
    /**
     * Adds velocity to this object
     * 
     * @param x X axis velocity
     * @param y Y axis velocity
     * @param z Z axis velocity
     */
    public void addVelocity(double x, double y, double z) {
	e[2].add(new Vector(x, y, z));
    }
    
    /**
     * Gets if this object has a velocity
     * 
     * @return boolean
     */
    public boolean hasVelocity() {
	return e[2].lengthSquared() > 0.0;
    }

    /**
     * Get if a point is within the bounds of this objects hitbox
     * 
     * @param vector A Vector
     * @return boolean
     */
    public boolean inHitbox(Vector vector) {
	Vector max = new Vector();
	Vector min = new Vector();
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
	Vector v = e[1].clone().divide(new Vector(2, 2, 2));
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
     * @return Vector
     */
    public Vector getRotation() {
	return e[0].clone();
    }
    
    /**
     * Gets the X-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationX() {
	return e[0].getX();
    }
    
    /**
     * Gets the Y-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationY() {
	return e[0].getY();
    }
    
    /**
     * Gets the Z-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationZ() {
	return e[0].getZ();
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
    public void setScale(double scale) {
	normalize();
	for (PANode node : b) {
	    node.setOffset(node.getOffset().multiply(scale));
	}
	f = new double[] { scale, Math.sqrt(3 * (scale * scale)) };
	update();
    }
    
    private void rotX(double deg) {
	e[0].setX((e[0].getX() + deg) % 360.0);
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
	e[0].setY((e[0].getY() + deg) % 360.0);
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
	e[0].setZ((e[0].getZ() + deg) % 360.0);
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
	Vector max = new Vector();
	Vector min = new Vector();
	for (PANode node : b) {
	    Vector o = node.getOffset();
	    max = Vector.getMaximum(max, o);
	    min = Vector.getMinimum(min, o);
	}
	e[1] = max.subtract(min);
    }
}
