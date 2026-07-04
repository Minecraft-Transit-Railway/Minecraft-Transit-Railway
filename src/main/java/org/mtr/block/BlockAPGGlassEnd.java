package org.mtr.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.mtr.registry.Items;

public class BlockAPGGlassEnd extends BlockPSDAPGGlassEndBase {

	public BlockAPGGlassEnd(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public Item asItem() {
		return Items.APG_GLASS_END.get();
	}
}
