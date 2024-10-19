package org.mtr.mod.item;

import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockNode;
import org.mtr.mod.generated.lang.TranslationProvider;

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
		if (radius == 0) {
			radius = 1;
		}
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
					playerEntity.sendMessage(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getText(TextHelper.translatable(neighborState.getBlock().getTranslationKey()).getString()), true);
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
			tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getMutableText(height).formatted(TextFormatting.GRAY));
		}
		tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getMutableText(width).formatted(TextFormatting.GRAY));

		if (canSaveBlock) {
			final BlockState state = getSavedState(stack);
			final String[] textSplit = (state.isAir() ? TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_SELECT_MATERIAL : TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_CLEAR).getString(InitClient.getShiftText(), TextHelper.translatable(org.mtr.mod.Blocks.RAIL_NODE.get().getTranslationKey()).data).split("\\|");
			for (String text : textSplit) {
				tooltip.add(TextHelper.literal(text).formatted(TextFormatting.GRAY).formatted(TextFormatting.ITALIC));
			}
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getMutableText(TextHelper.translatable(state.getBlock().getTranslationKey()).getString()).formatted(TextFormatting.GREEN));
		}

		super.addTooltips(stack, world, tooltip, options);
	}

	@Override
	protected final void onConnect(World world, ItemStack itemStack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayerEntity serverPlayerEntity) {
		if (serverPlayerEntity != null) {
			getRail(world, posStart, posEnd, serverPlayerEntity, rail -> onConnect(rail, serverPlayerEntity, itemStack, radius, height));
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

	protected abstract void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height);
}
