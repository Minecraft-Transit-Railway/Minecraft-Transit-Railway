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

import java.util.HashSet;

public class BlockTrainSpawner extends HorizontalFacingBlock {

	public BlockTrainSpawner(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClient()) {
			RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData != null) {
				railwayData.addTrainSpawner(new TrainSpawner(pos, new HashSet<>()));
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).getItem() != Items.BRUSH) {
			return ActionResult.FAIL;
		} else {
			if (!world.isClient()) {
				RailwayData railwayData = RailwayData.getInstance(world);
				if (railwayData != null) {
					PacketTrainDataGuiServer.sendRoutesTrainSpawnersAndPosS2C(player, railwayData.getRoutes(), railwayData.getTrainSpawners(), pos, true);
					return ActionResult.PASS;
				}
			}
			return ActionResult.SUCCESS;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
