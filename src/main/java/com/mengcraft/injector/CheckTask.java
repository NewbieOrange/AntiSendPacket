package com.mengcraft.injector;

import java.util.List;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class CheckTask implements Runnable {

	@Override
	public void run() {
		List<EntityPlayer> list = getEntityPlayers();
		for (EntityPlayer player : list) {
			getConnection(player).resetCount();
		}
	}

	private Connection getConnection(EntityPlayer player) {
		if (player.playerConnection instanceof Connection) {
			return (Connection) player.playerConnection;
		}
		return new Connection(player.server, player.playerConnection.networkManager, player);
	}

	@SuppressWarnings("unchecked")
	private List<EntityPlayer> getEntityPlayers() {
		return MinecraftServer.getServer().getPlayerList().players;
	}
}
