package mtr.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.MTR;
import mtr.data.LiftInstructions;
import mtr.entity.EntityLift;
import mtr.mappings.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Lift Panel Block
 * @author LX86
 * @since 1.1.6
 */
public class BlockLiftPanel1 extends BlockDirectionalMapper implements EntityBlockMapper {
    public static final BooleanProperty LEFT = BooleanProperty.create("left");

    public BlockLiftPanel1() {
        super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, LEFT) ? IBlock.getStatePropertySafe(state, FACING).getCounterClockWise() : IBlock.getStatePropertySafe(state, FACING).getClockWise();

        if (facing == direction && !newState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction direction = ctx.getHorizontalDirection().getOpposite();
        return IBlock.isReplaceable(ctx, direction.getCounterClockWise(), 2) ? defaultBlockState().setValue(FACING, direction).setValue(LEFT, true) : null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return mtr.block.IBlock.getVoxelShapeByDirection(0, 0, 15, 16, 10, 16, state.getValue(FACING));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!world.isClientSide) {
            final Direction direction = IBlock.getStatePropertySafe(state, FACING);
            world.setBlock(pos.relative(direction.getCounterClockWise()), defaultBlockState().setValue(FACING, direction).setValue(LEFT, false), 3);
            world.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!IBlock.getStatePropertySafe(state, LEFT)) {
            IBlock.onBreakCreative(world, player, pos.relative(IBlock.getStatePropertySafe(state, FACING).getClockWise()));
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (player.isHolding(Items.LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.LIFT_BUTTONS_LINK_REMOVER.get())) {
                return InteractionResult.PASS;
            } else {
                final double y = hit.getLocation().y;
                LiftInstructions.addInstruction(world, pos, y - Math.floor(y) > 0.25);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityLiftPanel(pos, state);
    }

    public static class TileEntityLiftPanel extends BlockEntityClientSerializableMapper implements TickableMapper {
        private final Set<BlockPos> trackPositions = new HashSet<>();
        private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";
        private static final int UPDATE_INTERVAL = 60;

        public TileEntityLiftPanel(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.LIFT_PANEL_1_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            trackPositions.clear();
            for (final long position : compoundTag.getLongArray(KEY_TRACK_FLOOR_POS)) {
                trackPositions.add(BlockPos.of(position));
            }
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            final List<Long> trackPositionsList = new ArrayList<>();
            trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
            compoundTag.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);
        }

        @Override
        public void tick() {
            tick(level, worldPosition, this);
        }

        public void registerFloor(BlockPos pos, boolean isAdd) {
            if (isAdd) {
                trackPositions.add(pos);
            } else {
                trackPositions.remove(pos);
            }
            setChanged();
            syncData();
        }

        public void forEachTrackPosition(Level world, BiConsumer<BlockPos, BlockLiftTrackFloor.TileEntityLiftTrackFloor> callback) {
            final Set<BlockPos> trackPositionsToRemove = new HashSet<>();
            trackPositions.forEach(trackPosition -> {
                final BlockEntity blockEntity = world.getBlockEntity(trackPosition);
                if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
                    if (callback != null) {
                        callback.accept(trackPosition, (BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity);
                    }
                } else {
                    trackPositionsToRemove.add(trackPosition);
                }
            });
            trackPositionsToRemove.forEach(trackPositions::remove);
        }

        public static <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
            if (world != null && EntityLift.playerVerticallyNearby(world, pos.getX(), pos.getZ()) && blockEntity instanceof TileEntityLiftPanel && !world.isClientSide && MTR.isGameTickInterval(UPDATE_INTERVAL, (int) pos.asLong())) {
                ((TileEntityLiftPanel) blockEntity).forEachTrackPosition(world, null);
                blockEntity.setChanged();
                ((TileEntityLiftPanel) blockEntity).syncData();
            }
        }
    }
}
