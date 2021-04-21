package mtr.block;

import mtr.Items;
import net.minecraft.item.Item;

public class BlockPSDGlassEnd extends BlockPSDAPGGlassEndBase {

	private final int style;

	public BlockPSDGlassEnd(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS_END_1 : Items.PSD_GLASS_END_2;
	}
}
