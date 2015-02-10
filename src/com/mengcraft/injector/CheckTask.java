package com.mengcraft.injector;

import java.util.List;

public class CheckTask implements Runnable
{
    @Override
    public void run()
    {
        List<Object> list = ReflectUtil.getEntityPlayers();
        for (Object player : list)
        {
            ReflectUtil.getConnection(player).resetCount();
        }
    }
}
