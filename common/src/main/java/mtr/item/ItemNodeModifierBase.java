package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockRailNode;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class ItemNodeModifierBase extends Item {

	protected final boolean isConnector;

	public static final String TAG_POS = "pos";

	public ItemNodeModifierBase(boolean isConnector) {
		super(new Item.Properties().tab(ItemGroups.CORE).stacksTo(1));
		this.isConnector = isConnector;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level world = context.getLevel();
		if (!world.isClientSide) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			final BlockPos posStart = context.getClickedPos();
			final BlockState stateStart = world.getBlockState(posStart);

			if (railwayData != null && stateStart.getBlock() instanceof BlockRailNode) {
				final CompoundTag compoundTag = context.getItemInHand().getOrCreateTag();

				if (compoundTag.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.of(compoundTag.getLong(TAG_POS));
					final BlockState stateEnd = world.getBlockState(posEnd);

					if (stateEnd.getBlock() instanceof BlockRailNode) {
						final Player player = context.getPlayer();

						if (isConnector) {
							if (!posStart.equals(posEnd)) {
								final float angle1 = BlockRailNode.getAngle(stateStart);
								final float angle2 = BlockRailNode.getAngle(stateEnd);

								final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
								final RailAngle railAngleStart = RailAngle.fromAngle(angle1 + (RailAngle.similarFacing(angleDifference, angle1) ? 0 : 180));
								final RailAngle railAngleEnd = RailAngle.fromAngle(angle2 + (RailAngle.similarFacing(angleDifference, angle2) ? 180 : 0));

								onConnect(world, context.getItemInHand(), stateStart, stateEnd, posStart, posEnd, railAngleStart, railAngleEnd, player, railwayData);
							}
						} else {
							onRemove(world, posStart, posEnd, railwayData);
						}
					}

					compoundTag.remove(TAG_POS);
				} else {
					compoundTag.putLong(TAG_POS, posStart.asLong());
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		} else {
			return super.useOn(context);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		final CompoundTag compoundTag = stack.getOrCreateTag();
		final long posLong = compoundTag.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(new TranslatableComponent("tooltip.mtr.selected_block", BlockPos.of(posLong).toShortString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
		}
	}

	protected abstract void onConnect(Level world, ItemStack stack, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData);

	protected abstract void onRemove(Level world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData);
}
