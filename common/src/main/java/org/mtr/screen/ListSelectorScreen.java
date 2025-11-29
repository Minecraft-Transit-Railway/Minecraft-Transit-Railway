package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.util.Identifier;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.awt.*;
import java.util.Collections;
import java.util.function.Consumer;

public abstract class ListSelectorScreen<T extends NameColorDataBase> extends WindowBase {

	private final ObjectArrayList<T> availableData = new ObjectArrayList<>();
	private final ObjectArrayList<T> selectedData = new ObjectArrayList<>();
	private final boolean canSelectMultiple;
	private final boolean canSelectDuplicate;
	private final boolean canManuallySortSelectedList;
	private final Consumer<ObjectArrayList<T>> onClose;

	private final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of());
	private final ListComponent<T> availableListComponent = createMainComponents();
	private final ListComponent<T> selectedListComponent = createMainComponents();

	public ListSelectorScreen(boolean canSelectMultiple, boolean canSelectDuplicate, boolean canManuallySortSelectedList, Consumer<ObjectArrayList<T>> onClose, WindowBase previousScreen) {
		super(previousScreen);
		this.canSelectMultiple = canSelectMultiple;
		this.canSelectDuplicate = canSelectDuplicate;
		this.canManuallySortSelectedList = canManuallySortSelectedList;
		this.onClose = onClose;
	}

	@Override
	public void onScreenClose() {
		super.onScreenClose();
		onClose.accept(selectedData);
	}

	public void setAvailableList(ObjectCollection<T> availableList) {
		availableData.clear();
		availableData.addAll(availableList);
		updateAvailableData();
	}

	public void selectData(T data) {
		if (canSelectDuplicate || !selectedData.contains(data)) {
			if (!canSelectMultiple) {
				availableData.addAll(selectedData);
				selectedData.clear();
			}
			selectedData.add(data);
			if (!canSelectDuplicate) {
				availableData.remove(data);
			}

			updateAvailableData();
			updateSelectedData();
		}
	}

	protected abstract void setData(ListComponent<T> listComponent, ObjectCollection<T> dataList, ObjectArrayList<ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<T>>> actions);

	private ListComponent<T> createMainComponents() {
		final UIContainer container = (UIContainer) new UIContainer()
				.setChildOf(backgroundComponent)
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new ScaleConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)), 0.5F))
				.setHeight(new RelativeConstraint());

		final TextInputComponent textInputComponent = (TextInputComponent) new TextInputComponent()
				.setChildOf(container)
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

		textInputComponent.setPlaceholderText(TranslationProvider.GUI_MTR_SEARCH.getString());

		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
				.setChildOf(container)
				.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint())
				.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

		slotBackgroundComponent.setBackgroundColor(Color.BLACK);

		final ScrollPanelComponent scrollPanelComponent = (ScrollPanelComponent) new ScrollPanelComponent(false)
				.setChildOf(slotBackgroundComponent)
				.setX(new CenterConstraint())
				.setY(new CenterConstraint())
				.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(2)))
				.setHeight(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(2)));

		scrollPanelComponent.setScrollbarColor(Color.WHITE);

		final ListComponent<T> listComponent = new ListComponent<>();
		listComponent.setChildOf(scrollPanelComponent.contentContainer).setWidth(new RelativeConstraint()).setHeight(new RelativeConstraint());
		textInputComponent.onChange(() -> listComponent.setFilter(textInputComponent.getText()));
		return listComponent;
	}

	private void updateAvailableData() {
		Collections.sort(availableData);
		setData(availableListComponent, availableData, ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.ADD_TEXTURE_ID, (index, data) -> selectData(data))));
	}

	private void updateSelectedData() {
		if (!canManuallySortSelectedList) {
			Collections.sort(selectedData);
		}

		final ObjectObjectImmutablePair<Identifier, ListItem.ActionConsumer<T>> deleteAction = new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (index, data) -> {
			selectedData.remove(data);
			if (!availableData.contains(data)) {
				availableData.add(data);
			}

			updateAvailableData();
			updateSelectedData();
		});

		setData(selectedListComponent, selectedData, canManuallySortSelectedList ? ObjectArrayList.of(
				ListComponent.createUpButton(selectedData, null),
				ListComponent.createDownButton(selectedData, null),
				deleteAction
		) : ObjectArrayList.of(deleteAction));
	}
}
