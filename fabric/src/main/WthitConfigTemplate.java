package org.mtr.init;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import org.mtr.mod.EntityTypes;
import org.mtr.mod.Init;

public final class WthitConfig implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		registrar.addBlacklist(EntityTypes.RENDERING.get().data);
		Init.LOGGER.info("WTHIT detected");
	}
}
