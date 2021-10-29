package mtr.item;

import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final DyeColor color;

	public ItemSignalModifier(boolean isConnector, DyeColor color) {
		super(isConnector);
		this.color = color;
	}

	@Override
	protected void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Direction facingStart, Direction facingEnd, PlayerEntity player, RailwayData railwayData) {
		railwayData.addSignal(color, posStart, posEnd);
		PacketTrainDataGuiServer.createSignalS2C(world, color, posStart, posEnd);
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData) {
		railwayData.removeSignal(color, posStart, posEnd);
		PacketTrainDataGuiServer.removeSignalS2C(world, color, posStart, posEnd);
	}
}
