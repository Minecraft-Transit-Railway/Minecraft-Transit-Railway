package mtr.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelDoorOverlayTop extends EntityModel<Entity> {

	private final ModelMapper bb_main;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;

	private static final ResourceLocation TEXTURE_ID = new ResourceLocation("mtr:textures/block/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTop() {
		final int textureWidth = 24;
		final int textureHeight = 3;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		bb_main = new ModelMapper(modelDataWrapper);
		bb_main.setPos(0, 24, 0);


		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPos(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.texOffs(0, -12).addBox(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPos(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.texOffs(0, -12).addBox(-1.1F, -21, 0, 0, 3, 12, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		bb_main.setModelPart();
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public final void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, int position) {
		ModelTrainBase.renderMirror(bb_main, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light / 4 * 3, position);
	}
}