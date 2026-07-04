package org.mtr.screen;

import net.minecraft.resources.ResourceLocation;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Platform;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class PlatformListSelectorScreen extends ListSelectorScreen<Platform, NameColorDataBase> {

	public PlatformListSelectorScreen(Consumer<ObjectArrayList<Platform>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	public PlatformListSelectorScreen(Consumer<ObjectArrayList<Platform>> onClose) {
		super(false, false, false, onClose, (WindowBase) null);
	}

	@Override
	protected void setData(ListComponent<Platform> listComponent, ObjectCollection<Platform> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Platform>>> actions) {
		ListComponent.setSavedRails(listComponent, dataList, actions);
	}
}
