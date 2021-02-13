package mtr.render;

import mtr.block.BlockRail;
import mtr.data.Rail;
import mtr.gui.IGui;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Collection;

public class RenderRail extends BlockEntityRenderer<BlockRail.TileEntityRail> implements IGui {

	public RenderRail(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockRail.TileEntityRail entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final Collection<Rail> railList = entity.railMap.values();
		final BlockPos pos = entity.getPos();

		matrices.push();
		matrices.translate(-pos.getX(), 0.0625 + SMALL_OFFSET, -pos.getZ());

		for (final Rail rail : railList) {
			rail.render((x1, z1, x2, z2, x3, z3, x4, z4) -> {
				final float textureOffset = (((int) (x1 + z1)) % 4) * 0.25F;
				final BlockPos pos2 = new BlockPos(x1, pos.getY(), z1);
				final int light2 = LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos2), world.getLightLevel(LightType.SKY, pos2));

				IGui.drawTexture(matrices, vertexConsumers, "textures/block/rail.png", x4, 0, z4, x3, 0, z3, x2, 0, z2, x1, 0, z1, 0, 0.1875F + textureOffset, 1, 0.3125F + textureOffset, Direction.UP, -1, light2);
			});
		}

		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(BlockRail.TileEntityRail blockEntity) {
		return true;
	}
}
