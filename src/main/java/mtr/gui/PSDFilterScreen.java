package mtr.gui;

import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.data.NameColorDataBase;
import mtr.data.Route;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public abstract class PSDFilterScreen extends ScreenMapper implements IGui, IPacket {

	protected final BlockPos pos;

	private final Set<Long> filterRouteIds;
	private final ButtonWidget filterButton;
	private final int yStart;

	public PSDFilterScreen(BlockPos pos) {
		super(new LiteralText(""));
		this.pos = pos;

		final World world = MinecraftClient.getInstance().world;
		if (world == null) {
			filterRouteIds = new HashSet<>();
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			filterRouteIds = entity instanceof BlockPSDTop.TileEntityPSDTop ? ((BlockPSDTop.TileEntityPSDTop) entity).getRouteIds() : new HashSet<>();
		}

		filterButton = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText(""), button -> {
			if (client != null) {
				final List<NameColorDataBase> routes = new ArrayList<>(ClientData.ROUTES);
				Collections.sort(routes);
				UtilitiesClient.setScreen(client, new DashboardListSelectorScreen(this, routes, filterRouteIds, false, false));
			}
		});

		yStart = SQUARE_SIZE;
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, yStart + SQUARE_SIZE * 2, PANEL_WIDTH / 2);
		filterButton.setMessage(new TranslatableText("selectWorld.edit"));
		addDrawableChild(filterButton);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textRenderer.draw(matrices, new TranslatableText("gui.mtr.filtered_routes", filterRouteIds.size()), SQUARE_SIZE, yStart + TEXT_PADDING, ARGB_WHITE);
			textRenderer.draw(matrices, new TranslatableText(filterRouteIds.isEmpty() ? "gui.mtr.filtered_routes_empty_psd" : "gui.mtr.filtered_routes_condition_psd"), SQUARE_SIZE, yStart + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			int i = 0;
			for (final long routeId : filterRouteIds) {
				final Route route = ClientData.DATA_CACHE.routeIdMap.get(routeId);
				if (route != null) {
					textRenderer.draw(matrices, new LiteralText(IGui.formatStationName(route.name)), SQUARE_SIZE, yStart + SQUARE_SIZE * 3 + TEXT_PADDING + i, ARGB_WHITE);
				}
				i += TEXT_HEIGHT;
			}
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendPSDFilterC2S(pos, filterRouteIds);
		World world = MinecraftClient.getInstance().world;
		if(world != null) {
			updateFilter(world, pos, IBlock.getStatePropertySafe(world.getBlockState(pos), BlockPSDTop.FACING).rotateYClockwise(), filterRouteIds);
			updateFilter(world, pos, IBlock.getStatePropertySafe(world.getBlockState(pos), BlockPSDTop.FACING).rotateYCounterclockwise(), filterRouteIds);
		}
		super.onClose();
	}

	public static void updateFilter(World world, BlockPos pos, Direction offset, Set<Long> data) {
		BlockPos offsetPos = pos.offset(offset);
		BlockEntity entity = world.getBlockEntity(offsetPos);
		if(entity instanceof BlockPSDTop.TileEntityPSDTop) {
			/* Temp fix to clear the filtered list from the client; I can't get the client to sync route deletion */
			((BlockPSDTop.TileEntityPSDTop) entity).clearData();
			PacketTrainDataGuiClient.sendPSDFilterC2S(pos, data);
			updateFilter(world, offsetPos, offset, data);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
