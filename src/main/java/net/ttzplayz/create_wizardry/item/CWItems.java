package net.ttzplayz.create_wizardry.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.fluids.CWFluidRegistry;
import static io.redspace.ironsspellbooks.registries.FluidRegistry.*;

public class CWItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateWizardry.MOD_ID);

//    public static final DeferredHolder<Item, BlockItem> BLAZE_CASTER =
//            ITEMS.register("blaze_caster", () ->
//                    new BlockItem(ModBlocks.BLAZE_CASTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRUSHED_MITHRIL = ITEMS.register("crushed_mithril",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MITHRIL_NUGGET = ITEMS.register("mithril_nugget",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MANA_BUCKET = ITEMS.register("mana_bucket",
            () -> new BucketItem(CWFluidRegistry.MANA, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LIGHTNING_BUCKET = ITEMS.register("lightning_bucket",
            () -> new BucketItem(CWFluidRegistry.LIGHTNING, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLOOD_BUCKET = ITEMS.register("blood_bucket",
            () -> new BucketItem(BLOOD, new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
