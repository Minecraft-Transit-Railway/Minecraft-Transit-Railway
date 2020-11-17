package mtr.block;

import mtr.Items;
import mtr.data.RailwayData;
import mtr.data.TrainSpawner;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTrainSpawner extends HorizontalFacingBlock {

	public BlockTrainSpawner(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.addTrainSpawner(new TrainSpawner(pos));
				PacketTrainDataGuiServer.broadcastS2C(world, railwayData);
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				if (player.getStackInHand(hand).getItem() == Items.BRUSH) {
					PacketTrainDataGuiServer.openTrainSpawnerScreenS2C(player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), railwayData.getTrainSpawners(), pos);
				} else {
					PacketTrainDataGuiServer.openScheduleScreenS2C(player, railwayData.getStations(), railwayData.getPlatforms(world), railwayData.getRoutes(), railwayData.getTrainSpawners(), pos);
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
