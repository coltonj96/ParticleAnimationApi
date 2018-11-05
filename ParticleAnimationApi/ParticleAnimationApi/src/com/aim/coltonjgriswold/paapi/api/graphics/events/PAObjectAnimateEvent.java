package com.aim.coltonjgriswold.paapi.api.graphics.events;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectAnimateEvent extends PAObjectEvent {

    private long a;

    /**
     * Called when an object is animated
     * 
     * @param object
     *            The object
     * @param period
     *            The time interval
     */
    public PAObjectAnimateEvent(PAObject object, long period) {
	super(object);
	a = period;
    }

    /**
     * Gets the delay between animating an object
     * 
     * @return long
     */
    public long getPeriod() {
	if (!cancelled)
	    return a;
	return -1;
    }
}
