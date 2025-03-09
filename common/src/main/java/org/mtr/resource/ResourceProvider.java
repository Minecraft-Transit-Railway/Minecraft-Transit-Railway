package org.mtr.resource;

import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface ResourceProvider {

	@Nonnull
	String get(Identifier identifier);
}
