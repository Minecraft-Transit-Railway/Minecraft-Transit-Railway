package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockDriverKeyDispenser;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Depot;
import org.mtr.data.IGui;
import org.mtr.item.ItemDriverKey;
import org.mtr.registry.Items;

public class RenderDriverKeyDispenser extends BlockEntityRendererExtension<BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity> {

	private static final double ROTATION_OFFSET = Math.sqrt(2) / 2;
	private static final double ROTATION_SCALE = 14 / ROTATION_OFFSET / 16 / 2;
	private static final int ROTATION_DURATION = 5000;

	@Override
	public void render(BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity entity, ClientWorld world, ClientPlayerEntity player, float tickDelta, int light, int overlay) {
		final Depot depot = MTRClient.findDepot(entity.getPos());
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos().getX(), entity.getPos().getY(), 0.5 + entity.getPos().getZ());

		if (depot == null) {
			MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
				storedMatrixTransformations.transform(matrixStack, offset);
				matrixStack.translate(0, 0.5, 0);
				MTRClient.transformToFacePlayer(matrixStack, entity.getPos().getX() + 0.5, entity.getPos().getY() + 0.5, entity.getPos().getZ() + 0.5);
				IDrawing.rotateZDegrees(matrixStack, 45);
				matrixStack.scale((float) ROTATION_SCALE, (float) ROTATION_SCALE, 1);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.0625F, -0.5F, 0.125F, 1, 0, 0, 1, 1, Direction.UP, 0xFFFF0000, IGui.DEFAULT_LIGHT);
				IDrawing.drawTexture(matrixStack, vertexConsumer, -0.5F, -0.0625F, 1, 0.125F, 0, 0, 1, 1, Direction.UP, 0xFFFF0000, IGui.DEFAULT_LIGHT);
				matrixStack.pop();
			});
		} else {
			final IntArrayList colors = new IntArrayList();
			if (entity.getDispenseBasicDriverKey()) {
				colors.add(((ItemDriverKey) Items.BASIC_DRIVER_KEY.get()).color);
			}
			if (entity.getDispenseAdvancedDriverKey()) {
				colors.add(((ItemDriverKey) Items.ADVANCED_DRIVER_KEY.get()).color);
			}
			if (entity.getDispenseGuardKey()) {
				colors.add(((ItemDriverKey) Items.GUARD_KEY.get()).color);
			}

			if (!colors.isEmpty()) {
				MainRenderer.scheduleRender(Identifier.of(MTR.MOD_ID, "textures/item/driver_key.png"), false, QueuedRenderLayer.INTERIOR, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					matrixStack.translate(0, 0.5, 0);
					MTRClient.transformToFacePlayer(matrixStack, entity.getPos().getX() + 0.5, entity.getPos().getY() + 0.5, entity.getPos().getZ() + 0.5);
					IDrawing.rotateZDegrees(matrixStack, 180);
					final Vec3d offsetVector;

					if (colors.size() > 1) {
						matrixStack.scale((float) ROTATION_SCALE / 2, (float) ROTATION_SCALE / 2, 1);
						offsetVector = new Vec3d(ROTATION_OFFSET, 0, 0);
					} else {
						matrixStack.scale((float) ROTATION_SCALE, (float) ROTATION_SCALE, 1);
						offsetVector = Vec3d.ZERO;
					}

					for (int i = 0; i < colors.size(); i++) {
						final Vec3d newOffsetVector = offsetVector.rotateZ((float) (2 * Math.PI * ((float) i / colors.size() + (float) (System.currentTimeMillis() % ROTATION_DURATION) / ROTATION_DURATION)));
						IDrawing.drawTexture(matrixStack, vertexConsumer, (float) newOffsetVector.x - 0.5F, (float) newOffsetVector.y - 0.5F, 1, 1, 0, 0, 1, 1, Direction.DOWN, IGui.ARGB_BLACK | colors.getInt(i), IGui.DEFAULT_LIGHT);
					}

					matrixStack.pop();
				});
			}
		}
	}
}
