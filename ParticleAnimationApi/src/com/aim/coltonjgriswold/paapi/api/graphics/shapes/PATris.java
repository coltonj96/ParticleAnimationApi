package com.aim.coltonjgriswold.paapi.api.graphics.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class PATris extends PAPoly {

    public PATris(Particle type, Location location, Vector p1, Vector p2, Vector p3, int line_size) {
	this(type, location, p1, p2, p3, line_size, null);
    }
    
    public PATris(Particle type, Location location, Vector p1, Vector p2, Vector p3, int line_size, PAColor color) {
	super(type, location, line_size, color, p1, p2, p3);
    }
}
