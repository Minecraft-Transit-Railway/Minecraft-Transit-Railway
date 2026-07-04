package org.mtr.screen;

import net.minecraft.resources.ResourceLocation;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.resource.ObjectResource;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class EyeCandySelectorScreen extends ListSelectorScreen<ObjectResource, ObjectResource> {

	public EyeCandySelectorScreen(Consumer<ObjectArrayList<ObjectResource>> onClose, WindowBase previousScreen) {
		super(false, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<ObjectResource> listComponent, ObjectCollection<ObjectResource> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<ObjectResource>>> actions) {
		ListComponent.setGeneric(listComponent, dataList, ObjectResource::getName, ObjectResource::getColor, actions);
	}
}
