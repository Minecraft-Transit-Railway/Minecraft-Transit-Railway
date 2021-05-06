package mtr.model;

import mtr.render.MoreRenderLayers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ModelDoorOverlayTop extends EntityModel<Entity> {

	private final ModelPart bb_main;
	private final ModelPart outer_roof_2_r1;
	private final ModelPart outer_roof_1_r1;

	private static final Identifier TEXTURE_ID = new Identifier("mtr:textures/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTop() {
		textureWidth = 24;
		textureHeight = 3;
		bb_main = new ModelPart(this);
		bb_main.setPivot(0.0F, 24.0F, 0.0F);


		outer_roof_2_r1 = new ModelPart(this);
		outer_roof_2_r1.setPivot(-20.0F, -14.0F, 0.0F);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0.0F, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(1.1F, -21.0F, 0.0F, 0.0F, 3.0F, 12.0F, 0.0F, false);

		outer_roof_1_r1 = new ModelPart(this);
		outer_roof_1_r1.setPivot(-20.0F, -14.0F, 0.0F);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0.0F, 0.0F, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-1.1F, -21.0F, 0.0F, 0.0F, 3.0F, 12.0F, 0.0F, false);
	}

	@Override
	public final void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	}

	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int position) {
		ModelTrainBase.renderMirror(bb_main, matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(TEXTURE_ID)), light / 4 * 3, position);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
}