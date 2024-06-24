package mtr.item;

import mtr.CreativeModeTabs;
import mtr.block.BlockNode;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class ItemNodeModifierBase extends ItemBlockClickingBase {

	public final boolean forNonContinuousMovementNode;
	public final boolean forContinuousMovementNode;
	public final boolean forAirplaneNode;
	protected final boolean isConnector;

	public static final String TAG_POS = "pos";
	private static final String TAG_TRANSPORT_MODE = "transport_mode";

	public ItemNodeModifierBase(boolean forNonContinuousMovementNode, boolean forContinuousMovementNode, boolean forAirplaneNode, boolean isConnector) {
		super(CreativeModeTabs.CORE, properties -> properties.stacksTo(1));
		this.forNonContinuousMovementNode = forNonContinuousMovementNode;
		this.forContinuousMovementNode = forContinuousMovementNode;
		this.forAirplaneNode = forAirplaneNode;
		this.isConnector = isConnector;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final CompoundTag compoundTag = stack.getOrCreateTag();
		final long posLong = compoundTag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(Text.translatable("tooltip.mtr.selected_block", BlockPos.of(posLong).toShortString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
		}
	}

	@Override
	protected void onStartClick(UseOnContext context, CompoundTag compoundTag) {
		compoundTag.putString(TAG_TRANSPORT_MODE, ((BlockNode) context.getLevel().getBlockState(context.getClickedPos()).getBlock()).transportMode.toString());
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd, CompoundTag compoundTag) {
		final Level world = context.getLevel();
		final RailwayData railwayData = RailwayData.getInstance(world);
		final BlockPos posStart = context.getClickedPos();
		final BlockState stateStart = world.getBlockState(posStart);
		final Block blockStart = stateStart.getBlock();
		final BlockState stateEnd = world.getBlockState(posEnd);

		if (railwayData != null && stateEnd.getBlock() instanceof BlockNode && ((BlockNode) blockStart).transportMode.toString().equals(compoundTag.getString(TAG_TRANSPORT_MODE))) {
			final Player player = context.getPlayer();

			if (isConnector) {
				if (!posStart.equals(posEnd)) {
					final float angle1 = BlockNode.getAngle(stateStart);
					final float angle2 = BlockNode.getAngle(stateEnd);

					final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
					final RailAngle railAngleStart = RailAngle.fromAngle(angle1 + (RailAngle.similarFacing(angleDifference, angle1) ? 0 : 180));
					final RailAngle railAngleEnd = RailAngle.fromAngle(angle2 + (RailAngle.similarFacing(angleDifference, angle2) ? 180 : 0));

					onConnect(world, context.getItemInHand(), ((BlockNode) blockStart).transportMode, stateStart, stateEnd, posStart, posEnd, railAngleStart, railAngleEnd, player, railwayData);
				}
			} else {
				onRemove(world, posStart, posEnd, player, railwayData);
			}
		}

		compoundTag.remove(TAG_TRANSPORT_MODE);
	}

	@Override
	protected boolean clickCondition(UseOnContext context) {
		final Level world = context.getLevel();
		final Block blockStart = world.getBlockState(context.getClickedPos()).getBlock();
		if (blockStart instanceof BlockNode) {
			final BlockNode blockNode = (BlockNode) blockStart;
			if (blockNode.transportMode == TransportMode.AIRPLANE) {
				return forAirplaneNode;
			} else {
				return blockNode.transportMode.continuousMovement ? forContinuousMovementNode : forNonContinuousMovementNode;
			}
		} else {
			return false;
		}
	}

	protected abstract void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData);

	protected abstract void onRemove(Level world, BlockPos posStart, BlockPos posEnd, Player player, RailwayData railwayData);
}
