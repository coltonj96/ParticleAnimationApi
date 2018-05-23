package com.aim.coltonjgriswold.paapi.api.graphics.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class PALine extends PAPoly {

    public PALine(Particle type, Location location, Vector start, Vector end, int line_size) {
	this(type, location, start, end, line_size, null);
    }
    
    public PALine(Particle type, Location location, Vector start, Vector end, int line_size, PAColor color) {
	super(type, location, line_size, color, start, end);
    }
}
