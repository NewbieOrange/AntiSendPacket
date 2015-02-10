package com.mengcraft.injector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketListener
{
    private static PacketAdapter tabCompleteListener;
    
    public static void register(Injector plugin)
    {
        tabCompleteListener = new PacketAdapter(plugin,
                PacketType.Play.Client.TAB_COMPLETE)
        {
            @Override
            public void onPacketReceiving(PacketEvent event)
            {
                ReflectUtil.getConnection(ReflectUtil.getEntityPlayer(event.getPlayer()))
                        .handlePacket(event.getPacket().getHandle());
                
                event.setCancelled(true);
            }
        };
        
        ProtocolLibrary.getProtocolManager().addPacketListener(tabCompleteListener);
    }
}
