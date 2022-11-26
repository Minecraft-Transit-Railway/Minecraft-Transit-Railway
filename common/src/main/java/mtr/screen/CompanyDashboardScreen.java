package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;

import java.util.ArrayList;
import java.util.List;

public class CompanyDashboardScreen extends ScreenMapper implements IGui, IPacket {

	private SelectedTab selectedTab;
	private SelectedSubTab selectedSubTab;

	private final Button buttonTabStations;
	private final Button buttonTabRoutes;
	private final Button buttonTabDepots;

	private final Button buttonSubTabStations;
	private final Button buttonSubTabStationsUnclaimed;
	private final Button buttonSubTabRoutes;
	private final Button buttonSubTabRoutesUnclaimed;
	private final Button buttonSubTabDepots;
	private final Button buttonSubTabDepotsUnclaimed;

	private final Button buttonAddCompany;
	private final Button buttonEditCompany;
	private final Button buttonDoneEditingCompany;
	private final Button buttonRailActions;
	private final Button buttonOptions;

	private final CompanyDashboardList dashboardList;
	private final WidgetBetterTextField textFieldName;
	private final WidgetColorSelector colorSelector;

	private static final int COLOR_WIDTH = 48;
	private Company editingCompany;
	private boolean isNew;

	public CompanyDashboardScreen(boolean useTimeAndWindSync) {
		super(Text.literal(""));

		textFieldName = new WidgetBetterTextField(Text.translatable("gui.mtr.name").getString());
		colorSelector = new WidgetColorSelector(this, this::changeColours);

		buttonTabStations = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.stations"), button -> onSelectTab(SelectedTab.STATIONS));
		buttonTabRoutes = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.routes"), button -> onSelectTab(SelectedTab.ROUTES));
		buttonTabDepots = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.depots"), button -> onSelectTab(SelectedTab.DEPOTS));

		buttonSubTabStations = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.claimed"), button -> onSelectSubTab(SelectedSubTab.STATIONS_CLAIMED));
		buttonSubTabStationsUnclaimed = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.unclaimed"), button -> onSelectSubTab(SelectedSubTab.STATIONS_UNCLAIMED));
		buttonSubTabRoutes = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.claimed"), button -> onSelectSubTab(SelectedSubTab.ROUTES_CLAIMED));
		buttonSubTabRoutesUnclaimed = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.unclaimed"), button -> onSelectSubTab(SelectedSubTab.ROUTES_UNCLAIMED));
		buttonSubTabDepots = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.claimed"), button -> onSelectSubTab(SelectedSubTab.DEPOTS_CLAIMED));
		buttonSubTabDepotsUnclaimed = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.unclaimed"), button -> onSelectSubTab(SelectedSubTab.DEPOTS_UNCLAIMED));

		buttonAddCompany = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.add_company"), button -> startEditingCompany(new Company(), true));
		buttonEditCompany = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.edit_company"), button -> startEditingCompany(new Company(), false));
		buttonDoneEditingCompany = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> onDoneEditingCompany());

