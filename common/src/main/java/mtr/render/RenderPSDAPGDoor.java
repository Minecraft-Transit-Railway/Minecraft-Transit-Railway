package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.block.*;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RenderPSDAPGDoor<T extends BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase> extends BlockEntityRendererMapper<T> implements IGui, IBlock {

	private final int type;
	private static final ModelSingleCube MODEL_PSD = new ModelSingleCube(36, 18, 0, 0, 0, 16, 16, 2);
	private static final ModelSingleCube MODEL_PSD_END_LEFT_1 = new ModelSingleCube(20, 18, 0, 0, 0, 8, 16, 2);
	private static final ModelSingleCube MODEL_PSD_END_RIGHT_1 = new ModelSingleCube(20, 18, 8, 0, 0, 8, 16, 2);
	private static final ModelSingleCube MODEL_PSD_END_LEFT_2 = new ModelSingleCube(20, 18, 8, 0, 2, 8, 16, 2);
	private static final ModelSingleCube MODEL_PSD_END_RIGHT_2 = new ModelSingleCube(20, 18, 0, 0, 2, 8, 16, 2);
	private static final ModelSingleCube MODEL_PSD_LIGHT_LEFT = new ModelSingleCube(16, 16, 0, -1, 5, 1, 1, 1);
	private static final ModelSingleCube MODEL_PSD_LIGHT_RIGHT = new ModelSingleCube(16, 16, 15, -1, 5, 1, 1, 1);
	private static final ModelSingleCube MODEL_APG_TOP = new ModelSingleCube(34, 9, 0, 8, 1, 16, 8, 1);
	private static final ModelAPGDoorBottom MODEL_APG_BOTTOM = new ModelAPGDoorBottom();
	private static final ModelAPGDoorLight MODEL_APG_LIGHT = new ModelAPGDoorLight();
	private static final ModelSingleCube MODEL_APG_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 10, 1, 6, 6, 0);
	private static final ModelSingleCube MODEL_PSD_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 6, 1, 6, 6, 0);
	private static final ModelSingleCube MODEL_LIFT_LEFT = new ModelSingleCube(28, 18, 0, 0, 0, 12, 16, 2);
	private static final ModelSingleCube MODEL_LIFT_RIGHT = new ModelSingleCube(28, 18, 4, 0, 0, 12, 16, 2);

	public RenderPSDAPGDoor(BlockEntityRenderDispatcher dispatcher, int type) {
		super(dispatcher);
		this.type = type;
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		if (IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.TEMP)) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.FACING);
		final boolean side = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
		final boolean half = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
		final boolean end = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.END);
		final boolean unlocked = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.UNLOCKED);
		final float open = Math.min(entity.getOpen(MTRClient.getLastFrameDuration()), type >= 3 ? 0.75F : 1);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			matricesNew.translate(0.5 + entity.getBlockPos().getX(), entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
			matricesNew.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
			matricesNew.mulPose(Vector3f.XP.rotationDegrees(180));
		});
		final StoredMatrixTransformations storedMatrixTransformationsLight = storedMatrixTransformations.copy();

		switch (type) {
			case 0:
			case 1:
				if (half) {
					RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/light_%s.png", open > 0 ? "on" : "off")), false, resourceLocation -> open > 0 ? MoreRenderLayers.getLight(resourceLocation, false) : MoreRenderLayers.getExterior(resourceLocation), (matricesNew, vertexConsumer) -> {
						storedMatrixTransformationsLight.transform(matricesNew);
						(side ? MODEL_PSD_LIGHT_RIGHT : MODEL_PSD_LIGHT_LEFT).renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				if (end) {
					RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/psd_door_end_%s_%s_2_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformationsLight.transform(matricesNew);
						matricesNew.translate(open / 2 * (side ? -1 : 1), 0, 0);
						(side ? MODEL_PSD_END_RIGHT_2 : MODEL_PSD_END_LEFT_2).renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				break;
			case 2:
				if (half) {
					final Block block = world.getBlockState(pos.relative(side ? facing.getClockWise() : facing.getCounterClockWise())).getBlock();
					if (block instanceof BlockAPGGlass || block instanceof BlockAPGGlassEnd) {
						RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/apg_door_light_%s.png", open > 0 ? "on" : "off")), false, resourceLocation -> open > 0 ? MoreRenderLayers.getLight(resourceLocation, true) : MoreRenderLayers.getExterior(resourceLocation), (matricesNew, vertexConsumer) -> {
							storedMatrixTransformationsLight.transform(matricesNew);
							matricesNew.translate(side ? -0.515625 : 0.515625, 0, 0);
							matricesNew.scale(0.5F, 1, 1);
							MODEL_APG_LIGHT.renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
							matricesNew.popPose();
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
					RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/psd_door_end_%s_%s_1_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						(side ? MODEL_PSD_END_RIGHT_1 : MODEL_PSD_END_LEFT_1).renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
					if (half && !unlocked) {
						storedMatrixTransformations.add(matricesNew -> matricesNew.translate(side ? 0.25 : -0.25, 0, 0));
					}
				} else {
					RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/psd_door_%s_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1")), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						MODEL_PSD.renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				if (half && !unlocked) {
					RenderTrains.scheduleRender(new ResourceLocation("mtr:textures/sign/door_not_in_use.png"), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						MODEL_PSD_DOOR_LOCKED.renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				break;
			case 2:
				RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/apg_door_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left")), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
					storedMatrixTransformations.transform(matricesNew);
					(half ? MODEL_APG_TOP : MODEL_APG_BOTTOM).renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
					matricesNew.popPose();
				});
				if (half && !unlocked) {
					RenderTrains.scheduleRender(new ResourceLocation("mtr:textures/sign/door_not_in_use.png"), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						MODEL_APG_DOOR_LOCKED.renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				break;
			case 4:
				if (IBlock.getStatePropertySafe(world, pos, ITripleBlock.ODD)) {
					break;
				}
				storedMatrixTransformations.add(matricesNew -> matricesNew.translate(side ? 0.5 : -0.5, 0, 0));
			case 3:
				RenderTrains.scheduleRender(new ResourceLocation(String.format("mtr:textures/block/lift_door_%s_%s_1.png", half ? "top" : "bottom", side ? "right" : "left")), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
					storedMatrixTransformations.transform(matricesNew);
					(side ? MODEL_LIFT_RIGHT : MODEL_LIFT_LEFT).renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
					matricesNew.popPose();
				});
				if (half && !unlocked) {
					RenderTrains.scheduleRender(new ResourceLocation("mtr:textures/sign/door_not_in_use.png"), false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						matricesNew.translate(side ? 0.125 : -0.125, 0, 0);
						MODEL_PSD_DOOR_LOCKED.renderToBuffer(matricesNew, vertexConsumer, light, overlay, 1, 1, 1, 1);
						matricesNew.popPose();
					});
				}
				break;
		}
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	private static class ModelSingleCube extends EntityModel<Entity> {

		private final ModelMapper cube;

		private ModelSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int length, int height, int depth) {
			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
			cube = new ModelMapper(modelDataWrapper);
			cube.texOffs(0, 0).addBox(x - 8, y - 16, z - 8, length, height, depth, 0, false);
			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			cube.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			cube.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}

	private static class ModelAPGDoorBottom extends EntityModel<Entity> {

		private final ModelMapper bone;

		private ModelAPGDoorBottom() {
			final int textureWidth = 34;
			final int textureHeight = 27;

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			bone = new ModelMapper(modelDataWrapper);
			bone.texOffs(0, 0).addBox(-8, -16, -7, 16, 16, 1, 0, false);
			bone.texOffs(0, 17).addBox(-8, -6, -8, 16, 6, 1, 0, false);

			final ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
			cube_r1.setPos(0, -6, -8);
			bone.addChild(cube_r1);
			cube_r1.setRotationAngle(-0.7854F, 0, 0);
			cube_r1.texOffs(0, 24).addBox(-8, -2, 0, 16, 2, 1, 0, false);

			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			bone.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			bone.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}

	private static class ModelAPGDoorLight extends EntityModel<Entity> {

		private final ModelMapper bone;

		private ModelAPGDoorLight() {
			final int textureWidth = 8;
			final int textureHeight = 8;

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			bone = new ModelMapper(modelDataWrapper);
			bone.texOffs(0, 4).addBox(-0.5F, -9, -7, 1, 1, 3, 0.05F, false);

			final ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
			cube_r1.setPos(0, -9.05F, -4.95F);
			bone.addChild(cube_r1);
			cube_r1.setRotationAngle(0.3927F, 0, 0);
			cube_r1.texOffs(0, 0).addBox(-0.5F, 0.05F, -3.05F, 1, 1, 3, 0.05F, false);

			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			bone.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			bone.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}
}
