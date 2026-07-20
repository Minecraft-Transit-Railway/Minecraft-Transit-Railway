package org.mtr.mod.api.instancing;

public final class InstancedModelOptions {

	public boolean allowTranslucent = false;
	public boolean allowPerInstanceColor = true;
	public boolean allowPerInstanceLight = true;
	public boolean immutableGeometry = true;
	public String debugName = "";
	public InstancingUploadMode uploadMode = InstancingUploadMode.AUTO;

	public static InstancedModelOptions defaults() {
		return new InstancedModelOptions();
	}
}
