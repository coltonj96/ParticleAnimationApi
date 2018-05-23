package com.aim.coltonjgriswold.paapi;

import org.bukkit.plugin.java.JavaPlugin;

public class ParticleAnimationApi extends JavaPlugin {

    public void onEnable() {}
    
    public void onDisable() {}
    
    public static ParticleAnimationApi instance() {
	return getPlugin(ParticleAnimationApi.class);
    }
}
