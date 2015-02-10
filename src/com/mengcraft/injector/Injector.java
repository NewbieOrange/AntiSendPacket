package com.mengcraft.injector;

import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        if (!ReflectUtil.init())
        {
            getLogger().severe("�ڳ�ʼ������ʱ����, ������Զ�������.");
            setEnabled(false);
            return;
        }
        
        PacketListener.register(this);
        getServer().getScheduler().runTaskTimer(this, new CheckTask(), 20L, 20L);
    }
    
    @Override
    public void onDisable()
    {
        PacketListener.remove();
    }
}
