package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMTRSign extends ModelBase {
	// fields
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Pole;
	ModelRenderer Pole2;
	ModelRenderer Main;
	ModelRenderer Outer;

	public ModelMTRSign() {
		textureWidth = 64;
		textureHeight = 32;

		Shape1 = new ModelRenderer(this, 0, 0);
		Shape1.addBox(0F, 0F, 0F, 1, 16, 1);
		Shape1.setRotationPoint(-8F, 8F, 0.5F);
		Shape1.setTextureSize(64, 32);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 1.047198F, 0F);
		Shape2 = new ModelRenderer(this, 0, 0);
		Shape2.addBox(0F, 0F, 0F, 1, 16, 1);
		Shape2.setRotationPoint(-8F, 8F, -0.5F);
		Shape2.setTextureSize(64, 32);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0.5235988F, 0F);
		Pole = new ModelRenderer(this, 7, 0);
		Pole.addBox(0F, 0F, 0F, 2, 16, 2);
		Pole.setRotationPoint(-8F, 8F, -1F);
		Pole.setTextureSize(64, 32);
		Pole.mirror = true;
		setRotation(Pole, 0F, 0F, 0F);
		Pole2 = new ModelRenderer(this, 0, 0);
		Pole2.addBox(0F, 0F, 0F, 1, 16, 2);
		Pole2.setRotationPoint(-7.15F, 8F, -1F);
		Pole2.setTextureSize(64, 32);
		Pole2.mirror = true;
		setRotation(Pole2, 0F, 0F, 0F);
		Main = new ModelRenderer(this, 0, 19);
		Main.addBox(0F, 0F, 0F, 20, 10, 2);
		Main.setRotationPoint(-5F, 13.5F, -1F);
		Main.setTextureSize(64, 32);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		Outer = new ModelRenderer(this, 16, 0);
		Outer.addBox(0F, 0F, 0F, 22, 14, 2);
		Outer.setRotationPoint(-6.5F, 10F, -1F);
		Outer.setTextureSize(64, 32);
		Outer.mirror = true;
		setRotation(Outer, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape1.render(f5);
		Shape2.render(f5);
		Pole.render(f5);
		Pole2.render(f5);
		Main.render(f5);
		Outer.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
