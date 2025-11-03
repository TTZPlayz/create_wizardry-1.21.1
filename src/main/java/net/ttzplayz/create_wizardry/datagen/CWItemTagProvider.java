package net.ttzplayz.create_wizardry.datagen;

import com.simibubi.create.AllTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ttzplayz.create_wizardry.item.CWItems;

import java.util.concurrent.CompletableFuture;

import static net.neoforged.neoforge.common.Tags.Items.BUCKETS;
import static net.neoforged.neoforge.common.Tags.Items.NUGGETS;
import static net.ttzplayz.create_wizardry.item.CWItems.*;
import static net.ttzplayz.create_wizardry.util.CWTags.Items.CW_BUCKETS;

public class CWItemTagProvider extends ItemTagsProvider {
    public CWItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CW_BUCKETS)
                .add(MANA_BUCKET.get())
                .add(LIGHTNING_BUCKET.get())
                .add(BLOOD_BUCKET.get());

        tag(AllTags.AllItemTags.CRUSHED_RAW_MATERIALS.tag)
                .add(CWItems.CRUSHED_MITHRIL.get());
        tag(NUGGETS)
                .add(MITHRIL_NUGGET.get());

        tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                .addTag(CW_BUCKETS);
        tag(BUCKETS)
                .addTag(CW_BUCKETS);
    }
}
