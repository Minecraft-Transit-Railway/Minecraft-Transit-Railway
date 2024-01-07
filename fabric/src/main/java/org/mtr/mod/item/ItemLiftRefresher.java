package org.mtr.mod.item;

import org.mtr.core.data.LiftFloor;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.packet.PacketData;
import org.mtr.mod.packet.PacketOpenLiftCustomizationScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemLiftRefresher extends ItemExtension implements DirectionHelper {

	public ItemLiftRefresher(ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		final World world = context.getWorld();
		final PlayerEntity playerEntity = context.getPlayer();

		if (world.isClient() || playerEntity == null) {
			return super.useOnBlock2(context);
		} else {
			final BlockPos blockPos = context.getBlockPos();
			final ObjectOpenHashSet<BlockPos> blacklistedBlockPos = new ObjectOpenHashSet<>();

			final ObjectArrayList<LiftFloor> liftFloors1 = new ObjectArrayList<>();
			if (!findPath(world, blockPos, null, liftFloors1, blacklistedBlockPos, true)) {
				playerEntity.sendMessage(new Text(TextHelper.translatable("gui.mtr.lift_track_required").data), true);
				return ActionResult.getFailMapped();
			}

			blacklistedBlockPos.remove(blockPos);
			final ObjectArrayList<LiftFloor> liftFloors2 = new ObjectArrayList<>();
			findPath(world, blockPos, null, liftFloors2, blacklistedBlockPos, false);

			if (liftFloors1.isEmpty() && liftFloors2.isEmpty()) {
				return ActionResult.getFailMapped();
			}

			final ObjectArrayList<LiftFloor> liftFloors = new ObjectArrayList<>();
			liftFloors.addAll(reverseList(liftFloors1));
			liftFloors.addAll(liftFloors2);
			final boolean needsReverse = Utilities.getElement(liftFloors, -1).getPosition().getY() < Utilities.getElement(liftFloors, 0).getPosition().getY();
			PacketData.generateLiftPath(ServerWorld.cast(world), needsReverse ? reverseList(liftFloors) : liftFloors);
			Registry.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenLiftCustomizationScreen(Init.positionToBlockPos(liftFloors.get(0).getPosition())));
			return ActionResult.getSuccessMapped();
		}
	}

	private static boolean findPath(World world, BlockPos blockPos, @Nullable Direction direction, ObjectArrayList<LiftFloor> liftFloors, ObjectOpenHashSet<BlockPos> blacklistedBlockPos, boolean addFirstFloor) {
		if (blacklistedBlockPos.contains(blockPos)) {
			return false;
		}

		blacklistedBlockPos.add(blockPos);
		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();

		if (!(block.data instanceof BlockLiftTrackBase)) {
			return false;
		}

		final ObjectArrayList<Direction> connectingDirections = ((BlockLiftTrackBase) (block.data)).getConnectingDirections(blockState);

		if (direction != null && !connectingDirections.contains(direction.getOpposite())) {
			return false;
		}

		final BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (addFirstFloor && blockEntity != null && blockEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			liftFloors.add(new LiftFloor(
					Init.blockPosToPosition(blockPos),
					((BlockLiftTrackFloor.BlockEntity) (blockEntity.data)).getFloorNumber(),
					((BlockLiftTrackFloor.BlockEntity) (blockEntity.data)).getFloorDescription()
			));
		}

		for (final Direction connectingDirection : connectingDirections) {
			if (findPath(world, blockPos.offset(connectingDirection), connectingDirection, liftFloors, blacklistedBlockPos, true)) {
				break;
			}
		}

		return true;
	}

	private static ObjectArrayList<LiftFloor> reverseList(ObjectArrayList<LiftFloor> liftFloors) {
		final ObjectArrayList<LiftFloor> liftFloorsReversed = new ObjectArrayList<>();
		for (final LiftFloor liftFloor : liftFloors) {
			liftFloorsReversed.add(0, liftFloor);
		}
		return liftFloorsReversed;
	}
}
