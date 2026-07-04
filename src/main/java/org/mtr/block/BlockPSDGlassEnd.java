package org.mtr.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.mtr.registry.Items;

public class BlockPSDGlassEnd extends BlockPSDAPGGlassEndBase {

	private final int style;

	public BlockPSDGlassEnd(BlockBehaviour.Properties settings, int style) {
		super(settings);
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS_END_1.get() : Items.PSD_GLASS_END_2.get();
	}
}
