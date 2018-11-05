package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PALivingEntityInteractEvent extends PAEntityInteractEvent {

    private LivingEntity a;

    /**
     * Called when a LivingEntity interacts with an object
     * 
     * @param object
     *            The object
     * @param entity
     *            The entity
     */
    public PALivingEntityInteractEvent(PAObject object, LivingEntity entity) {
	super(object, entity);
	a = entity;
    }

    /**
     * Gets the LivingEntity
     * 
     * @return LivingEntity
     */
    public LivingEntity getLivingEntity() {
	if (!cancelled)
	    return a;
	return null;
    }
}
