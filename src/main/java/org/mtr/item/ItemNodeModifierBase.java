package org.mtr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
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
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.registry.DataComponentTypes;

import java.util.function.Consumer;

public abstract class ItemNodeModifierBase extends ItemBlockClickingBase {

	public final boolean forNonContinuousMovementNode;
	public final boolean forContinuousMovementNode;
	public final boolean forAirplaneNode;
	protected final boolean isConnector;

	public ItemNodeModifierBase(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isConnector, Item.Properties settings) {
		super(settings);
		this.forNonContinuousMovementNode = forNonContinuousMovementNode;
		this.forContinuousMovementNode = forContinuousMovementNode;
		this.forAirplaneNode = forAirplaneNode;
		this.isConnector = isConnector;
	}

	@Override
	protected void onStartClick(UseOnContext context) {
		context.getItemInHand().set(DataComponentTypes.TRANSPORT_MODE.get(), ((BlockNode) context.getLevel().getBlockState(context.getClickedPos()).getBlock()).transportMode.toString());
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd) {
		final Level world = context.getLevel();
		final BlockPos posStart = context.getClickedPos();
		final BlockState stateStart = world.getBlockState(posStart);
		final Block blockStart = stateStart.getBlock();
		final BlockState stateEnd = world.getBlockState(posEnd);
		final Player player = context.getPlayer();

		if (player instanceof ServerPlayer && stateEnd.getBlock() instanceof BlockNode && ((BlockNode) blockStart).transportMode.toString().equals(context.getItemInHand().get(DataComponentTypes.TRANSPORT_MODE.get()))) {
			if (isConnector) {
				if (!posStart.equals(posEnd)) {
					final ObjectObjectImmutablePair<Angle, Angle> angles = Rail.getAngles(MTR.blockPosToPosition(posStart), BlockNode.getAngle(stateStart), MTR.blockPosToPosition(posEnd), BlockNode.getAngle(stateEnd));
					onConnect(world, context.getItemInHand(), ((BlockNode) blockStart).transportMode, stateStart, stateEnd, posStart, posEnd, angles.left(), angles.right(), (ServerPlayer) player);
				}
			} else {
				onRemove(world, posStart, posEnd, (ServerPlayer) player);
			}
		}

		context.getItemInHand().remove(DataComponentTypes.TRANSPORT_MODE.get());
	}

	@Override
	protected boolean clickCondition(UseOnContext context) {
		final Level world = context.getLevel();
		final Block blockStart = world.getBlockState(context.getClickedPos()).getBlock();
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

	protected abstract void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayer player);

	protected abstract void onRemove(Level world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayer player);

	public static void getRail(Level world, BlockPos blockPos1, BlockPos blockPos2, @Nullable ServerPlayer serverPlayerEntity, Consumer<Rail> consumer) {
		MTR.sendMessageC2S(
			OperationProcessor.RAILS,
			world.getServer(),
			world,
			new RailsRequest().addRailId(TwoPositionsBase.getHexId(MTR.blockPosToPosition(blockPos1), MTR.blockPosToPosition(blockPos2))),
			railsResponse -> {
				final ObjectImmutableList<Rail> rails = railsResponse.getRails();
				if (rails.isEmpty()) {
					if (serverPlayerEntity != null) {
						serverPlayerEntity.displayClientMessage(TranslationProvider.GUI_MTR_RAIL_NOT_FOUND_ACTION.getText(), true);
					}
				} else {
					consumer.accept(rails.getFirst());
				}
			},
			RailsResponse.class
		);
	}

	public static TransportMode getTransportMode(ItemStack itemStack) {
		final String itemString = itemStack.get(DataComponentTypes.TRANSPORT_MODE.get());
		return EnumHelper.valueOf(TransportMode.TRAIN, itemString == null ? "" : itemString);
	}
}
