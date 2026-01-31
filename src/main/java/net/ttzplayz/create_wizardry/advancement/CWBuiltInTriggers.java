package net.ttzplayz.create_wizardry.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ttzplayz.create_wizardry.CreateWizardry;

import java.util.HashMap;
import java.util.Map;

public final class CWBuiltInTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CreateWizardry.MOD_ID);
    private static final Map<String, DeferredHolder<CriterionTrigger<?>, CWBuiltInTrigger>> ENTRIES = new HashMap<>();

    private CWBuiltInTriggers() {}

    public static DeferredHolder<CriterionTrigger<?>, CWBuiltInTrigger> register(ResourceLocation id) {
        return register(id.getPath());
    }

    public static DeferredHolder<CriterionTrigger<?>, CWBuiltInTrigger> register(String id) {
        return ENTRIES.computeIfAbsent(id, key -> TRIGGERS.register(key, CWBuiltInTrigger::new));
    }

    public static void register(IEventBus modBus) {
        TRIGGERS.register(modBus);
    }
}
