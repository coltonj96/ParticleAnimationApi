package com.aim.coltonjgriswold.paapi.api.graphics.events;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectEvent extends PAEvent {
    
    private PAObject a;

    /**
     * An object related event
     * 
     * @param object The object
     */
    public PAObjectEvent(PAObject object) {
	a = object;
    }

    /**
     * Gets the object
     * 
     * @return PAObject
     */
    public PAObject getObject() {
	if (!cancelled)
	    return a;
	return null;
    }
}
