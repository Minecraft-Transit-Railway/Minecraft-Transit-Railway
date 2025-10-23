package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public class BlockAPGGlassEnd extends BlockPSDAPGGlassEndBase {

	public BlockAPGGlassEnd(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.APG_GLASS_END.get();
	}
}
