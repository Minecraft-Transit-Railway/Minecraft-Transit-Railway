package org.mtr.item;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.block.BlockNode;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.operation.RailsRequest;
import org.mtr.core.operation.RailsResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.EnumHelper;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class ItemNodeModifierBase extends ItemBlockClickingBase {

	public final boolean forNonContinuousMovementNode;
	public final boolean forContinuousMovementNode;
	public final boolean forAirplaneNode;
	protected final boolean isConnector;

	private static final String TAG_TRANSPORT_MODE = "transport_mode";

	public ItemNodeModifierBase(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isConnector, Item.Settings settings) {
		super(settings);
		this.forNonContinuousMovementNode = forNonContinuousMovementNode;
		this.forContinuousMovementNode = forContinuousMovementNode;
		this.forAirplaneNode = forAirplaneNode;
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(ItemUsageContext context, NbtCompound nbtCompound) {
		nbtCompound.putString(TAG_TRANSPORT_MODE, ((BlockNode) context.getWorld().getBlockState(context.getBlockPos()).getBlock()).transportMode.toString());
	}

	@Override
	protected void onEndClick(ItemUsageContext context, BlockPos posEnd, NbtCompound nbtCompound) {
		final World world = context.getWorld();
		final BlockPos posStart = context.getBlockPos();
		final BlockState stateStart = world.getBlockState(posStart);
		final Block blockStart = stateStart.getBlock();
		final BlockState stateEnd = world.getBlockState(posEnd);
		final PlayerEntity player = context.getPlayer();

		if (player instanceof ServerPlayerEntity && stateEnd.getBlock() instanceof BlockNode && ((BlockNode) blockStart).transportMode.toString().equals(nbtCompound.getString(TAG_TRANSPORT_MODE))) {
			if (isConnector) {
				if (!posStart.equals(posEnd)) {
					final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(MTR.blockPosToPosition(posStart), BlockNode.getAngle(stateStart), MTR.blockPosToPosition(posEnd), BlockNode.getAngle(stateEnd));
					onConnect(world, context.getStack(), ((BlockNode) blockStart).transportMode, stateStart, stateEnd, posStart, posEnd, angles.left(), angles.right(), (ServerPlayerEntity) player);
				}
			} else {
				onRemove(world, posStart, posEnd, (ServerPlayerEntity) player);
			}
		}

		nbtCompound.remove(TAG_TRANSPORT_MODE);
	}

	@Override
	protected boolean clickCondition(ItemUsageContext context) {
		final World world = context.getWorld();
		final Block blockStart = world.getBlockState(context.getBlockPos()).getBlock();
		if (blockStart instanceof BlockNode blockNode) {
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
		MTR.sendMessageC2S(
				OperationProcessor.RAILS,
				world.getServer(),
				world,
				new RailsRequest().addRailId(TwoPositionsBase.getHexId(MTR.blockPosToPosition(blockPos1), MTR.blockPosToPosition(blockPos2))),
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

	public static TransportMode getTransportMode(NbtCompound nbtCompound) {
		return EnumHelper.valueOf(TransportMode.TRAIN, nbtCompound.getString(TAG_TRANSPORT_MODE));
	}
}
