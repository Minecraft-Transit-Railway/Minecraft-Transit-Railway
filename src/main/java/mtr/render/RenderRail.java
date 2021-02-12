package mtr.render;

import mtr.block.BlockRail;
import mtr.data.Rail;
import mtr.gui.IGui;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class RenderRail extends BlockEntityRenderer<BlockRail.TileEntityRail> {

	public RenderRail(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockRail.TileEntityRail entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final List<Rail> railList = entity.railList;
		final BlockPos pos = entity.getPos();

		matrices.push();
		matrices.translate(-pos.getX(), 0.0625, -pos.getZ());

		for (final Rail rail : railList) {
			rail.render((x1, z1, x2, z2, x3, z3, x4, z4) -> {
				IGui.drawTexture(matrices, vertexConsumers, "textures/block/rail.png", x1, 0, z1, x2, 0, z2, x3, 0, z3, x4, 0, z4, 0, 0, 1, 1, Direction.UP, -1, light);
				IGui.drawTexture(matrices, vertexConsumers, "textures/block/rail.png", x4, 0, z4, x3, 0, z3, x2, 0, z2, x1, 0, z1, 0, 0, 1, 1, Direction.UP, -1, light);
			});
		}

		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(BlockRail.TileEntityRail blockEntity) {
		return true;
	}
}
