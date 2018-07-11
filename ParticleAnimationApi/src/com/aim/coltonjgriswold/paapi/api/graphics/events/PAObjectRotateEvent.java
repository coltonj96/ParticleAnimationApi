package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectRotateEvent extends PAObjectEvent {
    
    private Vector a;
    private Vector b;
    
    /**
     * Called when an object is rotated
     * 
     * @param object The object
     * @param from The old rotation
     * @param to The new rotation
     */
    public PAObjectRotateEvent(PAObject object, Vector from, Vector to) {
	super(object);
	a = from;
	b = to;
    }

    /**
     * Gets the old rotation of the object
     * 
     * @return Vector
     */
    public Vector getFrom() {
	if (!cancelled)
	    return a;
	return null;
    }
    
    /**
     * Gets the new rotation of the object
     * 
     * @return Vector
     */
    public Vector getTo() {
	if (!cancelled)
	    return b;
	return null;
    }
}
