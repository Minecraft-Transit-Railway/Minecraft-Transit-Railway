package org.mtr.model;

import net.minecraft.client.util.math.MatrixStack;
import org.mtr.model.render.batch.BatchManager;
import org.mtr.model.render.shader.ModShaderHandler;
import org.mtr.model.render.shader.ShaderManager;
import org.mtr.model.render.tool.GlStateTracker;
import org.mtr.model.render.tool.Utilities;
import org.mtr.model.render.vertex.VertexAttributeState;

public final class OptimizedRenderer {

	private final BatchManager batchManager = new BatchManager();
	private final ShaderManager shaderManager = new ShaderManager();

	public void beginReload() {
		shaderManager.reloadShaders();
		GlStateTracker.capture();
	}

	public void finishReload() {
		GlStateTracker.restore();
	}

	public void queue(OptimizedModel optimizedModel, MatrixStack matrixStack, int color, int light) {
		batchManager.queue(optimizedModel.uploadedParts, new VertexAttributeState(color, light, Utilities.copy(matrixStack.peek().getPositionMatrix())));
	}

	public void render(boolean renderTranslucent) {
		if (shaderManager.isReady()) {
			GlStateTracker.capture();
			batchManager.drawAll(shaderManager, renderTranslucent);
			GlStateTracker.restore();
		}
	}

	public static boolean renderingShadows() {
		return ModShaderHandler.renderingShadows();
	}

	/**
	 * @return {@code true} for 1.17+, {@code false} otherwise
	 */
	public static boolean hasOptimizedRendering() {
		return true;
	}
}
