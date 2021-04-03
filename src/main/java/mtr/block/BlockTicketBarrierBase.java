package mtr.block;

import mtr.MTR;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class BlockTicketBarrierBase extends HorizontalFacingBlock {

	public static final BooleanProperty OPEN = BooleanProperty.of("open");

	protected static final int BASE_FARE = 2;
	protected static final int ZONE_FARE = 1;
	protected static final int EVASION_FINE = 500;

	private static final String BALANCE_OBJECTIVE = "mtr_balance";
	private static final String ENTRY_ZONE_OBJECTIVE = "mtr_entry_zone";

	public BlockTicketBarrierBase(Settings settings) {
		super(settings);
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
				if (!world.getScoreboard().containsObjective(BALANCE_OBJECTIVE)) {
					world.getScoreboard().addObjective(BALANCE_OBJECTIVE, ScoreboardCriterion.DUMMY, new LiteralText("Balance"), ScoreboardCriterion.RenderType.INTEGER);
				}
				if (!world.getScoreboard().containsObjective(ENTRY_ZONE_OBJECTIVE)) {
					world.getScoreboard().addObjective(ENTRY_ZONE_OBJECTIVE, ScoreboardCriterion.DUMMY, new LiteralText("Entry Zone"), ScoreboardCriterion.RenderType.INTEGER);
				}

				final PlayerEntity player = (PlayerEntity) entity;
				final ScoreboardObjective balanceObjective = world.getScoreboard().getObjective(BALANCE_OBJECTIVE);
				final ScoreboardObjective entryZoneObjective = world.getScoreboard().getObjective(ENTRY_ZONE_OBJECTIVE);
				final ScoreboardPlayerScore balanceScore = world.getScoreboard().getPlayerScore(player.getGameProfile().getName(), balanceObjective);
				final ScoreboardPlayerScore entryZoneScore = world.getScoreboard().getPlayerScore(player.getGameProfile().getName(), entryZoneObjective);

				onPassBarrier(world, pos, player, balanceScore, entryZoneScore);

				final boolean canOpen = canOpen(balanceScore.getScore());
				if (canOpen) {
					world.playSound(null, pos, isConcessionary(player) ? MTR.TICKET_BARRIER_CONCESSIONARY : MTR.TICKET_BARRIER, SoundCategory.BLOCKS, 1, 1);
				}
				world.setBlockState(pos, state.with(OPEN, canOpen));
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

	protected boolean isConcessionary(PlayerEntity player) {
		return player.isCreative();
	}

	private void onPassBarrier(World world, BlockPos pos, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final Station station = railwayData.getStations().stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).findFirst().orElse(null);
		if (station == null) {
			return;
		}

		onPassBarrier(world, station, player, balanceScore, entryZoneScore);
	}

	protected abstract void onPassBarrier(World world, Station station, PlayerEntity player, ScoreboardPlayerScore balanceScore, ScoreboardPlayerScore entryZoneScore);

	protected abstract boolean canOpen(int balance);
}
