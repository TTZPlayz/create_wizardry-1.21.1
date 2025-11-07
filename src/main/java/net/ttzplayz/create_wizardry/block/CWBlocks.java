package net.ttzplayz.create_wizardry.block;

import io.redspace.ironsspellbooks.registries.FluidRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.ttzplayz.create_wizardry.item.CWItems;

import java.util.function.Supplier;

public class CWBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CreateWizardry.MOD_ID);

//    public static final DeferredHolder<Block, BlazeCasterBlock> BLAZE_CASTER =
//            BLOCKS.register("blaze_caster", () -> new BlazeCasterBlock(
//                    Block.Properties.of()
//                            .mapColor(MapColor.COLOR_ORANGE)
//                            .strength(3.5F)
//                            .sound(SoundType.METAL)
//                            .lightLevel(state -> 12) // Glowing like a blaze
//                            .noOcclusion()
//            ));
//    public static final DeferredHolder<Block, LiquidBlock> MANA_BLOCK =
//            BLOCKS.register("mana_block",
//                    () -> new LiquidBlock(CWFluidRegistry.MANA.get(),
//                            BlockBehaviour.Properties.of().strength(100f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 15).noCollission().noLootTable()
//                                    .liquid()));
//    public static final DeferredHolder<Block, LiquidBlock> LIGHTNING_BLOCK =
//            BLOCKS.register("liquid_lightning_block",
//                    () -> new LiquidBlock(CWFluidRegistry.LIGHTNING.get(),
//                            BlockBehaviour.Properties.of().strength(100f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 15).noCollission().noLootTable()
//                                    .liquid()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        CWItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
