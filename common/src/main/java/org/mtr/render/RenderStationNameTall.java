package org.mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
	protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
		if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == IBlock.EnumThird.MIDDLE) {
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getTallStationName(color, stationName, stationColor, width / height).identifier, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -width / 2, (-height / 2) - offsetY, width, height, 0, 0, 1, 1, facing, ARGB_WHITE, light);
				matrixStack.pop();
			});
		}
	}
}
