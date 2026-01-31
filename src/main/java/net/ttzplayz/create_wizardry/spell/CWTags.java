package net.ttzplayz.create_wizardry.spell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.ttzplayz.create_wizardry.CreateWizardry;

public class CWTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CW_BUCKETS = createTag("create_wizardry_buckets");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, name));
        }
    }
}