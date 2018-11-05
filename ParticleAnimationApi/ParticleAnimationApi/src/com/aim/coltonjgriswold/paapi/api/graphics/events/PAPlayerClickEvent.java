package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.paapi.api.graphics.enums.PAButton;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class PAPlayerClickEvent extends PAPlayerInteractEvent {

    private PAButton a;

    /**
     * Called when a player clicks on an object
     * 
     * @param object
     *            The object
     * @param player
     *            The player
     * @param button
     *            The button
     */
    public PAPlayerClickEvent(PAObject object, Player player, PAButton button) {
	super(object, player);
	a = button;
    }

    /**
     * Gets the button involved
     * 
     * @return PAButton
     */
    public PAButton getButton() {
	if (!cancelled)
	    return a;
	return null;
    }
}
