package org.mtr.model.render.batch;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.opengl.GL33;
import org.mtr.model.OptimizedModel;
import org.mtr.model.render.vertex.VertexAttributeState;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Properties regarding material. Set during model loading. Affects batching.
 */
public final class MaterialProperties {

	/**
	 * The texture to use. Null disables texture.
	 */
	private Identifier texture;
	/**
	 * Name of the shader program. Must be loaded in ShaderManager.
	 */
	public final OptimizedModel.ShaderType shaderType;
	/**
	 * The vertex attribute values to use for those specified with VertAttrSrc MATERIAL.
	 */
	public final VertexAttributeState vertexAttributeState;
	/**
	 * If blending should be set up. True for entity_translucent_* and beacon_beam when translucent is true.
	 */
	public final boolean translucent;
	/**
	 * If depth buffer should be written to. False for beacon_beam when translucent is true, true for everything else.
	 */
	public final boolean writeDepthBuf;
	public final boolean cutoutHack;

	private static final Function<Identifier, RenderLayer> ENTITY_TRANSLUCENT_CULL = Util.memoize((texture) -> createTriangles(
			"entity_translucent_cull_triangles",
			true,
			true,
			RenderLayer.getEntityTranslucent(texture)
	));
	private static final BiFunction<Identifier, Boolean, RenderLayer> BEACON_BEAM = Util.memoize((texture, translucent) -> createTriangles(
			"beacon_beam_triangles",
			false,
			translucent,
			RenderLayer.getBeaconBeam(texture, translucent)
	));
	private static final Function<Identifier, RenderLayer> ENTITY_CUTOUT = Util.memoize((texture) -> createTriangles(
			"entity_cutout_triangles",
			true,
			false,
			RenderLayer.getEntityCutout(texture)
	));

	public MaterialProperties(OptimizedModel.ShaderType shaderType, Identifier texture, @Nullable Integer color) {
		this.shaderType = shaderType;
		this.texture = texture;
		switch (shaderType) {
			case TRANSLUCENT:
				translucent = true;
				writeDepthBuf = true;
				cutoutHack = false;
				vertexAttributeState = new VertexAttributeState(color, null);
				break;
			case CUTOUT_BRIGHT:
				translucent = false;
				writeDepthBuf = true;
				cutoutHack = false;
				vertexAttributeState = new VertexAttributeState(color, 15 << 4 | 15 << 20);
				break;
			case TRANSLUCENT_BRIGHT:
				translucent = true;
				writeDepthBuf = true;
				cutoutHack = false;
				vertexAttributeState = new VertexAttributeState(color, 15 << 4 | 15 << 20);
				break;
			case CUTOUT_GLOWING:
				translucent = false;
				writeDepthBuf = true;
				cutoutHack = true;
				vertexAttributeState = new VertexAttributeState(color, null);
				break;
			case TRANSLUCENT_GLOWING:
				translucent = true;
				writeDepthBuf = false;
				cutoutHack = false;
				vertexAttributeState = new VertexAttributeState(color, null);
				break;
			default:
				translucent = false;
				writeDepthBuf = true;
				cutoutHack = false;
				vertexAttributeState = new VertexAttributeState(color, null);
				break;
		}
	}

	public void setupCompositeState() {
		RenderSystem.setShaderTexture(0, texture);

		// HACK: To make cutout transparency on beacon_beam work
		if (translucent || cutoutHack) {
			RenderSystem.enableBlend(); // TransparentState
			RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		} else {
			RenderSystem.disableBlend();
		}

		RenderSystem.enableDepthTest(); // DepthTestState
		RenderSystem.depthFunc(GL33.GL_LEQUAL);
		RenderSystem.enableCull();
		MinecraftClient.getInstance().gameRenderer.getLightmapTextureManager().enable(); // LightmapState
		MinecraftClient.getInstance().gameRenderer.getOverlayTexture().setupOverlayColor(); // OverlayState
		RenderSystem.depthMask(writeDepthBuf); // WriteMaskState
	}

	public RenderLayer getBlazeRenderType() {
		return switch (shaderType) {
			case TRANSLUCENT, TRANSLUCENT_BRIGHT -> ENTITY_TRANSLUCENT_CULL.apply(texture);
			case CUTOUT_GLOWING, TRANSLUCENT_GLOWING -> BEACON_BEAM.apply(texture, translucent);
			default -> ENTITY_CUTOUT.apply(texture);
		};
	}

	public Identifier getTexture() {
		return texture;
	}

	public void setTexture(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof MaterialProperties materialProperties)) {
			return false;
		}
		return shaderType == materialProperties.shaderType &&
				Objects.equals(texture, materialProperties.texture) &&
				Objects.equals(vertexAttributeState, materialProperties.vertexAttributeState) &&
				translucent == materialProperties.translucent &&
				writeDepthBuf == materialProperties.writeDepthBuf &&
				cutoutHack == materialProperties.cutoutHack;
	}

	@Override
	public int hashCode() {
		return Objects.hash(shaderType, texture, vertexAttributeState, translucent, writeDepthBuf, cutoutHack);
	}

	private static RenderLayer createTriangles(String name, boolean hasCrumbling, boolean translucent, RenderLayer renderLayerForPhase) {
//		return RenderLayer.of(
//				name,
//				VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
//				VertexFormat.DrawMode.TRIANGLES,
//				256,
//				hasCrumbling,
//				translucent,
//				((RenderLayer.MultiPhase) renderLayerForPhase).phases
//		);
		return RenderLayer.getDebugQuads();
	}
}
