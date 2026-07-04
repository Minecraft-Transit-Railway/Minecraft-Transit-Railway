package org.mtr.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public abstract class BlockStationNameBase extends Block implements EntityBlock {

	public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 2);

	protected BlockStationNameBase(BlockBehaviour.Properties blockSettings) {
		super(blockSettings.noOcclusion());
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_STATION_COLOR_NAME.getMutableText().withStyle(ChatFormatting.GRAY));
	}

	public abstract static class BlockEntityBase extends BlockEntity implements IGui {

		public final float yOffset;
		public final float zOffset;
		public final boolean isDoubleSided;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float yOffset, float zOffset, boolean isDoubleSided) {
			super(type, pos, state);
			this.yOffset = yOffset;
			this.zOffset = zOffset;
			this.isDoubleSided = isDoubleSided;
		}

		public abstract int getColor(BlockState state);
	}
}
