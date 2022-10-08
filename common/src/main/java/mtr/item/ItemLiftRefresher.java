package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockLiftTrack;
import mtr.block.BlockLiftTrackFloor;
import mtr.block.IBlock;
import mtr.data.LiftServer;
import mtr.data.RailwayData;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemLiftRefresher extends Item {


	public ItemLiftRefresher() {
		super(new Properties().tab(ItemGroups.ESCALATORS_LIFTS).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!context.getLevel().isClientSide) {
			final Level world = context.getLevel();
			final BlockPos clickedPos = context.getClickedPos();
			final RailwayData railwayData = RailwayData.getInstance(world);
			final Player player = context.getPlayer();

			if (world.getBlockState(clickedPos).getBlock() instanceof BlockLiftTrack && railwayData != null) {
				final List<BlockPos> floors = new ArrayList<>();
				final Set<LiftServer> liftsToModify = new HashSet<>();
				int i = 0;
				boolean scanForFloors = false;
				BlockPos firstFloor = null;
				Direction facing = null;

				railwayData.lifts.removeIf(lift -> lift.isInvalidLift(world));

				while (true) {
					final BlockPos checkPos = clickedPos.below(i);
					final Block checkBlock = world.getBlockState(checkPos).getBlock();

					if (!(checkBlock instanceof BlockLiftTrack)) {
						if (scanForFloors) {
							break;
						} else {
							scanForFloors = true;
						}
					}

					if (scanForFloors && checkBlock instanceof BlockLiftTrackFloor) {
						final BlockEntity blockEntity = world.getBlockEntity(checkPos);
						if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
							floors.add(checkPos);
							if (firstFloor == null || facing == null) {
								firstFloor = checkPos;
								facing = IBlock.getStatePropertySafe(world, checkPos, HorizontalDirectionalBlock.FACING);
							}
						}
						railwayData.lifts.forEach(lift -> {
							if (lift.hasFloor(checkPos)) {
								liftsToModify.add(lift);
							}
						});
					}

					i += (scanForFloors ? -1 : 1);
				}

				final InteractionResult result;
				if (floors.isEmpty() || firstFloor == null || facing == null) {
					if (player != null) {
						player.displayClientMessage(Text.translatable("gui.mtr.no_lift_tracks_floor_found"), true);
					}
					result = InteractionResult.FAIL;
				} else {
					boolean hasSetFloors = false;
					for (final LiftServer lift : liftsToModify) {
						if (hasSetFloors) {
							railwayData.lifts.remove(lift);
						} else {
							lift.setFloors(floors);
							hasSetFloors = true;
						}
					}

					if (!hasSetFloors) {
						final LiftServer newLift = new LiftServer(firstFloor, facing);
						newLift.setFloors(floors);
						railwayData.lifts.add(newLift);
					}

					result = InteractionResult.SUCCESS;
				}

				railwayData.dataCache.sync();
				return result;
			} else {
				if (player != null) {
					player.displayClientMessage(Text.translatable("gui.mtr.lift_track_required"), true);
				}
				return InteractionResult.FAIL;
			}

		} else {
			return super.useOn(context);
		}
	}
}
