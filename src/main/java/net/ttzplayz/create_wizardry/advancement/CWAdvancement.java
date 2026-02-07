package net.ttzplayz.create_wizardry.advancement;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public abstract class CWAdvancement {
    static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "textures/gui/advancements.png");
    static final String LANG = "advancement." + CreateWizardry.MOD_ID + ".";
    static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";
    private Advancement.Builder builder = net.minecraft.advancements.Advancement.Builder.advancement();
    private RegistryObject<CWBuiltInTrigger> builtinTrigger;
    private CWAdvancement parent;
    private final CWAdvancement.Builder createBuilder = new CWAdvancement.Builder();

    Advancement datagenResult;

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
        this.builder.display(createBuilder.icon, Component.translatable(this.titleKey()), Component.translatable(this.descriptionKey()).withStyle((s) -> s.withColor(14393875)), id.equals("root") ? BACKGROUND : null, createBuilder.type.frame, createBuilder.type.toast, createBuilder.type.announce, createBuilder.type.hide);
        CWAdvancements.ENTRIES.add(this);
    }

    protected abstract RegistryObject<CWBuiltInTrigger> add(ResourceLocation id);

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
        if (player instanceof ServerPlayer sp) {
            Advancement advancement = sp.getServer().getAdvancements().getAdvancement(Create.asResource(this.id));
            return advancement == null ? true : sp.getAdvancements().getOrStartProgress(advancement).isDone();
        } else {
            return true;
        }
    }

    private ResourceLocation asResource(String id) {
        return ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, id);
    }

    public void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }

    void save(Consumer<Advancement> t) {
        if (this.parent != null) {
            this.builder.parent(this.parent.datagenResult);
        }

        this.datagenResult = this.builder.save(t, Create.asResource(this.id).toString());
    }

    public enum TaskType {
        SILENT(FrameType.TASK, false, false, false),
        NORMAL(FrameType.TASK, true, false, false),
        NOISY(FrameType.TASK, true, true, false),
        EXPERT(FrameType.GOAL, true, true, false),
        SECRET(FrameType.GOAL, true, true, true),
        SUPER_SECRET(FrameType.CHALLENGE, true, true, true);

        private final FrameType frame;
        private final boolean toast;
        private final boolean announce;
        private final boolean hide;

        TaskType(FrameType advancementType, boolean toast, boolean announce, boolean hide) {
            this.frame = advancementType;
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

        public CWAdvancement.Builder special(CWAdvancement.TaskType type) {
            this.type = type;
            return this;
        }

        public CWAdvancement.Builder after(CWAdvancement other) {
            CWAdvancement.this.parent = other;
            return this;
        }

        CWAdvancement.Builder icon(ItemProviderEntry<?> item) {
            return this.icon(item.asStack());
        }

        CWAdvancement.Builder icon(ItemLike item) {
            return this.icon(new ItemStack(item));
        }

        CWAdvancement.Builder icon(ItemStack stack) {
            this.icon = stack;
            return this;
        }

        CWAdvancement.Builder title(String title) {
            CWAdvancement.this.title = title;
            return this;
        }

        CWAdvancement.Builder description(String description) {
            CWAdvancement.this.description = description;
            return this;
        }

        CWAdvancement.Builder whenBlockPlaced(Block block) {
            return this.externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }

        CWAdvancement.Builder whenIconCollected() {
            return this.externalTrigger(net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{this.icon.getItem()}));
        }

        CWAdvancement.Builder whenItemCollected(ItemProviderEntry<?> item) {
            return this.whenItemCollected(item.asStack().getItem());
        }

        CWAdvancement.Builder whenItemCollected(ItemLike itemProvider) {
            return this.externalTrigger(net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{itemProvider}));
        }

        CWAdvancement.Builder whenItemCollected(TagKey<Item> tag) {
            return this.externalTrigger(net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate[]{new ItemPredicate(tag, (Set)null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion)null, NbtPredicate.ANY)}));
        }

        CWAdvancement.Builder awardedForFree() {
            return this.externalTrigger(net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[0]));
        }

        CWAdvancement.Builder externalTrigger(CriterionTriggerInstance trigger) {
            CWAdvancement.this.builder.addCriterion(String.valueOf(this.keyIndex), trigger);
            this.externalTrigger = true;
            ++this.keyIndex;
            return this;
        }
    }

}
