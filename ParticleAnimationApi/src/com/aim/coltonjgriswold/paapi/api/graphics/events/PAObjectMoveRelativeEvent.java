package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAObjectMoveRelativeEvent extends PAObjectMoveEvent {

    /**
     * Called when an object moves relative to itself
     * 
     * @param object
     *            The object
     * @param from
     *            The old location
     * @param to
     *            The new location
     */
    public PAObjectMoveRelativeEvent(PAObject object, Vector from, Vector to) {
	super(object, from, to);
    }
}
