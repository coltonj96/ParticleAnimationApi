package com.aim.coltonjgriswold.example;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.example.actions.Actions;
import com.aim.coltonjgriswold.paapi.api.graphics.PAAnimation;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PATris;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAColor;

public class ExampleParticleAnimation extends JavaPlugin implements Listener {
    
    private boolean running;

    public void onEnable() {
	running = false;
	getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void interact(PlayerInteractEvent event) {
	if (event.getAction().equals(Action.LEFT_CLICK_AIR) && !running) {
	    Player player = event.getPlayer();
	    PATris tris = new PATris(Particle.REDSTONE, player.getLocation().add(0, 1, 0), new Vector(1, 1, 0), new Vector(0, -1, 0), new Vector(-1, 1, 0), PAColor.fromRGB(0, 255, 0));
	    tris.setAction(new Actions());
	    PAAnimation animation = new PAAnimation();
	    animation.addObject(tris);
	    animation.start(2);
	    running = true;
	}
    }

}
