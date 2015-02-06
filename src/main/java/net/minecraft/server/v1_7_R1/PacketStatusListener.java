package net.minecraft.server.v1_7_R1;

import java.net.InetSocketAddress;
import java.util.Iterator;

import net.minecraft.server.v1_7_R1.ChatComponentText;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.EnumProtocol;
import net.minecraft.server.v1_7_R1.IChatBaseComponent;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.NetworkManager;
import net.minecraft.server.v1_7_R1.PacketStatusInListener;
import net.minecraft.server.v1_7_R1.PacketStatusInPing;
import net.minecraft.server.v1_7_R1.PacketStatusInStart;
import net.minecraft.server.v1_7_R1.PacketStatusOutPong;
import net.minecraft.server.v1_7_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_7_R1.ServerPing;
import net.minecraft.server.v1_7_R1.ServerPingPlayerSample;
import net.minecraft.server.v1_7_R1.ServerPingServerData;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

import org.bukkit.craftbukkit.v1_7_R1.util.CraftIconCache;
import org.bukkit.entity.Player;

public class PacketStatusListener implements PacketStatusInListener {

	private final MinecraftServer server;
	private final NetworkManager manager;

	private int status = 0;

	@Override
	public void a() {
	}

	@Override
	public void a(IChatBaseComponent ichatbasecomponent) {
	}

	@Override
	public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1) {
		if (enumprotocol1 != EnumProtocol.STATUS) {
			throw new UnsupportedOperationException((new StringBuilder()).append("Unexpected change in protocol to ").append(enumprotocol1).toString());
		}
	}

	@Override
	public void a(PacketStatusInPing packetstatusinping) {
		if (this.status != 1) {
			IChatBaseComponent component = null;
			this.manager.a(component);
			System.out.println("检测到频率异常高的 MOTD 请求");
			System.out.println("梦梦家服务器出租为您保驾护航");
			return;
		}
		this.status = 2;
		this.manager.handle(new PacketStatusOutPong(packetstatusinping.c()), new GenericFutureListener[0]);
	}

	@Override
	public void a(PacketStatusInStart packetstatusinstart) {
		if (this.status != 0) {
			IChatBaseComponent component = null;
			this.manager.a(component);
			System.out.println("检测到频率异常高的 MOTD 请求");
			System.out.println("梦梦家服务器出租为您保驾护航");
			return;
		}
		this.status = 1;
		final Object[] players = this.server.getPlayerList().players.toArray();
		class ServerListPingEvent extends org.bukkit.event.server.ServerListPingEvent {
			CraftIconCache icon = server.server.getServerIcon();

			ServerListPingEvent() {
				super(((InetSocketAddress) manager.getSocketAddress()).getAddress(), server.getMotd(), server.getPlayerList().getMaxPlayers());
			}

			@Override
			public void setServerIcon(org.bukkit.util.CachedServerIcon icon) {
				if (!(icon instanceof CraftIconCache)) {
					throw new IllegalArgumentException(icon + " was not created by " + org.bukkit.craftbukkit.v1_7_R1.CraftServer.class);
				}
				this.icon = (CraftIconCache) icon;
			}

			@Override
			public Iterator<Player> iterator() throws UnsupportedOperationException {
				return new Iterator<Player>() {
					int i;
					int ret = Integer.MIN_VALUE;
					EntityPlayer player;

					@Override
					public boolean hasNext() {
						if (player != null) {
							return true;
						}
						final Object[] currentPlayers = players;
						for (int length = currentPlayers.length, i = this.i; i < length; i++) {
							final EntityPlayer player = (EntityPlayer) currentPlayers[i];
							if (player != null) {
								this.i = i + 1;
								this.player = player;
								return true;
							}
						}
						return false;
					}

					@Override
					public Player next() {
						if (!hasNext()) {
							throw new java.util.NoSuchElementException();
						}
						final EntityPlayer player = this.player;
						this.player = null;
						this.ret = this.i - 1;
						return player.getBukkitEntity();
					}

					@Override
					public void remove() {
						final Object[] currentPlayers = players;
						final int i = this.ret;
						if (i < 0 || currentPlayers[i] == null) {
							throw new IllegalStateException();
						}
						currentPlayers[i] = null;
					}
				};
			}
		}

		ServerListPingEvent event = new ServerListPingEvent();
		this.server.server.getPluginManager().callEvent(event);

		java.util.List<GameProfile> profiles = new java.util.ArrayList<GameProfile>(players.length);
		for (Object player : players) {
			if (player != null) {
				profiles.add(((EntityPlayer) player).getProfile());
			}
		}

		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(event.getMaxPlayers(), profiles.size());
		playerSample.a(profiles.toArray(new GameProfile[profiles.size()]));

		ServerPing ping = new ServerPing();
		ping.setFavicon(event.icon.value);
		ping.setMOTD(new ChatComponentText(event.getMotd()));
		ping.setPlayerSample(playerSample);
		ping.setServerInfo(new ServerPingServerData(this.server.getServerModName() + " " + this.server.getVersion(), 4));

		this.manager.handle(new PacketStatusOutServerInfo(ping));
	}

	public PacketStatusListener(MinecraftServer server, NetworkManager manager) {
		this.server = server;
		this.manager = manager;
	}

}
