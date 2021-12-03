package mapper;

import net.minecraft.client.model.*;

public class ModelDataWrapper {

	public final ModelData modelData;
	public final ModelPartData modelPartData;
	public ModelPart modelPart;

	public ModelDataWrapper(Model model, int textureWidth, int textureHeight) {
		modelData = new ModelData();
		modelPartData = modelData.getRoot();
	}

	public void setModelPart(int textureWidth, int textureHeight) {
		modelPart = TexturedModelData.of(modelData, textureWidth, textureHeight).createModel();
	}
}
