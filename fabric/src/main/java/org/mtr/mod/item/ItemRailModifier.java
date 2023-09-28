package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tools.Angle;
import org.mtr.core.tools.Position;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.data.RailType;
import org.mtr.mod.packet.PacketData;

import javax.annotation.Nullable;
import java.util.List;

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
			tooltip.add(TextHelper.translatable("tooltip.mtr.rail_speed_limit", railType.speedLimit).formatted(TextFormatting.GRAY));
		}
		super.addTooltips(stack, world, tooltip, options);
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		if (railType != null) {
			final boolean isValidContinuousMovement;
			final RailType newRailType;
			if (transportMode.continuousMovement) {
				final Block blockStart = stateStart.getBlock();
				final Block blockEnd = stateEnd.getBlock();

				if (blockStart.data instanceof BlockNode.BlockContinuousMovementNode && blockEnd.data instanceof BlockNode.BlockContinuousMovementNode) {
					if (((BlockNode.BlockContinuousMovementNode) blockStart.data).isStation && ((BlockNode.BlockContinuousMovementNode) blockEnd.data).isStation) {
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
					isValidContinuousMovement = false;
					newRailType = railType;
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
					rail = Rail.newPlatformRail(positionStart, facingStart, Rail.Shape.CURVE, positionEnd, facingEnd, Rail.Shape.CURVE, transportMode);
					break;
				case SIDING:
					rail = Rail.newSidingRail(positionStart, facingStart, Rail.Shape.CURVE, positionEnd, facingEnd, Rail.Shape.CURVE, transportMode);
					break;
				case TURN_BACK:
					rail = Rail.newTurnBackRail(positionStart, facingStart, Rail.Shape.CURVE, positionEnd, facingEnd, Rail.Shape.CURVE, transportMode);
					break;
				default:
					rail = Rail.newRail(positionStart, facingStart, newRailType.railShape, positionEnd, facingEnd, newRailType.railShape, isOneWay ? 0 : newRailType.speedLimit, newRailType.speedLimit, false, false, newRailType.canAccelerate, newRailType.hasSignal, transportMode);
			}

			if (rail.isValid() && isValidContinuousMovement) {
				world.setBlockState(posStart, stateStart.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				world.setBlockState(posEnd, stateEnd.with(new Property<>(BlockNode.IS_CONNECTED.data), true));
				PacketData.updateRail(ServerWorld.cast(world), rail);
			} else if (player != null) {
				player.sendMessage(new Text(TextHelper.translatable(isValidContinuousMovement ? "gui.mtr.invalid_orientation" : "gui.mtr.cable_car_invalid_orientation").data), true);
			}
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		PacketData.deleteRail(ServerWorld.cast(world), Rail.newRail(Init.blockPosToPosition(posStart), Angle.N, Rail.Shape.CURVE, Init.blockPosToPosition(posEnd), Angle.N, Rail.Shape.CURVE, 0, 0, false, false, false, false, TransportMode.TRAIN));
	}
}
