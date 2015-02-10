package com.mengcraft.injector;

import org.bukkit.plugin.java.JavaPlugin;

public class Injector extends JavaPlugin
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
        getServer().getScheduler().runTaskTimer(this, new CheckTask(), 20, 20);
    }
    
}
