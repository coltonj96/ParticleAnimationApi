package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class PAPoint extends PAPoly {

    /**
     * Create a point
     * 
     * @param type Particle type
     * @param location Location in world
     * @param offset Local offset around location
     */
    public PAPoint(Particle type, Location location, Vector offset) {
	this(type, location, offset, null);
    }
    
    /**
     * Create a point
     * 
     * @param type Particle type
     * @param location Location in world
     * @param offset Local offset around location
     * @param color Color of the particle
     */
    public PAPoint(Particle type, Location location, Vector offset, PAColor color) {
	super(type, location, color, offset);
    }
}
