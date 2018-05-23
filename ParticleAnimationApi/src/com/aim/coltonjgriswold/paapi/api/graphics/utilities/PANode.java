package com.aim.coltonjgriswold.paapi.api.graphics.utilities;

import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class PANode {

    private Particle a;
    private Vector b;
    private PAColor c;
    
    public PANode(Particle type, Vector offset) {
	a = type;
	b = offset;
    }
    
    public PAColor getColor() {
	return c;
    }
    
    public Vector getOffset() {
	return b.clone();
    }
    
    public Particle getParticle() {
	return a;
    }
    
    public boolean hasColor() {
	return c != null;
    }
    
    public boolean isColorable() {
	return a.equals(Particle.SPELL_MOB) || a.equals(Particle.SPELL_MOB_AMBIENT) || a.equals(Particle.REDSTONE);
    }
    
    public boolean setColor(PAColor color) {
	if (isColorable()) {
	    c = color;
	    return true;
	}
	return false;
    }
    
    public void setOffset(Vector offset) {
	b = offset;
    }
}
