package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.ClientPacketHelper;
import org.mtr.packet.PacketDeleteData;
import org.mtr.registry.Items;

import java.util.List;

public class BlockNode extends BlockWaterloggable implements SimpleWaterloggedBlock {

	public final TransportMode transportMode;

	public static final BooleanProperty FACING = BooleanProperty.create("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.create("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.create("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

	// Allows for ghost rails to use the correct HitResult
	private static final double SHAPE_PADDING = 0.1;

	public BlockNode(BlockBehaviour.Properties settings, TransportMode transportMode) {
		super(settings.noOcclusion());
		this.transportMode = transportMode;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide() && player.isHolding(Items.BRUSH.get())) {
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos == null) {
				return InteractionResult.FAIL;
			} else {
				ClientPacketHelper.openRailShapeModifierScreen(railAndBlockPos.left().getHexId());
				return InteractionResult.SUCCESS;
			}
		} else {
			return InteractionResult.FAIL;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
		final int quadrant = Angle.getQuadrant(itemPlacementContext.getRotation(), true);
		return super.getStateForPlacement(itemPlacementContext)
			.setValue(FACING, quadrant % 8 >= 4)
			.setValue(IS_45, quadrant % 4 >= 2)
			.setValue(IS_22_5, quadrant % 2 == 1)
			.setValue(IS_CONNECTED, false);
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (world instanceof ServerLevel serverWorld) {
			PacketDeleteData.sendDirectlyToServerRailNodePosition(serverWorld, MTR.blockPosToPosition(pos));
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public final VoxelShape getShape(BlockState blockState, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Block.box(SHAPE_PADDING, getShapeY1(), SHAPE_PADDING, 16 - SHAPE_PADDING, getShapeY2(blockState), 16 - SHAPE_PADDING);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
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

	public static void resetRailNode(ServerLevel serverWorld, BlockPos blockPos) {
		final BlockState state = serverWorld.getBlockState(blockPos);
		if (state.getBlock() instanceof BlockNode) {
			serverWorld.setBlockAndUpdate(blockPos, state.setValue(BlockNode.IS_CONNECTED, false));
		}
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockNode.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockNode.IS_45) ? 45 : 0);
	}

	public static class BlockContinuousMovementNode extends BlockNode {

		public final boolean upper;
		public final boolean isStation;

		public BlockContinuousMovementNode(BlockBehaviour.Properties settings, boolean upper, boolean isStation) {
			super(settings, TransportMode.CABLE_CAR);
			this.upper = upper;
			this.isStation = isStation;
		}

		@Override
		public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
			final int quadrant = Angle.getQuadrant(itemPlacementContext.getRotation(), false);
			return super.getStateForPlacement(itemPlacementContext)
				.setValue(FACING, quadrant % 4 >= 2)
				.setValue(IS_45, quadrant % 2 == 1)
				.setValue(IS_22_5, false)
				.setValue(IS_CONNECTED, false);
		}

		@Override
		public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
			final String[] strings = (isStation ? TranslationProvider.TOOLTIP_MTR_CABLE_CAR_NODE_STATION : TranslationProvider.TOOLTIP_MTR_CABLE_CAR_NODE).getString().split("\n");
			for (final String string : strings) {
				tooltip.add(Component.literal(string).withStyle(ChatFormatting.GRAY));
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
