package MTR.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTicketMachineTop extends ModelBase {
	// fields
	ModelRenderer LeftSide;
	ModelRenderer RightSide;
	ModelRenderer Top;
	ModelRenderer Map;
	ModelRenderer Front;
	ModelRenderer Light;
	ModelRenderer MoneyInput;
	ModelRenderer MainMachine;

	public ModelTicketMachineTop() {
		textureWidth = 64;
		textureHeight = 64;

		LeftSide = new ModelRenderer(this, 0, 27);
		LeftSide.addBox(0F, 0F, 0F, 1, 13, 6);
		LeftSide.setRotationPoint(-8F, 11F, -6F);
		LeftSide.setTextureSize(64, 128);
		LeftSide.mirror = true;
		setRotation(LeftSide, 0F, 0F, 0F);
		RightSide = new ModelRenderer(this, 0, 27);
		RightSide.addBox(0F, 0F, 0F, 1, 13, 6);
		RightSide.setRotationPoint(7F, 11F, -6F);
		RightSide.setTextureSize(64, 128);
		RightSide.mirror = true;
		setRotation(RightSide, 0F, 0F, 0F);
		Top = new ModelRenderer(this, 0, 53);
		Top.addBox(0F, 0F, 0F, 16, 1, 7);
		Top.setRotationPoint(-8F, 10F, 0F);
		Top.setTextureSize(64, 128);
		Top.mirror = true;
		setRotation(Top, -0.5235988F, 0F, 0F);
		Map = new ModelRenderer(this, 28, 27);
		Map.addBox(0F, 0F, 0F, 14, 6, 0);
		Map.setRotationPoint(-7F, 19F, -3F);
		Map.setTextureSize(64, 128);
		Map.mirror = true;
		setRotation(Map, -0.5235988F, 0F, 0F);
		Front = new ModelRenderer(this, 28, 34);
		Front.addBox(0F, 0F, 0F, 14, 5, 0);
		Front.setRotationPoint(-7F, 14F, -3F);
		Front.setTextureSize(64, 128);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		Light = new ModelRenderer(this, 28, 40);
		Light.addBox(0F, 0F, 0F, 14, 4, 0);
		Light.setRotationPoint(-7F, 13F, -6F);
		Light.setTextureSize(64, 128);
		Light.mirror = true;
		setRotation(Light, 1.134464F, 0F, 0F);
		MoneyInput = new ModelRenderer(this, 0, 0);
		MoneyInput.addBox(0F, 0F, 0F, 2, 2, 1);
		MoneyInput.setRotationPoint(4F, 16F, -4F);
		MoneyInput.setTextureSize(64, 128);
		MoneyInput.mirror = true;
		setRotation(MoneyInput, 0F, 0F, 0F);
		MainMachine = new ModelRenderer(this, 0, 0);
		MainMachine.addBox(0F, 0F, 0F, 16, 14, 12);
		MainMachine.setRotationPoint(-8F, 10F, -6F);
		MainMachine.setTextureSize(64, 128);
		MainMachine.mirror = true;
		setRotation(MainMachine, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		LeftSide.render(f5);
		RightSide.render(f5);
		Top.render(f5);
		Map.render(f5);
		Front.render(f5);
		Light.render(f5);
		MoneyInput.render(f5);
		MainMachine.render(f5);
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
