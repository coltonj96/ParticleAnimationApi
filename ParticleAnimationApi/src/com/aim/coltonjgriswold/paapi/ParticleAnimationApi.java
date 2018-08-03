package com.aim.coltonjgriswold.paapi;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PANode;

public class ParticleAnimationApi extends JavaPlugin {

    private static ParticleAnimationApi plugin;

    static {
	ConfigurationSerialization.registerClass(PANode.class, "PANode");
	ConfigurationSerialization.registerClass(PAObject.class, "PAObject");
    }

    public void onEnable() {
	plugin = this;
    }

    public static ParticleAnimationApi instance() {
	return plugin;
    }
}
