package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
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
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.FACING);
		final boolean side = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
		final boolean half = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
		final boolean end = IBlock.getStatePropertySafe(world, pos, BlockPSDAPGDoorBase.END);
		final int open = entity.getOpen();
		final ResourceLocation lightLocation = new ResourceLocation(String.format("mtr:textures/block/light_%s.png", open > 0 ? "on" : "off"));

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);
		matrices.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
		matrices.mulPose(Vector3f.XP.rotationDegrees(180));

		switch (type) {
			case 0:
			case 1:
				if (half) {
					final VertexConsumer vertexConsumerLight = vertexConsumers.getBuffer(open > 0 ? MoreRenderLayers.getLight(lightLocation, false) : MoreRenderLayers.getExterior(lightLocation));
					(side ? MODEL_PSD_LIGHT_RIGHT : MODEL_PSD_LIGHT_LEFT).renderToBuffer(matrices, vertexConsumerLight, light, overlay, 1, 1, 1, 1);
				}
				if (end) {
					matrices.pushPose();
					matrices.translate(open / 64F * (side ? -1 : 1), 0, 0);
					final VertexConsumer vertexConsumerPSDDoor = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation(String.format("mtr:textures/block/psd_door_end_%s_%s_2_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1"))));
					(side ? MODEL_PSD_END_RIGHT_2 : MODEL_PSD_END_LEFT_2).renderToBuffer(matrices, vertexConsumerPSDDoor, light, overlay, 1, 1, 1, 1);
					matrices.popPose();
				}
				break;
			case 2:
				break;
		}

		matrices.translate(open / 32F * (side ? -1 : 1), 0, 0);

		switch (type) {
			case 0:
			case 1:
				if (end) {
					final VertexConsumer vertexConsumerPSDDoor = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation(String.format("mtr:textures/block/psd_door_end_%s_%s_1_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1"))));
					(side ? MODEL_PSD_END_RIGHT_1 : MODEL_PSD_END_LEFT_1).renderToBuffer(matrices, vertexConsumerPSDDoor, light, overlay, 1, 1, 1, 1);
				} else {
					final VertexConsumer vertexConsumerPSDDoor = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation(String.format("mtr:textures/block/psd_door_%s_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left", type == 1 ? "2" : "1"))));
					MODEL_PSD.renderToBuffer(matrices, vertexConsumerPSDDoor, light, overlay, 1, 1, 1, 1);
				}
				break;
			case 2:
				final VertexConsumer vertexConsumerAPGDoor = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation(String.format("mtr:textures/block/apg_door_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left"))));
				(half ? MODEL_APG_TOP : MODEL_APG_BOTTOM).renderToBuffer(matrices, vertexConsumerAPGDoor, light, overlay, 1, 1, 1, 1);
				break;
		}

		matrices.popPose();
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
}
