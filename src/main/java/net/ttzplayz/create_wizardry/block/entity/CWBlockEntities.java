package net.ttzplayz.create_wizardry.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.block.CWBlocks;
//import net.ttzplayz.create_wizardry.block.BlazeCasterBlock;


public class CWBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateWizardry.MOD_ID);

    public static final RegistryObject<BlockEntityType<ChannelerBlockEntity>> CHANNELER_BE =
            BLOCK_ENTITIES.register(
                    "channeler_be",
                    () -> BlockEntityType.Builder.of(
                    ChannelerBlockEntity::new,
                            CWBlocks.CHANNELER.get())
                            .build(null)
            );


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
