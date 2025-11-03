//package net.ttzplayz.create_wizardry.block.entity;
//
//import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
//import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
//import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
//import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
//import io.redspace.ironsspellbooks.api.spells.CastSource;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.neoforge.items.IItemHandler;
//import net.neoforged.neoforge.items.ItemStackHandler;
//import net.ttzplayz.create_wizardry.block.BlazeCasterBlock;
//
//import javax.annotation.Nonnull;
//import java.security.Provider;
//import java.util.List;
//
//public class BlazeCasterBlockEntity extends SmartBlockEntity {
//
//    // Inventory for spell scrolls/spell books
//    private ItemStackHandler inventory = new ItemStackHandler(1) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            setChanged();
//            sendData();
//        }
//
//        @Override
//        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//            // Check if item is a spell scroll or spell book
//            return isSpellItem(stack);
//        }
//    };
//
//    // Spell casting properties
//    private int castingCooldown = 0;
//    private int maxCooldown = 60; // 3 seconds at 20 TPS
//    private int spellPower = 1;
//    private int manaCapacity = 1000;
//    private int currentMana = 1000;
//    private int manaRegenRate = 5; // mana per tick
//
//    public BlazeCasterBlockEntity(BlockPos pos, BlockState blockState) {
//        super(ModBlockEntities.BLAZE_CASTER_BE.get(), pos, blockState);
//    }
//
//    @Override
//    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
//        // Add Create behaviors here if needed
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (level == null || level.isClientSide)
//            return;
//
//        // Regenerate mana
//        if (currentMana < manaCapacity) {
//            currentMana = Math.min(manaCapacity, currentMana + manaRegenRate);
//        }
//
//        // Reduce casting cooldown
//        if (castingCooldown > 0) {
//            castingCooldown--;
//        }
//
//        // Check for redstone signal to cast spell
//        if (level.hasNeighborSignal(worldPosition) && castingCooldown <= 0) {
//            castSpell();
//        }
//    }
//
//    private void castSpell() {
//        // Find a spell to cast from inventory
//        AbstractSpell spell = getSpellFromInventory();
//        if (spell == null) return;
//
//        int spellLevel = 1;
//        int manaCost = spell.getManaCost(spellLevel); // Base mana cost
//        if (currentMana < manaCost) return;
//
//        // Get facing direction
//        Direction facing = getBlockState().getValue(BlazeCasterBlock.FACING);
//        Vec3 castPosition = Vec3.atCenterOf(worldPosition).add(
//                facing.getStepX() * 0.8,
//                0.5,
//                facing.getStepZ() * 0.8
//        );
//
//        // Try to cast the spell using the current API
//        try {
//            // In newer versions, spells might use different casting methods
//            // This is a simplified approach that should work with most versions
//            spell.onCast(level, spellLevel, null, CastSource.SCROLL, (null));
//
//            // Consume mana and set cooldown
//            currentMana -= manaCost;
//            castingCooldown = maxCooldown;
//
//            // Play effects
//            playSpellEffects(castPosition, spell);
//
//        } catch (Exception e) {
//            // Handle casting errors - try alternative casting method
//            try {
//                // Alternative method for different API versions
//                spell.onCast(level, spellLevel, null, CastSource.SCROLL, null);
//
//                currentMana -= manaCost;
//                castingCooldown = maxCooldown;
//                playSpellEffects(castPosition, spell);
//
//            } catch (Exception e2) {
//                System.err.println("Error casting spell from Blaze Caster: " + e2.getMessage());
//            }
//        }
//    }
//
//    private AbstractSpell getSpellFromInventory() {
//        for (int i = 0; i < inventory.getSlots(); i++) {
//            ItemStack stack = inventory.getStackInSlot(i);
//            if (!stack.isEmpty() && isSpellItem(stack)) {
//                // Extract spell from item
//                return extractSpellFromItem(stack);
//            }
//        }
//        return null;
//    }
//
//    private boolean isSpellItem(ItemStack stack) {
//        // Check if the item contains spell data
//        // This depends on how Iron's Spells and Spellbooks stores spell data
//        CompoundTag tag = (CompoundTag) stack.getTags();
//        if (tag != null && tag.contains("ISB_spell")) {
//            return true;
//        }
//
//        // Also check for spell books and scrolls by item type
//        String itemName = stack.getItem().toString().toLowerCase();
//        return itemName.contains("spell") || itemName.contains("scroll");
//    }
//
//    private AbstractSpell extractSpellFromItem(ItemStack stack) {
//        CompoundTag tag = (CompoundTag) stack.getTags();
//        if (tag != null && tag.contains("ISB_spell")) {
//            String spellId = tag.getString("ISB_spell");
//            return SpellRegistry.getSpell(spellId);
//        }
//        return null;
//    }
//
//    private void playSpellEffects(Vec3 position, AbstractSpell spell) {
//        // Add particle effects and sounds for spell casting
//        // This would depend on the specific spell being cast
//
//        // Basic flame particles for the blaze theme
//        if (level != null) {
//            level.addParticle(
//                    net.minecraft.core.particles.ParticleTypes.FLAME,
//                    position.x, position.y, position.z,
//                    0, 0.1, 0
//            );
//        }
//    }
//
//    public boolean onUse(Player player, InteractionHand hand) {
//        if (level == null || level.isClientSide)
//            return false;
//
//        ItemStack heldItem = player.getItemInHand(hand);
//
//        // If player is holding a spell item, try to insert it
//        if (isSpellItem(heldItem)) {
//            for (int i = 0; i < inventory.getSlots(); i++) {
//                ItemStack slotStack = inventory.getStackInSlot(i);
//                if (slotStack.isEmpty()) {
//                    inventory.setStackInSlot(i, heldItem.copy());
//                    heldItem.shrink(1);
//                    player.displayClientMessage(
//                            Component.literal("Spell added to Blaze Caster"),
//                            true
//                    );
//                    return true;
//                }
//            }
//        }
//
//        // If player is sneaking, eject items
//        if (player.isShiftKeyDown()) {
//            for (int i = 0; i < inventory.getSlots(); i++) {
//                ItemStack stack = inventory.getStackInSlot(i);
//                if (!stack.isEmpty()) {
//                    player.getInventory().placeItemBackInInventory(stack);
//                    inventory.setStackInSlot(i, ItemStack.EMPTY);
//                }
//            }
//            player.displayClientMessage(
//                    Component.literal("Blaze Caster emptied"),
//                    true
//            );
//            return true;
//        }
//
//        // Display status
//        player.displayClientMessage(
//                Component.literal(String.format(
//                        "Blaze Caster - Mana: %d/%d, Cooldown: %d",
//                        currentMana, manaCapacity, castingCooldown
//                )),
//                true
//        );
//
//        return true;
//    }
//
//    @Override
//    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
//        super.write(tag, registries, clientPacket);
//        tag.put("Inventory", inventory.serializeNBT(registries));
//        tag.putInt("CurrentMana", currentMana);
//        tag.putInt("CastingCooldown", castingCooldown);
//        tag.putInt("SpellPower", spellPower);
//    }
//
//    @Override
//    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
//        super.read(tag, registries, clientPacket);
//        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
//        currentMana = tag.getInt("CurrentMana");
//        castingCooldown = tag.getInt("CastingCooldown");
//        spellPower = tag.getInt("SpellPower");
//    }
//
//    // Capability system updated for NeoForge 1.21.1
//    public IItemHandler getItemHandler() {
//        return inventory;
//    }
//}
