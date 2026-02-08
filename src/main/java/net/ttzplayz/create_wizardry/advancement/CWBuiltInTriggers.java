package net.ttzplayz.create_wizardry.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CWBuiltInTriggers {
    private static final Map<String, CWBuiltInTrigger> ENTRIES = new HashMap<>();
    private static final List<CWBuiltInTrigger> TRIGGERS = new ArrayList<>();
    private static boolean registered;

    private CWBuiltInTriggers() {}

    public static CWBuiltInTrigger register(ResourceLocation id) {
        return register(id.getPath());
    }

    public static CWBuiltInTrigger register(String id) {
        return ENTRIES.computeIfAbsent(id, key -> {
            CWBuiltInTrigger trigger = new CWBuiltInTrigger(key);
            TRIGGERS.add(trigger);
            return trigger;
        });
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        TRIGGERS.forEach(CriteriaTriggers::register);
    }
}
