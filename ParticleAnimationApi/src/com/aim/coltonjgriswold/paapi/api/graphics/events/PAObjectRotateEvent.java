package com.aim.coltonjgriswold.paapi.api.graphics.events;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAQuaternion;

public class PAObjectRotateEvent extends PAObjectEvent {

    private PAQuaternion a;
    private PAQuaternion b;

    /**
     * Called when an object is rotated
     * 
     * @param object
     *            The object
     * @param from
     *            The old rotation
     * @param to
     *            The new rotation
     */
    public PAObjectRotateEvent(PAObject object, PAQuaternion from, PAQuaternion to) {
	super(object);
	a = from;
	b = to;
    }

    /**
     * Gets the old rotation of the object
     * 
     * @return PAQuaternion
     */
    public PAQuaternion getFrom() {
	if (!cancelled)
	    return a;
	return null;
    }

    /**
     * Gets the new rotation of the object
     * 
     * @return PAQuaternion
     */
    public PAQuaternion getTo() {
	if (!cancelled)
	    return b;
	return null;
    }
}
