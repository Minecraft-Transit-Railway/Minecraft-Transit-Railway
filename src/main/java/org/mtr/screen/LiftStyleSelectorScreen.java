package org.mtr.screen;

import net.minecraft.resources.ResourceLocation;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.resource.LiftResource;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class LiftStyleSelectorScreen extends ListSelectorScreen<LiftResource, LiftResource> {

	public LiftStyleSelectorScreen(Consumer<ObjectArrayList<LiftResource>> onClose, WindowBase previousScreen) {
		super(false, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<LiftResource> listComponent, ObjectCollection<LiftResource> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<LiftResource>>> actions) {
		ListComponent.setGeneric(listComponent, dataList, LiftResource::getName, LiftResource::getColor, actions);
	}
}
