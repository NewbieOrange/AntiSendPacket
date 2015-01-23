package com.mengcraft.injector;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Queue;

import net.minecraft.server.v1_7_R4.NetworkManager;

import org.bukkit.command.defaults.BanIpCommand;
import org.bukkit.command.defaults.PardonIpCommand;
import org.bukkit.plugin.Plugin;

public class CheckTask implements Runnable {

	private final List<NetworkManager> managers;
	private final Plugin plugin;
	private final int _kick;
	private final int _ban;
	private final BanIpCommand banner;
	private final PardonIpCommand unbanner;
	private Field input;
	private Field output;

	public CheckTask(List<NetworkManager> managers, Plugin plugin) {
		this.managers = managers;
		this.plugin = plugin;
		this._kick = plugin.getConfig().getInt("limit.kick", 128);
		this._ban = plugin.getConfig().getInt("limit.ban", 256);
		this.banner = new BanIpCommand();
		this.unbanner = new PardonIpCommand();
		setupField();
	}

	@Override
	public void run() {
		List<NetworkManager> list = this.managers;
		for (NetworkManager manager : list) {
			int i = getQueueSize(manager);
			checkKickBan(manager, i);
		}
	}

	private void checkKickBan(NetworkManager manager, int i) {
		if (i > this._ban) {
			InetSocketAddress addr = (InetSocketAddress) manager.getSocketAddress();
			String host = addr.getAddress().getHostAddress();
			this.plugin.getLogger().info("封禁 " + host + " 检测到强烈攻击行为");
			ban(host);
		} else if (i > this._kick) {
			InetSocketAddress addr = (InetSocketAddress) manager.getSocketAddress();
			String host = addr.getAddress().getHostAddress();
			this.plugin.getLogger().info("踢出 " + host + " 检测到轻度攻击行为");
			ban(host);
			unban(host);
		}
	}

	private void ban(String... host) {
		this.banner.execute(this.plugin.getServer().getConsoleSender(), null, host);
	}
	
	private void unban(String... host) {
		this.unbanner.execute(this.plugin.getServer().getConsoleSender(), null, host);
	}

	private void setupField() {
		try {
			Field in = NetworkManager.class.getDeclaredField("k");
			Field out = NetworkManager.class.getDeclaredField("l");
			in.setAccessible(true);
			out.setAccessible(true);
			this.input = in;
			this.output = out;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private int getQueueSize(NetworkManager manager) {
		try {
			Queue<?> in = (Queue<?>) this.input.get(manager);
			Queue<?> out = (Queue<?>) this.output.get(manager);
			return in.size() + out.size();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
