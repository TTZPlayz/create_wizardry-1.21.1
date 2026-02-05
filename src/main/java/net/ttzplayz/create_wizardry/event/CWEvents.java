package net.ttzplayz.create_wizardry.event;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;

public class CWEvents {
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ChannelerBlockEntity.registerCapabilities(event);
    }
}
