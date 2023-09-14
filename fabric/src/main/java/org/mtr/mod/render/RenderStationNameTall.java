package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockStationNameTallBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;

public class RenderStationNameTall<T extends BlockStationNameTallBase.BlockEntityTallBase> extends RenderStationNameBase<T> {

	private static final float WIDTH = 0.6875F;
	private static final float HEIGHT = 1.5F;

	public RenderStationNameTall(BlockEntityRendererArgument dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
		if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == IBlock.EnumThird.MIDDLE) {
			RenderTrains.scheduleRender(DynamicTextureCache.instance.getTallStationName(color, stationName, stationColor, WIDTH / HEIGHT).identifier, false, RenderTrains.QueuedRenderLayer.EXTERIOR, graphicsHolder -> {
				storedMatrixTransformations.transform(graphicsHolder);
				IDrawing.drawTexture(graphicsHolder, -WIDTH / 2, -HEIGHT / 2, WIDTH, HEIGHT, 0, 0, 1, 1, facing, ARGB_WHITE, light);
				graphicsHolder.pop();
			});
		}
	}
}
