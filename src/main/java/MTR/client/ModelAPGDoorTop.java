package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAPGDoorTop extends ModelBase {
	// fields
	ModelRenderer Main;

	public ModelAPGDoorTop() {
		textureWidth = 34;
		textureHeight = 9;

		Main = new ModelRenderer(this, 0, 0);
		Main.addBox(0F, 0F, 0F, 16, 8, 1);
		Main.setRotationPoint(-8F, 16F, 6F);
		Main.setTextureSize(34, 9);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Main.render(f5);
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
