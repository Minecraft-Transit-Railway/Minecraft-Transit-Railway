package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPSDTop extends ModelBase {
	// fields
	ModelRenderer Main;
	ModelRenderer TopCurve;
	ModelRenderer BottomCurve;
	ModelRenderer LightLeft;
	ModelRenderer LightRight;
	ModelRenderer GlowLeft;
	ModelRenderer GlowRight;
	ModelRenderer Light2;

	ModelRenderer ColorStrip, Arrow, ArrowColor, FrontWarning, Front;

	public ModelPSDTop() {
		textureWidth = 60;
		textureHeight = 32;

		Main = new ModelRenderer(this, 0, 0);
		Main.addBox(0F, 0F, 0F, 16, 16, 6);
		Main.setRotationPoint(-8F, 8F, 2F);
		Main.setTextureSize(60, 32);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		TopCurve = new ModelRenderer(this, 0, 30);
		TopCurve.addBox(0F, 0F, 0F, 16, 1, 1);
		TopCurve.setRotationPoint(-8F, 23F, 2F);
		TopCurve.setTextureSize(60, 32);
		TopCurve.mirror = true;
		setRotation(TopCurve, 0.4455616F, 0F, 0F);
		BottomCurve = new ModelRenderer(this, 0, 30);
		BottomCurve.addBox(0F, 0F, 0F, 16, 1, 1);
		BottomCurve.setRotationPoint(8F, 24F, 3F);
		BottomCurve.setTextureSize(60, 32);
		BottomCurve.mirror = true;
		setRotation(BottomCurve, -1.125231F, 0F, 3.141593F);
		LightLeft = new ModelRenderer(this, 0, 0);
		LightLeft.addBox(0F, 0F, 0F, 2, 1, 1);
		LightLeft.setRotationPoint(-9F, 23F, 2F);
		LightLeft.setTextureSize(60, 32);
		LightLeft.mirror = true;
		setRotation(LightLeft, 0F, 0F, 0F);
		LightRight = new ModelRenderer(this, 0, 3);
		LightRight.addBox(0F, 0F, 0F, 2, 1, 1);
		LightRight.setRotationPoint(7F, 23F, 2F);
		LightRight.setTextureSize(60, 32);
		LightRight.mirror = true;
		setRotation(LightRight, 0F, 0F, 0F);
		GlowLeft = new ModelRenderer(this, 0, 24);
		GlowLeft.addBox(-1.5F, 0.5F, 0.5F, 3, 2, 2);
		GlowLeft.setRotationPoint(-8F, 22F, 1F);
		GlowLeft.setTextureSize(60, 32);
		GlowLeft.mirror = true;
		setRotation(GlowLeft, 0F, 0F, 0F);
		GlowRight = new ModelRenderer(this, 10, 24);
		GlowRight.addBox(-1.5F, 0.5F, 0.5F, 3, 2, 2);
		GlowRight.setRotationPoint(8F, 22F, 1F);
		GlowRight.setTextureSize(60, 32);
		GlowRight.mirror = true;
		setRotation(GlowRight, 0F, 0F, 0F);

		Light2 = new ModelRenderer(this, 38, 0);
		Light2.addBox(0F, 0F, 0F, 2, 1, 1);
		Light2.setRotationPoint(-1F, 23F, 2F);
		Light2.setTextureSize(60, 32);
		Light2.mirror = true;
		setRotation(Light2, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Main.render(f5);
		TopCurve.render(f5);
		BottomCurve.render(f5);
		LightLeft.render(f5);
		LightRight.render(f5);
	}

	public void render2(float f5) {
		GlowLeft.render(f5);
		GlowRight.render(f5);
	}

	public void render2a(float f5) {
		Main.render(f5);
		TopCurve.render(f5);
		BottomCurve.render(f5);
		ColorStrip.render(f5);
	}

	public void render3(float f5) {
		Light2.render(f5);
	}

	public void render4(float f5, int color, int arrow, boolean isdoor, boolean side, boolean warning) {
		ColorStrip = new ModelRenderer(this, 44, color);
		ColorStrip.addBox(0F, 0F, 0F, 16, 1, 0);
		ColorStrip.setRotationPoint(-8F, 22F, 1.99F);
		ColorStrip.setTextureSize(60, 32);
		ColorStrip.mirror = true;
		setRotation(ColorStrip, 0F, 0F, 0F);
		ColorStrip.render(f5);
		if (isdoor) {
			if (side == (arrow == 1) && !warning) {
				Arrow = new ModelRenderer(this, side ? 49 : 44, 20);
				Arrow.addBox(0F, 0F, 0F, 5, 4, 0);
				Arrow.setRotationPoint(side ? -2F : -3F, 17F, 1.99F);
				Arrow.setTextureSize(60, 32);
				Arrow.mirror = true;
				setRotation(Arrow, 0F, 0F, 0F);
				if (color > 0)
					Arrow.render(f5);
			} else {
				if (warning) {
					FrontWarning = new ModelRenderer(this, 44, side ? 28 : 24);
					FrontWarning.addBox(0F, 0F, 0F, 16, 4, 0);
					FrontWarning.setRotationPoint(-8F, 17F, 1.99F);
					FrontWarning.setTextureSize(60, 32);
					FrontWarning.mirror = true;
					setRotation(FrontWarning, 0F, 0F, 0F);
					FrontWarning.render(f5);
				}
			}
		}
	}

	public void render4a(float f5, int color, int number, int arrow, boolean isdoor, boolean side) {
		if (isdoor) {
			if (side == (arrow == 0)) {
				Front = new ModelRenderer(this, side ? 0 : 16, color * 8 + (number % 2 == 0 ? 4 : 0));
				Front.addBox(0F, 0F, 0F, 16, 4, 0);
				Front.setRotationPoint(-8F, 17F, 1.99F);
				Front.setTextureSize(128, 104);
				Front.mirror = true;
				setRotation(Front, 0F, 0F, 0F);
				Front.render(f5);
			} else {
				int a = 64 + 4 * ((number - 1) % 4), b = color * 8;
				if (number > 4)
					b += 4;
				ArrowColor = new ModelRenderer(this, a, b);
				ArrowColor.addBox(0F, 0F, 0F, 4, 4, 0);
				ArrowColor.setRotationPoint(side ? -7.25F : 3.25F, 17F, 1.99F);
				ArrowColor.setTextureSize(128, 104);
				ArrowColor.mirror = true;
				setRotation(ArrowColor, 0F, 0F, 0F);
				ArrowColor.render(f5);
			}
		} else {
			Front = new ModelRenderer(this, side ? 48 : 32, color * 8 + (number % 2 == 0 ^ arrow == 1 ? 4 : 0));
			Front.addBox(0F, 0F, 0F, 16, 4, 0);
			Front.setRotationPoint(-8F, 17F, 1.99F);
			Front.setTextureSize(128, 104);
			Front.mirror = true;
			setRotation(Front, 0F, 0F, 0F);
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
