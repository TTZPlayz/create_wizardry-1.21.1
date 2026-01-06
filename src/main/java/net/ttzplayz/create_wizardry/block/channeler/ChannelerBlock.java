package net.ttzplayz.create_wizardry.block.channeler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class ChannelerBlock extends LightningRodBlock {

    public static final DirectionProperty FACING = LightningRodBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);

    public ChannelerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(POWERED, false));
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

    // LightningBolt hits a block -> capture some lightning
    @Override
    public void onLightningStrike(BlockState state, Level level, BlockPos pos) {
//        LightningBolt bolt =
        if (level.isClientSide) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ChannelerBlockEntity channeler) {
//            channeler.onLightningStrike(bolt);
            level.setBlock(pos, state.setValue(POWERED, true), 3);
            level.scheduleTick(pos, this, 20);
            level.levelEvent(3002, pos, ((Direction)state.getValue(FACING)).getAxis().ordinal());
        }
    }

    // Clear powered flag
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);
        }
    }

    // Entity passes through the block — check for charged creepers
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof Creeper creeper)) return;

        if (creeper.isPowered()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ChannelerBlockEntity) {
                ChannelerBlockEntity channeler = (ChannelerBlockEntity) be;
                if (channeler.drainFromChargedCreeper(creeper)) {
                    // feedback: play sound / particles (left as TODO)
                    level.setBlock(pos, state.setValue(POWERED, true), 3);
                    level.scheduleTick(pos, this, 20);
                }
            }
        }
    }

    // Right-click handling: fill a Lightning Bucket from this block (simple interaction without full capability wiring)
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        ItemStack held = player.getItemInHand(hand);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ChannelerBlockEntity channeler)) return InteractionResult.PASS;

        // If player holds empty bucket, try to give a lightning bucket
        if (held.is(Items.BUCKET)) {
            int drained = channeler.drainIntoBucket();
            if (drained > 0) {
                // consume one empty bucket and give lightning bucket
                if (!player.isCreative()) {
                    held.shrink(1);
                    player.addItem(new ItemStack(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get()));
                } else {
                    // creative: just give a bucket
                    player.addItem(new ItemStack(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get()));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                channeler.markDirtyAndNotify();
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        // If player holds a lightning bucket, try to empty into channeler (spill back)
        if (held.is(net.ttzplayz.create_wizardry.item.CWItems.LIGHTNING_BUCKET.get())) {
            int filled = channeler.fillFromBucket();
            if (filled > 0) {
                if (!player.isCreative()) {
                    held.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                channeler.markDirtyAndNotify();
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    @Nullable
//    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, bs, be) -> {
            if (be instanceof ChannelerBlockEntity channeler) channeler.tickServer();
        };
    }
}