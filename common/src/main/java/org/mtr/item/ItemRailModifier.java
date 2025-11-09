package org.mtr.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.block.BlockNode;
import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.tool.Angle;
import org.mtr.data.RailType;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketDeleteData;
import org.mtr.packet.PacketUpdateData;
import org.mtr.packet.PacketUpdateLastRailStyles;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemRailModifier extends ItemNodeModifierBase {

	private final boolean isOneWay;
	private final RailType railType;

	public ItemRailModifier(Item.Settings settings) {
		super(true, true, true, false, settings);
		isOneWay = false;
		railType = null;
	}

	public ItemRailModifier(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isOneWay, RailType railType, Item.Settings settings) {
		super(forNonContinuousMovementNode, forContinuousMovementNode, forAirplaneNode, true, settings);
		this.isOneWay = isOneWay;
		this.railType = railType;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (isConnector && railType != null && railType.canAccelerate) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_SPEED_LIMIT.getMutableText(railType.speedLimit).formatted(Formatting.GRAY));
		}
		super.appendTooltip(stack, context, tooltip, type);
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
		if (railType != null) {
			final Rail rail = createRail(player == null ? null : player.getUuid(), transportMode, stateStart, stateEnd, posStart, posEnd, facingStart, facingEnd);
			if (rail != null) {
				world.setBlockState(posStart, stateStart.with(BlockNode.IS_CONNECTED, true));
				world.setBlockState(posEnd, stateEnd.with(BlockNode.IS_CONNECTED, true));
				PacketUpdateData.sendDirectlyToServerRail((ServerWorld) world, rail);
			} else if (player != null) {
				player.sendMessage(TranslationProvider.GUI_MTR_INVALID_ORIENTATION.getText(), true);
			}
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		PacketDeleteData.sendDirectlyToServerRailId((ServerWorld) world, TwoPositionsBase.getHexId(MTR.blockPosToPosition(posStart), MTR.blockPosToPosition(posEnd)));
	}

	@Nullable
	public Rail createRail(@Nullable UUID uuid, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd) {
		if (railType != null && uuid != null) {
			final boolean isValidContinuousMovement;
			final RailType newRailType;

			if (transportMode.continuousMovement) {
				final Block blockStart = stateStart.getBlock();
				final Block blockEnd = stateEnd.getBlock();

				if (blockStart instanceof BlockNode.BlockContinuousMovementNode && blockEnd instanceof BlockNode.BlockContinuousMovementNode && ((BlockNode.BlockContinuousMovementNode) blockStart).isStation && ((BlockNode.BlockContinuousMovementNode) blockEnd).isStation) {
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

			final Position positionStart = MTR.blockPosToPosition(posStart);
			final Position positionEnd = MTR.blockPosToPosition(posEnd);
			final Rail rail = switch (newRailType) {
				case PLATFORM -> Rail.newPlatformRail(
						positionStart, facingStart,
						positionEnd, facingEnd,
						Rail.Shape.QUADRATIC, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						new ObjectArrayList<>(), transportMode
				);
				case SIDING -> Rail.newSidingRail(
						positionStart, facingStart,
						positionEnd, facingEnd,
						Rail.Shape.QUADRATIC, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						new ObjectArrayList<>(), transportMode
				);
				case TURN_BACK -> Rail.newTurnBackRail(
						positionStart, facingStart,
						positionEnd, facingEnd,
						Rail.Shape.QUADRATIC, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						new ObjectArrayList<>(), transportMode
				);
				default -> Rail.newRail(
						positionStart, facingStart,
						positionEnd, facingEnd,
						newRailType.railShape, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						new ObjectArrayList<>(), isOneWay ? 0 : newRailType.speedLimit, newRailType.speedLimit,
						false, false, newRailType.canAccelerate, newRailType == RailType.RUNWAY, newRailType.hasSignal, transportMode
				);
			};

			if (rail.isValid() && isValidContinuousMovement) {
				return PacketUpdateLastRailStyles.SERVER_CACHE.getRailWithLastStyles(uuid, rail);
			}
		}

		return null;
	}
}
