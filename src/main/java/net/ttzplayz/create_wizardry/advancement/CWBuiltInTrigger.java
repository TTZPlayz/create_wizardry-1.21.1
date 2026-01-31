package net.ttzplayz.create_wizardry.advancement;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.advancement.AllTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class CWBuiltInTrigger implements CriterionTrigger<CWBuiltInTrigger>, CriterionTriggerInstance {
    private final Map<PlayerAdvancements, Set<Listener<CWBuiltInTrigger>>> listeners = new IdentityHashMap<>();
    private final Codec<CWBuiltInTrigger> codec = Codec.unit(this);

    public void trigger(ServerPlayer player) {
        var advancements = player.getAdvancements();
        if (this.listeners.containsKey(advancements)) {
            this.listeners.get(advancements).forEach(listener -> listener.run(advancements));
        }
    }

    @Override
    public final void addPlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<CWBuiltInTrigger> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, it -> Sets.newHashSet()).add(listener);
    }

    @Override
    public final void removePlayerListener(PlayerAdvancements playerAdvancements, CriterionTrigger.Listener<CWBuiltInTrigger> listener) {
        Set<CriterionTrigger.Listener<CWBuiltInTrigger>> set = this.listeners.get(playerAdvancements);
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
    public Codec<CWBuiltInTrigger> codec() {
        return this.codec;
    }

    @Override
    public void validate(CriterionValidator validator) {}

}