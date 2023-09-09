package org.mtr.mod.block;

import org.mtr.core.data.TransportMode;
import org.mtr.core.tools.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockNode extends BlockExtension implements DirectionHelper {

	public final TransportMode transportMode;

	public static final BooleanProperty FACING = BooleanProperty.of("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.of("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.of("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

	public BlockNode(TransportMode transportMode) {
		super(BlockHelper.createBlockSettings(true).nonOpaque());
		this.transportMode = transportMode;
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final int quadrant = Angle.getQuadrant(ctx.getPlayerYaw(), true);
		return getDefaultState2().with(new Property<>(FACING.data), quadrant % 8 >= 4).with(new Property<>(IS_45.data), quadrant % 4 >= 2).with(new Property<>(IS_22_5.data), quadrant % 2 >= 1).with(new Property<>(IS_CONNECTED.data), false);
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient()) {
			PacketData.deleteRailNode(ServerWorld.cast(world), Init.blockPosToPosition(pos));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getStatePropertySafe(state, IS_CONNECTED) ? Block.createCuboidShape(0, 0, 0, 16, 1, 16) : VoxelShapes.fullCube();
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(IS_22_5);
		properties.add(IS_45);
		properties.add(IS_CONNECTED);
	}

	public static void resetRailNode(ServerWorld serverWorld, BlockPos blockPos) {
		final BlockState state = serverWorld.getBlockState(blockPos);
		if (state.getBlock().data instanceof BlockNode) {
			serverWorld.setBlockState(blockPos, state.with(new Property<>(BlockNode.IS_CONNECTED.data), false));
		}
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, BlockNode.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockNode.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockNode.IS_45) ? 45 : 0);
	}

	public static class BlockBoatNode extends BlockNode implements BlockWithEntity {

		public BlockBoatNode() {
			super(TransportMode.BOAT);
		}

		@Override
		public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
			return new BlockEntity(blockPos, blockState);
		}
	}

	public static class BlockEntity extends BlockEntityExtension {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.BOAT_NODE.get(), pos, state);
		}
	}

	public static class BlockContinuousMovementNode extends BlockNode {

		public final boolean upper;
		public final boolean isStation;

		public BlockContinuousMovementNode(boolean upper, boolean isStation) {
			super(TransportMode.CABLE_CAR);
			this.upper = upper;
			this.isStation = isStation;
		}

		@Override
		public BlockState getPlacementState2(ItemPlacementContext ctx) {
			final int quadrant = Angle.getQuadrant(ctx.getPlayerYaw(), false);
			return getDefaultState2().with(new Property<>(FACING.data), quadrant % 4 >= 2).with(new Property<>(IS_45.data), quadrant % 2 >= 1).with(new Property<>(IS_22_5.data), false).with(new Property<>(IS_CONNECTED.data), false);
		}

		@Nonnull
		@Override
		public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
			return Block.createCuboidShape(0, upper ? 8 : 0, 0, 16, upper ? 16 : 8, 16);
		}

		@Override
		public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
			final String[] strings = TextHelper.translatable("tooltip.mtr.cable_car_node" + (isStation ? "_station" : "")).getString().split("\n");
			for (final String string : strings) {
				tooltip.add(TextHelper.literal(string).formatted(TextFormatting.GRAY));
			}
		}
	}
}
