package net.ttzplayz.create_wizardry.event;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;

public class CWEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ChannelerBlockEntity.registerCapabilities(event);
    }
}
