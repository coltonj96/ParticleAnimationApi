package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAPlayerInteractEvent extends PALivingEntityInteractEvent {

    private Player a;

    /**
     * Called when a player interacts with an object
     * 
     * @param object
     *            The object
     * @param player
     *            The player
     */
    public PAPlayerInteractEvent(PAObject object, Player player) {
	super(object, player);
	a = player;
    }

    /**
     * Gets the player
     * 
     * @return Player
     */
    public Player getPlayer() {
	if (!cancelled)
	    return a;
	return null;
    }
}
