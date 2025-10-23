package org.mtr.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTRClient;
import org.mtr.block.BlockNode;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.DataComponentTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemNodeModifierSelectableBlockBase extends ItemNodeModifierBase {

	private final boolean canSaveBlock;
	private final int height;
	private final int width;
	private final int radius;

	private static final String TAG_BLOCK_ID = "block_id";

	public ItemNodeModifierSelectableBlockBase(boolean canSaveBlock, int height, int width, Item.Settings settings) {
		super(true, false, false, true, settings);
		this.canSaveBlock = canSaveBlock;
		this.height = height;
		this.width = width;
		radius = width / 2;
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (canSaveBlock) {
			final World world = context.getWorld();
			if (!world.isClient()) {
				final PlayerEntity playerEntity = context.getPlayer();
				if (playerEntity != null && playerEntity.isSneaking()) {
					final BlockState state = world.getBlockState(context.getBlockPos());
					final BlockState neighborState;
					if (state.getBlock() instanceof BlockNode) {
						neighborState = Blocks.AIR.getDefaultState();
					} else {
						neighborState = state;
					}
					playerEntity.sendMessage(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getText(Text.translatable(neighborState.getBlock().getTranslationKey()).getString()), true);
					context.getStack().set(DataComponentTypes.BLOCK_ID.get(), Block.getRawIdFromState(neighborState));
					return ActionResult.SUCCESS;
				}
			}
		}

		return super.useOnBlock(context);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (height > 0) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getMutableText(height).formatted(Formatting.GRAY));
		}
		tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getMutableText(width).formatted(Formatting.GRAY));

		if (canSaveBlock) {
			final BlockState state = getSavedState(stack);
			final String[] textSplit = (state.isAir() ? TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_SELECT_MATERIAL : TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_CLEAR).getString(MTRClient.getShiftText(), Text.translatable(org.mtr.registry.Blocks.RAIL_NODE.get().getTranslationKey())).split("\\|");
			for (String text : textSplit) {
				tooltip.add(Text.literal(text).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
			}
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getMutableText(Text.translatable(state.getBlock().getTranslationKey()).getString()).formatted(Formatting.GREEN));
		}

		super.appendTooltip(stack, context, tooltip, type);
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
		final Integer blockId = stack.get(DataComponentTypes.BLOCK_ID.get());
		return blockId == null ? Blocks.AIR.getDefaultState() : Block.getStateFromRawId(blockId);
	}

	protected abstract void onConnect(Rail rail, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, int radius, int height);
}
