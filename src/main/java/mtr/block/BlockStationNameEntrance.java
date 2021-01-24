package mtr.block;

import mtr.MTR;
import mtr.gui.IGui;
import mtr.model.ModelTrainBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockStationNameEntrance extends BlockStationNameBase implements IPropagateBlock {

	public BlockStationNameEntrance(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			world.setBlockState(pos, state.cycle(PROPAGATE_PROPERTY));
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), 1);
			propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), 1);
		});
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return world.getBlockState(pos.offset(facing)).isSideSolidFullSquare(world, pos.offset(facing), facing.getOpposite());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState().with(FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction.getOpposite() == IBlock.getStatePropertySafe(state, FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean tall = IBlock.getStatePropertySafe(state, PROPAGATE_PROPERTY) % 2 == 1;
		return IBlock.getVoxelShapeByDirection(0, tall ? 0 : 4, 0, 16, tall ? 16 : 12, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityStationNameEntrance();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, PROPAGATE_PROPERTY);
	}

	public static class TileEntityStationNameEntrance extends TileEntityStationNameBase {

		public TileEntityStationNameEntrance() {
			super(MTR.STATION_NAME_ENTRANCE_TILE_ENTITY, 0, 0.000625F);
		}

		@Override
		public boolean shouldRender() {
			return world != null && !(world.getBlockState(pos.offset(IBlock.getStatePropertySafe(world, pos, FACING).rotateYCounterclockwise())).getBlock() instanceof BlockStationNameEntrance);
		}

		@Override
		protected void drawStationName(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String stationName, int color) {
			if (world == null) {
				return;
			}

			final int propagateProperty = IBlock.getStatePropertySafe(world, pos, PROPAGATE_PROPERTY);
			final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
			final int length = getLength();
			IGui.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, IGui.addToStationName(stationName, "", "", "站", " Station"), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (length + logoSize) / 2 - 0.5F, 0, length - logoSize, logoSize - 0.125F, 40 / logoSize, propagateProperty < 2 ? ARGB_WHITE : ARGB_BLACK, false, ((x1, y1, x2, y2) -> {
				IGui.drawTexture(matrices, vertexConsumers, "mtr:textures/block/logo.png", x1 - logoSize, -logoSize / 2, logoSize, logoSize, ModelTrainBase.MAX_LIGHT);
			}));
		}

		private int getLength() {
			if (world == null) {
				return 1;
			}
			final Direction facing = IBlock.getStatePropertySafe(world, pos, FACING);

			int length = 1;
			while (true) {
				final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), length));
				if (state.getBlock() instanceof BlockStationNameEntrance) {
					length++;
				} else {
					break;
				}
			}

			return length;
		}
	}
}
