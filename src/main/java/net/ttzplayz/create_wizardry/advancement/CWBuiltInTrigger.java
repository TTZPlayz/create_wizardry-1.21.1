package net.ttzplayz.create_wizardry.advancement;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.ttzplayz.create_wizardry.CreateWizardry;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class CWBuiltInTrigger implements CriterionTrigger<CWBuiltInTrigger.Instance> {
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, Set<Listener<Instance>>> listeners = new IdentityHashMap<>();

    public CWBuiltInTrigger(String id) {
        this(CreateWizardry.id(id));
    }

    public CWBuiltInTrigger(ResourceLocation id) {
        this.id = id;
    }

    public void trigger(ServerPlayer player) {
        var advancements = player.getAdvancements();
        if (this.listeners.containsKey(advancements)) {
            this.listeners.get(advancements).forEach(listener -> listener.run(advancements));
        }
    }

    @Override
    public final void addPlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, it -> Sets.newHashSet()).add(listener);
    }

    @Override
    public final void removePlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<Instance> listener) {
        Set<CriterionTrigger.Listener<Instance>> set = this.listeners.get(playerAdvancements);
        if (set != null) {
            set.remove(listener);
            if (set.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public final void removePlayerListeners(PlayerAdvancements playerAdvancements) {
        this.listeners.remove(playerAdvancements);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Instance createInstance(JsonObject json, DeserializationContext context) {
        return new Instance(id);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ResourceLocation id) {
            super(id, ContextAwarePredicate.ANY);
        }
    }
}
