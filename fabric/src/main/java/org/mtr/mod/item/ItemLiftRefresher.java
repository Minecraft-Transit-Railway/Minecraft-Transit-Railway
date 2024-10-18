package org.mtr.mod.item;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Position;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.generated.lang.TranslationProvider;
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
			if (!findPath(world, blockPos, null, liftFloors1, new ObjectArrayList<>(), blacklistedBlockPos, true, false)) {
				playerEntity.sendMessage(TranslationProvider.GUI_MTR_LIFT_TRACK_REQUIRED.getText(), true);
				return ActionResult.getFailMapped();
			}

			blacklistedBlockPos.remove(blockPos);
			final ObjectArrayList<LiftFloor> liftFloors2 = new ObjectArrayList<>();
			findPath(world, blockPos, null, liftFloors2, new ObjectArrayList<>(), blacklistedBlockPos, false, false);

			if (liftFloors1.isEmpty() && liftFloors2.isEmpty()) {
				return ActionResult.getFailMapped();
			}

			final ObjectArrayList<LiftFloor> liftFloors = new ObjectArrayList<>();
			liftFloors.addAll(reverseList(liftFloors1));
			liftFloors.addAll(liftFloors2);
			final boolean needsReverse = Utilities.getElement(liftFloors, -1).getPosition().getY() < Utilities.getElement(liftFloors, 0).getPosition().getY();
			sendUpdate(ServerWorld.cast(world), needsReverse ? reverseList(liftFloors) : liftFloors);
			Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(playerEntity), new PacketOpenLiftCustomizationScreen(Init.positionToBlockPos(liftFloors.get(0).getPosition())));
			return ActionResult.getSuccessMapped();
		}
	}

	/**
	 * Find a path (of lift tracks) from one floor to another
	 */
	public static ObjectArrayList<Vector> findPath(World world, Position startPosition, Position endPosition) {
		final ObjectOpenHashSet<BlockPos> blacklistedBlockPos = new ObjectOpenHashSet<>();
		final BlockPos startBlockPos = Init.positionToBlockPos(startPosition);

		for (int i = 0; i < 2; i++) {
			final ObjectArrayList<LiftFloor> liftFloors = new ObjectArrayList<>();
			final ObjectArrayList<Vector> liftTrackPositions = new ObjectArrayList<>();
			if (findPath(world, startBlockPos, null, liftFloors, liftTrackPositions, blacklistedBlockPos, false, true)) {
				if (liftFloors.size() == 1 && liftFloors.get(0).getPosition().equals(endPosition)) {
					return liftTrackPositions;
				}
				blacklistedBlockPos.remove(startBlockPos);
			}
		}

		return ObjectArrayList.of(new Vector(startPosition.getX(), startPosition.getY(), startPosition.getZ()), new Vector(endPosition.getX(), endPosition.getY(), endPosition.getZ()));
	}

	/**
	 * Find a path (both lift tracks and lift track floors)
	 */
	private static boolean findPath(World world, BlockPos blockPos, @Nullable Direction direction, ObjectArrayList<LiftFloor> liftFloors, ObjectArrayList<Vector> liftTrackPositions, ObjectOpenHashSet<BlockPos> blacklistedBlockPos, boolean addFirstFloor, boolean findOneFloorOnly) {
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

		liftTrackPositions.add(((BlockLiftTrackBase) block.data).getCenterPoint(blockPos, blockState));
		final BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (addFirstFloor && blockEntity != null && blockEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			liftFloors.add(new LiftFloor(
					Init.blockPosToPosition(blockPos),
					((BlockLiftTrackFloor.BlockEntity) (blockEntity.data)).getFloorNumber(),
					((BlockLiftTrackFloor.BlockEntity) (blockEntity.data)).getFloorDescription()
			));

			if (findOneFloorOnly) {
				return true;
			}
		}

		for (final Direction connectingDirection : connectingDirections) {
			if (findPath(world, blockPos.offset(connectingDirection), connectingDirection, liftFloors, liftTrackPositions, blacklistedBlockPos, true, findOneFloorOnly)) {
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

	private static void sendUpdate(ServerWorld serverWorld, ObjectArrayList<LiftFloor> liftFloors) {
		final Lift lift = new Lift(new MinecraftClientData());
		lift.setFloors(liftFloors);
		lift.setDimensions(3, 2, 2, 0, 0, 0);
		Init.sendMessageC2S(OperationProcessor.GENERATE_BY_LIFT, serverWorld.getServer(), new World(serverWorld.data), lift, null, null);
	}
}
