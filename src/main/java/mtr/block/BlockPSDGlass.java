package mtr.block;

import mtr.Items;
import net.minecraft.item.Item;

public class BlockPSDGlass extends BlockPSDAPGGlassBase {

	public final int style;

	public BlockPSDGlass(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS : Items.PSD_GLASS_2;
	}
}
