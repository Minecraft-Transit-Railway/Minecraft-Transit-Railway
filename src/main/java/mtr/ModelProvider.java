package mtr;

import mtr.model.APGDoorModel;
import mtr.model.PSDDoorModel;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class ModelProvider implements ModelResourceProvider {

	private static final Identifier PSD_DOOR_MODEL = new Identifier("mtr:block/psd_door");
	private static final Identifier APG_DOOR_MODEL = new Identifier("mtr:block/apg_door");

	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
		if (identifier.equals(PSD_DOOR_MODEL)) {
			return new PSDDoorModel();
		} else if (identifier.equals(APG_DOOR_MODEL)) {
			return new APGDoorModel();
		} else {
			return null;
		}
	}
}
