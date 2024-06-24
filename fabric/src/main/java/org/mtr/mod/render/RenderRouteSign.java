package org.mtr.mod.render;

import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockRouteSignBase;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;

public class RenderRouteSign<T extends BlockRouteSignBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IBlock, IGui {

	private static final float SIDE = 2.5F / 16;
	private static final float BOTTOM = 10.5F / 16;
	private static final float MIDDLE = 13F / 16;
	private static final float TOP = 15.5F / 16;
	private static final float WIDTH = 1 - SIDE * 2;
	private static final float HEIGHT_BOTTOM = MIDDLE - BOTTOM + 1;
	private static final float HEIGHT_TOP = TOP - MIDDLE;
	private static final float TEXTURE_BREAK = MIDDLE / HEIGHT_BOTTOM;

	public RenderRouteSign(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos2();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final boolean isTop = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final int arrowDirection = IBlock.getStatePropertySafe(state, BlockRouteSignBase.ARROW_DIRECTION);

		final Station station = InitClient.findStation(pos);
		if (station == null) {
			return;
		}

		if (station.savedRails.isEmpty()) {
			return;
		}

		final Platform platform = station.savedRails.stream().filter(checkPlatform -> checkPlatform.getId() == entity.getPlatformId()).findFirst().orElse(null);
		if (platform == null) {
			return;
		}

		graphicsHolder.push();
		graphicsHolder.translate(0.5, 0, 0.5);
		graphicsHolder.rotateYDegrees(-facing.asRotation());
		graphicsHolder.translate(-0.5, 0, 0.4375 - SMALL_OFFSET * 2);

		graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(DynamicTextureCache.instance.getDirectionArrow(platform.getId(), (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.2F, WIDTH / HEIGHT_TOP, ARGB_BLACK, ARGB_WHITE, 0).identifier));
		IDrawing.drawTexture(graphicsHolder, 1 - SIDE, TOP + (isTop ? 0 : 1), 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, 1, 1, facing.getOpposite(), -1, light);

		graphicsHolder.createVertexConsumer(MoreRenderLayers.getExterior(DynamicTextureCache.instance.getRouteMap(platform.getId(), true, false, HEIGHT_BOTTOM / WIDTH, false).identifier));
		IDrawing.drawTexture(graphicsHolder, 1 - SIDE, MIDDLE + (isTop ? 0 : 1), 0, 1 - SIDE, isTop ? 0 : BOTTOM, 0, SIDE, isTop ? 0 : BOTTOM, 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, isTop ? TEXTURE_BREAK : 1, 1, facing.getOpposite(), -1, light);

		graphicsHolder.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox2(T blockEntity) {
		return true;
	}
}
