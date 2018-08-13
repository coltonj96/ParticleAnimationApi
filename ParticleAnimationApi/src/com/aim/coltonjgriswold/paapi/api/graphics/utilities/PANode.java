package com.aim.coltonjgriswold.paapi.api.graphics.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

@SerializableAs("PANode")
public class PANode implements ConfigurationSerializable {

    private Particle a;
    private Vector b;
    private Vector c;
    private Color d;
    private Set<PANode> e;
    private BlockData f;
    private UUID g;
    private static Map<UUID, PANode> objects;

    static {
	objects = new HashMap<UUID, PANode>();
    }

    /**
     * Contructor for deserializing
     * 
     * @param object
     *            The PANode to deserialize
     */
    public PANode(Map<String, Object> object) {
	a = Particle.valueOf((String) object.get("particle"));
	if (object.containsKey("offset")) {
	    Object raw = object.get("offset");
	    if (raw instanceof Map) {
		Map<?, ?> rawmap = (Map<?, ?>) raw;
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
		    map.put(entry.getKey().toString(), entry.getValue());
		}
		b = Vector.deserialize(map);
	    }
	}
	c = b;
	if (object.containsKey("color") && isColorable()) {
	    Object raw = object.get("color");
	    if (raw instanceof Map) {
		Map<?, ?> rawmap = (Map<?, ?>) raw;
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<?, ?> entry : rawmap.entrySet()) {
		    map.put(entry.getKey().toString(), entry.getValue());
		}
		d = Color.deserialize(map);
	    }
	}
	e = new HashSet<PANode>();
	if (object.containsKey("connected")) {
	    Object raw = object.get("connected");
	    if (raw instanceof List) {
		List<?> rawlist = (List<?>) raw;
		if (!rawlist.isEmpty()) {
		    for (Object s : rawlist) {
			if (s instanceof String) {
			    PANode node = PANode.getByUuid(UUID.fromString((String) s));
			    if (!e.contains(node))
				e.add(node);
			}
		    }
		}
	    }
	}
	if (object.containsKey("data") && canSetData()) {
	    f = Bukkit.createBlockData((String) object.get("data"));
	}
	g = UUID.fromString((String) object.get("uuid"));
	if (!objects.containsKey(g))
	    objects.put(g, this);
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
	e = new HashSet<PANode>();
	g = UUID.randomUUID();
	objects.put(g, this);
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

    public boolean isConnected(PANode node) {
	return e.contains(node);
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
    public BlockData getData() {
	return f;
    }

    /**
     * Gets the uuid of this node
     * 
     * @return UUID
     */
    public UUID getUuid() {
	return g;
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
	    f = data;
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

    @Override
    public Map<String, Object> serialize() {
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	/// List<Map<String, Object>> connected = new ArrayList<Map<String,
	/// Object>>();
	List<String> connected = new ArrayList<String>();
	result.put("particle", a.name());
	result.put("offset", c.serialize());
	if (d != null)
	    result.put("color", d.serialize());
	for (PANode node : getConnectedNodes()) {
	    if (!connected.contains(node.g.toString()))
		connected.add(node.g.toString());
	}
	result.put("connected", connected);
	if (f != null)
	    result.put("data", f.getAsString());
	result.put("uuid", g.toString());
	return result;
    }

    /**
     * Deserialize a PANode
     * 
     * @param object
     *            The PANode to deserialize
     * 
     * @return PANode
     */
    public static PANode deserialize(Map<String, Object> object) {
	return new PANode(object);
    }
}
