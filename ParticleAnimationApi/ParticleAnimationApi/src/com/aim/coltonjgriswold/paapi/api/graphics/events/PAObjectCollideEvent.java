package com.aim.coltonjgriswold.paapi.api.graphics.events;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectCollideEvent extends PAObjectEvent {

    private PAObject a;
    
    public PAObjectCollideEvent(PAObject object, PAObject other) {
	super(object);
	a = other;
    }

    /**
     * Gets the other object
     * 
     * @return PAObject
     */
    public PAObject getOther() {
	if (!cancelled)
	    return a;
	return null;
    }
}
