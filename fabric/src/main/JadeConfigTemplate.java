package org.mtr.init;

import org.mtr.mod.EntityTypes;
import org.mtr.mod.Init;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class JadeConfig implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.hideTarget(EntityTypes.RENDERING.get().data);
		Init.LOGGER.info("Jade detected");
	}
}
