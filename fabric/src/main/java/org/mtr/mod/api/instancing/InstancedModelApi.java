package org.mtr.mod.api.instancing;

import org.mtr.mod.config.Config;
import org.mtr.mod.render.GpuObjCompat;
import org.mtr.mod.render.ExternalInstancedModelRegistry;

public final class InstancedModelApi {

	public static boolean isAvailable() {
		return GpuObjCompat.isSupported() && Config.getClient().getEnableGpuObjInstancing();
	}

	public static InstancedModelHandle upload(InstancedModelSource source, InstancedModelOptions options) {
		return ExternalInstancedModelRegistry.upload(source, options == null ? InstancedModelOptions.defaults() : options);
	}

	private InstancedModelApi() {
	}
}
