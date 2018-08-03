package com.aim.coltonjgriswold.paapi.api.graphics.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PAEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    protected boolean cancelled;

    public PAEvent() {
	cancelled = false;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }

    @Override
    public boolean isCancelled() {
	return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
	cancelled = cancel;
    }

}
