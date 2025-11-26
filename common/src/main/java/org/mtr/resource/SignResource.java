package org.mtr.resource;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.generated.resource.SignResourceSchema;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

public final class SignResource extends SignResourceSchema {

	public final Identifier textureId;
	public final boolean hasCustomText;
	/**
	 * Default signs (the ones bundled with Minecraft Transit Railway) have IDs starting with {@code !}
	 */
	public final boolean isDefault;
	public final String signId;

	private static final float SMALL_SIGN_PADDING = 0.125F;

	public SignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		textureId = CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
		hasCustomText = !customText.isEmpty() || signType != SignType.NORMAL;
		isDefault = id.startsWith("!");
		signId = isDefault ? id.substring(1) : id;
	}

	public boolean getFlipTexture() {
		return flipTexture;
	}

	public SignType getSignType() {
		return signType;
	}

	public String getCustomText() {
		return customText.isEmpty() ? "" : Text.translatable(customText).getString();
	}

	public boolean getFlipCustomText() {
		return flipCustomText;
	}

	public boolean getSmall() {
		return small;
	}

	public int getBackgroundColor() {
		return CustomResourceTools.colorStringToInt(backgroundColor);
	}

	public static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, LongAVLTreeSet[] selectedIds, String[] signIds, float signSize, float zOffset) {
		final SignResource[] signResources = new SignResource[signIds.length];
		for (int i = 0; i < signIds.length; i++) {
			signResources[i] = CustomResourceLoader.getSignById(signIds[i]);
		}
		render(matrixStack, vertexConsumerProvider, 0, 0, selectedIds, signResources, signSize, zOffset);
	}

	public static void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float x, float y, LongAVLTreeSet[] selectedIds, SignResource[] signResources, float signSize, float zOffset) {
		final float signPadding = SMALL_SIGN_PADDING * signSize;
		boolean renderBackground = false;
		int backgroundColor = 0;

		// Get sign resources from sign IDs and find the background colour
		for (final SignResource signResource : signResources) {
			if (signResource != null) {
				renderBackground = true;
				backgroundColor = signResource.getBackgroundColor();
				break;
			}
		}

		// Draw background
		if (renderBackground) {
			new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGui())).setVerticesWH(x, y, signResources.length * signSize, signSize).setColor(GuiHelper.BLACK_COLOR | backgroundColor).draw();
		}

		// Draw sign tiles and text
		for (int i = 0; i < signResources.length; i++) {
			final SignResource signResource = signResources[i];

			if (signResource != null) {
				final LongAVLTreeSet selectedIdsSet = selectedIds[i];

				new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGuiTextured(signResource.textureId))).setVertices(
						x + i * signSize + (signResource.small ? signPadding : 0),
						y + (signResource.small ? signPadding : 0),
						x + (i + 1) * signSize - (signResource.small ? signPadding : 0),
						y + signSize - (signResource.small ? signPadding : 0),
						-zOffset
				).setUv(signResource.flipTexture ? 1 : 0, 0, signResource.flipTexture ? 0 : 1, 1).draw();

				if (signResource.hasCustomText) {
					final float textSpace = (getTextSpace(signResources, i) - (signResource.small ? 1 : 2) * SMALL_SIGN_PADDING) * signSize;
					if (textSpace > 0) {
						final Drawing drawing = new Drawing(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getGui()));
						FontGroupRegistry.MTR.get().render(drawing, signResource.getCustomText(), FontRenderOptions.builder()
								.horizontalSpace(textSpace)
								.verticalSpace(signSize * (1 - SMALL_SIGN_PADDING * 2))
								.horizontalTextAlignment(signResource.flipCustomText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
								.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
								.horizontalPositioning(signResource.flipCustomText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
								.offsetX(x + (i + (signResource.flipCustomText ? 0 : 1) + (signResource.small ? 0 : (signResource.flipCustomText ? -1 : 1) * SMALL_SIGN_PADDING)) * signSize)
								.offsetY(y + signSize * SMALL_SIGN_PADDING)
								.offsetZ(-zOffset)
								.cjkScaling(2)
								.maxFontSize(signSize / 4)
								.lineBreak(FontRenderOptions.LineBreak.SPLIT)
								.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
								.build());
					}
				}
			}
		}
	}

	private static float getTextSpace(SignResource[] signResources, int index) {
		final SignResource signResource = signResources[index];
		final int direction = signResource.flipCustomText ? -1 : 1;
		int checkIndex = index + direction;
		float space = 0;

		while (checkIndex >= 0 && checkIndex < signResources.length) {
			final SignResource checkSignResource = signResources[checkIndex];
			if (checkSignResource == null) {
				space++;
			} else {
				if (checkSignResource.small) {
					space += SMALL_SIGN_PADDING;
				}
				break;
			}
			checkIndex += direction;
		}

		return space;
	}
}
