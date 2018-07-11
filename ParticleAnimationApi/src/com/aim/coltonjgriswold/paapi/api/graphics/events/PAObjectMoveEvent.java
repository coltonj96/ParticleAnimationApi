package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectMoveEvent extends PAObjectEvent {
    
    private Vector a;
    private Vector b;

    /**
     * Called when an object moves in relation to the world space
     * 
     * @param object The object
     * @param from The old location
     * @param to The new location
     */
    public PAObjectMoveEvent(PAObject object, Vector from, Vector to) {
	super(object);
	a = from;
	b = to;
    }

    /**
     * Gets the old location
     * 
     * @return Vector
     */
    public Vector getFrom() {
	if (!cancelled)
	    return a;
	return null;
    }
    
    /**
     * Gets the new location
     * 
     * @return Vector
     */
    public Vector getTo() {
	if (!cancelled)
	    return b;
	return null;
    }
}
