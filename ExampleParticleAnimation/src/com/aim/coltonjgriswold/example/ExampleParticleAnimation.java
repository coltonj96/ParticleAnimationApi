package com.aim.coltonjgriswold.example;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.example.actions.Physics;
import com.aim.coltonjgriswold.example.shapes.Cube;
import com.aim.coltonjgriswold.paapi.api.graphics.PAAnimation;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAEntityInteractEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PALivingEntityInteractEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAObjectMoveEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.events.PAPlayerClickEvent;
import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public class ExampleParticleAnimation extends JavaPlugin implements Listener {
    
    private PAAnimation animation;
    private Cube cube;
    
    public void onEnable() {
	getServer().getPluginManager().registerEvents(this, this);
	cube = new Cube(Bukkit.getWorlds().get(0).getSpawnLocation().add(2.0, 1.0, 0), 0.5);
	cube.setAction(new Physics());
	animation = new PAAnimation();
	animation.addObject(cube);
	animation.start(2L);
    }
    
    @EventHandler
    public void interact(PlayerInteractEvent event) {
	Player player = event.getPlayer();
	if (event.getAction().equals(Action.LEFT_CLICK_AIR) && !cube.isVisible()) {
	    if (!player.hasPermission("paapi.spawn"))
		return;
	    cube.setVisible(true);
	}
    }
    
    @EventHandler
    public void interact(PAEntityInteractEvent event) {
	Entity entity = event.getEntity();
	PAObject object = event.getObject();
	Location loc = object.getLocation();
	Vector v = entity.getLocation().toVector().subtract(loc.toVector()).multiply(-1).normalize().multiply(object.getHitbox().divide(new Vector(2, 2, 2)));
	object.move(v);
	object.addVelocity(entity.getVelocity());
    }
    
    @EventHandler
    public void onMove(PAObjectMoveEvent event) {
	Location loc = event.getObject().getLocation();
	Block block = loc.getBlock();
	if (block.isLiquid()) {
	    switch (block.getType()) {
	    case LAVA:
		cube.setVisible(false);
		event.getObject().setLocation(Bukkit.getWorlds().get(0).getSpawnLocation().add(2.0, 1.0, 0));
		break;
	    default:
		break;
	    }
	}
	if (loc.getY() <= 1.0) {
	    cube.setVisible(false);
	    event.getObject().setLocation(Bukkit.getWorlds().get(0).getSpawnLocation().add(2.0, 1.0, 0));
	}
    }
    
    @EventHandler
    public void interact(PALivingEntityInteractEvent event) {
	LivingEntity e = event.getLivingEntity();
	e.damage(1.0);
    }
    
    @EventHandler
    public void onClick(PAPlayerClickEvent event) {
	Player p = event.getPlayer();
	Physics.setPlayer(p);
    }
}
