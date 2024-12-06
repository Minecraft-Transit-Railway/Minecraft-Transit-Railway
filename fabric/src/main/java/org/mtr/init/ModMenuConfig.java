package org.mtr.init;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.mtr.mapping.holder.Screen;
import org.mtr.mod.screen.ConfigScreen;

public final class ModMenuConfig implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new ConfigScreen(new Screen(parent));
	}
}
