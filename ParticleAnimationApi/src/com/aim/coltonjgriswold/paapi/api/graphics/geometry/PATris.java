package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class PATris extends PAPoly {

    /**
     * Create a Triangle
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param p1
     *            Local point around location
     * @param p2
     *            Local point around location
     * @param p3
     *            Local point around location
     */
    public PATris(Particle type, Location location, Vector p1, Vector p2, Vector p3) {
	this(type, location, p1, p2, p3, null);
    }

    /**
     * Create a Triangle
     * 
     * @param type
     *            Particle type
     * @param location
     *            Location in world
     * @param p1
     *            Local point around location
     * @param p2
     *            Local point around location
     * @param p3
     *            Local point around location
     * @param color
     *            Color of the particle
     */
    public PATris(Particle type, Location location, Vector p1, Vector p2, Vector p3, Color color) {
	super(type, location, color, p1, p2, p3);
    }
}
