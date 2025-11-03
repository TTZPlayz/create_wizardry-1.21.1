package net.ttzplayz.create_wizardry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
//import net.ttzplayz.create_wizardry.block.entity.BlazeCasterBlockEntity;
import net.ttzplayz.create_wizardry.block.entity.ModBlockEntities;

//public class BlazeCasterBlock extends Block implements IBE<BlazeCasterBlockEntity> {
//
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//
//    // Shape similar to Blaze Burner
//    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);
//
//    public BlazeCasterBlock(Properties properties) {
//        super(properties);
//        this.registerDefaultState(this.stateDefinition.any()
//                .setValue(FACING, Direction.NORTH));
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(FACING);
//    }
//
//
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return this.defaultBlockState()
//                .setValue(FACING, context.getHorizontalDirection().getOpposite());
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        return SHAPE;
//    }
//
//    @Override
//    public Class<BlazeCasterBlockEntity> getBlockEntityClass() {
//        return BlazeCasterBlockEntity.class;
//    }
//
//    @Override
//    public BlockEntityType<? extends BlazeCasterBlockEntity> getBlockEntityType() {
//        return ModBlockEntities.BLAZE_CASTER_BE.get(); // You'll need to register this
//    }
//}