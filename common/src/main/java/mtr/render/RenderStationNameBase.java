package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
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

		matrices.pushPose();
		matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
		matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
		matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
		final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
		final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			matrices.pushPose();
			matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
			drawStationName(world, pos, state, facing, matrices, vertexConsumers, immediate, station == null ? Text.translatable("gui.mtr.untitled").getString() : station.name, station == null ? 0 : station.color, color, light);
			matrices.popPose();
			matrices.mulPose(Vector3f.YP.rotationDegrees(180));
		}
		immediate.endBatch();
		matrices.popPose();
	}

	protected abstract void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int stationColor, int color, int light);
}
