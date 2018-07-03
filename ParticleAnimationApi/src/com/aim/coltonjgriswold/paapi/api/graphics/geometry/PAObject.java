package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public abstract class PAObject {
    
    private Location a;
    private Set<PANode> b;
    private double c;
    private UUID d;
    private PAAction e;
    
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
	e = new PAAction() {

	    @Override
	    public void run() {
		draw();
	    }
	    
	};
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
			    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, red, green, blue, 1.0);
			} else {
			    a.getWorld().spawnParticle(node.getParticle(), a.clone().add(v), 0, 0, 0, 0, 1.0);
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
	a.add(x, y, z);
    }
    
    /**
     * Move this object
     * 
     * @param vector A vector
     */
    public void move(Vector vector) {
	a.add(vector);
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
	rotateX(degreesX);
	rotateY(degreesY);
	rotateZ(degreesZ);
    }
    
    /**
     * Rotates on the X-axis
     * 
     * @param degrees
     */
    public void rotateX(double degrees) {
	double theta = Math.toRadians(degrees);
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
    
    /**
     * Rotates on the Y-axis
     * 
     * @param degrees
     */
    public void rotateY(double degrees) {
	double theta = Math.toRadians(degrees);
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
    
    /**
     * Rotates on the Z-axis
     * 
     * @param degrees
     */
    public void rotateZ(double degrees) {
	double theta = Math.toRadians(degrees);
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
    
    /**
     * Get the action for this object
     * 
     * @return PAAction
     */
    public PAAction getAction() {
	return e;
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
}
