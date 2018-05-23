package com.aim.coltonjgriswold.paapi.api.graphics.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class PAQuad extends PAPoly {
    
    public PAQuad(Particle type, Location location, Vector top_left, Vector top_right, Vector bottom_left, Vector bottom_right, int line_size) {
	this(type, location, top_left, top_right, bottom_left, bottom_right, line_size, null);
    }
    
    public PAQuad(Particle type, Location location, Vector p1, Vector p2, Vector p3, Vector p4, int line_size, PAColor color) {
	super(type, location, line_size, color, p1, p2, p3, p4);
    }
}
