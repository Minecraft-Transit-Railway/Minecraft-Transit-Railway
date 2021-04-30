package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.data.Station;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public abstract class RenderStationNameBase<T extends BlockStationNameBase.TileEntityStationNameBase> extends BlockEntityRenderer<T> implements IGui {

	public RenderStationNameBase(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!entity.shouldRender()) {
			return;
		}

		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		if (RenderSeat.shouldNotRender(pos, RenderSeat.maxTrainRenderDistance)) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final int color;
		switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
			case 1:
				color = ARGB_LIGHT_GRAY;
				break;
			case 2:
				color = ARGB_BLACK;
				break;
			default:
				color = ARGB_WHITE;
				break;
		}

		matrices.push();
		matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
		final Station station = ClientData.getStation(pos);
		drawStationName(entity, matrices, vertexConsumers, station == null ? new TranslatableText("gui.mtr.untitled").getString() : station.name, color);
		matrices.pop();
	}

	protected abstract void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, String stationName, int color);
}
