package mtr.gui;

import mtr.block.BlockRailwaySign;
import mtr.packet.PacketTrainDataGuiClient;
import mtr.render.RenderRailwaySign;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class RailwaySignScreen extends Screen implements IGui {

	private int platformRouteIndex;
	private int editingIndex;

	private final BlockPos signPos;
	private final int length;
	private final BlockRailwaySign.SignType[] signTypes;

	private final ButtonWidget[] buttonsEdit;
	private final ButtonWidget buttonClear;
	private final ButtonWidget[] buttonsSelection;

	private static final int SIGN_SIZE = 40;
	private static final int ROW_START = 64;
	private static final int COLUMNS = 21;
	final BlockRailwaySign.SignType[] ALL_SIGN_TYPES = BlockRailwaySign.SignType.values();

	public RailwaySignScreen(BlockPos signPos) {
		super(new LiteralText(""));
		editingIndex = -1;
		this.signPos = signPos;
		final ClientWorld world = MinecraftClient.getInstance().world;

		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				signTypes = ((BlockRailwaySign.TileEntityRailwaySign) entity).getSign();
				platformRouteIndex = ((BlockRailwaySign.TileEntityRailwaySign) entity).getPlatformRouteIndex();
			} else {
				signTypes = new BlockRailwaySign.SignType[0];
			}
			if (world.getBlockState(signPos).getBlock() instanceof BlockRailwaySign) {
				length = ((BlockRailwaySign) world.getBlockState(signPos).getBlock()).length;
			} else {
				length = 0;
			}
		} else {
			length = 0;
			signTypes = new BlockRailwaySign.SignType[0];
		}

		buttonsEdit = new ButtonWidget[length];
		for (int i = 0; i < buttonsEdit.length; i++) {
			final int index = i;
			buttonsEdit[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("selectWorld.edit"), button -> edit(index));
		}

		buttonsSelection = new ButtonWidget[ALL_SIGN_TYPES.length];
		for (int i = 0; i < ALL_SIGN_TYPES.length; i++) {
			final int index = i;
			buttonsSelection[i] = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> setNewSignType(ALL_SIGN_TYPES[index]));
		}

		buttonClear = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.reset_sign"), button -> setNewSignType(null));
	}

	@Override
	protected void init() {
		for (int i = 0; i < buttonsEdit.length; i++) {
			IGui.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
			addButton(buttonsEdit[i]);
		}

		int column = 0;
		int row = 0;
		for (int i = 0; i < buttonsSelection.length; i++) {
			final int columns = ALL_SIGN_TYPES[i].hasCustomText ? 3 : 1;

			IGui.setPositionAndWidth(buttonsSelection[i], (width - SQUARE_SIZE * COLUMNS) / 2 + column * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE * columns);
			buttonsSelection[i].visible = false;
			addButton(buttonsSelection[i]);

			column += columns;
			if (column >= COLUMNS) {
				column = 0;
				row++;
			}
		}

		IGui.setPositionAndWidth(buttonClear, (width - SQUARE_SIZE * COLUMNS) / 2 + column * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE * (COLUMNS - column));
		buttonClear.visible = false;
		addButton(buttonClear);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			if (client == null) {
				return;
			}

			for (int i = 0; i < signTypes.length; i++) {
				if (signTypes[i] != null) {
					client.getTextureManager().bindTexture(signTypes[i].id);
					RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signTypes[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, i, signTypes.length - i - 1, platformRouteIndex, Direction.UP, (x, y, size, flipTexture) -> drawTexture(matrices, (int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));
				}
			}

			if (editingIndex >= 0) {
				int column = 0;
				int row = 0;
				for (final BlockRailwaySign.SignType signType : ALL_SIGN_TYPES) {
					final int columns = signType.hasCustomText ? 3 : 1;
					final boolean moveRight = signType.hasCustomText && signType.flipped;

					client.getTextureManager().bindTexture(signType.id);
					RenderRailwaySign.drawSign(matrices, null, textRenderer, signPos, signType, (width - SQUARE_SIZE * COLUMNS) / 2F + (column + (moveRight ? 2 : 0)) * SQUARE_SIZE, row * SQUARE_SIZE + ROW_START, SQUARE_SIZE, 2, 2, platformRouteIndex, Direction.UP, (x, y, size, flipTexture) -> drawTexture(matrices, (int) x, (int) y, 0, 0, (int) size, (int) size, (int) (flipTexture ? -size : size), (int) size));

					column += columns;
					if (column >= COLUMNS) {
						column = 0;
						row++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendSignTypesC2S(signPos, platformRouteIndex, signTypes);
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		for (ButtonWidget button : buttonsSelection) {
			button.visible = false;
		}
		editingIndex = -1;
	}

	private void edit(int editingIndex) {
		this.editingIndex = editingIndex;
		for (ButtonWidget button : buttonsEdit) {
			button.active = true;
		}
		for (ButtonWidget button : buttonsSelection) {
			button.visible = true;
		}
		buttonClear.visible = true;
		buttonsEdit[editingIndex].active = false;
	}

	private void setNewSignType(BlockRailwaySign.SignType newSignType) {
		if (editingIndex >= 0 && editingIndex < signTypes.length) {
			if (newSignType == signTypes[editingIndex] && (newSignType == BlockRailwaySign.SignType.PLATFORM || newSignType == BlockRailwaySign.SignType.PLATFORM_FLIPPED || newSignType == BlockRailwaySign.SignType.LINE || newSignType == BlockRailwaySign.SignType.LINE_FLIPPED)) {
				platformRouteIndex++;
			}

			if (newSignType != null && newSignType.hasCustomText) {
				if (newSignType.flipped) {
					for (int i = editingIndex - 1; i >= 0; i--) {
						signTypes[i] = null;
					}
				} else {
					for (int i = editingIndex + 1; i < signTypes.length; i++) {
						signTypes[i] = null;
					}
				}
			}

			for (int i = 0; i < signTypes.length; i++) {
				if (signTypes[i] != null && signTypes[i].hasCustomText && (i < editingIndex && !signTypes[i].flipped || i > editingIndex && signTypes[i].flipped)) {
					signTypes[i] = null;
				}
			}

			signTypes[editingIndex] = newSignType;
		}
	}
}
