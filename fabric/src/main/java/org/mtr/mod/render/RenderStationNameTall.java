package org.mtr.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mod.block.BlockStationNameTallBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;

public class RenderStationNameTall<T extends BlockStationNameTallBase.BlockEntityTallBase> extends RenderStationNameBase<T> {

	private final float width;
	private final float height;
	private final float offsetY;

	public RenderStationNameTall(Argument dispatcher, float width, float height, float offsetY) {
		super(dispatcher);
		this.width = width;
		this.height = height;
		this.offsetY = offsetY;
	}

	@Override
	protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
		if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == IBlock.EnumThird.MIDDLE) {
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getTallStationName(color, stationName, stationColor, width / height).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -width / 2, (-height / 2) - offsetY, width, height, 0, 0, 1, 1, facing, ARGB_WHITE, light);
				graphicsHolder.pop();
			});
		}
	}
}
