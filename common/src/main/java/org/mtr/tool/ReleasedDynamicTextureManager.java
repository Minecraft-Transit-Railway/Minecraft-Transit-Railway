package org.mtr.tool;

import gg.essential.universal.UGraphics;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.mtr.MTR;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class ReleasedDynamicTextureManager {

	private static final ObjectArrayList<Holder> TEXTURES = new ObjectArrayList<>();

	public static final Holder BACKGROUND_TEXTURE = create(Identifier.of("textures/gui/container/generic_54.png"));

	public static final Holder BUTTON_TEXTURE = create(Identifier.of("textures/gui/sprites/widget/button.png"));
	public static final Holder BUTTON_DISABLED_TEXTURE = create(Identifier.of("textures/gui/sprites/widget/button_disabled.png"));
	public static final Holder BUTTON_HIGHLIGHTED_TEXTURE = create(Identifier.of("textures/gui/sprites/widget/button_highlighted.png"));

	public static final Holder SLOT_TEXTURE = create(Identifier.of("textures/gui/sprites/container/slot.png"));

	public static final Holder TAB_LEFT_TEXTURE = create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_unselected_1.png"));
	public static final Holder TAB_LEFT_SELECTED_TEXTURE = create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_selected_1.png"));
	public static final Holder TAB_MIDDLE_TEXTURE = create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_unselected_2.png"));
	public static final Holder TAB_MIDDLE_SELECTED_TEXTURE = create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_selected_2.png"));

	public static final Holder DIAMOND_PICKAXE_TEXTURE = create(Identifier.of("textures/item/diamond_pickaxe.png"));
	public static final Holder POPPY_TEXTURE = create(Identifier.of("textures/block/poppy.png"));

	public static Holder create(Identifier identifier) {
		final Holder holder = new Holder(identifier);
		TEXTURES.add(holder);
		return holder;
	}

	public static void reload() {
		TEXTURES.forEach(Holder::reload);
	}

	public static class Holder {

		private ReleasedDynamicTexture releasedDynamicTexture;
		private final Identifier identifier;

		private Holder(Identifier identifier) {
			this.identifier = identifier;
			reload();
		}

		public ReleasedDynamicTexture get() {
			return releasedDynamicTexture;
		}

		private void reload() {
			releasedDynamicTexture = create();
		}

		private ReleasedDynamicTexture create() {
			final Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
			if (optionalResource.isPresent()) {
				try (final InputStream inputStream = optionalResource.get().getInputStream()) {
					return UGraphics.getTexture(inputStream);
				} catch (IOException e) {
					MTR.LOGGER.error("Failed to load resource [{}]", identifier, e);
				}
			}
			return new ReleasedDynamicTexture(1, 1);
		}
	}
}
