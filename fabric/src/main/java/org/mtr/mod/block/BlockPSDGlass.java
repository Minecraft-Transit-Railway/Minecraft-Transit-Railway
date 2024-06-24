package org.mtr.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;

public class BlockPSDGlass extends BlockPSDAPGGlassBase {

	private final int style;

	public BlockPSDGlass(int style) {
		super();
		this.style = style;
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return style == 0 ? Items.PSD_GLASS_1.get() : Items.PSD_GLASS_2.get();
	}
}
