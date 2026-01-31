package net.ttzplayz.create_wizardry.advancement;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class CWAdvancement {
    static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "textures/gui/advancements.png");
    static final String LANG = "advancement." + CreateWizardry.MOD_ID + ".";
    static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";

    private final Advancement.Builder mcBuilder = Advancement.Builder.advancement();
    private DeferredHolder<CriterionTrigger<?>, CWBuiltInTrigger> builtinTrigger;
    private CWAdvancement parent;
    private final CWAdvancement.Builder createBuilder = new CWAdvancement.Builder();

    AdvancementHolder datagenResult;

    private String id;
    private String title;
    private String description;

    public CWAdvancement(String id, UnaryOperator<CWAdvancement.Builder> b) {
        this.id = id;

        b.apply(createBuilder);

        if (!createBuilder.externalTrigger)
            builtinTrigger = add(asResource(id));

        if (createBuilder.type == CWAdvancement.TaskType.SECRET)
            description += SECRET_SUFFIX;

        CWAdvancements.ENTRIES.add(this);
    }

    protected abstract DeferredHolder<CriterionTrigger<?>, CWBuiltInTrigger> add(ResourceLocation id);

    private String titleKey() {
        return LANG + id;
    }

    private String descriptionKey() {
        return titleKey() + ".desc";
    }

    public void awardTo(Player player) {
        if (!(player instanceof ServerPlayer sp))
            return;
        if (builtinTrigger == null)
            throw new UnsupportedOperationException(
                    "Advancement " + id + " uses external Triggers, it cannot be awarded directly");
        builtinTrigger.get().trigger(sp);
    }

    public boolean isAlreadyAwardedTo(Player player) {
        if (!(player instanceof ServerPlayer sp))
            return true;
        AdvancementHolder advancement = sp.getServer()
                .getAdvancements()
                .get(asResource(id));
        if (advancement == null)
            return true;
        return sp.getAdvancements()
                .getOrStartProgress(advancement)
                .isDone();
    }

    private ResourceLocation asResource(String id) {
        return ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, id);
    }

    public void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }

    void save(Consumer<AdvancementHolder> t, HolderLookup.Provider registries) {
        if (parent != null)
            mcBuilder.parent(parent.datagenResult);

        if (!createBuilder.externalTrigger) {
            var trigger = builtinTrigger.get();
            mcBuilder.addCriterion("builtin", trigger.createCriterion(trigger));
        }

        if (createBuilder.func != null)
            createBuilder.icon(createBuilder.func.apply(registries));

        if (createBuilder.pendingIconCollected && createBuilder.icon != null)
            createBuilder.externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(createBuilder.icon.getItem()));

        mcBuilder.display(createBuilder.icon, Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(s -> s.withColor(0xDBA213)),
                id.equals("root") ? BACKGROUND : null, createBuilder.type.advancementType, createBuilder.type.toast,
                createBuilder.type.announce, createBuilder.type.hide);

        datagenResult = mcBuilder.save(t, asResource(id).toString());
    }

    public enum TaskType {
        SILENT(AdvancementType.TASK, false, false, false),
        NORMAL(AdvancementType.TASK, true, false, false),
        NOISY(AdvancementType.TASK, true, true, false),
        EXPERT(AdvancementType.GOAL, true, true, false),
        SECRET(AdvancementType.GOAL, true, true, true);

        private final AdvancementType advancementType;
        private final boolean toast;
        private final boolean announce;
        private final boolean hide;

        TaskType(AdvancementType advancementType, boolean toast, boolean announce, boolean hide) {
            this.advancementType = advancementType;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }

    public class Builder {
        private CWAdvancement.TaskType type = CWAdvancement.TaskType.NORMAL;
        private boolean externalTrigger;
        private int keyIndex;
        private ItemStack icon;
        private Function<HolderLookup.Provider, ItemStack> func;
        private boolean pendingIconCollected;

        public CWAdvancement.Builder special(CWAdvancement.TaskType type) {
            this.type = type;
            return this;
        }

        public CWAdvancement.Builder after(CWAdvancement other) {
            CWAdvancement.this.parent = other;
            return this;
        }

        public CWAdvancement.Builder icon(ItemProviderEntry<?, ?> item) {
            return icon(item.asStack());
        }

        public CWAdvancement.Builder icon(ItemLike item) {
            return icon(new ItemStack(item));
        }

        public CWAdvancement.Builder icon(ItemStack stack) {
            icon = stack;
            return this;
        }

        public CWAdvancement.Builder icon(Function<HolderLookup.Provider, ItemStack> func) {
            this.func = func;
            return this;
        }

        public CWAdvancement.Builder title(String title) {
            CWAdvancement.this.title = title;
            return this;
        }

        public CWAdvancement.Builder description(String description) {
            CWAdvancement.this.description = description;
            return this;
        }

        public CWAdvancement.Builder whenBlockPlaced(Block block) {
            return externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }

        public CWAdvancement.Builder whenIconCollected() {
            if (icon != null)
                return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(icon.getItem()));
            pendingIconCollected = true;
            externalTrigger = true;
            return this;
        }

        public CWAdvancement.Builder whenItemCollected(ItemProviderEntry<?, ?> item) {
            return whenItemCollected(item.asStack()
                    .getItem());
        }

        public CWAdvancement.Builder whenItemCollected(ItemLike itemProvider) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider));
        }

        public CWAdvancement.Builder awardedForFree() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] {}));
        }

        public CWAdvancement.Builder externalTrigger(Criterion<?> trigger) {
            mcBuilder.addCriterion(String.valueOf(keyIndex), trigger);
            externalTrigger = true;
            keyIndex++;
            return this;
        }
    }

}
