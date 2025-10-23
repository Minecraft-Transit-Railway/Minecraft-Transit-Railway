package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockPSDGlass extends BlockPSDAPGGlassBase {

	private final int style;

	public BlockPSDGlass(AbstractBlock.Settings settings, int style) {
		super(settings);
		this.style = style;
	}

	@Nonnull
	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS_1.get() : Items.PSD_GLASS_2.get();
	}
}
