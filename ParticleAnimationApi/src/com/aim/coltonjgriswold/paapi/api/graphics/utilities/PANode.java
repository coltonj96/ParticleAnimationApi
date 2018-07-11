package com.aim.coltonjgriswold.paapi.api.graphics.utilities;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Particle;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class PANode {

    private Particle a;
    private Vector b;
    private Vector c;
    private PAColor d;
    private Set<PANode> e;
    private MaterialData f;
    
    /**
     * Utility class used to hold information about object gemoetry
     * 
     * @param type Particle type
     * @param offset Offset of this node from objects location
     */
    public PANode(Particle type, Vector offset) {
	a = type;
	b = offset;
	c = offset;
	e = new HashSet<PANode>();
    }
    
    /**
     * Connect this node to other nodes
     * 
     * @param nodes
     */
    public void connect(PANode... nodes) {
	for (PANode node : nodes) {
	    if (!e.contains(node))
		e.add(node);
	}
    }
    
    /**
     * Connect this node to another
     * 
     * @param node
     */
    public void connect(PANode node) {
	if (!e.contains(node))
	    e.add(node);
    }
    
    /**
     * disconnect this node from another
     * 
     * @param node
     */
    public void disconnect(PANode node) {
	if (!node.equals(this) && e.contains(node))
	    e.remove(node);
    }
    
    /**
     * disconnect this node from others
     * 
     * @param nodes
     */
    public void disconnect(PANode... nodes) {
	for (PANode node : nodes) {
	    if (!node.equals(this) && e.contains(node))
		e.remove(node);
	}
    }
    
    /**
     * disconnect this node from another
     * 
     * @param index
     */
    public void disconnect(int index) {
	if (index < e.size())
	    e.remove(index);
    }
    
    /**
     * Gets the color of the node
     * 
     * @return PAColor or null
     */
    public PAColor getColor() {
	return d;
    }
    
    /**
     * Get all nodes this node connects to
     * 
     * @return Set<PANode>
     */
    public Set<PANode> getConnectedNodes() {
	return e;
    }
    
    /**
     * Gets the offset of this node
     * 
     * @return Vector
     */
    public Vector getOffset() {
	return b.clone();
    }
    
    /**
     * Gets the Particle associated with this node
     * 
     * @return Particle
     */
    public Particle getParticle() {
	return a;
    }
    
    /**
     * Gets the value of the particle data
     * 
     * @return Integer
     */
    public MaterialData getData() {
	return f;
    }
    
    /**
     * Gets if this node can have extra data
     * 
     * @return boolean
     */
    public boolean canSetData() {
	return a.equals(Particle.BLOCK_CRACK) || a.equals(Particle.BLOCK_DUST) || a.equals(Particle.FALLING_DUST);
    }
    
    /**
     * Gets if this node has a color
     * 
     * @return boolean
     */
    public boolean hasColor() {
	return d != null;
    }
    
    /**
     * Gets if this node has extra data
     * 
     * @return boolean
     */
    public boolean hasData() {
	return f != null;
    }
    
    /**
     * Gets if this node is colorable depending on its particle
     * 
     * @return boolean
     */
    public boolean isColorable() {
	return a.equals(Particle.SPELL_MOB) || a.equals(Particle.SPELL_MOB_AMBIENT) || a.equals(Particle.REDSTONE);
    }
    
    /**
     * Resets the offset to the original value that was initalized
     */
    public void resetOffset() {
	b = c;
    }
    
    /**
     * Attempts to set the color of this node
     * 
     * @param color PAColor to use
     * @return boolean
     */
    public boolean setColor(PAColor color) {
	if (isColorable()) {
	    d = color;
	    return true;
	}
	return false;
    }
    
    /**
     * Attempts to set the data for this node
     * 
     * @param value The data value
     * @return boolean
     */
    public boolean setData(MaterialData data) {
	if (canSetData()) {
	    f = data;
	    return true;
	}
	return false;
    }
    
    /**
     * Sets the nodes offset
     * 
     * @param x Offset x amount
     */
    public void setOffset(double x, double y, double z) {
	setOffset(new Vector(x, y, z));
    }
    
    /**
     * Sets the nodes offset
     * 
     * @param offset Offset from the object location
     */
    public void setOffset(Vector offset) {
	b = offset;
    }
}
