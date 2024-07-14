package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.BlockStationNameEntrance;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;

public class RenderStationNameTiled<T extends BlockStationNameBase.BlockEntityBase> extends RenderStationNameBase<T> {

	private final boolean showLogo;

	public RenderStationNameTiled(Argument dispatcher, boolean showLogo) {
		super(dispatcher);
		this.showLogo = showLogo;
	}

	@Override
	protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
		final int lengthLeft = getLength(world, pos, false);
		final int lengthRight = getLength(world, pos, true);

		final int totalLength = lengthLeft + lengthRight - 1;
		if (showLogo) {
			final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
			final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationNameEntrance(propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, IGui.insertTranslation(TranslationProvider.GUI_MTR_STATION_CJK, TranslationProvider.GUI_MTR_STATION, 1, stationName), totalLength / logoSize).identifier, false, QueuedRenderLayer.INTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				graphicsHolder.pop();
			});
		} else {
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationName(stationName, totalLength).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
				storedMatrixTransformations.transform(graphicsHolder, offset);
				IDrawing.drawTexture(graphicsHolder, -0.5F, -0.5F, 1, 1, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				graphicsHolder.pop();
			});
		}
	}

	private int getLength(@Nullable World world, BlockPos pos, boolean lookRight) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
		final Block thisBlock = world.getBlockState(pos).getBlock();

		int length = 1;
		while (true) {
			final Block checkBlock = world.getBlockState(pos.offset(lookRight ? facing.rotateYClockwise() : facing.rotateYCounterclockwise(), length)).getBlock();
			if (checkBlock.data instanceof BlockStationNameBase && checkBlock.data == thisBlock.data) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
