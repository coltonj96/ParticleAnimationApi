package com.aim.coltonjgriswold.paapi.api.graphics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAxis;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class PAObject {
    
    private Location a;
    private List<PANode> b;
    
    public PAObject(Location location) {
	this(location, new ArrayList<PANode>());
    }
    
    public PAObject(Location location, List<PANode> nodes) {
	a = location;
	b = nodes;
    }
    
    public void draw() {
	for (PANode node : b) {
	    if (node.isColorable() && node.hasColor()) {
		double red = ((node.getColor().getRed() + 1.0) / 255.0);
		double green = node.getColor().getGreen() / 255.0;
		double blue = node.getColor().getBlue() / 255.0;
		a.getWorld().spawnParticle(node.getParticle(), a.clone().add(node.getOffset()), 0, red, green, blue, 1.0);
	    } else {
		a.getWorld().spawnParticle(node.getParticle(), a.clone().add(node.getOffset()), 0, 0, 0, 0, 1.0);
	    }
	}
    }
    
    public Location getLocation() {
	return a;
    }
    
    protected void addNode(PANode node) {
	b.add(node);
    }
    
    protected Vector lerp(Vector start, Vector end, double percent) {
	return start.clone().add(end.clone().subtract(start).multiply(percent));
    }
    
    protected void removeNode(int index) {
	b.remove(index);
    }
    
    public void move(PAAxis axis, double amount) {
	switch (axis) {
	case X_AXIS:
	    a.add(amount, 0, 0);
	    break;
	case Y_AXIS:
	    a.add(0, amount, 0);
	    break;
	case Z_AXIS:
	    a.add(0, 0, amount);
	    break;
	default:
	    break;
	}
    }
    
    public void resize(double factor) {
	for (PANode node : b) {
	    node.setOffset(node.getOffset().multiply(factor));
	}
    }
    
    public void rotate(PAAxis axis, double theta) {
	theta = Math.toRadians(theta);
	double sin = Math.sin(theta);
	double cos = Math.cos(theta);
	for (PANode node : b) {
	    Vector v = node.getOffset();
	    double x = v.getX();
	    double y = v.getY();
	    double z = v.getZ();
	    double x2 = 0;
	    double y2 = 0;
	    double z2 = 0;
	    switch (axis) {
	    case X_AXIS:
		x2 = x;
		y2 = (y * cos) - (z * sin);
		z2 = (z * cos) + (y * sin);
		break;
	    case Y_AXIS:
		x2 = (x * cos) - (z * sin);
		y2 = y;
		z2 = (z * cos) + (x * sin);
		break;
	    case Z_AXIS:
		x2 = (x * cos) - (y * sin);
		y2 = (y * cos) + (x * sin);
		z2 = z;
		break;
	    default:
		break;
	    }
	    node.setOffset(new Vector(x2, y2, z2));
	}
    }
    
    public void setLocation(Location location) {
	a = location;
    }
}
