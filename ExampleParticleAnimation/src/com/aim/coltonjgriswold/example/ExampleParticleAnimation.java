package com.aim.coltonjgriswold.example;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.aim.coltonjgriswold.example.actions.Actions;
import com.aim.coltonjgriswold.example.shapes.Cube;
import com.aim.coltonjgriswold.paapi.api.graphics.PAAnimation;
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
	    Cube cube = new Cube(Particle.REDSTONE, player.getLocation(), PAColor.fromRGB(255, 0, 0), 0.75);
	    cube.setAction(new Actions(cube));
	    PAAnimation animation = new PAAnimation();
	    animation.addObject(cube);
	    animation.start(2);
	    running = true;
	}
    }

}
