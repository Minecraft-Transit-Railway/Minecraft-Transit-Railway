package org.mtr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.core.data.SignalModification;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.packet.PacketUpdateData;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final int color;

	public static final int[] COLORS = {
		MapColor.SNOW.col,
		MapColor.COLOR_ORANGE.col,
		MapColor.COLOR_MAGENTA.col,
		MapColor.COLOR_LIGHT_BLUE.col,
		MapColor.COLOR_YELLOW.col,
		MapColor.COLOR_LIGHT_GREEN.col,
		MapColor.COLOR_PINK.col,
		MapColor.COLOR_GRAY.col,
		MapColor.COLOR_LIGHT_GRAY.col,
		MapColor.COLOR_CYAN.col,
		MapColor.COLOR_PURPLE.col,
		MapColor.COLOR_BLUE.col,
		MapColor.COLOR_BROWN.col,
		MapColor.COLOR_GREEN.col,
		MapColor.COLOR_RED.col,
		MapColor.COLOR_BLACK.col,
	};

	public ItemSignalModifier(boolean isConnector, int color, Item.Properties settings) {
		super(true, false, true, isConnector, settings);
		this.color = color;
	}

	@Override
	protected void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayer serverPlayerEntity) {
		final SignalModification signalModification = new SignalModification(MTR.blockPosToPosition(posStart), MTR.blockPosToPosition(posEnd), false);
		signalModification.putColorToAdd(color);
		getRail(world, posStart, posEnd, serverPlayerEntity, rail -> PacketUpdateData.sendDirectlyToServerSignalModification((ServerLevel) world, signalModification));
	}

	@Override
	protected void onRemove(Level world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayer player) {
		final SignalModification signalModification = new SignalModification(MTR.blockPosToPosition(posStart), MTR.blockPosToPosition(posEnd), false);
		signalModification.putColorToRemove(color);
		PacketUpdateData.sendDirectlyToServerSignalModification((ServerLevel) world, signalModification);
	}
}
