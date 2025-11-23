package org.mtr.font;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.util.Identifier;
import org.mtr.tool.DynamicReloadableRegistry;

public final class FontGroupRegistry extends DynamicReloadableRegistry<FontGroup, ObjectImmutableList<FontProvider>, FontGroupRegistry.Holder> {

	public static final FontGroupRegistry INSTANCE = new FontGroupRegistry();

	public static final Holder MINECRAFT;
	public static final Holder MTR;
	public static final Holder KCR;
	public static final Holder CALLIGRAPHY;

	static {
		final String modId = org.mtr.MTR.MOD_ID;
		final FontProvider notoSansProvider = new FontProvider(Identifier.of(modId, "font/noto-sans-semibold.ttf"));
		MINECRAFT = INSTANCE.create(ObjectImmutableList.of(new FontProvider(Identifier.of("font/default.json"))));
		MTR = INSTANCE.create(ObjectImmutableList.of(notoSansProvider, new FontProvider(Identifier.of(modId, "font/noto-serif-tc-semibold.ttf")), new FontProvider(Identifier.of(modId, "font/noto-serif-sc-semibold.ttf"))));
		KCR = INSTANCE.create(ObjectImmutableList.of(notoSansProvider, new FontProvider(Identifier.of(modId, "font/noto-sans-tc-semibold.ttf")), new FontProvider(Identifier.of(modId, "font/noto-sans-sc-semibold.ttf"))));
		CALLIGRAPHY = INSTANCE.create(ObjectImmutableList.of(new FontProvider(Identifier.of(modId, "font/masa-font-bold.ttf"))));
	}

	@Override
	protected Holder createHolderInstance(ObjectImmutableList<FontProvider> input) {
		return new Holder(input);
	}

	public static class Holder extends DynamicReloadableRegistry.Holder<FontGroup, ObjectImmutableList<FontProvider>> {

		protected Holder(ObjectImmutableList<FontProvider> input) {
			super(input);
		}

		@Override
		protected FontGroup create(ObjectImmutableList<FontProvider> input) {
			return new FontGroup(input);
		}
	}
}
