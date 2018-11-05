package com.aim.coltonjgriswold.paapi.api.graphics.geometry.mesh;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

public class PANode {

    private Particle a;
    private Vector b;
    private Vector c;
    private Color d;
    private BlockData e;
    private UUID f;
    private float g;
    private static Map<UUID, PANode> objects;

    static {
	objects = new HashMap<UUID, PANode>();
    }

    /**
     * Utility class used to hold information about object gemoetry
     * 
     * @param type
     *            Particle type
     * @param offset
     *            Offset of this node from objects location
     */
    public PANode(Particle type, Vector offset) {
	a = type;
	b = offset;
	c = offset;
	d = null;
	e = null;
	f = UUID.randomUUID();
	g = 1.0f;
	objects.put(f, this);
    }
    
    /**
     * Gets the color of the node
     * 
     * @return PAColor or null
     */
    public Color getColor() {
	return d;
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
    public BlockData getData() {
	return e;
    }

    /**
     * Gets the uuid of this node
     * 
     * @return UUID
     */
    public UUID getUuid() {
	return f;
    }
    
    /**
     * Gets the size of the particle
     * 
     * @return float
     */
    public float getSize() {
	return g;
    }

    /**
     * Gets if this node can have block data
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
	return e != null;
    }

    /**
     * Gets if this node is colorable depending on its particle
     * 
     * @return boolean
     */
    public boolean isColorable() {
	return a.equals(Particle.REDSTONE);
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
     * @param color
     *            PAColor to use
     * @return boolean
     */
    public boolean setColor(Color color) {
	if (isColorable()) {
	    d = color;
	    return true;
	}
	return false;
    }

    /**
     * Attempts to set the data for this node
     * 
     * @param data
     *            The data value
     * @return boolean
     */
    public boolean setData(BlockData data) {
	if (canSetData()) {
	    e = data;
	    return true;
	}
	return false;
    }

    /**
     * Sets the nodes offset
     * 
     * @param x
     *            Offset x amount
     */
    public void setOffset(double x, double y, double z) {
	setOffset(new Vector(x, y, z));
    }

    /**
     * Sets the nodes offset
     * 
     * @param offset
     *            Offset from the object location
     */
    public void setOffset(Vector offset) {
	b = offset;
    }
    
    /**
     * Sets the size of the particle (Only for Particle.REDSTONE)
     * 
     * @param size
     *            Size of the particle and must be positive and not zero
     */
    public void setSize(float size) {
	if (isColorable() && size > 0)
	    g = size;
    }

    /**
     * Gets a PANode by id
     * 
     * @param uuid
     *            The Uuid
     * @return PANode
     */
    public static PANode getByUuid(UUID uuid) {
	if (objects.containsKey(uuid))
	    return objects.get(uuid);
	return null;
    }
}
