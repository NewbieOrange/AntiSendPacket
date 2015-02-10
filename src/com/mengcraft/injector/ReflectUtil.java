package com.mengcraft.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.entity.Player;

import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.utility.MinecraftReflection;

public class ReflectUtil
{
    private static Object playerList;
    
    private static Constructor<?> playerConnectionConstructor;
    
    private static Class<?> craftPlayerClass;
    
    private static Method craftPlayerGetHandle;
    private static Method handlePacket;
    private static Method disconnect;
    
    private static Field players;
    private static Field playerServer;
    private static Field playerConnection;
    private static Field playerConnectionNetworkManager;
    
    public static boolean init()
    {
        try
        {
            Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
            Class<?> networkManagerClass = MinecraftReflection.getNetworkManagerClass();
            Class<?> entityPlayerClass = MinecraftReflection.getEntityPlayerClass();
            Class<?> playerListClass = MinecraftReflection.getPlayerListClass();
            Class<?> inTabCompleteClass = MinecraftReflection
                    .getMinecraftClass("PacketPlayInTabComplete");
            
            craftPlayerClass = MinecraftReflection.getCraftPlayerClass();
            craftPlayerGetHandle = FuzzyReflection
                    .fromClass(craftPlayerClass, true)
                    .getMethodListByParameters(
                            MinecraftReflection.getEntityPlayerClass(), new Class[] {})
                    .get(0);
            
            FuzzyReflection fuzzyServer = FuzzyReflection.fromClass(serverClass, true);
            
            Method getServer = fuzzyServer.getMethodListByParameters(serverClass,
                    new Class[] {}).get(0);
            Method getPlayerList = fuzzyServer.getMethodListByParameters(playerListClass,
                    new Class[] {}).get(0);
            
            Object server = getServer.invoke(serverClass, (Object[]) null);
            playerList = getPlayerList.invoke(server, (Object[]) null);
            
            FuzzyReflection fuzzyPlayerList = FuzzyReflection.fromClass(playerListClass,
                    true);
            
            players = fuzzyPlayerList.getFieldListByType(List.class).get(0);
            players.setAccessible(true);
            
            FuzzyReflection fuzzyEntityPlayer = FuzzyReflection.fromClass(
                    entityPlayerClass, true);
            Class<?> playerConnectionClass = MinecraftReflection
                    .getMinecraftClass("PlayerConnection");
            playerConnectionConstructor = playerConnectionClass.getDeclaredConstructor(
                    serverClass, networkManagerClass, entityPlayerClass);
            
            playerConnection = fuzzyEntityPlayer
                    .getFieldListByType(playerConnectionClass).get(0);
            playerConnection.setAccessible(true);
            playerServer = fuzzyEntityPlayer.getFieldListByType(
                    MinecraftReflection.getMinecraftServerClass()).get(0);
            playerServer.setAccessible(true);
            
            FuzzyReflection fuzzyPlayerConnection = FuzzyReflection.fromClass(
                    playerConnectionClass, true);
            playerConnectionNetworkManager = fuzzyPlayerConnection.getFieldListByType(
                    networkManagerClass).get(0);
            playerConnectionNetworkManager.setAccessible(true);
            
            handlePacket = fuzzyPlayerConnection.getMethodListByParameters(void.class,
                    new Class[] { inTabCompleteClass }).get(0);
            disconnect = fuzzyPlayerConnection.getMethodListByParameters(void.class,
                    new Class[] { String.class }).get(0);
            
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Connection getConnection(Object entityPlayer)
    {
        Connection connection = Connection.getConnectionByEntityPlayer(entityPlayer);
        
        if (connection != null)
            return connection;
        else
            return new Connection(getPlayerServer(entityPlayer),
                    getNetworkManager(getPlayerConnection(entityPlayer)), entityPlayer);
    }
    
    public static Object getEntityPlayer(Player player)
    {
        try
        {
            Object craftPlayer = craftPlayerClass.cast(player);
            Object entityPlayer = craftPlayerGetHandle.invoke(craftPlayer);
            
            return entityPlayer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> getEntityPlayers()
    {
        try
        {
            return (List<Object>) players.get(playerList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void handlePacket(Object connection, Object packet)
    {
        try
        {
            handlePacket.invoke(connection, packet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void disconnect(Object connection, String message)
    {
        try
        {
            disconnect.invoke(connection, message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static Object getPlayerServer(Object entityPlayer)
    {
        try
        {
            return playerServer.get(entityPlayer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getPlayerConnection(Object entityPlayer)
    {
        try
        {
            return playerConnection.get(entityPlayer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getNetworkManager(Object playerConnection)
    {
        try
        {
            return playerConnectionNetworkManager.get(playerConnection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object createPlayerConnection(Object server, Object networkManager,
            Object entityPlayer)
    {
        try
        {
            return playerConnectionConstructor.newInstance(server, networkManager,
                    entityPlayer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
