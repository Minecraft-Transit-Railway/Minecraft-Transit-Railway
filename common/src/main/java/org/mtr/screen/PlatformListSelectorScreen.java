package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Platform;
import org.mtr.widget.ListComponent;
import org.mtr.widget.ListItem;

import java.util.function.Consumer;

public final class PlatformListSelectorScreen extends ListSelectorScreen<Platform, NameColorDataBase> {

	public PlatformListSelectorScreen(Consumer<ObjectArrayList<Platform>> onClose, WindowBase previousScreen) {
		super(true, false, false, onClose, previousScreen);
	}

	@Override
	protected void setData(ListComponent<Platform> listComponent, ObjectCollection<Platform> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<Platform>>> actions) {
		ListComponent.setSavedRails(listComponent, dataList, actions);
	}
}
