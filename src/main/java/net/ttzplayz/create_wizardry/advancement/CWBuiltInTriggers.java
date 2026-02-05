package net.ttzplayz.create_wizardry.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.ttzplayz.create_wizardry.CreateWizardry;

import java.util.HashMap;
import java.util.Map;

public final class CWBuiltInTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CreateWizardry.MOD_ID);
    private static final Map<String, RegistryObject<CWBuiltInTrigger>> ENTRIES = new HashMap<>();

    private CWBuiltInTriggers() {}

    public static RegistryObject<CWBuiltInTrigger> register(ResourceLocation id) {
        return register(id.getPath());
    }

    public static RegistryObject<CWBuiltInTrigger> register(String id) {
        return ENTRIES.computeIfAbsent(id, key -> TRIGGERS.register(key, CWBuiltInTrigger::new));
    }

    public static void register(IEventBus modBus) {
        TRIGGERS.register(modBus);
    }
}
