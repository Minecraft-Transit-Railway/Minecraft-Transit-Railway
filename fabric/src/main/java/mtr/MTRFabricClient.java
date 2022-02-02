package mtr;

import mtr.client.CustomResources;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class MTRFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MTRClient.init();
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CustomResourcesWrapper());
	}

	private static class CustomResourcesWrapper implements SimpleSynchronousResourceReloadListener {

		@Override
		public ResourceLocation getFabricId() {
			return new ResourceLocation(MTR.MOD_ID, CustomResources.CUSTOM_RESOURCES_ID);
		}

		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			CustomResources.reload(resourceManager);
		}
	}
}
