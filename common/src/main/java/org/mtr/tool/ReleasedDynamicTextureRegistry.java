package org.mtr.tool;

import gg.essential.universal.UGraphics;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.mtr.MTR;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class ReleasedDynamicTextureRegistry extends DynamicReloadableRegistry<ReleasedDynamicTexture, Identifier, ReleasedDynamicTextureRegistry.Holder> {

	public static final ReleasedDynamicTextureRegistry INSTANCE = new ReleasedDynamicTextureRegistry();

	public static final Holder BACKGROUND_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/container/generic_54.png"));

	public static final Holder BUTTON_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/widget/button.png"));
	public static final Holder BUTTON_DISABLED_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/widget/button_disabled.png"));
	public static final Holder BUTTON_HIGHLIGHTED_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/widget/button_highlighted.png"));

	public static final Holder SLOT_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/slot.png"));

	public static final Holder ARROW_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/statistics/sort_down.png"));
	public static final Holder CROSS_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/beacon/cancel.png"));

	public static final Holder TAB_LEFT_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_unselected_1.png"));
	public static final Holder TAB_LEFT_SELECTED_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_selected_1.png"));
	public static final Holder TAB_MIDDLE_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_unselected_2.png"));
	public static final Holder TAB_MIDDLE_SELECTED_TEXTURE = INSTANCE.create(Identifier.of("textures/gui/sprites/container/creative_inventory/tab_top_selected_2.png"));

	public static final Holder DIAMOND_PICKAXE_TEXTURE = INSTANCE.create(Identifier.of("textures/item/diamond_pickaxe.png"));
	public static final Holder POPPY_TEXTURE = INSTANCE.create(Identifier.of("textures/block/poppy.png"));

	@Override
	protected Holder createHolderInstance(Identifier input) {
		return new Holder(input);
	}

	public static class Holder extends DynamicReloadableRegistry.Holder<ReleasedDynamicTexture, Identifier> {

		protected Holder(Identifier input) {
			super(input);
		}

		@Override
		protected ReleasedDynamicTexture create(Identifier input) {
			final Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(input);
			if (optionalResource.isPresent()) {
				try (final InputStream inputStream = optionalResource.get().getInputStream()) {
					return UGraphics.getTexture(inputStream);
				} catch (IOException e) {
					MTR.LOGGER.error("Failed to load resource [{}]", input, e);
				}
			}
			return new ReleasedDynamicTexture(1, 1);
		}
	}
}
