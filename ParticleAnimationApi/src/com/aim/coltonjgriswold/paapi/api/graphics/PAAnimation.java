package com.aim.coltonjgriswold.paapi.api.graphics;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.aim.coltonjgriswold.paapi.ParticleAnimationApi;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAAnimation {
    
    private Set<PAObject> a;
    private BukkitTask b;

    /**
     * Create a new animation to put objects into
     */
    public PAAnimation() {
	a = new HashSet<PAObject>();
    }

    /**
     * Add objects to this animation
     * 
     * @param objects Objects to add
     */
    public void addObjects(PAObject... objects) {
	for (PAObject object : objects) {
	    if (!a.contains(object))
		a.add(object);
	}
    }
    
    /**
     * Add an object to this animation
     * 
     * @param object Object to add
     */
    public void addObject(PAObject object) {
	if (!a.contains(object))
	    a.add(object);
    }
    
    /**
     * Remove objects from this animation
     * 
     * @param objects Objects to remove
     */
    public void removeObjects(PAObject... objects) {
	for (PAObject object : objects) {
	    if (a.contains(object))
		a.remove(object);
	}
    }
    
    /**
     * Remove an object from this animation
     * 
     * @param object Object to remove
     */
    public void removeObject(PAObject object) {
	if (a.contains(object))
	    a.remove(object);
    }
    
    /**
     * Start running the animation
     * 
     * @param period Ticks to wait before updating the animation
     */
    public void start(long period) {
	if (a.size() == 0)
	    return;
	b = new BukkitRunnable() {
	    
	    @Override
	    public void run() {
		if (a.size() == 0) {
		    cancel();
		    return;
		}
		for (PAObject object : a) {
		    if (object.getAction() != null)
			object.getAction().run(object);
		    object.draw();
		}
	    }
	    
	}.runTaskTimer(ParticleAnimationApi.instance(), 0, period);
    }
    
    /**
     * Stops the animation
     */
    public void end() {
	if (b != null && !b.isCancelled())
	    b.cancel();
    }
    
    /**
     * Gets if the animation is running
     * 
     * @return boolean
     */
    public boolean isRunning() {
	return !b.isCancelled();
    }
}
