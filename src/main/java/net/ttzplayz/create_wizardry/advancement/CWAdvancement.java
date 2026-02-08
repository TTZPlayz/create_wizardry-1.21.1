package net.ttzplayz.create_wizardry.advancement;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllTriggers;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class CWAdvancement {
    static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "textures/gui/advancements.png");
    static final String LANG = "advancement." + CreateWizardry.MOD_ID + ".";
    static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";
    private Advancement.Builder builder = net.minecraft.advancements.Advancement.Builder.advancement();
    private CWBuiltInTrigger builtinTrigger;
    private CWAdvancement parent;
    private final CWAdvancement.Builder createBuilder = new CWAdvancement.Builder();

    Advancement datagenResult;

    private String id;
    private String title;
    private String description;

    public CWAdvancement(String id, UnaryOperator<CWAdvancement.Builder> b) {
        this.id = id;
        b.apply(createBuilder);
        if (!createBuilder.externalTrigger) {
            builtinTrigger = add(asResource(id));
            builtinTrigger = CWTriggers.addSimple(id + "_builtin");
            this.builder.addCriterion("0", this.builtinTrigger.instance());
        }

        if (createBuilder.type == CWAdvancement.TaskType.SECRET) {
            description += SECRET_SUFFIX;
        }
        CWAdvancements.ENTRIES.add(this);
    }

    protected abstract CWBuiltInTrigger add(ResourceLocation id);

    private String titleKey() {
        return LANG + id;
    }

    private String descriptionKey() {
        return titleKey() + ".desc";
    }

    public void awardTo(Player player) {
        if (!(player instanceof ServerPlayer sp)) {
            return;
        }
        if (builtinTrigger == null) {
            throw new UnsupportedOperationException(
                    "Advancement " + id + " uses external Triggers, it cannot be awarded directly");
        }
        builtinTrigger.trigger(sp);
    }

    public boolean isAlreadyAwardedTo(Player player) {
        if (player instanceof ServerPlayer sp) {
            Advancement advancement = sp.getServer().getAdvancements().getAdvancement(this.asResource(id));
            return advancement == null || sp.getAdvancements().getOrStartProgress(advancement).isDone();
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

    String getId() {
        return id;
    }

    void save(Consumer<Advancement> t) {
        if (this.parent != null) {
            this.builder.parent(this.parent.datagenResult);
        }

        ItemStack icon = Objects.requireNonNull(createBuilder.iconSupplier, "Missing icon for advancement " + id).get();
        this.builder.display(icon, Component.translatable(this.titleKey()), Component.translatable(this.descriptionKey())
                .withStyle((s) -> s.withColor(14393875)),
                id.equals("root") ? BACKGROUND : null, createBuilder.type.frame, createBuilder.type.toast,
                createBuilder.type.announce, createBuilder.type.hide);
        createBuilder.applyCriteria(this.builder);
        this.datagenResult = this.builder.save(t,
                this.asResource(this.id).toString()
//                Create.asResource(this.id).toString() To add to create advancements
        );
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
        private Supplier<ItemStack> iconSupplier;
        private final List<Supplier<CriterionTriggerInstance>> criteriaSuppliers = new ArrayList<>();

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
            return this.icon(() -> new ItemStack(item));
        }

        CWAdvancement.Builder icon(ItemStack stack) {
            return this.icon(() -> stack);
        }

        CWAdvancement.Builder icon(ResourceLocation itemId) {
            return this.icon(() -> {
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                if (item == null) {
                    throw new IllegalStateException("Missing item for advancement icon: " + itemId);
                }
                return new ItemStack(item);
            });
        }

        CWAdvancement.Builder icon(Supplier<ItemStack> supplier) {
            this.iconSupplier = supplier;
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
            return this.externalTrigger(() -> ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }

        CWAdvancement.Builder whenIconCollected() {
            return this.externalTrigger(() -> InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{
                    this.iconSupplier.get().getItem()
            }));
        }

        CWAdvancement.Builder whenItemCollected(ItemProviderEntry<?> item) {
            return this.whenItemCollected(item.asStack().getItem());
        }

        CWAdvancement.Builder whenItemCollected(ItemLike itemProvider) {
            return this.externalTrigger(() -> InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{
                    itemProvider
            }));
        }

        CWAdvancement.Builder whenItemCollected(TagKey<Item> tag) {
            return this.externalTrigger(() -> InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate[]{
                    new ItemPredicate(tag, (Set) null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
                            EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion) null, NbtPredicate.ANY)
            }));
        }

        CWAdvancement.Builder awardedForFree() {
            return this.externalTrigger(() -> InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[0]));
        }

        CWAdvancement.Builder externalTrigger(CriterionTriggerInstance trigger) {
            return this.externalTrigger(() -> trigger);
        }

        CWAdvancement.Builder externalTrigger(Supplier<CriterionTriggerInstance> triggerSupplier) {
            this.criteriaSuppliers.add(triggerSupplier);
            this.externalTrigger = true;
            return this;
        }

        void applyCriteria(Advancement.Builder builder) {
            for (int i = 0; i < criteriaSuppliers.size(); i++) {
                builder.addCriterion(String.valueOf(i), criteriaSuppliers.get(i).get());
            }
        }
    }
}
