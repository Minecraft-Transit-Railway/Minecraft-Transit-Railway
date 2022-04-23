package mtr.item;

import mtr.block.BlockNode;
import mtr.data.*;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ItemRailModifier extends ItemNodeModifierBase {

	private final boolean isOneWay;
	private final RailType railType;

	public ItemRailModifier() {
		super(false);
		isOneWay = false;
		railType = null;
	}

	public ItemRailModifier(boolean isOneWay, RailType railType) {
		super(true);
		this.isOneWay = isOneWay;
		this.railType = railType;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		if (isConnector && railType != null && railType.canAccelerate) {
			tooltip.add(new TranslatableComponent("tooltip.mtr.rail_speed_limit", railType.speedLimit).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
		super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
	}

	@Override
	protected void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData) {
		if (railType.hasSavedRail && (railwayData.hasSavedRail(posStart) || railwayData.hasSavedRail(posEnd))) {
			if (player != null) {
				player.displayClientMessage(new TranslatableComponent("gui.mtr.platform_or_siding_exists"), true);
			}
		} else {
			final Rail rail1 = new Rail(posStart, facingStart, posEnd, facingEnd, isOneWay ? RailType.NONE : railType, transportMode);
			final Rail rail2 = new Rail(posEnd, facingEnd, posStart, facingStart, railType, transportMode);

			final boolean goodRadius = rail1.goodRadius() && rail2.goodRadius();
			final boolean isValid = rail1.isValid() && rail2.isValid();

			if (goodRadius && isValid) {
				railwayData.addRail(transportMode, posStart, posEnd, rail1, false);
				final long newId = railwayData.addRail(transportMode, posEnd, posStart, rail2, true);
				world.setBlockAndUpdate(posStart, stateStart.setValue(BlockNode.IS_CONNECTED, true));
				world.setBlockAndUpdate(posEnd, stateEnd.setValue(BlockNode.IS_CONNECTED, true));
				PacketTrainDataGuiServer.createRailS2C(world, transportMode, posStart, posEnd, rail1, rail2, newId);
			} else if (player != null) {
				player.displayClientMessage(new TranslatableComponent(goodRadius ? "gui.mtr.invalid_orientation" : "gui.mtr.radius_too_small"), true);
			}
		}
	}

	@Override
	protected void onRemove(Level world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData) {
		railwayData.removeRailConnection(posStart, posEnd);
		PacketTrainDataGuiServer.removeRailConnectionS2C(world, posStart, posEnd);
	}
}
