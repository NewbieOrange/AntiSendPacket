package com.mengcraft.injector;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.ServerConnection;

import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			MinecraftServer server = MinecraftServer.getServer();
			ServerConnection connection = server.ai();
			List<NetworkManager> managers = getNetworkManagers(connection);
			getServer().getScheduler().runTaskTimer(this, new CheckTask(managers, this), 20, 20);
		} catch (Exception e) {
			e.printStackTrace();
			setEnabled(false);
		}
	}

	@SuppressWarnings("unchecked")
	private List<NetworkManager> getNetworkManagers(ServerConnection in) throws Exception {
		Field field = ServerConnection.class.getDeclaredField("f");
		field.setAccessible(true);
		List<NetworkManager> out = (List<NetworkManager>) field.get(in);
		return out;
	}

}
