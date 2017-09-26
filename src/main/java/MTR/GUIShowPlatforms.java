package MTR;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

public class GUIShowPlatforms extends GuiScreen {
	private GuiButton buttonDone, buttonPrev, buttonNext;
	private GuiButton[] buttons = new GuiButton[1000];
	private int[] x, y, z, station, number, tePlatforms;
	private int maxPlatforms, page, flag = 0;
	private TileEntityNextTrainEntity te1;
	private TileEntityPIDS1Entity te2;

	public GUIShowPlatforms(PlatformData data) {
		x = data.platformX;
		y = data.platformY;
		z = data.platformZ;
		station = data.platformAlias;
		number = data.platformNumber;
		page = 1;
	}

	public GUIShowPlatforms(PlatformData data, TileEntityNextTrainEntity t) {
		this(data);
		te1 = t;
		tePlatforms = t.platformID;
		flag = 1;
	}

	public GUIShowPlatforms(PlatformData data, TileEntityPIDS1Entity t) {
		this(data);
		te2 = t;
		tePlatforms = new int[1];
		tePlatforms[0] = t.platform;
		flag = 2;
	}

	@Override
	public void initGui() {
		buttonDone = new GuiButton(1, width / 2 - 75, height - 20, 150, 20, I18n.format("gui.done", new Object[0]));
		buttonPrev = new GuiButton(2, 0, height - 20, width / 2 - 75, 20, I18n.format("gui.prevpage", new Object[0]));
		buttonNext = new GuiButton(3, width / 2 + 75, height - 20, width / 2 - 75, 20,
				I18n.format("gui.nextpage", new Object[0]));
		for (int i = 0; i < 1000; i++)
			buttons[i] = new GuiButton(i + 4, width - 70, 0, 70, 20, "");
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float ticks) {
		buttonList.clear();
		try {
			drawDefaultBackground();
			maxPlatforms = MathHelper.floor_float((height - 40) / 20F);
			ArrayList<Integer> platforms = new ArrayList<>();
			for (int i = 0; i < 1000; i++)
				if (number[i] != 0)
					platforms.add(i);
			int pages = (int) Math.ceil(platforms.size() / (float) maxPlatforms);
			if (page > pages)
				page = pages;
			int j = 0;
			for (int i = (page - 1) * maxPlatforms; i < Math.min(platforms.size(), page * maxPlatforms); i++) {
				String coords = x[platforms.get(i)] + ", " + y[platforms.get(i)] + ", " + z[platforms.get(i)];
				String alias = I18n.format("station." + station[platforms.get(i)] + "c", new Object[0]) + " "
						+ I18n.format("station." + station[platforms.get(i)], new Object[0]);
				String platform = I18n.format("gui.platform", new Object[0]) + " " + number[platforms.get(i)];
				int yPos = 20 * j + 25;
				drawString(fontRendererObj, String.valueOf(i + 1) + ".", 5, yPos, 0xFFFFFF);
				drawString(fontRendererObj, coords, 30, yPos, 0xFFFFFF);
				drawString(fontRendererObj, alias, 125, yPos, 0xFFFFFF);
				drawString(fontRendererObj, platform, width - 130, yPos, 0xFFFFFF);
				String buttonText = I18n.format("gui.remove", new Object[0]);
				if (flag >= 1) {
					buttonText = I18n.format("gui.select", new Object[0]);
					for (int k = 0; k < tePlatforms.length; k++)
						if (tePlatforms[k] == platforms.get(i) + 1)
							buttonText = I18n.format("gui.deselect", new Object[0]);
				}
				buttons[platforms.get(i)].yPosition = yPos - 5;
				buttons[platforms.get(i)].displayString = buttonText;
				buttonList.add(buttons[platforms.get(i)]);
				j++;
			}
			drawCenteredString(fontRendererObj,
					I18n.format("gui.platformlist", new Object[0]) + " (" + page + "/" + pages + ")", width / 2, 5,
					0xFFFFFF);
			buttonPrev.enabled = page > 1;
			buttonNext.enabled = page < pages;
			buttonList.add(buttonPrev);
			buttonList.add(buttonNext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		buttonList.add(buttonDone);
		super.drawScreen(mouseX, mouseY, ticks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		int id = button.id;
		switch (id) {
		case 1:
			mc.displayGuiScreen((GuiScreen) null);
			break;
		case 2:
			page--;
			if (page < 1)
				page = 1;
			break;
		case 3:
			page++;
			break;
		default:
			int platformID = id - 4;
			if (flag == 0) {
				MTR.network.sendToServer(new MessagePlatformMaker(x[platformID], y[platformID], z[platformID], 0, 0));
				x[platformID] = 0;
				y[platformID] = 0;
				z[platformID] = 0;
				station[platformID] = 0;
				number[platformID] = 0;
			}
			if (flag == 1) {
				boolean remove = buttons[platformID].displayString.contains(I18n.format("gui.deselect", new Object[0]));
				platformID++;
				tePlatforms = MessageNextTrain.change(tePlatforms, platformID, remove);
				MTR.network.sendToServer(new MessageNextTrain(te1.getPos().getX(), te1.getPos().getY(),
						te1.getPos().getZ(), remove ? -1 : platformID));
			}
			if (flag == 2) {
				boolean remove = buttons[platformID].displayString.contains(I18n.format("gui.deselect", new Object[0]));
				platformID++;
				tePlatforms[0] = platformID;
				MTR.network.sendToServer(new MessageNextTrain(te2.getPos().getX(), te2.getPos().getY(),
						te2.getPos().getZ(), remove ? -1 : platformID));
			}
			break;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}