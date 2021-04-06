package mtr.block;

import mtr.MTR;
import mtr.data.TicketSystem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockTicketBarrier extends HorizontalFacingBlock {

	private final boolean isEntrance;

	public static final BooleanProperty OPEN = BooleanProperty.of("open");

	public BlockTicketBarrier(boolean isEntrance) {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
		this.isEntrance = isEntrance;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof PlayerEntity) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final Vec3d playerPosRotated = entity.getPos().subtract(pos.getX() + 0.5, 0, pos.getZ() + 0.5).rotateY((float) Math.toRadians(facing.asRotation()));
			final boolean open = IBlock.getStatePropertySafe(state, OPEN);

			if (open && playerPosRotated.z > 0) {
				world.setBlockState(pos, state.with(OPEN, false));
			} else if (!open && playerPosRotated.z < 0) {
				world.setBlockState(pos, state.with(OPEN, TicketSystem.passThrough(world, pos, (PlayerEntity) entity, isEntrance, !isEntrance, MTR.TICKET_BARRIER, MTR.TICKET_BARRIER_CONCESSIONARY)));
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing()).with(OPEN, false);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 15, 16, facing);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final boolean open = IBlock.getStatePropertySafe(state, OPEN);
		final VoxelShape base = IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 24, 16, facing);
		return open ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 24, 9, facing), base);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN);
	}
}
