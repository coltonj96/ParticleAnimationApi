package com.aim.coltonjgriswold.paapi.api.graphics.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class PAPoly extends PAObject {

    public PAPoly(Particle type, Location location, int line_size, Vector... vertices) {
	this(type, location, line_size, null, vertices);
    }
    
    public PAPoly(Particle type, Location location, int line_size, PAColor color, Vector... vertices) {
	super(location);
	for (int n = 0; n <= vertices.length - 1; n++) {
	    for (double i = 0.0; i < line_size; i++) {
		PANode node = new PANode(type, lerp(vertices[n], vertices[(n + 1) % vertices.length], i / line_size));
		node.setColor(color);
		addNode(node);
	    }
	}
    }
}
