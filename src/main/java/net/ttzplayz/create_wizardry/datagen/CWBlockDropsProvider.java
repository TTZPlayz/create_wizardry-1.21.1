package net.ttzplayz.create_wizardry.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.ttzplayz.create_wizardry.block.CWBlocks;

import java.util.Set;

public class CWBlockDropsProvider extends BlockLootSubProvider {
    protected CWBlockDropsProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(CWBlocks.CHANNELER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CWBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
