package org.mtr.screen;

import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.universal.UMinecraft;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;
import org.mtr.block.BlockTrainSensorBase;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Route;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.packet.PacketUpdateTrainSensorConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.TextInputComponent;

import java.awt.*;
import java.util.stream.Collectors;

/**
 * Base Elementa screen for train sensor configuration screens.
 */
public abstract class TrainSensorScreenBase extends SingleTabBackgroundScreenBase implements IGui {

	private boolean stoppedOnly;
	private boolean movingOnly;

	protected final BlockPos blockPos;
	private final LongAVLTreeSet filterRouteIds;

	@Nullable
	private final CheckboxComponent stoppedOnlyCheckbox;
	@Nullable
	private final CheckboxComponent movingOnlyCheckbox;
	private final UIWrappedText filteredRoutesText;
	private final UIWrappedText filteredRoutesConditionText;
	private final UIWrappedText routeNamesText;

	protected final ObjectArrayList<TextInputComponent> textFields = new ObjectArrayList<>();

	public TrainSensorScreenBase(BlockPos blockPos, boolean hasSpeedCheckboxes) {
		super(""); // TODO pass title
		this.blockPos = blockPos;

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld == null) {
			filterRouteIds = new LongAVLTreeSet();
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity instanceof BlockTrainSensorBase.BlockEntityBase blockEntityBase) {
				filterRouteIds = blockEntityBase.getRouteIds();
				stoppedOnly = blockEntityBase.getStoppedOnly();
				movingOnly = blockEntityBase.getMovingOnly();
			} else {
				filterRouteIds = new LongAVLTreeSet();
			}
		}

		if (hasSpeedCheckboxes) {
			stoppedOnlyCheckbox = createCheckbox(TranslationProvider.GUI_MTR_STOPPED_ONLY.getString(), stoppedOnly, this::setChecked1);
			movingOnlyCheckbox = createCheckbox(TranslationProvider.GUI_MTR_MOVING_ONLY.getString(), movingOnly, this::setChecked2);
			setChecked(stoppedOnly, movingOnly);
		} else {
			stoppedOnlyCheckbox = null;
			movingOnlyCheckbox = null;
		}

		final ButtonComponent filterButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new PixelConstraint(80));
		filterButton.setText(Text.translatable("selectWorld.edit").getString());
		filterButton.onClick(() -> {
			final ObjectArrayList<DashboardListItem> routes = MinecraftClientData.getInstance().simplifiedRoutes.stream().map(simplifiedRoute -> new DashboardListItem(simplifiedRoute.getId(), simplifiedRoute.getName(), simplifiedRoute.getColor())).sorted().collect(Collectors.toCollection(ObjectArrayList::new));
			UMinecraft.setCurrentScreenObj(new DashboardListSelectorScreen(new ObjectImmutableList<>(routes), filterRouteIds, false, false, this));
		});

		filteredRoutesText = createLabel("", GuiHelper.WHITE_COLOR);
		filteredRoutesConditionText = createLabel("", GuiHelper.WHITE_COLOR);
		routeNamesText = createLabel("", GuiHelper.WHITE_COLOR);
	}

	@Override
	public void onTick() {
		super.onTick();
		filteredRoutesText.setText(TranslationProvider.GUI_MTR_FILTERED_ROUTES.getString(filterRouteIds.size()));
		filteredRoutesConditionText.setText((filterRouteIds.isEmpty() ? TranslationProvider.GUI_MTR_FILTERED_ROUTES_EMPTY : TranslationProvider.GUI_MTR_FILTERED_ROUTES_CONDITION).getString());

		final StringBuilder routeNames = new StringBuilder();
		for (final long routeId : filterRouteIds) {
			final Route route = MinecraftClientData.getInstance().routeIdMap.get(routeId);
			if (route != null) {
				if (!routeNames.isEmpty()) {
					routeNames.append("\n");
				}
				routeNames.append(IGui.formatStationName(route.getName()));
			}
		}
		routeNamesText.setText(routeNames.toString());
	}

	@Override
	public void onScreenClose() {
		sendUpdate(blockPos, filterRouteIds, stoppedOnly, movingOnly);
		super.onScreenClose();
	}

	protected final TextInputComponent addTextField(String label, @Nullable String filter, int maxLength, String initialText) {
		createLabel(label, GuiHelper.WHITE_COLOR);
		final TextInputComponent textInputComponent = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));
		if (filter != null) {
			textInputComponent.setFilter(filter);
		}
		textInputComponent.setMaxLength(maxLength);
		textInputComponent.setText(initialText);
		textFields.add(textInputComponent);
		return textInputComponent;
	}

	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		new PacketUpdateTrainSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly).send(MinecraftClient.getInstance().world);
	}

	private CheckboxComponent createCheckbox(String text, boolean checked, Runnable onClick) {
		final CheckboxComponent checkboxComponent = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		checkboxComponent.setText(text);
		checkboxComponent.setChecked(checked);
		checkboxComponent.onClick(onClick);
		return checkboxComponent;
	}

	private UIWrappedText createLabel(String text, int color) {
		return (UIWrappedText) new UIWrappedText(text, false)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING / 2F))
			.setWidth(new RelativeConstraint())
			.setColor(new Color(color));
	}

	private void setChecked(boolean newStoppedOnly, boolean newMovingOnly) {
		if (newMovingOnly && stoppedOnly) {
			stoppedOnly = false;
		} else {
			stoppedOnly = newStoppedOnly;
		}
		if (newStoppedOnly && movingOnly) {
			movingOnly = false;
		} else {
			movingOnly = newMovingOnly;
		}
		if (stoppedOnlyCheckbox != null && movingOnlyCheckbox != null) {
			stoppedOnlyCheckbox.setChecked(stoppedOnly);
			movingOnlyCheckbox.setChecked(movingOnly);
		}
	}

	private void setChecked1() {
		if (stoppedOnlyCheckbox != null && movingOnlyCheckbox != null) {
			setChecked(!stoppedOnlyCheckbox.isChecked(), movingOnlyCheckbox.isChecked());
		}
	}

	private void setChecked2() {
		if (stoppedOnlyCheckbox != null && movingOnlyCheckbox != null) {
			setChecked(stoppedOnlyCheckbox.isChecked(), !movingOnlyCheckbox.isChecked());
		}
	}
}
