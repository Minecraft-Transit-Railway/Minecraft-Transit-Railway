package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockEyeCandy;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;

public class RenderEyeCandy extends BlockEntityRenderer<BlockEyeCandy.BlockEntity> {

	public RenderEyeCandy(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockEyeCandy.BlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = blockEntity.getWorld2();
		if (world == null) {
			return;
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		if (clientPlayerEntity == null) {
			return;
		}

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + blockEntity.getPos2().getX(), blockEntity.getPos2().getY(), 0.5 + blockEntity.getPos2().getZ());
		final int newLight = blockEntity.getFullBrightness() ? GraphicsHolder.getDefaultLight() : light;

		if ((RenderRails.isHoldingRailRelated(clientPlayerEntity) || clientPlayerEntity.isHolding(Blocks.EYE_CANDY.get().asItem())) && minecraftClient.getCurrentScreenMapped() == null) {
			MainRenderer.scheduleRender(new Identifier(Init.MOD_ID_NTE, "textures/item/eye_candy.png"), false, QueuedRenderLayer.INTERIOR, (graphicsHolderNew, offset) -> {
				storedMatrixTransformations.transform(graphicsHolderNew, offset);
				graphicsHolderNew.translate(0, 0.5, 0);
				InitClient.transformToFacePlayer(graphicsHolderNew, blockEntity.getPos2().getX() + 0.5, blockEntity.getPos2().getY() + 0.5, blockEntity.getPos2().getZ() + 0.5);
				graphicsHolderNew.rotateZDegrees(180);
				IDrawing.drawTexture(graphicsHolderNew, -0.5F, -0.5F, 1, 1, Direction.UP, GraphicsHolder.getDefaultLight());
				graphicsHolderNew.pop();
			});
		}

		final BlockPos blockPos = blockEntity.getPos2();
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockEyeCandy.FACING);
		final String modelId = blockEntity.getModelId();
		if (modelId != null) {
			CustomResourceLoader.getObjectById(modelId, objectResource -> {
				final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
				storedMatrixTransformationsNew.add(graphicsHolderNew -> {
					graphicsHolderNew.translate(blockEntity.getTranslateX(), blockEntity.getTranslateY(), blockEntity.getTranslateZ());
					graphicsHolderNew.rotateYDegrees(180 - facing.asRotation());
					graphicsHolderNew.rotateXRadians(blockEntity.getRotateX() + (float) Math.PI);
					graphicsHolderNew.rotateYRadians(blockEntity.getRotateY());
					graphicsHolderNew.rotateZRadians(blockEntity.getRotateZ());
				});
				objectResource.render(storedMatrixTransformationsNew, newLight);
			});
		}
	}

	@Override
	public boolean isInRenderDistance(BlockEyeCandy.BlockEntity blockEntity, Vector3d position) {
		return true;
	}
}
