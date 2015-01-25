package com.mengcraft.injector;

import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getScheduler().runTaskTimer(this, new CheckTask(), 20, 20);
	}

}
