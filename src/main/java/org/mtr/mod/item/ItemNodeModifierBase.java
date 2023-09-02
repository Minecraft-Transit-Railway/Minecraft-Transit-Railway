package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.core.tools.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockNode;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemNodeModifierBase extends ItemBlockClickingBase {

	public final boolean forNonContinuousMovementNode;
	public final boolean forContinuousMovementNode;
	public final boolean forAirplaneNode;
	protected final boolean isConnector;

	public static final String TAG_POS = "pos";
	private static final String TAG_TRANSPORT_MODE = "transport_mode";

	public ItemNodeModifierBase(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isConnector, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.forNonContinuousMovementNode = forNonContinuousMovementNode;
		this.forContinuousMovementNode = forContinuousMovementNode;
		this.forAirplaneNode = forAirplaneNode;
		this.isConnector = isConnector;
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		final CompoundTag compoundTag = stack.getOrCreateTag();
		final long posLong = compoundTag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(TextHelper.translatable("tooltip.mtr.selected_block", BlockPos.fromLong(posLong).toShortString()).formatted(TextFormatting.GOLD));
		}
	}

	@Override
	protected void onStartClick(ItemUsageContext context, CompoundTag compoundTag) {
		compoundTag.putString(TAG_TRANSPORT_MODE, ((BlockNode) context.getWorld().getBlockState(context.getBlockPos()).getBlock().data).transportMode.toString());
	}

	@Override
	protected void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag) {
		final World world = context.getWorld();
		final BlockPos posStart = context.getBlockPos();
		final BlockState stateStart = world.getBlockState(posStart);
		final Block blockStart = stateStart.getBlock();
		final BlockState stateEnd = world.getBlockState(posEnd);
		final PlayerEntity player = context.getPlayer();

		if (ServerPlayerEntity.isInstance(player) && stateEnd.getBlock().data instanceof BlockNode && ((BlockNode) blockStart.data).transportMode.toString().equals(compoundTag.getString(TAG_TRANSPORT_MODE))) {
			if (isConnector) {
				if (!posStart.equals(posEnd)) {
					final float angle1 = BlockNode.getAngle(stateStart);
					final float angle2 = BlockNode.getAngle(stateEnd);

					final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
					final Angle angleStart = Angle.fromAngle(angle1 + (Angle.similarFacing(angleDifference, angle1) ? 0 : 180));
					final Angle angleEnd = Angle.fromAngle(angle2 + (Angle.similarFacing(angleDifference, angle2) ? 180 : 0));

					onConnect(world, context.getStack(), ((BlockNode) blockStart.data).transportMode, stateStart, stateEnd, posStart, posEnd, angleStart, angleEnd, ServerPlayerEntity.cast(player));
				}
			} else {
				onRemove(world, posStart, posEnd, ServerPlayerEntity.cast(player));
			}
		}

		compoundTag.remove(TAG_TRANSPORT_MODE);
	}

	@Override
	protected boolean clickCondition(ItemUsageContext context) {
		final World world = context.getWorld();
		final Block blockStart = world.getBlockState(context.getBlockPos()).getBlock();
		if (blockStart.data instanceof BlockNode) {
			final BlockNode blockNode = (BlockNode) blockStart.data;
			if (blockNode.transportMode == TransportMode.AIRPLANE) {
				return forAirplaneNode;
			} else {
				return blockNode.transportMode.continuousMovement ? forContinuousMovementNode : forNonContinuousMovementNode;
			}
		} else {
			return false;
		}
	}

	protected abstract void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player);

	protected abstract void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player);
}
