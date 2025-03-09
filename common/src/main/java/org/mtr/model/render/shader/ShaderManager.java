package org.mtr.model.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.util.Window;
import org.mtr.MTR;
import org.mtr.model.OptimizedModel;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.tool.Utilities;

public final class ShaderManager {

	private final Object2ObjectOpenHashMap<String, ShaderProgram> shaders = new Object2ObjectOpenHashMap<>();

	private static final VertexFormatElement MINECRAFT_ELEMENT_MATRIX = new VertexFormatElement(0, 0, VertexFormatElement.ComponentType.FLOAT, VertexFormatElement.Usage.GENERIC, 16);
//	private static final VertexFormat MINECRAFT_VERTEX_FORMAT_BLOCK = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
//			.put("Position", VertexFormats.POSITION)
//			.put("Color", VertexFormats.COLOR_ELEMENT)
//			.put("UV0", VertexFormats.UV_ELEMENT)
//			.put("UV1", VertexFormats.OVERLAY_ELEMENT)
//			.put("UV2", VertexFormats.LIGHT_ELEMENT)
//			.put("Normal", VertexFormats.NORMAL_ELEMENT)
//			.put("ModelMat", MINECRAFT_ELEMENT_MATRIX)
//			.put("Padding", VertexFormats.PADDING_ELEMENT)
//			.build());

	public boolean isReady() {
		return !this.shaders.isEmpty();
	}

	public void reloadShaders() {
		shaders.values().forEach(ShaderProgram::close);
		shaders.clear();
		final PatchingResourceProvider patchingResourceProvider = new PatchingResourceProvider(MinecraftClient.getInstance().getResourceManager());
		loadShader(patchingResourceProvider, getShaderName(OptimizedModel.ShaderType.CUTOUT));
		loadShader(patchingResourceProvider, getShaderName(OptimizedModel.ShaderType.TRANSLUCENT));
		loadShader(patchingResourceProvider, getShaderName(OptimizedModel.ShaderType.CUTOUT_GLOWING));
	}

	private void loadShader(PatchingResourceProvider resourceManager, String shaderName) {
		try {
//			shaders.put(shaderName, ShaderProgram.create(resourceManager, shaderName, MINECRAFT_VERTEX_FORMAT_BLOCK));
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public void setupShaderBatchState(MaterialProperties materialProperties) {
		final ShaderProgram shaderProgram;
		if (Utilities.canUseCustomShader()) {
			shaderProgram = shaders.get(getShaderName(materialProperties.shaderType));
			materialProperties.setupCompositeState();
		} else {
			materialProperties.getBlazeRenderType().startDrawing();
			shaderProgram = RenderSystem.getShader();
		}

		if (shaderProgram == null) {
			throw new IllegalArgumentException("Cannot get shader: " + materialProperties.shaderType);
		}

//		for (int i = 0; i < 8; i++) {
//			shaderProgram.addSampler("Sampler" + i, RenderSystem.getShaderTexture(i));
//		}
		if (shaderProgram.modelViewMat != null) {
			shaderProgram.modelViewMat.set(Utilities.copy(RenderSystem.getModelViewMatrix()));
		}
		if (shaderProgram.projectionMat != null) {
			shaderProgram.projectionMat.set(RenderSystem.getProjectionMatrix());
		}
		if (shaderProgram.colorModulator != null) {
			shaderProgram.colorModulator.set(RenderSystem.getShaderColor());
		}
		if (shaderProgram.textureMat != null) {
			shaderProgram.textureMat.set(RenderSystem.getTextureMatrix());
		}
		if (shaderProgram.gameTime != null) {
			shaderProgram.gameTime.set(RenderSystem.getShaderGameTime());
		}
		if (shaderProgram.screenSize != null) {
			final Window window = MinecraftClient.getInstance().getWindow();
			shaderProgram.screenSize.set(window.getWidth(), window.getHeight());
		}

		RenderSystem.setupShaderLights(shaderProgram);
		shaderProgram.bind();
	}

	public void cleanupShaderBatchState() {
		if (!Utilities.canUseCustomShader()) {
			final ShaderProgram shaderProgram = RenderSystem.getShader();
			if (shaderProgram != null && shaderProgram.modelViewMat != null) {
				// ModelViewMatrix might have got set in VertexAttributeState, reset it
				shaderProgram.modelViewMat.set(RenderSystem.getModelViewMatrix());
				shaderProgram.bind();
			}
		}
	}

	private static String getShaderName(OptimizedModel.ShaderType shaderType) {
		return switch (shaderType) {
			case TRANSLUCENT, TRANSLUCENT_BRIGHT -> "rendertype_entity_translucent_cull";
			case CUTOUT_GLOWING, TRANSLUCENT_GLOWING -> "rendertype_beacon_beam";
			default -> "rendertype_entity_cutout";
		};
	}
}
