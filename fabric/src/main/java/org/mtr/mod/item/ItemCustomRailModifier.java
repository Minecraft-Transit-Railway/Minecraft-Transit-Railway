package org.mtr.mod.item;

import org.jetbrains.annotations.Nullable;
import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.TwoPositionsBase;
import org.mtr.core.tool.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.data.RailType;
import org.mtr.mod.packet.PacketDeleteData;
import org.mtr.mod.packet.PacketOpenCustomRailScreen;
import org.mtr.mod.packet.PacketUpdateData;

import java.util.List;

public class ItemCustomRailModifier extends ItemNodeModifierBase {

    private final int speedLimit;
    private final boolean isOneWay;
    private final RailType railType;


    public ItemCustomRailModifier(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, RailType railType, ItemSettings itemSettings) {
        super(forNonContinuousMovementNode, forContinuousMovementNode, forAirplaneNode, true, itemSettings);
        this.isOneWay = false;
        this.railType = railType;
        this.speedLimit = railType.speedLimit;
    }

    @Override
    public void addTooltips(ItemStack stack, @javax.annotation.Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        if (isConnector && railType != null && railType.canAccelerate) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.rail_speed_limit", getSpeedLimit(stack)).formatted(TextFormatting.GRAY));
            if (getIsOneWay(stack)) {
                tooltip.add(TextHelper.translatable("tooltip.mtr.is_one_way").formatted(TextFormatting.GRAY));
            }
        }
        super.addTooltips(stack, world, tooltip, options);
    }

    @Override
    protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity player) {
        if (railType != null) {
            final Rail rail = createRail(transportMode, stateStart, stateEnd, posStart, posEnd, facingStart, facingEnd, stack);
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
    public Rail createRail(TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, ItemStack stack) {
        if (railType != null) {
            final Position positionStart = Init.blockPosToPosition(posStart);
            final Position positionEnd = Init.blockPosToPosition(posEnd);
            final Rail rail = Rail.newRail(positionStart, facingStart, positionEnd, facingEnd, railType.railShape, 0, "", getIsOneWay(stack) ? 0 : getSpeedLimit(stack), getSpeedLimit(stack), false, false, railType.canAccelerate, false, railType.hasSignal, transportMode);


            if (rail.isValid()) {
                return rail;
            }
        }

        return null;
    }

    @Override
    public void useWithoutResult(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !world.isClient()) {
            Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(user), new PacketOpenCustomRailScreen(getSpeedLimit(user.getMainHandStack()), getIsOneWay(user.getMainHandStack())));
        }
    }

    private int getSpeedLimit(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().getInt("railSpeed") > 0 && itemStack.getTag().getInt("railSpeed") < 301 ? itemStack.getTag().getInt("railSpeed") : speedLimit;
    }

    private boolean getIsOneWay(ItemStack itemStack) {
        return itemStack.getTag() != null ? itemStack.getTag().getBoolean("isOneWay") : isOneWay;
    }
}
