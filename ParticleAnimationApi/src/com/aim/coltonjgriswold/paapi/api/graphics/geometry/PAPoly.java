package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class PAPoly extends PAObject {

    /**
     * Create a Polygon
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param vertices
     *            Vertices of this polygon
     */
    public PAPoly(Particle type, Location location, Vector... vertices) {
	this(type, location, null, vertices);
    }

    /**
     * Create a Polygon
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param color
     *            Particle color
     * @param vertices
     *            Vertices of this polygon
     */
    public PAPoly(Particle type, Location location, Color color, Vector... vertices) {
	super(location);
	PANode[] nodes = new PANode[vertices.length];
	for (int i = 0; i <= vertices.length - 1; i++) {
	    PANode node = new PANode(type, vertices[i]);
	    node.setColor(color);
	    nodes[i] = node;
	}
	for (int i = 0; i <= vertices.length - 1; i++) {
	    nodes[i].connect(nodes[(i + 1) % nodes.length]);
	}
	addNodes(nodes);
    }
}
