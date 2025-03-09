package org.mtr.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.core.data.SignalModification;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.packet.PacketUpdateData;

import javax.annotation.Nullable;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final int color;

	public static final int[] COLORS = {
			MapColor.WHITE.color,
			MapColor.ORANGE.color,
			MapColor.MAGENTA.color,
			MapColor.LIGHT_BLUE.color,
			MapColor.YELLOW.color,
			MapColor.LIME.color,
			MapColor.PINK.color,
			MapColor.GRAY.color,
			MapColor.LIGHT_GRAY.color,
			MapColor.CYAN.color,
			MapColor.PURPLE.color,
			MapColor.BLUE.color,
			MapColor.BROWN.color,
			MapColor.GREEN.color,
			MapColor.RED.color,
			MapColor.BLACK.color,
	};

	public ItemSignalModifier(boolean isConnector, int color, Item.Settings settings) {
		super(true, false, true, isConnector, settings);
		this.color = color;
	}

	@Override
	protected void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity serverPlayerEntity) {
		final SignalModification signalModification = new SignalModification(MTR.blockPosToPosition(posStart), MTR.blockPosToPosition(posEnd), false);
		signalModification.putColorToAdd(color);
		getRail(world, posStart, posEnd, serverPlayerEntity, rail -> PacketUpdateData.sendDirectlyToServerSignalModification((ServerWorld) world, signalModification));
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity player) {
		final SignalModification signalModification = new SignalModification(MTR.blockPosToPosition(posStart), MTR.blockPosToPosition(posEnd), false);
		signalModification.putColorToRemove(color);
		PacketUpdateData.sendDirectlyToServerSignalModification((ServerWorld) world, signalModification);
	}
}
