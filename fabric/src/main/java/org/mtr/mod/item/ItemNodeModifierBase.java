package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.operation.RailsRequest;
import org.mtr.core.operation.RailsResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.EnumHelper;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class ItemNodeModifierBase extends ItemBlockClickingBase {

	public final boolean forNonContinuousMovementNode;
	public final boolean forContinuousMovementNode;
	public final boolean forAirplaneNode;
	protected final boolean isConnector;

	private static final String TAG_TRANSPORT_MODE = "transport_mode";

	public ItemNodeModifierBase(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isConnector, ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
		this.forNonContinuousMovementNode = forNonContinuousMovementNode;
		this.forContinuousMovementNode = forContinuousMovementNode;
		this.forAirplaneNode = forAirplaneNode;
		this.isConnector = isConnector;
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
					final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(Init.blockPosToPosition(posStart), BlockNode.getAngle(stateStart), Init.blockPosToPosition(posEnd), BlockNode.getAngle(stateEnd));
					onConnect(world, context.getStack(), ((BlockNode) blockStart.data).transportMode, stateStart, stateEnd, posStart, posEnd, angles.left(), angles.right(), ServerPlayerEntity.cast(player));
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

	public static void getRail(World world, BlockPos blockPos1, BlockPos blockPos2, @Nullable ServerPlayerEntity serverPlayerEntity, Consumer<Rail> consumer) {
		Init.sendMessageC2S(
				OperationProcessor.RAILS,
				world.getServer(),
				world,
				new RailsRequest().addRailId(TwoPositionsBase.getHexId(Init.blockPosToPosition(blockPos1), Init.blockPosToPosition(blockPos2))),
				railsResponse -> {
					final ObjectImmutableList<Rail> rails = railsResponse.getRails();
					if (rails.isEmpty()) {
						if (serverPlayerEntity != null) {
							serverPlayerEntity.sendMessage(TranslationProvider.GUI_MTR_RAIL_NOT_FOUND_ACTION.getText(), true);
						}
					} else {
						consumer.accept(rails.get(0));
					}
				},
				RailsResponse.class
		);
	}

	public static TransportMode getTransportMode(CompoundTag compoundTag) {
		return EnumHelper.valueOf(TransportMode.TRAIN, compoundTag.getString(TAG_TRANSPORT_MODE));
	}
}
