package com.aim.coltonjgriswold.paapi.api.graphics.shapes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class PAPoint extends PAPoly {

    public PAPoint(Particle type, Location location, Vector offset) {
	this(type, location, offset, null);
    }
    
    public PAPoint(Particle type, Location location, Vector offset, PAColor color) {
	super(type, location, 1, color, offset);
    }
}
