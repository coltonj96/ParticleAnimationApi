package com.aim.coltonjgriswold.paapi;

import org.bukkit.plugin.java.JavaPlugin;

public class ParticleAnimationApi extends JavaPlugin {

    private static ParticleAnimationApi plugin;

    public void onEnable() {
	plugin = this;
    }

    public static ParticleAnimationApi instance() {
	return plugin;
    }
}
