package com.aim.coltonjgriswold.example.shapes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class Cube extends PAObject {

    public Cube(Location location) {
	this(location, 1.0);
    }

    public Cube(Location location, double size) {
	super(location);
	PANode[] nodes = {
		node(Particle.REDSTONE, Color.RED, size, size, size),
		node(Particle.REDSTONE, Color.RED, -size, size, size),
		node(Particle.REDSTONE, Color.RED, -size, size, -size),
		node(Particle.REDSTONE, Color.RED, size, size, -size),
		node(Particle.REDSTONE, Color.RED, size, -size, size),
		node(Particle.REDSTONE, Color.RED, -size, -size, size),
		node(Particle.REDSTONE, Color.RED, -size, -size, -size),
		node(Particle.REDSTONE, Color.RED, size, -size, -size),
		node(Particle.FLAME, Color.ORANGE, 0, 0, 0)
	};
	//nodes[0].connect(nodes[1], nodes[4]);
	//nodes[1].connect(nodes[2], nodes[5]);
	//nodes[2].connect(nodes[3], nodes[6]);
	//nodes[3].connect(nodes[0], nodes[7]);
	//nodes[4].connect(nodes[5]);
	//nodes[5].connect(nodes[6]);
	//nodes[6].connect(nodes[7]);
	//nodes[7].connect(nodes[4]);
	nodes[0].connect(nodes[1], nodes[2], nodes[4], nodes[5], nodes[7]);
	nodes[1].connect(nodes[2], nodes[3], nodes[4], nodes[5], nodes[6]);
	nodes[2].connect(nodes[3], nodes[5], nodes[6], nodes[7]);
	nodes[3].connect(nodes[0], nodes[4], nodes[6], nodes[7]);
	nodes[4].connect(nodes[5], nodes[6]);
	nodes[5].connect(nodes[6], nodes[7]);
	nodes[6].connect(nodes[7]);
	nodes[7].connect(nodes[4]);
	addNodes(nodes);
    }

    private PANode node(Particle type, Color color, double x, double y, double z) {
	PANode node = new PANode(type, new Vector(x, y, z));
	if (node.isColorable())
	    node.setColor(color);
	return node;
    }
}
