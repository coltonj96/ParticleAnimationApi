package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.entity.Entity;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAEntityInteractEvent extends PAObjectEvent {
    
    private Entity a;

    /**
     * Called when an entity interacts with an object
     * 
     * @param object The object
     * @param entity The entity
     */
    public PAEntityInteractEvent(PAObject object, Entity entity) {
	super(object);
	a = entity;
    }

    /**
     * Gets the entity that interacted
     * 
     * @return Entity
     */
    public Entity getEntity() {
	if (!cancelled)
	    return a;
	return null;
    }
}
