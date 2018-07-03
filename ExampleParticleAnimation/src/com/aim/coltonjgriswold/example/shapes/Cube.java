package com.aim.coltonjgriswold.example.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class Cube extends PAObject {

    public Cube(Particle type, Location location, PAColor color) {
	this(type, location, color, 1.0);
    }

    public Cube(Particle type, Location location, PAColor color, double size) {
	super(location);
	PANode[] nodes = {
		node(type, color, size, size, size),
		node(type, color, -size, size, size),
		node(type, color, -size, size, -size),
		node(type, color, size, size, -size),
		node(type, color, size, -size, size),
		node(type, color, -size, -size, size),
		node(type, color, -size, -size, -size),
		node(type, color, size, -size, -size)
	};
	nodes[0].connect(nodes[1], nodes[4]);
	nodes[1].connect(nodes[2], nodes[5]);
	nodes[2].connect(nodes[3], nodes[6]);
	nodes[3].connect(nodes[0], nodes[7]);
	nodes[4].connect(nodes[5]);
	nodes[5].connect(nodes[6]);
	nodes[6].connect(nodes[7]);
	nodes[7].connect(nodes[4]);
	addNodes(nodes);
    }

    private PANode node(Particle type, PAColor color, double x, double y, double z) {
	PANode node = new PANode(type, new Vector(x, y, z));
	node.setColor(color);
	return node;
    }
}
