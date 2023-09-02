package org.mtr.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;

public class BlockPSDGlassEnd extends BlockPSDAPGGlassEndBase {

	private final int style;

	public BlockPSDGlassEnd(int style) {
		super();
		this.style = style;
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return style == 0 ? Items.PSD_GLASS_END_1.get() : Items.PSD_GLASS_END_2.get();
	}
}
