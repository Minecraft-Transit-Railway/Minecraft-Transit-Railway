package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RenderStationNameBase<T extends BlockStationNameBase.TileEntityStationNameBase> extends BlockEntityRendererMapper<T> implements IGui, IDrawing {

	public RenderStationNameBase(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final BlockGetter world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final int color = RenderRouteBase.getShadingColor(facing, entity.getColor(state));

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			matricesNew.translate(0.5 + entity.getBlockPos().getX(), 0.5 + entity.yOffset + entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
			UtilitiesClient.rotateYDegrees(matricesNew, -facing.toYRot());
			UtilitiesClient.rotateZDegrees(matricesNew, 180);
		});

		final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			final boolean shouldFlip = i == 1;
			storedMatrixTransformations2.add(matricesNew -> {
				if (shouldFlip) {
					UtilitiesClient.rotateYDegrees(matricesNew, 180);
				}
				matricesNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
			});
			drawStationName(world, pos, state, facing, storedMatrixTransformations2, vertexConsumers, station == null ? Text.translatable("gui.mtr.untitled").getString() : station.name, station == null ? 0 : station.color, color, light);
		}
	}

	protected abstract void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, MultiBufferSource vertexConsumers, String stationName, int stationColor, int color, int light);
}
