package org.mtr.render;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.block.BlockStationNameTallBase;
import org.mtr.block.IBlock;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;

public class RenderStationNameTall<T extends BlockStationNameTallBase.BlockEntityTallBase> extends RenderStationNameBase<T> {

	private final float width;
	private final float height;
	private final float offsetY;

	public RenderStationNameTall(float width, float height, float offsetY) {
		super();
		this.width = width;
		this.height = height;
		this.offsetY = offsetY;
	}

	@Override
	protected void drawStationName(Level world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
		if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == IBlock.EnumThird.MIDDLE) {
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getTallStationName(color, stationName, stationColor, width / height).identifier, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -width / 2, (-height / 2) - offsetY, width, height, 0, 0, 1, 1, facing, ARGB_WHITE, light);
				matrixStack.popPose();
			});
		}
	}
}
