package org.mtr.mod.item;

import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.data.RailType;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDeleteData;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.packet.PacketUpdateLastRailStyles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemRailModifier extends ItemNodeModifierBase {

	private final boolean isOneWay;
	private final RailType railType;

	public ItemRailModifier(ItemSettings itemSettings) {
		super(true, true, true, false, itemSettings);
		isOneWay = false;
		railType = null;
	}

	public ItemRailModifier(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isOneWay, RailType railType, ItemSettings itemSettings) {
		super(forNonContinuousMovementNode, forContinuousMovementNode, forAirplaneNode, true, itemSettings);
		this.isOneWay = isOneWay;
		this.railType = railType;
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		if (isConnector && railType != null && railType.canAccelerate) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_SPEED_LIMIT.getMutableText(railType.speedLimit).formatted(TextFormatting.GRAY));
		}
		super.addTooltips(stack, world, tooltip, options);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		final PlayerEntity player = context.getPlayer();
		final CompoundTag compoundTag = context.getStack().getOrCreateTag();
		if (isConnector && railType != null && player != null && player.isSneaking() && compoundTag.contains(TAG_POS)) {
			return context.getWorld().isClient() ? ActionResult.SUCCESS : placeNodeAndConnect(context, compoundTag, player);
		} else {
			return super.useOnBlock2(context);
		}
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		if (railType != null) {
			final Rail rail = createRail(player == null ? null : player.getUuid(), transportMode, stateStart, stateEnd, posStart, posEnd, facingStart, facingEnd);
			if (rail != null) {
				world.setBlockState(posStart, stateStart.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				PacketUpdateData.sendDirectlyToServerRail(ServerWorld.cast(world), rail);
			} else if (player != null) {
				player.sendMessage(TranslationProvider.GUI_MTR_INVALID_ORIENTATION.getText(), true);
			}
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		PacketDeleteData.sendDirectlyToServerRailId(ServerWorld.cast(world), TwoPositionsBase.getHexId(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd)));
	}

	@Nullable
	public Rail createRail(@Nullable UUID uuid, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd) {
		if (railType != null && uuid != null) {
			final boolean isValidContinuousMovement;
			final RailType newRailType;

			if (transportMode.continuousMovement) {
				final Block blockStart = stateStart.getBlock();
				final Block blockEnd = stateEnd.getBlock();

				if (blockStart.data instanceof BlockNode.BlockContinuousMovementNode && blockEnd.data instanceof BlockNode.BlockContinuousMovementNode && ((BlockNode.BlockContinuousMovementNode) blockStart.data).isStation && ((BlockNode.BlockContinuousMovementNode) blockEnd.data).isStation) {
					isValidContinuousMovement = true;
					newRailType = railType.isSavedRail ? railType : RailType.CABLE_CAR_STATION;
				} else {
					final int differenceX = posEnd.getX() - posStart.getX();
					final int differenceZ = posEnd.getZ() - posStart.getZ();
					isValidContinuousMovement = !railType.isSavedRail && facingStart.isParallel(facingEnd)
							&& ((facingStart == Angle.N || facingStart == Angle.S) && differenceX == 0
							|| (facingStart == Angle.E || facingStart == Angle.W) && differenceZ == 0
							|| (facingStart == Angle.NE || facingStart == Angle.SW) && differenceX == -differenceZ
							|| (facingStart == Angle.SE || facingStart == Angle.NW) && differenceX == differenceZ);
					newRailType = RailType.CABLE_CAR;
				}
			} else {
				isValidContinuousMovement = true;
				newRailType = railType;
			}

			final Position positionStart = Init.blockPosToPosition(posStart);
			final Position positionEnd = Init.blockPosToPosition(posEnd);
			final Rail rail;

			switch (newRailType) {
				case PLATFORM:
					rail = Rail.newPlatformRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, new ObjectArrayList<>(), transportMode);
					break;
				case SIDING:
					rail = Rail.newSidingRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, new ObjectArrayList<>(), transportMode);
					break;
				case TURN_BACK:
					rail = Rail.newTurnBackRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, new ObjectArrayList<>(), transportMode);
					break;
				default:
					rail = Rail.newRail(positionStart, facingStart, positionEnd, facingEnd, newRailType.railShape, 0, new ObjectArrayList<>(), isOneWay ? 0 : newRailType.speedLimit, newRailType.speedLimit, false, false, newRailType.canAccelerate, newRailType == RailType.RUNWAY, newRailType.hasSignal, transportMode);
			}

			if (rail.isValid() && isValidContinuousMovement) {
				return PacketUpdateLastRailStyles.SERVER_CACHE.getRailWithLastStyles(uuid, rail);
			}
		}

		return null;
	}

	@Nonnull
	private ActionResult placeNodeAndConnect(ItemUsageContext context, CompoundTag compoundTag, PlayerEntity player) {
		final World world = context.getWorld();
		final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(TAG_POS));
		final BlockState stateEnd = world.getBlockState(posEnd);
		final Block blockEnd = stateEnd.getBlock();

		if (!(blockEnd.data instanceof BlockNode) || !ServerPlayerEntity.isInstance(player)) {
			return ActionResult.FAIL;
		}

		final BlockNode blockNodeEnd = (BlockNode) blockEnd.data;
		if (!canConnectToNode(blockNodeEnd) || blockNodeEnd.transportMode != getTransportMode(compoundTag)) {
			return ActionResult.FAIL;
		}

		final Vector3d hitPos = context.getHitPos();
		final BlockPos posStart = Init.newBlockPos(hitPos.getXMapped(), hitPos.getYMapped(), hitPos.getZMapped());
		if (posStart.equals(posEnd)) {
			return ActionResult.FAIL;
		}

		final BlockState replacedState = world.getBlockState(posStart);
		final boolean targetContainsWater = world.getFluidState(posStart).getFluid().data == Fluids.getWaterMapped().data;
		if (!replacedState.isAir() && !targetContainsWater) {
			return ActionResult.FAIL;
		}

		final float angleEnd = BlockNode.getAngle(stateEnd);
		final float playerYaw = EntityHelper.getYaw(new Entity(player.data));
		final boolean continuousMovementNode = blockEnd.data instanceof BlockNode.BlockContinuousMovementNode;
		final float nodeAngleStart = continuousMovementNode ? angleEnd : playerYaw;
		final float railAngleStart = continuousMovementNode ? angleEnd : playerYaw + 90;
		final BlockState stateStart = BlockNode.getStateWithAngle(blockEnd.getDefaultState(), nodeAngleStart)
				.with(new Property<>(SlabBlockExtension.WATERLOGGED), targetContainsWater);
		final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(Init.blockPosToPosition(posStart), railAngleStart, Init.blockPosToPosition(posEnd), angleEnd);
		final Rail rail = createRail(player.getUuid(), blockNodeEnd.transportMode, stateStart, stateEnd, posStart, posEnd, angles.left(), angles.right());

		if (rail == null) {
			player.sendMessage(TranslationProvider.GUI_MTR_INVALID_ORIENTATION.getText(), true);
			return ActionResult.FAIL;
		}

		world.setBlockState(posStart, stateStart.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
		world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
		PacketUpdateData.sendDirectlyToServerRail(ServerWorld.cast(world), rail);
		compoundTag.remove(TAG_POS);
		compoundTag.remove(TAG_TRANSPORT_MODE);
		return ActionResult.SUCCESS;
	}

	private boolean canConnectToNode(BlockNode blockNode) {
		if (blockNode.transportMode == TransportMode.AIRPLANE) {
			return forAirplaneNode;
		} else {
			return blockNode.transportMode.continuousMovement ? forContinuousMovementNode : forNonContinuousMovementNode;
		}
	}
}
