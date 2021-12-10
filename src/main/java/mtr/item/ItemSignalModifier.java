package mtr.item;

import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
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
	protected void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, PlayerEntity player, RailwayData railwayData) {
		if (railwayData.containsRail(posStart, posEnd)) {
			PacketTrainDataGuiServer.createSignalS2C(world, railwayData.addSignal(color, posStart, posEnd), color, PathData.getRailProduct(posStart, posEnd));
		} else if (player != null) {
			player.sendMessage(new TranslatableText("gui.mtr.rail_not_found"), true);
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData) {
		PacketTrainDataGuiServer.removeSignalS2C(world, railwayData.removeSignal(color, posStart, posEnd), color, PathData.getRailProduct(posStart, posEnd));
	}
}
