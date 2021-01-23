package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class RenderStationNameEntrance<T extends BlockStationNameBase.TileEntityStationNameBase> extends RenderStationName<T> {

	public RenderStationNameEntrance(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
	}

	@Override
	protected String formatStationName(String name) {
		return IGui.addToStationName(name, "", "", "站", " Station");
	}

	private int getLength(WorldAccess world, BlockPos pos, Direction facing) {
		int length = 1;

		while (true) {
			final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), length));
			if (state.getBlock() instanceof BlockStationNameEntrance) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
