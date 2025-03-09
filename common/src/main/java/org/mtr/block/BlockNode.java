package org.mtr.block;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.ClientPacketHelper;
import org.mtr.packet.PacketDeleteData;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockNode extends BlockWaterloggable implements Waterloggable {

	public final TransportMode transportMode;

	public static final BooleanProperty FACING = BooleanProperty.of("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.of("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.of("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.of("is_connected");

	// Allows for ghost rails to use the correct HitResult
	private static final double SHAPE_PADDING = 0.1;

	public BlockNode(AbstractBlock.Settings settings, TransportMode transportMode) {
		super(settings.nonOpaque());
		this.transportMode = transportMode;
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient() && player.isHolding(Items.BRUSH.get())) {
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos == null) {
				return ActionResult.FAIL;
			} else {
				ClientPacketHelper.openRailShapeModifierScreen(railAndBlockPos.left().getHexId());
				return ActionResult.SUCCESS;
			}
		} else {
			return ActionResult.FAIL;
		}
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		final int quadrant = Angle.getQuadrant(itemPlacementContext.getPlayerYaw(), true);
		return super.getPlacementState(itemPlacementContext)
				.with(FACING, quadrant % 8 >= 4)
				.with(IS_45, quadrant % 4 >= 2)
				.with(IS_22_5, quadrant % 2 == 1)
				.with(IS_CONNECTED, false);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world instanceof ServerWorld serverWorld) {
			PacketDeleteData.sendDirectlyToServerRailNodePosition(serverWorld, MTR.blockPosToPosition(pos));
		}
		return super.onBreak(world, pos, state, player);
	}

	@Nonnull
	@Override
	public final VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(SHAPE_PADDING, getShapeY1(), SHAPE_PADDING, 16 - SHAPE_PADDING, getShapeY2(blockState), 16 - SHAPE_PADDING);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
		builder.add(IS_22_5);
		builder.add(IS_45);
		builder.add(IS_CONNECTED);
	}

	double getShapeY1() {
		return SHAPE_PADDING;
	}

	double getShapeY2(BlockState blockState) {
		return IBlock.getStatePropertySafe(blockState, IS_CONNECTED) ? 1 : 16 - SHAPE_PADDING;
	}

	public static void resetRailNode(ServerWorld serverWorld, BlockPos blockPos) {
		final BlockState state = serverWorld.getBlockState(blockPos);
		if (state.getBlock() instanceof BlockNode) {
			serverWorld.setBlockState(blockPos, state.with(BlockNode.IS_CONNECTED, false));
		}
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, BlockNode.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockNode.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockNode.IS_45) ? 45 : 0);
	}

	public static class BlockContinuousMovementNode extends BlockNode {

		public final boolean upper;
		public final boolean isStation;

		public BlockContinuousMovementNode(AbstractBlock.Settings settings, boolean upper, boolean isStation) {
			super(settings, TransportMode.CABLE_CAR);
			this.upper = upper;
			this.isStation = isStation;
		}

		@Nonnull
		@Override
		public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
			final int quadrant = Angle.getQuadrant(itemPlacementContext.getPlayerYaw(), false);
			return super.getPlacementState(itemPlacementContext)
					.with(FACING, quadrant % 4 >= 2)
					.with(IS_45, quadrant % 2 == 1)
					.with(IS_22_5, false)
					.with(IS_CONNECTED, false);
		}

		@Override
		public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
			final String[] strings = (isStation ? TranslationProvider.TOOLTIP_MTR_CABLE_CAR_NODE_STATION : TranslationProvider.TOOLTIP_MTR_CABLE_CAR_NODE).getString().split("\n");
			for (final String string : strings) {
				tooltip.add(Text.literal(string).formatted(Formatting.GRAY));
			}
		}

		@Override
		double getShapeY1() {
			return upper ? 8 : SHAPE_PADDING;
		}

		@Override
		double getShapeY2(BlockState blockState) {
			return upper ? 16 - SHAPE_PADDING : 8;
		}
	}
}
