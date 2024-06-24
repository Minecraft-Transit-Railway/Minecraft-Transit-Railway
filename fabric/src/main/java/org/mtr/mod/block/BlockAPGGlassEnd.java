package org.mtr.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;

public class BlockAPGGlassEnd extends BlockPSDAPGGlassEndBase {

	@Nonnull
	@Override
	public Item asItem2() {
		return Items.APG_GLASS_END.get();
	}
}
