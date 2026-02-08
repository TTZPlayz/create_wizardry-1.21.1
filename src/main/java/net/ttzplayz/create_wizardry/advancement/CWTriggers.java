package net.ttzplayz.create_wizardry.advancement;

import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import net.minecraft.advancements.CriteriaTriggers;

import java.util.LinkedList;
import java.util.List;

public class CWTriggers {
    private static final List<CWBuiltInTrigger> triggers = new LinkedList<>();

    public static CWBuiltInTrigger addSimple(String id) {
        return add(new CWBuiltInTrigger(id));
    }

    private static <T extends CriterionTriggerBase<?>> CWBuiltInTrigger add(CWBuiltInTrigger instance) {
        triggers.add(instance);
        return instance;
    }

    public static void register() {
        triggers.forEach(CriteriaTriggers::register);
    }
}
