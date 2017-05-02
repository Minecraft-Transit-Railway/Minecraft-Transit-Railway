package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAPGGlass extends ModelBase {
	// fields
	ModelRenderer Main;
	ModelRenderer Top;
	ModelRenderer TopSlanted;
	ModelRenderer LeftGlow;
	ModelRenderer RightGlow;
	ModelRenderer ColorStrip, PlatformNumber, Arrow, ArrowColor, Front;

	public ModelAPGGlass() {
		textureWidth = 72;
		textureHeight = 40;

		Main = new ModelRenderer(this, 0, 0);
		Main.addBox(0F, 0F, 0F, 16, 24, 2);
		Main.setRotationPoint(-8F, 0F, 4F);
		Main.setTextureSize(72, 40);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		Top = new ModelRenderer(this, 0, 26);
		Top.addBox(0F, 0F, 0F, 16, 1, 4);
		Top.setRotationPoint(-8F, -1F, 4F);
		Top.setTextureSize(72, 40);
		Top.mirror = true;
		setRotation(Top, 0F, 0F, 0F);
		TopSlanted = new ModelRenderer(this, 0, 31);
		TopSlanted.addBox(0F, 0F, 0F, 16, 1, 3);
		TopSlanted.setRotationPoint(-8F, -1F, 5F);
		TopSlanted.setTextureSize(72, 40);
		TopSlanted.mirror = true;
		setRotation(TopSlanted, -0.1745329F, 0F, 0F);
		LeftGlow = new ModelRenderer(this, 36, 0);
		LeftGlow.addBox(-0.3F, -0.5F, -0.5F, 1, 2, 5);
		LeftGlow.setRotationPoint(-8F, -1F, 4F);
		LeftGlow.setTextureSize(72, 40);
		LeftGlow.mirror = true;
		setRotation(LeftGlow, 0F, 0F, 0F);
		RightGlow = new ModelRenderer(this, 36, 10);
		RightGlow.addBox(-0.7F, -0.5F, -0.5F, 1, 2, 5);
		RightGlow.setRotationPoint(8F, -1F, 4F);
		RightGlow.setTextureSize(72, 40);
		RightGlow.mirror = true;
		setRotation(RightGlow, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Main.render(f5);
		Top.render(f5);
		TopSlanted.render(f5);
	}

	public void renderGlow(float f5) {
		textureWidth = 72;
		textureHeight = 36;
		LeftGlow.render(f5);
		RightGlow.render(f5);
	}

	public void renderFront(float f5, int number, boolean arrow, boolean side, boolean warning) {
		textureWidth = 32;
		textureHeight = 32;

		ColorStrip = new ModelRenderer(this, 6, 28);
		ColorStrip.addBox(0F, 0F, 0F, 10, 1, 0);
		ColorStrip.setRotationPoint(side ? -8F : -2F, 2.5F, 4.01F);
		ColorStrip.setTextureSize(32, 32);
		ColorStrip.mirror = true;
		ColorStrip.render(f5);
		ColorStrip.setRotationPoint(side ? -8F : -2F, 3.5F, 5.99F);
		setRotation(ColorStrip, (float) Math.PI, 0F, 0F);
		ColorStrip.render(f5);

		textureWidth = 16;
		textureHeight = 16;

		if (warning) {
			if (side == !arrow) {
				Front = new ModelRenderer(this, side ? 0 : 8, number % 2 == 0 ? 10 : 8);
				Front.addBox(0F, 0.5F, 0F, 8, 2, 0);
				Front.setRotationPoint(side ? -8F : 0F, 0F, 3.99F);
				Front.setTextureSize(16, 16);
				Front.mirror = true;
				Front.render(f5);
			} else {
				Arrow = new ModelRenderer(this, side ? 2 : 0, 12);
				Arrow.addBox(0.5F, 0.5F, 0F, 2, 2, 0);
				Arrow.setRotationPoint(side ? -5F : 2F, 0F, 3.99F);
				Arrow.setTextureSize(16, 16);
				Arrow.mirror = true;
				Arrow.render(f5);
				ArrowColor = new ModelRenderer(this, number * 2 - 2, 6);
				ArrowColor.addBox(0.5F, 0.5F, 0F, 2, 2, 0);
				ArrowColor.setRotationPoint(side ? -8F : 5F, 0F, 3.99F);
				ArrowColor.setTextureSize(16, 16);
				ArrowColor.mirror = true;
				ArrowColor.render(f5);
			}
		} else {
			Front = new ModelRenderer(this, side ? 8 : 0, number % 2 == 0 ^ arrow ? 3 : 0);
			Front.addBox(0F, 0F, 0F, 8, 3, 0);
			Front.setRotationPoint(side ? -8F : 0F, 0F, 3.99F);
			Front.setTextureSize(16, 16);
			Front.mirror = true;
			Front.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
