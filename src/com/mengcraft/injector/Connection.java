package com.mengcraft.injector;

public class Connection
{
    private Object connection;
    private int count;
    
    public Connection(Object minecraftserver, Object networkManager, Object entityPlayer)
    {
        connection = ReflectUtil.createPlayerConnection(minecraftserver, networkManager,
                entityPlayer);
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
        return this.count++;
    }
    
    public int getCount()
    {
        return this.count;
    }
    
    public void resetCount()
    {
        this.count = 0;
    }
    
}
