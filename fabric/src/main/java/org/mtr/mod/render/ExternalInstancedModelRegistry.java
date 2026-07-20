package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.api.instancing.InstancedModelHandle;
import org.mtr.mod.api.instancing.InstancedModelOptions;
import org.mtr.mod.api.instancing.InstancedModelSource;

public final class ExternalInstancedModelRegistry {

	private static final ObjectArrayList<InstancedModelHandleImpl> HANDLES = new ObjectArrayList<>();

	public static InstancedModelHandle upload(InstancedModelSource source, InstancedModelOptions options) {
		return InstancedModelHandleImpl.upload(source, options);
	}

	static void register(InstancedModelHandleImpl handle) {
		if (!HANDLES.contains(handle)) {
			HANDLES.add(handle);
		}
	}

	static void unregister(InstancedModelHandleImpl handle) {
		HANDLES.remove(handle);
	}

	public static void handleRendererReload() {
		final ObjectArrayList<InstancedModelHandleImpl> handles = new ObjectArrayList<>(HANDLES);
		HANDLES.clear();
		for (final InstancedModelHandleImpl handle : handles) {
			handle.markRendererReloaded();
		}
	}

	private ExternalInstancedModelRegistry() {
	}
}
