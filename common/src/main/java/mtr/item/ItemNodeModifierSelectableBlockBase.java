package mtr.item;

import mtr.block.BlockNode;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class ItemNodeModifierSelectableBlockBase extends ItemNodeModifierBase {

	private final boolean canSaveBlock;
	private final int height;
	private final int width;
	private final int radius;

	private static final String TAG_BLOCK_ID = "block_id";

	public ItemNodeModifierSelectableBlockBase(boolean canSaveBlock, int height, int width) {
		super(true, false, false, true);
		this.canSaveBlock = canSaveBlock;
		this.height = height;
		this.width = width;
		radius = width / 2;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (canSaveBlock) {
			final Level world = context.getLevel();
			if (!world.isClientSide) {
				final Player player = context.getPlayer();
				if (player != null && player.isShiftKeyDown()) {
					final BlockState state = world.getBlockState(context.getClickedPos());
					final BlockState newState;
					if (state.getBlock() instanceof BlockNode) {
						newState = Blocks.AIR.defaultBlockState();
					} else {
						newState = state;
					}
					player.displayClientMessage(Text.translatable("tooltip.mtr.selected_material", Text.translatable(newState.getBlock().getDescriptionId())), true);
					final CompoundTag compoundTag = context.getItemInHand().getOrCreateTag();
					compoundTag.putInt(TAG_BLOCK_ID, Block.getId(newState));
					return InteractionResult.SUCCESS;
				}
			}
		}

		return super.useOn(context);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		if (height > 0) {
			tooltip.add(Text.translatable("tooltip.mtr.rail_action_height", height).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		}
		tooltip.add(Text.translatable("tooltip.mtr.rail_action_width", width).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

		if (canSaveBlock) {
			final BlockState state = getSavedState(stack);
			final String[] textSplit = Text.translatable(state.isAir() ? "tooltip.mtr.shift_right_click_to_select_material" : "tooltip.mtr.shift_right_click_to_clear", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage(), Text.translatable(mtr.Blocks.RAIL_NODE.get().getDescriptionId())).getString().split("\\|");
			for (String text : textSplit) {
				tooltip.add(Text.literal(text).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).applyFormat(ChatFormatting.ITALIC)));
			}
			tooltip.add(Text.translatable("tooltip.mtr.selected_material", Text.translatable(state.getBlock().getDescriptionId())).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
		}

		super.appendHoverText(stack, level, tooltip, tooltipFlag);
	}

	@Override
	protected final void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData) {
		if (player != null && !onConnect(player, stack, railwayData, posStart, posEnd, radius, height)) {
			player.displayClientMessage(Text.translatable("gui.mtr.rail_not_found_action"), true);
		}
	}

	@Override
	protected final void onRemove(Level world, BlockPos posStart, BlockPos posEnd, Player player, RailwayData railwayData) {
	}

	protected BlockState getSavedState(ItemStack stack) {
		final CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_BLOCK_ID)) {
			return Block.stateById(tag.getInt(TAG_BLOCK_ID));
		} else {
			return Blocks.AIR.defaultBlockState();
		}
	}

	protected abstract boolean onConnect(Player player, ItemStack itemStack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height);
}
