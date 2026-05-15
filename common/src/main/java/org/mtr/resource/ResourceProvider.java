package org.mtr.resource;

import net.minecraft.util.Identifier;

@FunctionalInterface
public interface ResourceProvider {

	String get(Identifier identifier);
}
