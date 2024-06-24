package org.mtr.init;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.mtr.mod.screen.ConfigScreen;

public class ModMenuConfig implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new ConfigScreen();
	}
}
