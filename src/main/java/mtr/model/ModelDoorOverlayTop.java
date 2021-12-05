package mtr.model;

import minecraftmappings.ModelDataWrapper;
import minecraftmappings.ModelMapper;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ModelDoorOverlayTop extends EntityModel<Entity> {

	private final ModelMapper bb_main;
	private final ModelMapper outer_roof_2_r1;
	private final ModelMapper outer_roof_1_r1;

	private static final Identifier TEXTURE_ID = new Identifier("mtr:textures/sign/door_overlay_sp1900_top.png");

	public ModelDoorOverlayTop() {
		final int textureWidth = 24;
		final int textureHeight = 3;

		final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

		bb_main = new ModelMapper(modelDataWrapper);
		bb_main.setPivot(0, 24, 0);


		outer_roof_2_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_2_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_2_r1);
		ModelTrainBase.setRotationAngle(outer_roof_2_r1, 0, 3.1416F, 0.1107F);
		outer_roof_2_r1.setTextureOffset(0, -12).addCuboid(1.1F, -21, 0, 0, 3, 12, 0, false);

		outer_roof_1_r1 = new ModelMapper(modelDataWrapper);
		outer_roof_1_r1.setPivot(-20, -14, 0);
		bb_main.addChild(outer_roof_1_r1);
		ModelTrainBase.setRotationAngle(outer_roof_1_r1, 0, 0, 0.1107F);
		outer_roof_1_r1.setTextureOffset(0, -12).addCuboid(-1.1F, -21, 0, 0, 3, 12, 0, false);

		modelDataWrapper.setModelPart(textureWidth, textureHeight);
		bb_main.setModelPart();
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