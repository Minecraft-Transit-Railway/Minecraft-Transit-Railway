package mapper;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;

public class ModelDataWrapper {
	public final Model model;

	public ModelPart modelPart;

	public ModelDataWrapper(Model model, int textureWidth, int textureHeight) {
		this.model = model;
		model.texWidth = textureWidth;
		model.texHeight = textureHeight;
	}

	public void setModelPart(int textureWidth, int textureHeight) {
	}
}
