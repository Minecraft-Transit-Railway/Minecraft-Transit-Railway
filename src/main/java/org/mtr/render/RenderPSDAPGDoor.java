package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.MTR;
import org.mtr.block.*;
import org.mtr.data.IGui;
import org.mtr.tool.Drawing;

import java.util.function.Consumer;

public class RenderPSDAPGDoor<T extends BlockPSDAPGDoorBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IBlock {

	private final int type;

	public static final ModelPart MODEL_SMALL_CUBE = createSingleCube(16, 16, 12, 20, 12, 8, 8, 8);
	private static final ModelPart MODEL_PSD = createSingleCube(36, 18, 0, 0, 0, 16, 16, 2);
	private static final ModelPart MODEL_PSD_END_LEFT_1 = createSingleCube(20, 18, 0, 0, 0, 8, 16, 2);
	private static final ModelPart MODEL_PSD_END_RIGHT_1 = createSingleCube(20, 18, 8, 0, 0, 8, 16, 2);
	private static final ModelPart MODEL_PSD_END_LEFT_2 = createSingleCube(20, 18, 8, 0, 2, 8, 16, 2);
	private static final ModelPart MODEL_PSD_END_RIGHT_2 = createSingleCube(20, 18, 0, 0, 2, 8, 16, 2);
	private static final ModelPart MODEL_PSD_LIGHT_LEFT = createSingleCube(16, 16, 0, -1, 5, 1, 1, 1);
	private static final ModelPart MODEL_PSD_LIGHT_RIGHT = createSingleCube(16, 16, 15, -1, 5, 1, 1, 1);
	private static final ModelPart MODEL_APG_TOP = createSingleCube(34, 9, 0, 8, 1, 16, 8, 1);
	private static final ModelPart MODEL_APG_BOTTOM = createModelPart(modelPartData -> {
		createModelPartData(modelPartData, 0, 0, -8, -16, -7, 16, 16, 1, 0, 0, 0, 0, 0, 0, 0);
		createModelPartData(modelPartData, 0, 17, -8, -6, -8, 16, 6, 1, 0, 0, 0, 0, 0, 0, 0);
		createModelPartData(modelPartData, 0, 0, -8, -2, 0, 16, 2, 1, 0, 0, -6, 0, -0.7854F, 0, 0);
	}, 34, 27);
	private static final ModelPart MODEL_APG_LIGHT = createModelPart(modelPartData -> {
		createModelPartData(modelPartData, 0, 4, -0.5F, -9, -7, 1, 1, 3, 0.05F, 0, 0, 0, 0, 0, 0);
		createModelPartData(modelPartData, 0, 0, -0.5F, 0.05F, -3.05F, 1, 1, 3, 0.05F, 0, -9.05F, -4.95F, 0.3927F, 0, 0);
	}, 8, 8);
	private static final ModelPart MODEL_APG_DOOR_LOCKED = createSingleCube(6, 6, 5, 10, 1, 6, 6, 0);
	private static final ModelPart MODEL_PSD_DOOR_LOCKED = createSingleCube(6, 6, 5, 6, 1, 6, 6, 0);
	private static final ModelPart MODEL_LIFT_LEFT = createSingleCube(28, 18, 0, 0, 0, 12, 16, 2);
	private static final ModelPart MODEL_LIFT_RIGHT = createSingleCube(28, 18, 4, 0, 0, 12, 16, 2);

	public RenderPSDAPGDoor(int type) {
		this.type = type;
	}

