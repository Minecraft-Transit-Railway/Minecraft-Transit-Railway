package org.mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.MTRClient;
import org.mtr.block.BlockRouteSignBase;
import org.mtr.block.IBlock;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.data.IGui;

public class RenderRouteSign<T extends BlockRouteSignBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IBlock, IGui {

	private static final float SIDE = 2.5F / 16;
	private static final float BOTTOM = 10.5F / 16;
	private static final float MIDDLE = 13F / 16;
	private static final float TOP = 15.5F / 16;
	private static final float WIDTH = 1 - SIDE * 2;
	private static final float HEIGHT_BOTTOM = MIDDLE - BOTTOM + 1;
	private static final float HEIGHT_TOP = TOP - MIDDLE;
	private static final float TEXTURE_BREAK = MIDDLE / HEIGHT_BOTTOM;

	@Override
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		final boolean isTop = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		final int arrowDirection = IBlock.getStatePropertySafe(state, BlockRouteSignBase.ARROW_DIRECTION);

		final Station station = MTRClient.findStation(pos);
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

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		storedMatrixTransformations.add(matrixStack -> {
			IDrawing.rotateYDegrees(matrixStack, -facing.getPositiveHorizontalDegrees());
			matrixStack.translate(-0.5, 0, 0.4375 - SMALL_OFFSET * 2);
		});

		MainRenderer.scheduleRender(
				DynamicTextureCache.instance.getDirectionArrow(platform.getId(), (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.2F, WIDTH / HEIGHT_TOP, ARGB_BLACK, ARGB_WHITE, 0).identifier,
				false,
				QueuedRenderLayer.EXTERIOR,
				(matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					IDrawing.drawTexture(matrixStack, vertexConsumer, 1 - SIDE, TOP + (isTop ? 0 : 1), 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, 1, 1, facing.getOpposite(), -1, light);
					matrixStack.pop();
				}
		);

		MainRenderer.scheduleRender(
				DynamicTextureCache.instance.getRouteMap(platform.getId(), true, false, HEIGHT_BOTTOM / WIDTH, false).identifier,
				false,
				QueuedRenderLayer.EXTERIOR,
				(matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					IDrawing.drawTexture(matrixStack, vertexConsumer, 1 - SIDE, MIDDLE + (isTop ? 0 : 1), 0, 1 - SIDE, isTop ? 0 : BOTTOM, 0, SIDE, isTop ? 0 : BOTTOM, 0, SIDE, MIDDLE + (isTop ? 0 : 1), 0, 0, 0, isTop ? TEXTURE_BREAK : 1, 1, facing.getOpposite(), -1, light);
					matrixStack.pop();
				}
		);
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}
}
