package org.mtr.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.block.BlockStationNameBase;
import org.mtr.block.BlockStationNameEntrance;
import org.mtr.block.IBlock;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nullable;

public class RenderStationNameTiled<T extends BlockStationNameBase.BlockEntityBase> extends RenderStationNameBase<T> {

	private final boolean showLogo;

	public RenderStationNameTiled(boolean showLogo) {
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
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationNameEntrance(propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, IGui.insertTranslation(TranslationProvider.GUI_MTR_STATION_CJK, TranslationProvider.GUI_MTR_STATION, 1, stationName), totalLength / logoSize).identifier, false, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				matrixStack.pop();
			});
		} else {
			MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationName(stationName, totalLength).identifier, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.5F, -0.5F, 1, 1, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
				matrixStack.pop();
			});
		}
	}

	private int getLength(@Nullable World world, BlockPos pos, boolean lookRight) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, Properties.FACING);
		final Block thisBlock = world.getBlockState(pos).getBlock();

		int length = 1;
		while (true) {
			final Block checkBlock = world.getBlockState(pos.offset(lookRight ? facing.rotateYClockwise() : facing.rotateYCounterclockwise(), length)).getBlock();
			if (checkBlock instanceof BlockStationNameBase && checkBlock == thisBlock) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
