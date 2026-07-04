package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.*;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.awt.*;
import java.util.Collections;
import java.util.function.Consumer;

public abstract class ListSelectorScreen<T extends U, U extends Comparable<U>> extends WindowBase {

	private final ObjectArrayList<T> availableData = new ObjectArrayList<>();
	protected final ObjectArrayList<T> selectedData = new ObjectArrayList<>();
	private final boolean canSelectMultiple;
	private final boolean canSelectDuplicate;
	private final boolean canManuallySortSelectedList;
	private final Consumer<ObjectArrayList<T>> onClose;

	private final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of());
	private final ListComponent<T> availableListComponent = createMainComponents(TranslationProvider.GUI_MTR_AVAILABLE);
	private final ListComponent<T> selectedListComponent = createMainComponents(TranslationProvider.GUI_MTR_SELECTED);

	public ListSelectorScreen(boolean canSelectMultiple, boolean canSelectDuplicate, boolean canManuallySortSelectedList, Consumer<ObjectArrayList<T>> onClose, @Nullable WindowBase previousScreen) {
		super(previousScreen);
		this.canSelectMultiple = canSelectMultiple;
		this.canSelectDuplicate = canSelectDuplicate;
		this.canManuallySortSelectedList = canManuallySortSelectedList;
		this.onClose = onClose;
	}

	/**
	 * Legacy constructor for opening list selectors from old {@link ScreenBase} screens.
	 */
	@Deprecated
	public ListSelectorScreen(boolean canSelectMultiple, boolean canSelectDuplicate, boolean canManuallySortSelectedList, Consumer<ObjectArrayList<T>> onClose, @Nullable ScreenBase previousScreenLegacy) {
		super(previousScreenLegacy);
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

	protected void onSelectionChanged() {
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
			onSelectionChanged();
		}
	}

	protected abstract void setData(ListComponent<T> listComponent, ObjectCollection<T> dataList, boolean isSelectedList, ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<T>>> actions);

	private ListComponent<T> createMainComponents(TranslationProvider.TranslationHolder title) {
		final UIContainer container = (UIContainer) new UIContainer()
			.setChildOf(backgroundComponent)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new ScaleConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)), 0.5F))
			.setHeight(new RelativeConstraint());

		new UIText(title.getString(), false)
			.setChildOf(container)
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final TextInputComponent textInputComponent = (TextInputComponent) new TextInputComponent()
			.setChildOf(container)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		textInputComponent.setPlaceholderText(TranslationProvider.GUI_MTR_SEARCH.getString());

		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(container)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)));

		final ListComponent<T> listComponent = GuiHelper.createListComponent(slotBackgroundComponent);
		textInputComponent.onChange(() -> listComponent.setFilter(textInputComponent.getText()));
		return listComponent;
	}

	private void updateAvailableData() {
		Collections.sort(availableData);
		setData(availableListComponent, availableData, false, ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.ADD_TEXTURE_ID, (indexList, data) -> selectData(data))));
	}

	private void updateSelectedData() {
		if (!canManuallySortSelectedList) {
			Collections.sort(selectedData);
		}

		final ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<T>> deleteAction = new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, data) -> {
			selectedData.remove(data);
			if (!availableData.contains(data)) {
				availableData.add(data);
			}

			updateAvailableData();
			updateSelectedData();
			onSelectionChanged();
		});

		setData(selectedListComponent, selectedData, true, canManuallySortSelectedList ? ObjectArrayList.of(
			ListComponent.createUpButton(selectedData, this::updateSelectedData),
			ListComponent.createDownButton(selectedData, this::updateSelectedData),
			deleteAction
		) : ObjectArrayList.of(deleteAction));
	}
}
