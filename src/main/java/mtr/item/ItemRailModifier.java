package mtr.item;

import mtr.block.BlockRail;
import mtr.data.Rail;
import mtr.data.RailAngle;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

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
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (isConnector && railType != null && railType.canAccelerate) {
			tooltip.add(new TranslatableText("tooltip.mtr.rail_speed_limit", railType.speedLimit).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	protected void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, PlayerEntity player, RailwayData railwayData) {
		// TODO hasSavedRail always false
		if (railType.hasSavedRail && (railwayData.hasSavedRail(posStart) || railwayData.hasSavedRail(posEnd))) {
			if (player != null) {
				player.sendMessage(new TranslatableText("gui.mtr.platform_or_siding_exists"), true);
			}
		} else {
			final Rail rail2 = new Rail(posEnd, facingEnd, posStart, facingStart, railType);
			if (rail2.isValid()) {
				final Rail rail1 = new Rail(posStart, facingStart, posEnd, facingEnd, isOneWay ? RailType.NONE : railType);
				railwayData.addRail(posStart, posEnd, rail1, false);
				final long newId = railwayData.addRail(posEnd, posStart, rail2, true);
				world.setBlockState(posStart, stateStart.with(BlockRail.IS_CONNECTED, true));
				world.setBlockState(posEnd, stateEnd.with(BlockRail.IS_CONNECTED, true));
				PacketTrainDataGuiServer.createRailS2C(world, posStart, posEnd, rail1, rail2, newId);
			} else {
				if (player != null) {
					player.sendMessage(new TranslatableText("gui.mtr.invalid_orientation"), true);
				}
			}
		}
	}

	@Override
	protected void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData) {
		railwayData.removeRailConnection(posStart, posEnd);
		PacketTrainDataGuiServer.removeRailConnectionS2C(world, posStart, posEnd);
	}
}
