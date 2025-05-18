package org.mtr.mod.render;

import org.mtr.core.data.Depot;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockDriverKeyDispenser;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.item.ItemDriverKey;

public class RenderDriverKeyDispenser extends BlockEntityRenderer<BlockDriverKeyDispenser.BlockEntity> {

	private static final double ROTATION_OFFSET = Math.sqrt(2) / 2;
	private static final double ROTATION_SCALE = 14 / ROTATION_OFFSET / 16 / 2;
	private static final int ROTATION_DURATION = 5000;

	public RenderDriverKeyDispenser(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockDriverKeyDispenser.BlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final Depot depot = InitClient.findDepot(blockEntity.getPos2());
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + blockEntity.getPos2().getX(), blockEntity.getPos2().getY(), 0.5 + blockEntity.getPos2().getZ());

		if (depot == null) {
			MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.INTERIOR, (graphicsHolderNew, offset) -> {
				storedMatrixTransformations.transform(graphicsHolderNew, offset);
				graphicsHolderNew.translate(0, 0.5, 0);
				InitClient.transformToFacePlayer(graphicsHolderNew, blockEntity.getPos2().getX() + 0.5, blockEntity.getPos2().getY() + 0.5, blockEntity.getPos2().getZ() + 0.5);
				graphicsHolderNew.rotateZDegrees(45);
				graphicsHolderNew.scale((float) ROTATION_SCALE, (float) ROTATION_SCALE, 1);
				IDrawing.drawTexture(graphicsHolderNew, -0.0625F, -0.5F, 0.125F, 1, 0, 0, 1, 1, Direction.UP, 0xFFFF0000, GraphicsHolder.getDefaultLight());
				IDrawing.drawTexture(graphicsHolderNew, -0.5F, -0.0625F, 1, 0.125F, 0, 0, 1, 1, Direction.UP, 0xFFFF0000, GraphicsHolder.getDefaultLight());
				graphicsHolderNew.pop();
			});
		} else {
			final IntArrayList colors = new IntArrayList();
			if (blockEntity.getDispenseBasicDriverKey()) {
				colors.add(((ItemDriverKey) Items.BASIC_DRIVER_KEY.get().data).color);
			}
			if (blockEntity.getDispenseAdvancedDriverKey()) {
				colors.add(((ItemDriverKey) Items.ADVANCED_DRIVER_KEY.get().data).color);
			}
			if (blockEntity.getDispenseGuardKey()) {
				colors.add(((ItemDriverKey) Items.GUARD_KEY.get().data).color);
			}

			if (!colors.isEmpty()) {
				MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/item/driver_key.png"), false, QueuedRenderLayer.INTERIOR, (graphicsHolderNew, offset) -> {
					storedMatrixTransformations.transform(graphicsHolderNew, offset);
					graphicsHolderNew.translate(0, 0.5, 0);
					InitClient.transformToFacePlayer(graphicsHolderNew, blockEntity.getPos2().getX() + 0.5, blockEntity.getPos2().getY() + 0.5, blockEntity.getPos2().getZ() + 0.5);
					graphicsHolderNew.rotateZDegrees(180);
					final Vector3d offsetVector;

					if (colors.size() > 1) {
						graphicsHolderNew.scale((float) ROTATION_SCALE / 2, (float) ROTATION_SCALE / 2, 1);
						offsetVector = new Vector3d(ROTATION_OFFSET, 0, 0);
					} else {
						graphicsHolderNew.scale((float) ROTATION_SCALE, (float) ROTATION_SCALE, 1);
						offsetVector = Vector3d.getZeroMapped();
					}

					for (int i = 0; i < colors.size(); i++) {
						final Vector3d newOffsetVector = offsetVector.rotateZ((float) (2 * Math.PI * ((float) i / colors.size() + (float) (System.currentTimeMillis() % ROTATION_DURATION) / ROTATION_DURATION)));
						IDrawing.drawTexture(graphicsHolderNew, (float) newOffsetVector.getXMapped() - 0.5F, (float) newOffsetVector.getYMapped() - 0.5F, 1, 1, 0, 0, 1, 1, Direction.DOWN, IGui.ARGB_BLACK | colors.getInt(i), GraphicsHolder.getDefaultLight());
					}

					graphicsHolderNew.pop();
				});
			}
		}
	}
}
