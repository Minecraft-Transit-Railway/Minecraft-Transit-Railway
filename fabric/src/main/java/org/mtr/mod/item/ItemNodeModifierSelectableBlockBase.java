package org.mtr.mod.item;

import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemNodeModifierSelectableBlockBase extends ItemNodeModifierBase {

	private final boolean canSaveBlock;
	private final int height;
	private final int width;
	private final int radius;

	private static final String TAG_BLOCK_ID = "block_id";

	public ItemNodeModifierSelectableBlockBase(boolean canSaveBlock, int height, int width, ItemSettings itemSettings) {
		super(true, false, false, true, itemSettings);
		this.canSaveBlock = canSaveBlock;
		this.height = height;
		this.width = width;
		radius = width / 2;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		if (canSaveBlock) {
			final World world = context.getWorld();
			if (!world.isClient()) {
				final PlayerEntity playerEntity = context.getPlayer();
				if (playerEntity != null && playerEntity.isSneaking()) {
					final BlockState state = world.getBlockState(context.getBlockPos());
					final BlockState neighborState;
					if (state.getBlock().data instanceof BlockNode) {
						neighborState = Blocks.getAirMapped().getDefaultState();
					} else {
						neighborState = state;
					}
					playerEntity.sendMessage(new Text(TextHelper.translatable("tooltip.mtr.selected_material", TextHelper.translatable(neighborState.getBlock().getTranslationKey())).data), true);
					final CompoundTag compoundTag = context.getStack().getOrCreateTag();
					compoundTag.putInt(TAG_BLOCK_ID, Block.getRawIdFromState(neighborState));
					return ActionResult.SUCCESS;
				}
			}
		}

		return super.useOnBlock2(context);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		if (height > 0) {
			tooltip.add(TextHelper.translatable("tooltip.mtr.rail_action_height", height).formatted(TextFormatting.GRAY));
		}
		tooltip.add(TextHelper.translatable("tooltip.mtr.rail_action_width", width).formatted(TextFormatting.GRAY));

		if (canSaveBlock) {
			final BlockState state = getSavedState(stack);
			final String[] textSplit = TextHelper.translatable(state.isAir() ? "tooltip.mtr.shift_right_click_to_select_material" : "tooltip.mtr.shift_right_click_to_clear", InitClient.getShiftText(), TextHelper.translatable(org.mtr.mod.Blocks.RAIL_NODE.get().getTranslationKey()).data).getString().split("\\|");
			for (String text : textSplit) {
				tooltip.add(TextHelper.literal(text).formatted(TextFormatting.GRAY).formatted(TextFormatting.ITALIC));
			}
			tooltip.add(TextHelper.translatable("tooltip.mtr.selected_material", TextHelper.translatable(state.getBlock().getTranslationKey()).data).formatted(TextFormatting.GREEN));
		}

		super.addTooltips(stack, world, tooltip, options);
	}

	@Override
	protected final void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity != null && !onConnect(serverPlayerEntity, stack, posStart, posEnd, radius, height)) {
			serverPlayerEntity.sendMessage(new Text(TextHelper.translatable("gui.mtr.rail_not_found_action").data), true);
		}
	}

	@Override
	protected final void onRemove(World world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayerEntity serverPlayerEntity) {
	}

	protected BlockState getSavedState(ItemStack stack) {
		final CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_BLOCK_ID)) {
			return Block.getStateFromRawId(tag.getInt(TAG_BLOCK_ID));
		} else {
			return Blocks.getAirMapped().getDefaultState();
		}
	}

	protected abstract boolean onConnect(ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, BlockPos posStart, BlockPos posEnd, int radius, int height);
}
