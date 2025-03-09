package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockPSDGlassEnd extends BlockPSDAPGGlassEndBase {

	private final int style;

	public BlockPSDGlassEnd(AbstractBlock.Settings settings, int style) {
		super(settings);
		this.style = style;
	}

	@Nonnull
	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS_END_1.createAndGet() : Items.PSD_GLASS_END_2.createAndGet();
	}
}
