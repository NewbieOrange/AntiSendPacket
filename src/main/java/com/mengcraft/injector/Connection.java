package com.mengcraft.injector;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PacketPlayInTabComplete;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class Connection extends PlayerConnection {

	private int count;

	public Connection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
		super(minecraftserver, networkmanager, entityplayer);
	}

	@Override
	public void a(PacketPlayInTabComplete packet) {
		if (checkCount() < 16) {
			super.a(packet);
		} else {
			disconnect("You are a cracker?");
		}
	}

	private int checkCount() {
		return this.count++;
	}

	public int getCount() {
		return this.count;
	}

	public void resetCount() {
		this.count = 0;
	}

}
