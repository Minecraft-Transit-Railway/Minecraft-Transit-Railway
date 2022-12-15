package mtr.item;

import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemSignalModifier extends ItemNodeModifierBase {

	private final DyeColor color;

	public ItemSignalModifier(boolean isConnector, DyeColor color) {
		super(true, false, true, isConnector);
		this.color = color;
	}

	@Override
	protected void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData) {
		if (railwayData.containsRail(posStart, posEnd)) {
			PacketTrainDataGuiServer.createSignalS2C(world, railwayData.addSignal(player, color, posStart, posEnd), color, PathData.getRailProduct(posStart, posEnd));
		} else if (player != null) {
			player.displayClientMessage(Text.translatable("gui.mtr.rail_not_found"), true);
		}
	}

	@Override
	protected void onRemove(Level world, BlockPos posStart, BlockPos posEnd, Player player, RailwayData railwayData) {
		PacketTrainDataGuiServer.removeSignalS2C(world, railwayData.removeSignal(player, color, posStart, posEnd), color, PathData.getRailProduct(posStart, posEnd));
	}
}
