package mtr.block;

import mtr.BlockEntityTypes;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.mappings.BlockDirectionalMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class BlockNode extends BlockDirectionalMapper {

	public final TransportMode transportMode;

	public static final BooleanProperty FACING = BooleanProperty.create("facing");
	public static final BooleanProperty IS_22_5 = BooleanProperty.create("is_22_5");
	public static final BooleanProperty IS_45 = BooleanProperty.create("is_45");
	public static final BooleanProperty IS_CONNECTED = BooleanProperty.create("is_connected");

	public BlockNode(TransportMode transportMode) {
		super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).strength(2).noOcclusion());
		this.transportMode = transportMode;
		registerDefaultState(defaultBlockState().setValue(FACING, false).setValue(IS_22_5, false).setValue(IS_45, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final int quadrant = RailAngle.getQuadrant(ctx.getRotation(), true);
		return defaultBlockState().setValue(FACING, quadrant % 8 >= 4).setValue(IS_45, quadrant % 4 >= 2).setValue(IS_22_5, quadrant % 2 >= 1).setValue(IS_CONNECTED, false);
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.removeNode(player, pos, transportMode);
				PacketTrainDataGuiServer.removeNodeS2C(world, pos);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		return IBlock.getStatePropertySafe(state, IS_CONNECTED) ? Block.box(0, 0, 0, 16, 1, 16) : Shapes.block();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState blockState) {
		return PushReaction.BLOCK;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, IS_22_5, IS_45, IS_CONNECTED);
	}

	public static void resetRailNode(Level world, BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockNode) {
			world.setBlockAndUpdate(pos, state.setValue(BlockNode.IS_CONNECTED, false));
		}
	}

	public static float getAngle(BlockState state) {
		return (IBlock.getStatePropertySafe(state, BlockNode.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(state, BlockNode.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(state, BlockNode.IS_45) ? 45 : 0);
	}

	public static class BlockBoatNode extends BlockNode implements EntityBlockMapper {

		public BlockBoatNode() {
			super(TransportMode.BOAT);
		}

		@Override
		public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
			if (state.canSurvive(world, pos)) {
				return state;
			} else {
				return Blocks.AIR.defaultBlockState();
			}
		}

		@Override
		public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
			final BlockPos posBelow = pos.below();
			return (world.getFluidState(posBelow).getType() != Fluids.EMPTY || world.getBlockState(posBelow).getMaterial() == Material.ICE) && world.getFluidState(pos).getType() == Fluids.EMPTY;
		}

		@Override
		public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
			return new TileEntityBoatNode(pos, state);
		}
	}

	public static class TileEntityBoatNode extends BlockEntityMapper {

		public TileEntityBoatNode(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.BOAT_NODE_TILE_ENTITY.get(), pos, state);
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
		public BlockState getStateForPlacement(BlockPlaceContext ctx) {
			final int quadrant = RailAngle.getQuadrant(ctx.getRotation(), false);
			return defaultBlockState().setValue(FACING, quadrant % 4 >= 2).setValue(IS_45, quadrant % 2 >= 1).setValue(IS_22_5, false).setValue(IS_CONNECTED, false);
		}

		@Override
		public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
			return Block.box(0, upper ? 8 : 0, 0, 16, upper ? 16 : 8, 16);
		}

		@Override
		public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
			final String[] strings = Text.translatable("tooltip.mtr.cable_car_node" + (isStation ? "_station" : "")).getString().split("\n");
			for (final String string : strings) {
				tooltip.add(Text.literal(string).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
			}
		}
	}
}
