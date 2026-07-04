package org.mtr.screen;

import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.mtr.block.BlockTrainSensorBase;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Route;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateTrainSensorConfig;
import org.mtr.tool.DataHelper;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.MultiLineTextWidget;

import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base Elementa screen for train sensor configuration screens.
 */
public abstract class TrainSensorScreenBase<T extends BlockTrainSensorBase.BlockEntityBase> extends SingleTabBackgroundScreenBase {

	private String oldRouteIdsKey = "";

	protected final BlockPos blockPos;
	private final LongAVLTreeSet filterRouteIds;

	@Nullable
	private final CheckboxComponent stoppedOnlyCheckbox;
	@Nullable
	private final CheckboxComponent movingOnlyCheckbox;
	private final UIWrappedText filteredRoutesText;
	private final UIWrappedText filteredRoutesConditionText;
	private final MultiLineTextWidget multiLineTextWidget;

	protected static final int LEFT_WIDTH = 96;

	public TrainSensorScreenBase(String title, BlockPos blockPos, T blockEntity, boolean hasSpeedCheckboxes) {
		super(title);
		this.blockPos = blockPos;
		filterRouteIds = blockEntity.getRouteIds();

		if (hasSpeedCheckboxes) {
			stoppedOnlyCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(contentContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			stoppedOnlyCheckbox.setText(TranslationProvider.GUI_MTR_STOPPED_ONLY.getString());
			stoppedOnlyCheckbox.onClick(() -> setStoppedOnly(stoppedOnlyCheckbox.isChecked()));

			movingOnlyCheckbox = (CheckboxComponent) new CheckboxComponent()
				.setChildOf(contentContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			movingOnlyCheckbox.setText(TranslationProvider.GUI_MTR_MOVING_ONLY.getString());
			movingOnlyCheckbox.onClick(() -> setMovingOnly(movingOnlyCheckbox.isChecked()));

			setStoppedOnly(blockEntity.getStoppedOnly());
			setMovingOnly(blockEntity.getMovingOnly());
		} else {
			stoppedOnlyCheckbox = null;
			movingOnlyCheckbox = null;
		}

		GuiHelper.createSpacing(contentContainer);
		filteredRoutesText = GuiHelper.createLabel(contentContainer, "");

		final ButtonComponent filterButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		filterButton.setText(Component.translatable("selectWorld.edit").getString());
		filterButton.onClick(() -> UMinecraft.setCurrentScreenObj(createRouteListSelectorScreen()));

		GuiHelper.createSpacing(contentContainer);

		filteredRoutesConditionText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		GuiHelper.createSpacing(contentContainer);

		multiLineTextWidget = (MultiLineTextWidget) new MultiLineTextWidget()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
	}

	@Override
	public void onTick() {
		super.onTick();
		filteredRoutesText.setText(TranslationProvider.GUI_MTR_FILTERED_ROUTES.getString(filterRouteIds.size()));
		filteredRoutesConditionText.setText((filterRouteIds.isEmpty() ? TranslationProvider.GUI_MTR_FILTERED_ROUTES_EMPTY : TranslationProvider.GUI_MTR_FILTERED_ROUTES_CONDITION).getString());

		final String routeIdsKey = getRouteIdsKey();
		if (!routeIdsKey.equals(oldRouteIdsKey)) {
			final ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<String, @Nullable Color>>> lines = new ObjectArrayList<>();
			filterRouteIds.longStream().mapToObj(MinecraftClientData.getDashboardInstance().routeIdMap::get).filter(Objects::nonNull).sorted().forEach(route -> {
				final String routeNumber = Utilities.formatName(route.getRouteNumber());
				lines.add(ObjectArrayList.of(
					new ObjectObjectImmutablePair<>("- ", new Color(route.getColor())),
					new ObjectObjectImmutablePair<>(DataHelper.getNameOrUntitled(DataHelper.formatNameOnly(route.getName()) + (routeNumber.isEmpty() ? "" : " " + routeNumber)), null)
				));
			});
			multiLineTextWidget.write(lines);
			oldRouteIdsKey = routeIdsKey;
		}
	}

	@Override
	public void onScreenClose() {
		sendUpdate(blockPos, filterRouteIds, stoppedOnlyCheckbox != null && stoppedOnlyCheckbox.isChecked(), movingOnlyCheckbox != null && movingOnlyCheckbox.isChecked());
		super.onScreenClose();
	}

	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		new PacketUpdateTrainSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly).send(Minecraft.getInstance().level);
	}

	private void setStoppedOnly(boolean checked) {
		if (stoppedOnlyCheckbox != null && movingOnlyCheckbox != null) {
			if (checked) {
				stoppedOnlyCheckbox.setChecked(true);
				movingOnlyCheckbox.setChecked(false);
			} else {
				stoppedOnlyCheckbox.setChecked(false);
			}
		}
	}

	private void setMovingOnly(boolean checked) {
		if (stoppedOnlyCheckbox != null && movingOnlyCheckbox != null) {
			if (checked) {
				stoppedOnlyCheckbox.setChecked(false);
				movingOnlyCheckbox.setChecked(true);
			} else {
				movingOnlyCheckbox.setChecked(false);
			}
		}
	}

	private RouteListSelectorScreen createRouteListSelectorScreen() {
		final RouteListSelectorScreen routeListSelectorScreen = new RouteListSelectorScreen(selectedRoutes -> {
			filterRouteIds.clear();
			selectedRoutes.forEach(route -> filterRouteIds.add(route.getId()));
		}, false, false, false, this);

		final ObjectArraySet<Route> routes = MinecraftClientData.getDashboardInstance().routes;
		routeListSelectorScreen.setAvailableList(routes);
		routes.forEach(route -> {
			if (filterRouteIds.contains(route.getId())) {
				routeListSelectorScreen.selectData(route);
			}
		});

		return routeListSelectorScreen;
	}

	private String getRouteIdsKey() {
		return filterRouteIds.longStream().mapToObj(String::valueOf).collect(Collectors.joining("_"));
	}
}
