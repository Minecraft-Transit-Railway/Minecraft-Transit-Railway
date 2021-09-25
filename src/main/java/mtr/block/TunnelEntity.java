// Made with Blockbench 3.9.3
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports

	package mtr.block;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class TunnelEntity extends EntityModel<Entity> {
private final ModelPart bb_main;
public TunnelEntity() {
		textureWidth = 16;
		textureHeight = 16;
		bb_main = new ModelPart(this);
		bb_main.setPivot(0.0F, 24.0F, 0.0F);
	bb_main.setTextureOffset(0, 0).addCuboid(0.0F, -24.0F, 0.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
}
@Override
public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
}
@Override
public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
}
public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
}

	}