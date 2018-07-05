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
	    PATris tris1 = new PATris(Particle.REDSTONE, player.getEyeLocation().add(0, 1, 0), new Vector(0.75, 0.75, 0), new Vector(0, -0.75, 0), new Vector(-0.75, 0.75, 0), PAColor.fromRGB(255, 0, 0));
	    PATris tris2 = new PATris(Particle.REDSTONE, player.getEyeLocation().add(0, 1, 0), new Vector(0.5, 0.5, 0), new Vector(0, -0.5, 0), new Vector(-0.5, 0.5, 0), PAColor.fromRGB(255, 0, 0));
	    tris1.setAction(new Actions(event.getPlayer()));
	    tris2.setAction(new Actions(event.getPlayer()));
	    PAAnimation animation = new PAAnimation();
	    animation.addObjects(tris1, tris2);
	    animation.start(2);
	    running = true;
	}
    }

}
