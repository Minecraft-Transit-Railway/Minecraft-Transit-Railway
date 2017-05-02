package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelClock extends ModelBase {
	// fields
	ModelRenderer MinuteHand1;
	ModelRenderer MinuteHand2;
	ModelRenderer HourHand1;
	ModelRenderer HourHand2;

	public long time;

	public ModelClock() {
		textureWidth = 8;
		textureHeight = 8;

		MinuteHand1 = new ModelRenderer(this, 0, 0);
		MinuteHand1.addBox(-4F, -4F, 0F, 8, 8, 0);
		MinuteHand1.setRotationPoint(0F, 19F, -1.6F);
		MinuteHand1.setTextureSize(8, 8);
		MinuteHand1.mirror = true;
		setRotation(MinuteHand1, 0F, 0F, 0F);
		MinuteHand2 = new ModelRenderer(this, 0, 0);
		MinuteHand2.addBox(-4F, -4F, 0F, 8, 8, 0);
		MinuteHand2.setRotationPoint(0F, 19F, 1.6F);
		MinuteHand2.setTextureSize(8, 8);
		MinuteHand2.mirror = true;
		setRotation(MinuteHand2, 0F, (float) Math.PI, 0F);
		HourHand1 = new ModelRenderer(this, 1, 1);
		HourHand1.addBox(-3F, -3F, 0F, 6, 6, 0);
		HourHand1.setRotationPoint(0F, 19F, -1.6F);
		HourHand1.setTextureSize(8, 8);
		HourHand1.mirror = true;
		setRotation(HourHand1, 0F, 0F, 0F);
		HourHand2 = new ModelRenderer(this, 1, 1);
		HourHand2.addBox(-3F, -3F, 0F, 6, 6, 0);
		HourHand2.setRotationPoint(0F, 19F, 1.6F);
		HourHand2.setTextureSize(8, 8);
		HourHand2.mirror = true;
		setRotation(HourHand2, 0F, (float) Math.PI, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		MinuteHand1.render(f5);
		MinuteHand2.render(f5);
		HourHand1.render(f5);
		HourHand2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		float minute = (float) (time * Math.PI / 500);
		float hour = (float) (time * Math.PI / 6000 - Math.PI);
		MinuteHand1.rotateAngleZ = minute;
		MinuteHand2.rotateAngleZ = -minute;
		HourHand1.rotateAngleZ = hour;
		HourHand2.rotateAngleZ = -hour;
	}
}
