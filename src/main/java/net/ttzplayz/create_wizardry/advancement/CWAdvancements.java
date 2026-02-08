package net.ttzplayz.create_wizardry.advancement;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.ttzplayz.create_wizardry.block.CWBlocks;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static io.redspace.ironsspellbooks.registries.ItemRegistry.*;
import static net.ttzplayz.create_wizardry.item.CWItems.*;

public class CWAdvancements implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final List<CWAdvancement> ENTRIES = new ArrayList<>();
    public static final CWAdvancement START = null,
            ROOT = create("root", b -> b.icon(LIGHTNING_BUCKET.getId()) //HAS to be getId to work
                    .title("Mechanical Sorcery") //todo: switch the title and advancement
                    .description("Welcome to Create: Wizardry!")
                    .awardedForFree()
                    .special(CWAdvancement.TaskType.SILENT)),
            CHANNELER = create("channeler", b -> b.icon(CWBlocks.CHANNELER.getId())
                    .title("Harvest the Heavens")
                    .description("Craft and Place Down a Channeler to harvest Liquid Lightning.")
                    .special(CWAdvancement.TaskType.NORMAL)
                    .whenIconCollected()
                    .after(ROOT)),
            SHOCKING = create("shocking", b -> b.icon(LIGHTNING_BOTTLE.getId())
                    .title("A *Shocking* Discovery")
                    .description("Yeah, SHOCKING...")
                    .special(CWAdvancement.TaskType.SECRET)
                    .after(CHANNELER)),
            UNLIMITED_POWER = create("unlimited_power", b -> b.icon(LIGHTNING_BUCKET.getId())
                    .title("UNLIMITED POWER!!!")
                    .description("Safe? No. Fun? Yes.")
                    .special(CWAdvancement.TaskType.SUPER_SECRET)
                    .after(SHOCKING));



    private static CWAdvancement create(String id, UnaryOperator<CWAdvancement.Builder> b) {
        return new CWAdvancement(id, b) {
            @Override
            protected CWBuiltInTrigger add(ResourceLocation id) {
                return CWBuiltInTriggers.register(id);
            }
        };
    }

    public static void registerTriggers() {
        CWBuiltInTriggers.register("root");
        CWBuiltInTriggers.register("channeler");
        CWBuiltInTriggers.register("shocking");
        CWBuiltInTriggers.register("unlimited_power");
    }

    // Datagen

    private final PackOutput output;

    public CWAdvancements(PackOutput output) {
        this.output = output;
    }





    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = this.output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancementx) -> {
            ResourceLocation id = advancementx.getId();
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate advancement " + id);
            } else {
                Path path = pathProvider.json(id);
                futures.add(DataProvider.saveStable(cache, advancementx.deconstruct().serializeToJson(), path));
            }
        };

        for(CWAdvancement advancement : ENTRIES) {
            advancement.save(consumer);
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create: Wizardry Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (CWAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }

//    private static java.util.function.Function<HolderLookup.Provider, ItemStack> itemFromRegistry(ResourceLocation id) {
//        return provider -> new ItemStack(
//                provider.lookupOrThrow(Registries.ITEM)
//                        .getOrThrow(ResourceKey.create(Registries.ITEM, id))
//                        .value());
//    }
}
