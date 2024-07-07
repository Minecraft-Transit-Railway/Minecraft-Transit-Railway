package org.mtr.mod.render;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public abstract class RenderStationNameBase<T extends BlockStationNameBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, IDrawing {

	public RenderStationNameBase(Argument dispatcher) {
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
		final int color = RenderRouteBase.getShadingColor(facing, entity.getColor(state));

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
		storedMatrixTransformations.add(graphicsHolderNew -> {
			graphicsHolderNew.rotateYDegrees(-facing.asRotation());
			graphicsHolderNew.rotateZDegrees(180);
		});

		final Station station = InitClient.findStation(pos);
		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			final boolean shouldFlip = i == 1;
			storedMatrixTransformations2.add(graphicsHolderNew -> {
				if (shouldFlip) {
					graphicsHolderNew.rotateYDegrees(180);
				}
				graphicsHolderNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
			});
			drawStationName(world, pos, state, facing, storedMatrixTransformations2, station == null ? TranslationProvider.GUI_MTR_UNTITLED.getString() : station.getName(), station == null ? 0 : station.getColor(), color, light);
		}
	}

	protected abstract void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light);
}
