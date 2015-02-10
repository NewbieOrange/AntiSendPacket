package com.mengcraft.injector;

import java.util.HashMap;
import java.util.Map;

public class Connection
{
    private static final Map<Object, Connection> connections = new HashMap<Object, Connection>();
    
    private Object connection;
    private int count = 0;
    
    public Connection(Object minecraftserver, Object networkManager, Object entityPlayer)
    {
        connection = ReflectUtil.createPlayerConnection(minecraftserver, networkManager,
                entityPlayer);
        connections.put(entityPlayer, this);
    }
    
    public void handlePacket(Object packet)
    {
        if (checkCount() < 16)
        {
            ReflectUtil.handlePacket(connection, packet);
        }
        else
        {
            ReflectUtil.disconnect(connection,
                    "Tab complete exploit is banned in this server.");
        }
    }
    
    private int checkCount()
    {
        return count++;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void resetCount()
    {
        count = 0;
    }
    
    public static Connection getConnectionByEntityPlayer(Object entityPlayer)
    {
        return connections.get(entityPlayer);
    }
}
