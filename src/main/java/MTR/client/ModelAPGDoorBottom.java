package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAPGDoorBottom extends ModelBase {
	// fields
	ModelRenderer Main;
	ModelRenderer Bottom;
	ModelRenderer BottomSlanted;

	public ModelAPGDoorBottom() {
		textureWidth = 34;
		textureHeight = 27;

		Main = new ModelRenderer(this, 0, 0);
		Main.addBox(0F, 0F, 0F, 16, 16, 1);
		Main.setRotationPoint(-8F, 8F, 6F);
		Main.setTextureSize(34, 27);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		Bottom = new ModelRenderer(this, 0, 17);
		Bottom.addBox(0F, 0F, 0F, 16, 6, 1);
		Bottom.setRotationPoint(-8F, 18F, 7F);
		Bottom.setTextureSize(34, 27);
		Bottom.mirror = true;
		setRotation(Bottom, 0F, 0F, 0F);
		BottomSlanted = new ModelRenderer(this, 0, 24);
		BottomSlanted.addBox(0F, 0F, -2F, 16, 1, 2);
		BottomSlanted.setRotationPoint(-8F, 18F, 8F);
		BottomSlanted.setTextureSize(34, 27);
		BottomSlanted.mirror = true;
		setRotation(BottomSlanted, -0.7853982F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Main.render(f5);
		Bottom.render(f5);
		BottomSlanted.render(f5);
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
