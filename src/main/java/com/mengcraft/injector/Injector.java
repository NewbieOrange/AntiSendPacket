package com.mengcraft.injector;

import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin {

	@Override
	public void onEnable() {
		String version = getServer().getVersion();
		if (version.contains("MCPC")) {
			getLogger().info("Not support MCPC server yet.");
			setEnabled(false);
		} else {
			getServer().getScheduler().runTaskTimer(this, new CheckTask(), 20, 20);
		}
	}

}