	@Override
	public void render(T blockEntity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		blockEntity.tick(tickDelta);

		final BlockPos blockPos = blockEntity.getBlockPos();
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockStateProperties.HORIZONTAL_FACING);
		final boolean side = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
		final boolean half = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
		final boolean end = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.END);
		final boolean unlocked = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.UNLOCKED);
		final double open = Math.min(blockEntity.getDoorValue(), type >= 3 ? 0.75F : 1);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), 0.5 + blockEntity.getBlockPos().getZ());
		storedMatrixTransformations.add(matrixStack -> {
			Drawing.rotateYDegrees(matrixStack, -facing.toYRot());
			Drawing.rotateXDegrees(matrixStack, 180);
		});
		final StoredMatrixTransformations storedMatrixTransformationsLight = storedMatrixTransformations.copy();

		switch (type) {
			case 0:
			case 1:
				if (half) {
					MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/light_%s.png", open > 0 ? "on" : "off")), false, open > 0 ? QueuedRenderLayer.LIGHT : QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformationsLight.transform(matrixStack, offset);
						(side ? MODEL_PSD_LIGHT_RIGHT : MODEL_PSD_LIGHT_LEFT).render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				if (end) {
					MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/psd_door_end_%s_%s_2_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformationsLight.transform(matrixStack, offset);
						matrixStack.translate(open / 2 * (side ? -1 : 1), 0, 0);
						(side ? MODEL_PSD_END_RIGHT_2 : MODEL_PSD_END_LEFT_2).render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				break;
			case 2:
				if (half) {
					final Block block = world.getBlockState(blockPos.relative(side ? facing.getClockWise() : facing.getCounterClockWise())).getBlock();
					if (block instanceof BlockAPGGlass || block instanceof BlockAPGGlassEnd) {
						MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/apg_door_light_%s.png", open > 0 ? "on" : "off")), false, open > 0 ? QueuedRenderLayer.LIGHT_TRANSLUCENT : QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
							storedMatrixTransformationsLight.transform(matrixStack, offset);
							matrixStack.translate(side ? -0.515625 : 0.515625, 0, 0);
							matrixStack.scale(0.5F, 1, 1);
							MODEL_APG_LIGHT.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
							matrixStack.popPose();
						});
					}
				}
				break;
		}

		storedMatrixTransformations.add(matricesNew -> matricesNew.translate(open * (side ? -1 : 1), 0, 0));

		switch (type) {
			case 0:
			case 1:
				if (end) {
					MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/psd_door_end_%s_%s_1_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						(side ? MODEL_PSD_END_RIGHT_1 : MODEL_PSD_END_LEFT_1).render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				} else {
					MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/psd_door_%s_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						MODEL_PSD.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				if (half && !unlocked) {
					MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/sign/door_not_in_use.png"), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						if (end) {
							matrixStack.translate(side ? 0.25 : -0.25, 0, 0);
						}
						MODEL_PSD_DOOR_LOCKED.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				break;
			case 2:
				MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/apg_door_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left")), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					(half ? MODEL_APG_TOP : MODEL_APG_BOTTOM).render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
					matrixStack.popPose();
				});
				if (half && !unlocked) {
					MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/sign/door_not_in_use.png"), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						MODEL_APG_DOOR_LOCKED.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				break;
			case 4:
				if (IBlock.getStatePropertySafe(world, blockPos, TripleHorizontalBlock.CENTER)) {
					break;
				}
				storedMatrixTransformations.add(matricesNew -> matricesNew.translate(side ? 0.5 : -0.5, 0, 0));
			case 3:
				MainRenderer.scheduleRender(ResourceLocation.parse(String.format("mtr:textures/block/lift_door_%s_%s_1.png", half ? "top" : "bottom", side ? "right" : "left")), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					(side ? MODEL_LIFT_RIGHT : MODEL_LIFT_LEFT).render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
					matrixStack.popPose();
				});
				if (half && !unlocked) {
					MainRenderer.scheduleRender(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "textures/block/sign/door_not_in_use.png"), false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
						storedMatrixTransformations.transform(matrixStack, offset);
						matrixStack.translate(side ? 0.125 : -0.125, 0, 0);
						MODEL_PSD_DOOR_LOCKED.render(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
						matrixStack.popPose();
					});
				}
				break;
		}
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	private static ModelPart createSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
		return createModelPart(modelPartData -> createModelPartData(modelPartData, 0, 0, x - 8, y - 16, z - 8, sizeX, sizeY, sizeZ, 0, 0, 0, 0, 0, 0, 0), textureWidth, textureHeight);
	}

	private static ModelPart createModelPart(Consumer<PartDefinition> builder, int textureWidth, int textureHeight) {
		final MeshDefinition modelData = new MeshDefinition();
		builder.accept(modelData.getRoot());
		return LayerDefinition.create(modelData, textureWidth, textureHeight).bakeRoot();
	}

	private static void createModelPartData(PartDefinition parent, int textureX, int textureY, float offsetX, float offsetY, float offsetZ, float sizeX, float sizeY, float sizeZ, float dilation, float pivotX, float pivotY, float pivotZ, float rotateX, float rotateY, float rotateZ) {
		parent.addOrReplaceChild(MTR.randomString(), CubeListBuilder.create().texOffs(textureX, textureY).addBox(offsetX, offsetY, offsetZ, sizeX, sizeY, sizeZ, new CubeDeformation(dilation)), PartPose.offsetAndRotation(pivotX, pivotY, pivotZ, rotateX, rotateY, rotateZ));
	}
}
