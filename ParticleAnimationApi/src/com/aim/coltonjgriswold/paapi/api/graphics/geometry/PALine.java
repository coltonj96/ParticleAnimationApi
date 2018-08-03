package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class PALine extends PAPoly {

    /**
     * Creat a line
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param start
     *            Local start around location
     * @param end
     *            Local end around location
     */
    public PALine(Particle type, Location location, Vector start, Vector end) {
	this(type, location, start, end, null);
    }

    /**
     * Creat a line
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param start
     *            Local start around location
     * @param end
     *            Local end around location
     * @param color
     *            Color of the particle
     */
    public PALine(Particle type, Location location, Vector start, Vector end, Color color) {
	super(type, location, color, start, end);
    }
}
