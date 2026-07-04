package org.mtr.resource;

import org.mtr.render.QueuedRenderLayer;

public enum RenderStage {
	LIGHT(false, true, QueuedRenderLayer.LIGHT),
	ALWAYS_ON_LIGHT(true, true, QueuedRenderLayer.LIGHT_TRANSLUCENT),
	INTERIOR(false, true, QueuedRenderLayer.INTERIOR),
	INTERIOR_TRANSLUCENT(true, true, QueuedRenderLayer.INTERIOR_TRANSLUCENT),
	EXTERIOR(false, false, QueuedRenderLayer.EXTERIOR);

	public final boolean isTranslucent;
	public final boolean isFullBrightness;
	public final QueuedRenderLayer queuedRenderLayer;

	RenderStage(boolean isTranslucent, boolean isFullBrightness, QueuedRenderLayer queuedRenderLayer) {
		this.isTranslucent = isTranslucent;
		this.isFullBrightness = isFullBrightness;
		this.queuedRenderLayer = queuedRenderLayer;
	}
}
