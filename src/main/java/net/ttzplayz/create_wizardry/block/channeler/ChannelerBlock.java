package net.ttzplayz.create_wizardry.block.channeler;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.ttzplayz.create_wizardry.block.entity.CWBlockEntities;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChannelerBlock extends Block implements IWrenchable, IBE<ChannelerBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);

    public ChannelerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

// add lightning bottle handling
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ChannelerBlockEntity channeler)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Empty bucket -> fill with lightning bucket if possible
        if (stack.is(Items.BUCKET)) {
            int drained = channeler.drainIntoBucket();
            if (drained > 0) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(new ItemStack(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get()));
                } else {
                    player.addItem(new ItemStack(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get()));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                channeler.markDirtyAndNotify();
                return ItemInteractionResult.CONSUME;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Lightning bucket -> empty into the channeler
        if (stack.is(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get())) {
            int filled = channeler.fillFromBucket();
            if (filled > 0) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                channeler.markDirtyAndNotify();
                return ItemInteractionResult.CONSUME;
            }
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // Called when a LightningBolt entity strikes this block


    // Clear powered flag (scheduled tick)
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);
        }
    }

    // Detect charged creepers passing through
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (entity instanceof LightningBolt bolt) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ChannelerBlockEntity channeler) {
                channeler.onLightningStrike(bolt);
                level.setBlock(pos, state.setValue(POWERED, true), 3);
                level.scheduleTick(pos, this, 20);
                level.levelEvent(3002, pos, ((Direction) state.getValue(FACING)).getAxis().ordinal());
            }
        }

        if (entity instanceof Creeper creeper) {
            if (creeper.isPowered()) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ChannelerBlockEntity channeler) {
                    if (channeler.drainFromChargedCreeper(creeper)) {
                        level.setBlock(pos, state.setValue(POWERED, true), 3);
                        level.scheduleTick(pos, this, 20);
                    }
                }
            }
        }
    }



    @Override
    public Class<ChannelerBlockEntity> getBlockEntityClass() {
        return ChannelerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChannelerBlockEntity> getBlockEntityType() {
        return CWBlockEntities.CHANNELER_BE.get();
    }

//    @Override
//    public InteractionResult onBlockEntityUse(BlockGetter world, BlockPos pos, Function<ChannelerBlockEntity, InteractionResult> action) {
//        return IBE.super.onBlockEntityUse(world, pos, action);
//    }
//
//    @Override
//    public ItemInteractionResult onBlockEntityUseItemOn(BlockGetter world, BlockPos pos, Function<ChannelerBlockEntity, ItemInteractionResult> action) {
//        return IBE.super.onBlockEntityUseItemOn(world, pos, action);
//    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        AdvancementBehaviour.setPlacedBy(worldIn, pos, placer);
    }

    @Nullable
//    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, bs, be) -> {
            if (be instanceof ChannelerBlockEntity channeler) channeler.tickServer();
        };
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return IWrenchable.super.onWrenched(state, context);
    }
}