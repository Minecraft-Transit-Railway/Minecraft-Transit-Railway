package org.mtr.widget;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.universal.UMatrixStack;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import org.mtr.registry.UConverters;
import org.mtr.resource.SignResource;
import org.mtr.tool.ReleasedDynamicTextureRegistry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;

public final class SignButtonsComponent extends UIComponent {

	@Setter
	private int editingIndex;
	@Nullable
	private SignResource hoveringSignResource;

	private final ObjectArrayList<SignResource> signResources;
	private final ObjectArrayList<SignResource> filteredSignResources = new ObjectArrayList<>();
	private final int columns;
	private final int signWidthUnits;
	private final LongAVLTreeSet[] selectedIds;

	public static final int PADDING = 2;

	public SignButtonsComponent(ObjectArrayList<SignResource> signResources, int columns, int signWidthUnits) {
		this.signResources = signResources;
		this.columns = columns;
		this.signWidthUnits = signWidthUnits;
		selectedIds = new LongAVLTreeSet[signWidthUnits];
		for (int i = 0; i < signWidthUnits; i++) {
			selectedIds[i] = new LongAVLTreeSet();
		}
		setSearch("");
	}

	@Override
	public void draw(UMatrixStack matrixStack) {
		beforeDrawCompat(matrixStack);
		final float columnWidth = getWidth() / columns;
		final float signWidth = columnWidth - PADDING * 2;
		final float signHeight = signWidth / signWidthUnits;
		final float rowHeight = signHeight + PADDING * 2;
		setHeight(new PixelConstraint(Math.max(1, Math.ceilDiv(filteredSignResources.size(), columns) * rowHeight)));

		final Pair<Float, Float> mousePosition = getMousePosition();
		final float mouseX = mousePosition.getFirst();
		final float mouseY = mousePosition.getSecond();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		hoveringSignResource = null;

		for (int i = 0; i < filteredSignResources.size(); i++) {
			final float x = getLeft() + (i % columns) * columnWidth;
			final float y = getTop() + Math.floorDiv(i, columns) * rowHeight;
			final SignResource signResource = filteredSignResources.get(i);
			final boolean mouseOver = mouseX >= x && mouseY >= y && mouseX < x + columnWidth && mouseY < y + rowHeight;

			if (mouseOver) {
				hoveringSignResource = signResource;
			}

			ImageComponentBase.drawTexture(mouseOver ? ReleasedDynamicTextureRegistry.BUTTON_HIGHLIGHTED_TEXTURE.get() : ReleasedDynamicTextureRegistry.BUTTON_TEXTURE.get(), vertexConsumer -> StitchedImageComponent.drawImage(
					matrixStack, vertexConsumer,
					x, y, columnWidth, rowHeight,
					200, 20, 4,
					false
			));

			final SignResource[] currentSignResources = new SignResource[signWidthUnits];
			currentSignResources[signResource.hasCustomText && signResource.getFlipCustomText() ? signWidthUnits - 1 : 0] = signResource;
			SignResource.render(UConverters.convert(matrixStack), minecraftClient.getBufferBuilders().getEntityVertexConsumers(), x + 2, y + 2, selectedIds, currentSignResources, signHeight, 0);
		}

		minecraftClient.getBufferBuilders().getEntityVertexConsumers().draw();
		super.draw(matrixStack);
	}

	public void onClick(EditSignCallback editSignCallback) {
		onMouseClickConsumer(clickEvent -> {
			if (hoveringSignResource != null) {
				editSignCallback.accept(editingIndex, hoveringSignResource);
			}
		});
	}

	public void setSearch(String text) {
		final String[] searchTerms = text.toLowerCase(Locale.ENGLISH).trim().split(" ");
		filteredSignResources.clear();
		signResources.forEach(signResource -> {
			if (searchTerms.length == 0) {
				filteredSignResources.add(signResource);
			} else {
				final String signId = signResource.signId.toLowerCase(Locale.ENGLISH);
				final String customText = signResource.getCustomText().toLowerCase(Locale.ENGLISH);
				if (Arrays.stream(searchTerms).allMatch(searchTerm -> signId.contains(searchTerm) || customText.contains(searchTerm))) {
					filteredSignResources.add(signResource);
				}
			}
		});
	}

	public interface EditSignCallback {
		void accept(int editingIndex, SignResource signResource);
	}
}
