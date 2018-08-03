package com.aim.coltonjgriswold.paapi.api.graphics.geometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class PAQuad extends PAPoly {

    /**
     * Create a Quadrilateral
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
     * @param p4
     *            Local point around location
     */
    public PAQuad(Particle type, Location location, Vector p1, Vector p2, Vector p3, Vector p4) {
	this(type, location, p1, p2, p3, p4, null);
    }

    /**
     * Create a Quadrilateral
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
     * @param p4
     *            Local point around location
     * @param color
     *            Color of the particle
     */
    public PAQuad(Particle type, Location location, Vector p1, Vector p2, Vector p3, Vector p4, Color color) {
	super(type, location, color, p1, p2, p3, p4);
    }
}