		buttonRailActions = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.rail_actions_button"), button -> {
			if (minecraft != null) {
				UtilitiesClient.setScreen(minecraft, new RailActionsScreen());
			}
		});
		buttonOptions = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("menu.options"), button -> {
			if (minecraft != null) {
				UtilitiesClient.setScreen(minecraft, new ConfigScreen(useTimeAndWindSync));
			}
		});

		dashboardList = new CompanyDashboardList();

		onSelectTab(SelectedTab.STATIONS);
	}

	private void changeColours() {
		this.editingCompany.color = colorSelector.getColor();
	}

	@Override
	protected void init() {
		super.init();

		final int tabCount = 3;
		final int subTabCount = 2;
		final int rightX = ((width - PANEL_WIDTH) + (width - PANEL_WIDTH)) / 2;
		final int bottomRowY = height - SQUARE_SIZE;

		IDrawing.setPositionAndWidth(buttonTabStations, 0, 0, PANEL_WIDTH / tabCount);
		IDrawing.setPositionAndWidth(buttonTabDepots, PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);
		IDrawing.setPositionAndWidth(buttonTabRoutes, 2 * PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);

		IDrawing.setPositionAndWidth(buttonSubTabStations, 0, SQUARE_SIZE, PANEL_WIDTH / subTabCount);
		IDrawing.setPositionAndWidth(buttonSubTabStationsUnclaimed, PANEL_WIDTH / subTabCount, SQUARE_SIZE, PANEL_WIDTH / subTabCount);
		IDrawing.setPositionAndWidth(buttonSubTabRoutes, 0, SQUARE_SIZE, PANEL_WIDTH / subTabCount);
		IDrawing.setPositionAndWidth(buttonSubTabRoutesUnclaimed, PANEL_WIDTH / subTabCount, SQUARE_SIZE, PANEL_WIDTH / subTabCount);
		IDrawing.setPositionAndWidth(buttonSubTabDepots, 0, SQUARE_SIZE, PANEL_WIDTH / subTabCount);
		IDrawing.setPositionAndWidth(buttonSubTabDepotsUnclaimed, PANEL_WIDTH / subTabCount, SQUARE_SIZE, PANEL_WIDTH / subTabCount);

		IDrawing.setPositionAndWidth(buttonRailActions, rightX, bottomRowY, PANEL_WIDTH / 2);
		IDrawing.setPositionAndWidth(buttonOptions, rightX + (PANEL_WIDTH / 2), bottomRowY, PANEL_WIDTH / 2);

		IDrawing.setPositionAndWidth(buttonAddCompany, rightX, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonEditCompany, rightX, 0, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonDoneEditingCompany, rightX, SQUARE_SIZE, PANEL_WIDTH);

		IDrawing.setPositionAndWidth(textFieldName, rightX+(TEXT_FIELD_PADDING / 2), 0, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, rightX+(TEXT_FIELD_PADDING / 2)+(PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING), 0, COLOR_WIDTH - TEXT_FIELD_PADDING);

		dashboardList.x = 0;
		dashboardList.y = SQUARE_SIZE * 2;
		dashboardList.width = PANEL_WIDTH;
		dashboardList.height = height - SQUARE_SIZE * 2;

		buttonDoneEditingCompany.visible = false;
		buttonEditCompany.visible = false;

		textFieldName.setVisible(false);
		colorSelector.visible = false;

		dashboardList.init(this::addDrawableChild);

		addDrawableChild(buttonTabStations);
		addDrawableChild(buttonTabRoutes);
		addDrawableChild(buttonTabDepots);
		addDrawableChild(buttonRailActions);
		addDrawableChild(buttonOptions);

		addDrawableChild(buttonSubTabStations);
		addDrawableChild(buttonSubTabStationsUnclaimed);
		addDrawableChild(buttonSubTabRoutes);
		addDrawableChild(buttonSubTabRoutesUnclaimed);
		addDrawableChild(buttonSubTabDepots);
		addDrawableChild(buttonSubTabDepotsUnclaimed);

		addDrawableChild(buttonAddCompany);
		addDrawableChild(buttonEditCompany);
		addDrawableChild(buttonDoneEditingCompany);

		addDrawableChild(textFieldName);
		addDrawableChild(colorSelector);

		if (editingCompany != null) {
			buttonDoneEditingCompany.visible = true;
			textFieldName.visible = true;
			colorSelector.visible = true;
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			matrices.pushPose();
			matrices.translate(0, 0, 500);
			Gui.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			Gui.fill(matrices, width-PANEL_WIDTH, 0, width, height, ARGB_BACKGROUND);
			dashboardList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			matrices.popPose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		textFieldName.tick();
		dashboardList.tick();

		try {
			switch (selectedTab) {
				case STATIONS:
					dashboardList.setData(ClientData.STATIONS, selectedSubTab);

					buttonSubTabRoutes.visible = false;
					buttonSubTabRoutesUnclaimed.visible = false;
					buttonSubTabDepots.visible = false;
					buttonSubTabDepotsUnclaimed.visible = false;

					buttonSubTabStations.visible = true;
					buttonSubTabStationsUnclaimed.visible = true;
					break;
				case ROUTES:
					dashboardList.setData(ClientData.ROUTES, selectedSubTab);

					buttonSubTabStations.visible = false;
					buttonSubTabStationsUnclaimed.visible = false;
					buttonSubTabDepots.visible = false;
					buttonSubTabDepotsUnclaimed.visible = false;

					buttonSubTabRoutes.visible = true;
					buttonSubTabRoutesUnclaimed.visible = true;
					break;
				case DEPOTS:
					dashboardList.setData(ClientData.DEPOTS, selectedSubTab);

					buttonSubTabStations.visible = false;
					buttonSubTabStationsUnclaimed.visible = false;
					buttonSubTabRoutes.visible = false;
					buttonSubTabRoutesUnclaimed.visible = false;

					buttonSubTabDepots.visible = true;
					buttonSubTabDepotsUnclaimed.visible = true;
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onSelectTab(SelectedTab tab) {
		selectedTab = tab;
		buttonTabStations.active = tab != SelectedTab.STATIONS;
		buttonTabRoutes.active = tab != SelectedTab.ROUTES;
		buttonTabDepots.active = tab != SelectedTab.DEPOTS;

		buttonSubTabStations.active = false;
		buttonSubTabRoutes.active = false;
		buttonSubTabDepots.active = false;

		if (tab == SelectedTab.STATIONS) {
			onSelectSubTab(SelectedSubTab.STATIONS_CLAIMED);
			buttonSubTabStations.active = false;
		}
		if (tab == SelectedTab.ROUTES) {
			onSelectSubTab(SelectedSubTab.ROUTES_CLAIMED);
			buttonSubTabRoutes.active = false;
		}
		if (tab == SelectedTab.DEPOTS) {
			onSelectSubTab(SelectedSubTab.DEPOTS_CLAIMED);
			buttonSubTabDepots.active = false;
		}
	}

	private void onSelectSubTab(SelectedSubTab tab) {
		selectedSubTab = tab;
		buttonSubTabStations.active = tab != SelectedSubTab.STATIONS_CLAIMED;
		buttonSubTabRoutes.active = tab != SelectedSubTab.ROUTES_CLAIMED;
		buttonSubTabDepots.active = tab != SelectedSubTab.DEPOTS_CLAIMED;
		buttonSubTabStationsUnclaimed.active = tab != SelectedSubTab.STATIONS_UNCLAIMED;
		buttonSubTabRoutesUnclaimed.active = tab != SelectedSubTab.ROUTES_UNCLAIMED;
		buttonSubTabDepotsUnclaimed.active = tab != SelectedSubTab.DEPOTS_UNCLAIMED;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		dashboardList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		dashboardList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
	public enum SelectedSubTab {STATIONS_CLAIMED, STATIONS_UNCLAIMED, ROUTES_CLAIMED, ROUTES_UNCLAIMED, DEPOTS_CLAIMED, DEPOTS_UNCLAIMED}

	private void startEditingCompany(Company editingCompany, boolean isNew) {
		this.editingCompany = editingCompany;
		this.isNew = isNew;
		List<String> owners = new ArrayList<>();
		if (minecraft != null && minecraft.player != null) {
			owners.add(minecraft.player.getUUID().toString());
		}
		editingCompany.employees.put("owners", owners);
		buttonAddCompany.visible = false;
		buttonDoneEditingCompany.visible = true;
		textFieldName.visible = true;
		colorSelector.visible = true;

		textFieldName.setValue(editingCompany.name);
		colorSelector.setColor(editingCompany.color);
	}

	private void onDoneEditingCompany() {
		if (isNew) {
			try {
				ClientData.COMPANIES.add(editingCompany);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (Company company: ClientData.COMPANIES) {
				if (minecraft != null && minecraft.player != null) {
					if (company.employees.get("owner").contains(minecraft.player.getUUID().toString())) {
						editingCompany = company;
						break;
					}
				}
			}
		}
		editingCompany.name = IGui.textOrUntitled(textFieldName.getValue());
		editingCompany.color = colorSelector.getColor();
		editingCompany.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_COMPANY, packet));
		editingCompany = null;

		buttonAddCompany.visible = false;
		buttonEditCompany.visible = true;
		buttonDoneEditingCompany.visible = false;
		textFieldName.visible = false;
		colorSelector.visible = false;
	}
}
