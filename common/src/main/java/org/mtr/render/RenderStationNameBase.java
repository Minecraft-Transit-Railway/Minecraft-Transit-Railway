package org.mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mtr.MTRClient;
import org.mtr.block.BlockStationNameBase;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Station;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

public abstract class RenderStationNameBase<T extends BlockStationNameBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IDrawing {

	@Override
	public void render(T entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		final int color = RenderRouteBase.getShadingColor(facing, entity.getColor(state));

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
		storedMatrixTransformations.add(matrixStack -> {
			IDrawing.rotateYDegrees(matrixStack, -facing.getPositiveHorizontalDegrees());
			IDrawing.rotateZDegrees(matrixStack, 180);
		});

		final Station station = MTRClient.findStation(pos);
		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			final boolean shouldFlip = i == 1;
			storedMatrixTransformations2.add(matrixStack -> {
				if (shouldFlip) {
					IDrawing.rotateYDegrees(matrixStack, 180);
				}
				matrixStack.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
			});
			drawStationName(world, pos, state, facing, storedMatrixTransformations2, station == null ? TranslationProvider.GUI_MTR_UNTITLED.getString() : station.getName(), station == null ? 0 : station.getColor(), color, light);
		}
	}

	protected abstract void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light);
}
