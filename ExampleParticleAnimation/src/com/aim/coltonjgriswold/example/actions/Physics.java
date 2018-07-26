package com.aim.coltonjgriswold.example.actions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;

public class Physics extends PAAction {
    
    private double a;
    private static Player b;

    public Physics() {
	a = 0;
	b = null;
    }

    @Override
    public void run(PAObject object) {
	Location loc = object.getLocation();
	Block lblock = loc.getBlock();
	Block rel = lblock.getRelative(BlockFace.DOWN);
	if (!lblock.isEmpty()) {
	    if (lblock.isLiquid())
		object.addVelocity(0, 0.1635, 0);
	    else
		object.move(0, object.getHeight(), 0);
	    a = 0;
	} else if ((rel.isEmpty() || rel.isLiquid()) && (object.getHeight() / 2.0) <= 1.0) {
	    if (rel.isLiquid())
		a -= 0.0545;
	    else
		a -= 0.1635;
	    object.move(0, a, 0);
	} else {
	    if (a != 0)
		a = 0;
	    if (loc.getY() != loc.getBlockY())
		object.move(0, (loc.getBlockY() - loc.getY()) + object.getHeight() / 2.0, 0);
	}
	List<Entity> e = object.getEntities();
	if (!e.isEmpty()) {
	    if (a != 0)
		a = 0;
	}
	if (b != null) {
	    Location l = b.getEyeLocation();
	    object.setLocation(l.add(-2.0 * Math.sin(Math.toRadians(l.getYaw())), -2.0 * Math.sin(Math.toRadians(l.getPitch())), 2.0 * Math.cos(Math.toRadians(l.getYaw()))));
	    a = 0;
	    if (b.isSneaking())
		b = null;
	}
    }
    
    public static void setPlayer(Player player) {
	b = player;
    }
    	//if (a[0] < 10) {
  	//    object.moveRelative(0, 0.1, 0);
  	//    a[0] += 1;
  	//}
  	//if (a[0] == 10 && a[1] < 10) {
  	//    object.moveRelative(0, -0.1, 0);
  	//    a[1] += 1;
  	//}
  	//if (a[0] == 10 && a[1] == 10)
  	//    a = new int[] { 0, 0 };
  	//object.rotate(0, 10, 0);
  	//object.setLocation(b.getEyeLocation());
  	//Vector p = b.getLocation().toVector();
  	//Vector p = b.getLocation().toVector();
  	//Vector o = object.getLocation().toVector();
  	//Vector n = p.subtract(o).normalize();
}
