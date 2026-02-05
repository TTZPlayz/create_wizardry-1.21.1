package net.ttzplayz.create_wizardry.block;

import io.redspace.ironsspellbooks.registries.FluidRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.block.channeler.ChannelerBlock;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import net.ttzplayz.create_wizardry.item.CWItems;

import java.util.function.Supplier;

public class CWBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreateWizardry.MOD_ID);

public static final RegistryObject<ChannelerBlock> CHANNELER =
        registerBlock("channeler", () -> new ChannelerBlock(
                Block.Properties.of()
                        .mapColor(MapColor.COLOR_ORANGE)
                        .strength(3.5F)
                        .sound(SoundType.METAL)
                        .lightLevel(powered -> 4)
                        .noOcclusion()
        ));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        CWItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
