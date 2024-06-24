package org.mtr.mod.item;

import org.mtr.mapping.holder.ActionResult;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemUsageContext;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.ItemExtension;

import javax.annotation.Nonnull;

public class ItemLiftRefresher extends ItemExtension implements DirectionHelper {

	public ItemLiftRefresher(ItemSettings itemSettings) {
		super(itemSettings.maxCount(1));
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		// TODO
		return super.useOnBlock2(context);
	}
}
