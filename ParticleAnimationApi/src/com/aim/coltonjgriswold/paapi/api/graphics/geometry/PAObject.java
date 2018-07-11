package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveRelativeEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectRotateEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public abstract class PAObject {
    
    private Location a;
    private Set<PANode> b;
    private double c;
    private UUID d;
    private PAAction e;
    private Vector f;
    
    /**
     * Where to spawn the center of this new PAObject with a size of 1.0
     * 
     * @param location The location
     */
    public PAObject(Location location) {
	this(location, new HashSet<PANode>(), 1.0);
    }
    
    /**
     * Where to spawn the center of this new PAObject with a size
     * 
     * @param location The location
     * @param size
     */
    public PAObject(Location location, double size) {
	this(location, new HashSet<PANode>(), size);
    }
    
    /**
     * Where to spawn the center of this new PAObject with a size and nodes
     * 
     * @param location The location
     * @param nodes
     * @param size
     */
    public PAObject(Location location, Set<PANode> nodes, double size) {
	a = location;
	b = nodes;
	c = size;
	d = UUID.randomUUID();
	f = new Vector(0, 0, 0);
    }
    
    /**
     * Draw all the nodes once per call
     */
    public void draw() {
	for (PANode node : b) {
	    PANode[] nodes = node.getConnectedNodes().toArray(new PANode[0]);
	    if (nodes.length > 0) {
		for (int n = 0; n <= nodes.length - 1; n++) {
		    for (double i = 0.0; i < (c * b.size()); i++) {
			Vector v = lerp(node.getOffset(), nodes[(n + 1) % nodes.length].getOffset(), i / (c * b.size()));
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
     * Gets the current size of this object
     * 
     * @return Double
     */
    public double getSize() {
	return c;
    }
    
    /**
     * Adds nodes to the object
     * 
     * @param node A node
     */
    protected void addNodes(PANode... nodes) {
	for (PANode node : nodes) {
	    if (!b.contains(node))
		b.add(node);
	}
    }
    
    /**
     * Adds a node to the object
     * 
     * @param node A node
     */
    protected void addNode(PANode node) {
	if (!b.contains(node))
	    b.add(node);
    }
    
    /**
     * Gets the nodes of this object
     * @return 
     * 
     * @return Set<PANode>
     */
    protected Set<PANode> getNodes() {
	return b;
    }
    
    /**
     * Lerp a Vector
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
     * @param index The index of the node
     */
    protected void removeNode(int index) {
	if (index < b.size())
	    b.remove(index);
    }
    
    /**
     * Remove nodes
     * 
     * @param node
     */
    protected void removeNodes(PANode... nodes) {
	for (PANode node : nodes) {
	    if (b.contains(node))
		b.remove(node);
	}
    }
    
    /**
     * Remove a node
     * 
     * @param node
     */
    protected void removeNode(PANode node) {
	if (b.contains(node))
	    b.remove(node);
    }
    
    /**
     * Gets the Unique id of this object
     * 
     * @return UUID
     */
    public UUID getUuid() {
	return d;
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
	}
    }
    
    /**
     * Normalize this object
     */
    public void normalize() {
	for (PANode node : b) {
	    node.setOffset(node.getOffset().normalize());
	}
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().add(new Vector(degreesX, degreesY, degreesZ)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().add(new Vector(degrees, 0, 0)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().add(new Vector(0, degrees, 0)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().add(new Vector(0, 0, degrees)));
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
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), new Vector(degreesX, degreesY, degreesZ));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(-f.getX());
	    f.setX(degreesX);
	    rotX(degreesX);
	    rotY(-f.getY());
	    f.setY(degreesY);
	    rotY(degreesY);
	    rotZ(-f.getZ());
	    f.setZ(degreesZ);
	    rotZ(degreesZ);
	}
    }
    
    /**
     * Set rotation on the X-axis
     * 
     * @param degrees
     */
    public void setRotationX(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().setX(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotX(-f.getX());
	    f.setX(degrees);
	    rotX(degrees);
	}
    }
    
    /**
     * Set rotation on the Y-axis
     * 
     * @param degrees
     */
    public void setRotationY(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().setY(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotY(-f.getY());
	    f.setY(degrees);
	    rotY(degrees);
	}
    }
    
    /**
     * Set rotation on the Z-axis
     * 
     * @param degrees
     */
    public void setRotationZ(double degrees) {
	PAObjectRotateEvent event = new PAObjectRotateEvent(this, f.clone(), f.clone().setZ(degrees));
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (!event.isCancelled()) {
	    rotZ(-f.getZ());
	    f.setZ(degrees);
	    rotZ(degrees);
	}
    }
    
    /**
     * Get the action for this object
     * 
     * @return PAAction
     */
    public PAAction getAction() {
	return e;
    }
    
    /**
     * Get if a point is within the objects hitbox
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
     * Gets the rotation of this object
     * 
     * @return Vector
     */
    public Vector getRotation() {
	return f.clone();
    }
    
    /**
     * Gets the X-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationX() {
	return f.getX();
    }
    
    /**
     * Gets the Y-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationY() {
	return f.getY();
    }
    
    /**
     * Gets the Z-axis rotation of this object
     * 
     * @return double
     */
    public double getRotationZ() {
	return f.getZ();
    }
    
    /**
     * Set the actions to be performed per update on this object
     * 
     * @param action The actions
     */
    public void setAction(PAAction action) {
	e = action;
    }
    
    /**
     * Sets the location of the object
     * 
     * @param location
     */
    public void setLocation(Location location) {
	a = location;
    }
    
    /**
     * Sets the size of the object
     * 
     * @param size
     */
    public void setSize(double size) {
	normalize();
	for (PANode node : b) {
	    node.setOffset(node.getOffset().multiply(size));
	}
	c = size;
    }
    
    private void rotX(double deg) {
	f.setX(f.getX() + deg);
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
    }
    
    private void rotY(double deg) {
	f.setY(f.getY() + deg);
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
    }
    
    private void rotZ(double deg) {
	f.setZ(f.getZ() + deg);
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
    }
}
