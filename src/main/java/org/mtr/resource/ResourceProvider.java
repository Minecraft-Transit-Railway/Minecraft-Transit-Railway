package org.mtr.resource;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ResourceProvider {

	String get(ResourceLocation identifier);
}
