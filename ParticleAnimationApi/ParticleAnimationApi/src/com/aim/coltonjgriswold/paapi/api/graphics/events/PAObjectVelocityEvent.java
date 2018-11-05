package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectVelocityEvent extends PAObjectEvent {

    private Vector a;

    /**
     * Called whenever an object has a velocity
     * 
     * @param object
     *            The Object
     * @param velocity
     *            The Velocity
     */
    public PAObjectVelocityEvent(PAObject object, Vector velocity) {
	super(object);
	a = velocity;
    }

    /**
     * Gets the velocity of the object in this event
     * 
     * @return Vector
     */
    public Vector getVelocity() {
	if (!cancelled)
	    return a;
	return null;
    }
}
