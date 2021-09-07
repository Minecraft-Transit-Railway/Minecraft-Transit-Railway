package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockPSDTopOnly extends HorizontalFacingBlock implements BlockEntityProvider, IBlock, IPropagateBlock {
	public static final BooleanProperty HAS_ARROW = BooleanProperty.of("arrow");

	public BlockPSDTopOnly() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.OFF_WHITE).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isHolding(Items.ARROW)) { // platform arrows
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
			final BlockState leftState = world.getBlockState(leftPos);
			final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
			final BlockState rightState = world.getBlockState(rightPos);
			if (leftState.getBlock() instanceof BlockPSDTopOnly) {
				if(leftState.get(HAS_ARROW) == true && leftState.get(SIDE_EXTENDED) == EnumSide.LEFT) { // We are the right side of an arrow
					int side = leftState.get(PROPAGATE_PROPERTY);
					int newLeftSide = 1;
					switch(side) {
						case 0:
							newLeftSide = 1;
							break;
						case 1:
							newLeftSide = 2;
							break;
						case 2:
							newLeftSide = 3;
							break;
						case 3:
							newLeftSide = 0;
							world.setBlockState(leftPos, leftState.with(HAS_ARROW, false));
							world.setBlockState(pos, state.with(HAS_ARROW, false));
							world.setBlockState(leftPos, leftState.with(SIDE_EXTENDED, EnumSide.SINGLE));
							world.setBlockState(pos, state.with(SIDE_EXTENDED, EnumSide.SINGLE));
							break;
					}
					world.setBlockState(leftPos, leftState.with(PROPAGATE_PROPERTY, newLeftSide));
					world.setBlockState(pos, state.with(PROPAGATE_PROPERTY, newLeftSide));
					return ActionResult.SUCCESS;
				}
			}
			if (rightState.getBlock() instanceof BlockPSDTopOnly) {
				if (rightState.get(HAS_ARROW) == true && rightState.get(SIDE_EXTENDED) == EnumSide.RIGHT) {
					int side = rightState.get(PROPAGATE_PROPERTY);
					int newLeftSide = 1;
					switch(side) {
						case 0:
							break;
						case 1:
							newLeftSide = 2;
							break;
						case 2:
							newLeftSide = 3;
							break;
						case 3:
							newLeftSide = 0;
							world.setBlockState(rightPos, rightState.with(HAS_ARROW, false));
							world.setBlockState(pos, state.with(HAS_ARROW, false));
							world.setBlockState(rightPos, rightState.with(SIDE_EXTENDED, EnumSide.SINGLE));
							world.setBlockState(pos, state.with(SIDE_EXTENDED, EnumSide.SINGLE));
							break;
					}
					world.setBlockState(rightPos, rightState.with(PROPAGATE_PROPERTY, newLeftSide));
					world.setBlockState(pos, state.with(PROPAGATE_PROPERTY, newLeftSide));
					return ActionResult.SUCCESS;
				}
				world.setBlockState(rightPos, rightState.with(HAS_ARROW, true).with(SIDE_EXTENDED,
						EnumSide.RIGHT).with(PROPAGATE_PROPERTY, 0));
				world.setBlockState(pos, state.with(HAS_ARROW, true).with(SIDE_EXTENDED,
						EnumSide.LEFT).with(PROPAGATE_PROPERTY, 0));
			}
			return ActionResult.SUCCESS;
		}
		return IBlock.checkHoldingBrush(world, player, () -> {
			for (int y = -1; y <= 1; y++) {
				BlockState scanState = world.getBlockState(pos.up(y));
				if (is(scanState.getBlock())) {
					connectGlass(world, pos.up(y), scanState);
				}
			}
		});
	}

	private void connectGlass(World world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = (leftState.getBlock() instanceof BlockPSDTopOnly);

		if (leftValid) {
			final EnumSide side = IBlock.getStatePropertySafe(leftState, SIDE_EXTENDED);
			EnumSide newLeftSide;

			if (side == EnumSide.RIGHT) {
				newLeftSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newLeftSide = EnumSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockState(leftPos, leftState.with(SIDE_EXTENDED, newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = (rightState.getBlock() instanceof BlockPSDTopOnly);

		if (rightValid) {
			final EnumSide side = IBlock.getStatePropertySafe(rightState, SIDE_EXTENDED);
			EnumSide newRightSide;

			if (side == EnumSide.LEFT) {
				newRightSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newRightSide = EnumSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockState(rightPos, rightState.with(SIDE_EXTENDED, newRightSide));
		}

		EnumSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumSide.LEFT;
		} else {
			newSide = EnumSide.SINGLE;
		}

		world.setBlockState(pos, state.with(SIDE_EXTENDED, newSide));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, SIDE_EXTENDED, PROPAGATE_PROPERTY, HAS_ARROW);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityPSDTopOnly();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing()).with(HAS_ARROW, false);
	}

	public static class TileEntityPSDTopOnly extends BlockEntity {

		public TileEntityPSDTopOnly() {
			super(MTR.PSD_TOP_ONLY_TILE_ENTITY);
		}
	}
}
