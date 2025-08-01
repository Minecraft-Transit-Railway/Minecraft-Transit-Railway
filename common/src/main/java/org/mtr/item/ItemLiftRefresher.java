package org.mtr.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.block.BlockLiftTrackBase;
import org.mtr.block.BlockLiftTrackFloor;
import org.mtr.client.CustomResourceLoader;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.Position;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketOpenLiftCustomizationScreen;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemLiftRefresher extends Item {

	public ItemLiftRefresher(Item.Settings settings) {
		super(settings.maxCount(1));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		final PlayerEntity playerEntity = context.getPlayer();

		if (world.isClient() || playerEntity == null) {
			return super.useOnBlock(context);
		} else {
			final BlockPos blockPos = context.getBlockPos();
			final ObjectOpenHashSet<BlockPos> blacklistedBlockPos = new ObjectOpenHashSet<>();

			final ObjectArrayList<LiftFloor> liftFloors1 = new ObjectArrayList<>();
			if (!findPath(world, blockPos, null, liftFloors1, new ObjectArrayList<>(), blacklistedBlockPos, true, false)) {
				playerEntity.sendMessage(TranslationProvider.GUI_MTR_LIFT_TRACK_REQUIRED.getText(), true);
				return ActionResult.FAIL;
			}

			blacklistedBlockPos.remove(blockPos);
			final ObjectArrayList<LiftFloor> liftFloors2 = new ObjectArrayList<>();
			findPath(world, blockPos, null, liftFloors2, new ObjectArrayList<>(), blacklistedBlockPos, false, false);

			if (liftFloors1.isEmpty() && liftFloors2.isEmpty()) {
				return ActionResult.FAIL;
			}

			final ObjectArrayList<LiftFloor> liftFloors = new ObjectArrayList<>();
			liftFloors.addAll(reverseList(liftFloors1));
			liftFloors.addAll(liftFloors2);
			final boolean needsReverse = Utilities.getElement(liftFloors, -1).getPosition().getY() < Utilities.getElement(liftFloors, 0).getPosition().getY();
			sendUpdate((ServerWorld) world, needsReverse ? reverseList(liftFloors) : liftFloors);
			Registry.sendPacketToClient((ServerPlayerEntity) playerEntity, new PacketOpenLiftCustomizationScreen(MTR.positionToBlockPos(liftFloors.get(0).getPosition())));
			return ActionResult.SUCCESS;
		}
	}

	/**
	 * Find a path (of lift tracks) from one floor to another
	 */
	public static ObjectArrayList<Vector> findPath(World world, Position startPosition, Position endPosition) {
		final ObjectOpenHashSet<BlockPos> blacklistedBlockPos = new ObjectOpenHashSet<>();
		final BlockPos startBlockPos = MTR.positionToBlockPos(startPosition);

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

		if (!(block instanceof BlockLiftTrackBase)) {
			return false;
		}

		final ObjectArrayList<Direction> connectingDirections = ((BlockLiftTrackBase) (block)).getConnectingDirections(blockState);

		if (direction != null && !connectingDirections.contains(direction.getOpposite())) {
			return false;
		}

		liftTrackPositions.add(((BlockLiftTrackBase) block).getCenterPoint(blockPos, blockState));
		final BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (addFirstFloor && blockEntity != null && blockEntity instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity) {
			liftFloors.add(new LiftFloor(
					MTR.blockPosToPosition(blockPos),
					((BlockLiftTrackFloor.LiftTrackFloorBlockEntity) (blockEntity)).getFloorNumber(),
					((BlockLiftTrackFloor.LiftTrackFloorBlockEntity) (blockEntity)).getFloorDescription()
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
		lift.setStyle(CustomResourceLoader.DEFAULT_LIFT_TRANSPARENT_ID);
		MTR.sendMessageC2S(OperationProcessor.GENERATE_BY_LIFT, serverWorld.getServer(), serverWorld, lift, null, null);
	}
}
