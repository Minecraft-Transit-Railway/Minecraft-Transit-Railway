package org.mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.jspecify.annotations.Nullable;
import org.mtr.block.BlockNode;
import org.mtr.core.data.Rail;
import org.mtr.core.tool.Utilities;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represent an action that runs along a rail, for example one made by the Tunnel Creator or Bridge Creator
 */
public class RailAction {

	private double distance;

	public final long id;
	private final ServerLevel serverWorld;
	private final UUID uuid;
	private final String playerName;
	private final RailActionType railActionType;
	private final Rail rail;
	private final int radius;
	private final int height;
	private final double length;
	@Nullable
	private final BlockState state;
	private final boolean isSlab;
	private final ObjectOpenHashSet<BlockPos> blacklistedPositions = new ObjectOpenHashSet<>();

	private static final double INCREMENT = 0.1;

	public RailAction(ServerLevel serverWorld, ServerPlayer serverPlayerEntity, RailActionType railActionType, Rail rail, int radius, int height, @Nullable BlockState state) {
		id = new Random().nextLong();
		this.serverWorld = serverWorld;
		uuid = serverPlayerEntity.getUUID();
		playerName = serverPlayerEntity.getName().getString();
		this.railActionType = railActionType;
		this.rail = rail;
		this.radius = radius;
		this.height = height;
		this.state = state;
		isSlab = state != null && state.getBlock() instanceof SlabBlock;
		length = rail.railMath.getLength();
		distance = 0;
	}

	/**
	 * Perform a build action, should be called every tick.
	 *
	 * @return whether the rail action is completed
	 */
	public boolean build() {
		return switch (railActionType) {
			case BRIDGE -> createBridge();
			case TUNNEL -> createTunnel();
			case TUNNEL_WALL -> createTunnelWall();
		};
	}

	public String getDescription() {
		return railActionType.nameTranslation.getString(playerName, Utilities.round(length, 1), state == null ? "" : Component.translatable(state.getBlock().getDescriptionId()).getString());
	}

	public int getColor() {
		return railActionType.color;
	}

	private boolean createTunnel() {
		return create(true, vector -> {
			final BlockPos blockPos = fromVector(vector);
			if (!blacklistedPositions.contains(blockPos) && canPlace(serverWorld, blockPos)) {
				serverWorld.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
				blacklistedPositions.add(blockPos);
			}
		});
	}

	private boolean createTunnelWall() {
		return create(false, vector -> {
			final BlockPos blockPos = fromVector(vector);
			if (!blacklistedPositions.contains(blockPos) && canPlace(serverWorld, blockPos)) {
				serverWorld.setBlockAndUpdate(blockPos, state);
				blacklistedPositions.add(blockPos);
			}
		});
	}

	private boolean createBridge() {
		return create(false, vector -> {
			final boolean isTopHalf = vector.y() - Math.floor(vector.y()) >= 0.5;
			final BlockPos blockPos = fromVector(vector);

			final BlockPos placePos;
			final BlockState placeState;
			final boolean placeHalf;

			if (isSlab && isTopHalf) {
				placePos = blockPos;
				placeState = state == null ? null : state.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
				placeHalf = false;
			} else {
				placePos = blockPos.below();
				placeState = isSlab && state != null ? state.setValue(SlabBlock.TYPE, SlabType.TOP) : state;
				placeHalf = true;
			}

			final BlockPos halfPos = getHalfPos(placePos, placeHalf);
			if (blacklistedPositions.contains(halfPos)) {
				return;
			}

			if (placePos != blockPos && canPlace(serverWorld, blockPos)) {
				serverWorld.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
			}
			if (canPlace(serverWorld, placePos)) {
				serverWorld.setBlockAndUpdate(placePos, placeState);
				blacklistedPositions.add(halfPos);
			}
		});
	}

	private boolean create(boolean includeMiddle, Consumer<Vector> consumer) {
		final long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 2) {
			final Vector pos1 = rail.railMath.getPosition(distance, false);
			distance += INCREMENT;
			final Vector pos2 = rail.railMath.getPosition(distance, false);
			final Vector Vec3 = new Vector(pos2.x() - pos1.x(), 0, pos2.z() - pos1.z()).normalize().rotateY((float) Math.PI / 2);

			for (double x = -radius; x <= radius; x += INCREMENT) {
				final Vector editPos = pos1.add(Vec3.multiply(x, 0, x));
				final boolean wholeNumber = Math.floor(editPos.y()) == Math.ceil(editPos.y());
				if (includeMiddle || Math.abs(x) > radius - INCREMENT || radius == 0) {
					for (int y = 0; y <= height; y++) {
						if (y < height || !wholeNumber || (height == 0 && radius == 0)) {
							consumer.accept(editPos.add(0, y, 0));
						}
					}
				} else {
					consumer.accept(editPos.add(0, Math.max(0, wholeNumber ? height - 1 : height), 0));
				}
			}

			if (length - distance < INCREMENT) {
				sendProgressMessage(100);
				return true;
			}
		}

		sendProgressMessage((float) Utilities.round(100 * distance / length, 1));
		return false;
	}

	private void sendProgressMessage(float percentage) {
		final Player playerEntity = serverWorld.getPlayerByUUID(uuid);
		if (playerEntity != null) {
			playerEntity.displayClientMessage(railActionType.progressTranslation.getText(percentage), true);
		}
	}

	private static boolean canPlace(ServerLevel serverWorld, BlockPos pos) {
		return serverWorld.getBlockEntity(pos) == null && !(serverWorld.getBlockState(pos).getBlock() instanceof BlockNode);
	}

	private static BlockPos getHalfPos(BlockPos pos, boolean isTopHalf) {
		return new BlockPos(pos.getX(), pos.getY() * 2 + (isTopHalf ? 1 : 0), pos.getZ());
	}

	private static BlockPos fromVector(Vector vector) {
		return BlockPos.containing(vector.x(), vector.y(), vector.z());
	}
}
