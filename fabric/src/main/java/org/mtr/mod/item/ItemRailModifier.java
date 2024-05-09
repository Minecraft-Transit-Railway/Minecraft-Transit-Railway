package org.mtr.mod.item;

import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.data.RailType;
import org.mtr.mod.packet.PacketDeleteData;
import org.mtr.mod.packet.PacketUpdateData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRailModifier extends ItemNodeModifierBase {

	private final boolean isOneWay;
	private final RailType railType;

	private static final Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<String>> LAST_STYLES = new Object2ObjectAVLTreeMap<>();

	static {
		for (final TransportMode transportMode : TransportMode.values()) {
			LAST_STYLES.put(transportMode, transportMode == TransportMode.BOAT ? new ObjectArrayList<>() : ObjectArrayList.of(CustomResourceLoader.DEFAULT_RAIL_ID));
		}
	}

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
			tooltip.add(TextHelper.translatable("tooltip.mtr.rail_speed_limit", railType.speedLimit).formatted(TextFormatting.GRAY));
		}
		super.addTooltips(stack, world, tooltip, options);
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		if (railType != null) {
			final Rail rail = createRail(transportMode, stateStart, stateEnd, posStart, posEnd, facingStart, facingEnd);
			if (rail != null) {
				world.setBlockState(posStart, stateStart.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				PacketUpdateData.sendDirectlyToServerRail(ServerWorld.cast(world), rail);
			} else if (player != null) {
				player.sendMessage(new Text(TextHelper.translatable("gui.mtr.invalid_orientation").data), true);
			}
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		PacketDeleteData.sendDirectlyToServerRailId(ServerWorld.cast(world), TwoPositionsBase.getHexId(Init.blockPosToPosition(posStart), Init.blockPosToPosition(posEnd)));
	}

	@Nullable
	public Rail createRail(TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd) {
		if (railType != null) {
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
			final ObjectArrayList<String> styles = LAST_STYLES.get(transportMode);
			final Rail rail;

			switch (newRailType) {
				case PLATFORM:
					rail = Rail.newPlatformRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, styles, transportMode);
					break;
				case SIDING:
					rail = Rail.newSidingRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, styles, transportMode);
					break;
				case TURN_BACK:
					rail = Rail.newTurnBackRail(positionStart, facingStart, positionEnd, facingEnd, Rail.Shape.QUADRATIC, 0, styles, transportMode);
					break;
				default:
					rail = Rail.newRail(positionStart, facingStart, positionEnd, facingEnd, newRailType.railShape, 0, styles, isOneWay ? 0 : newRailType.speedLimit, newRailType.speedLimit, false, false, newRailType.canAccelerate, newRailType == RailType.RUNWAY, newRailType.hasSignal, transportMode);
			}

			if (rail.isValid() && isValidContinuousMovement) {
				return rail;
			}
		}

		return null;
	}

	public static void setLastStyles(TransportMode transportMode, ObjectArrayList<String> styles) {
		LAST_STYLES.get(transportMode).clear();
		LAST_STYLES.get(transportMode).addAll(styles);
	}
}
