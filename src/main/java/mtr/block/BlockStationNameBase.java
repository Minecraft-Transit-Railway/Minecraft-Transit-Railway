package mtr.block;

import mapper.BlockEntityMapper;
import mapper.BlockEntityProviderMapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public abstract class BlockStationNameBase extends HorizontalFacingBlock implements BlockEntityProviderMapper {

	public static final IntProperty COLOR = IntProperty.of("color", 0, 2);

	protected BlockStationNameBase(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		tooltip.add(new TranslatableText("tooltip.mtr.station_color_name").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}

	public abstract static class TileEntityStationNameBase extends BlockEntityMapper {

		public final float yOffset;
		public final float zOffset;

		public TileEntityStationNameBase(BlockEntityType<?> type, BlockPos pos, BlockState state, float yOffset, float zOffset) {
			super(type, pos, state);
			this.yOffset = yOffset;
			this.zOffset = zOffset;
		}

		public abstract boolean shouldRender();
	}
}
