package org.mtr.screen;

import net.minecraft.resources.ResourceLocation;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.resource.RailResource;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class RailStyleSelectorScreen extends ListSelectorScreen<RailResource, RailResource> {

	public RailStyleSelectorScreen(Consumer<ObjectArrayList<RailResource>> onClose) {
		super(true, false, false, onClose, (WindowBase) null);
	}

	@Override
	protected void setData(ListComponent<RailResource> listComponent, ObjectCollection<RailResource> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<RailResource>>> actions) {
		ListComponent.setGeneric(listComponent, dataList, RailResource::getName, RailResource::getColor, actions);
	}
}
