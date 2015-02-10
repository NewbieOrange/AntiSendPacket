package com.mengcraft.injector;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        if (!ReflectUtil.init())
        {
            getLogger().severe("在初始化反射时出错, 插件将自动被禁用.");
            setEnabled(false);
            return;
        }
        
        PacketListener.register(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().runTaskTimer(this, new CheckTask(), 20L, 20L);
    }
    
    @Override
    public void onDisable()
    {
        PacketListener.remove();
    }
    
    @EventHandler
    public void onPlayerQuiting(PlayerQuitEvent event)
    {
        Connection
                .removeConnectionInCache(ReflectUtil.getEntityPlayer(event.getPlayer()));
    }
    
    @EventHandler
    public void onPlayerBeingKicked(PlayerKickEvent event)
    {
        Connection
                .removeConnectionInCache(ReflectUtil.getEntityPlayer(event.getPlayer()));
    }
}
