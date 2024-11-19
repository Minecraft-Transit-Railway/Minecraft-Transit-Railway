package org.mtr.mod.resource;

import org.mtr.mapping.holder.Identifier;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface ResourceProvider {

	@Nonnull
	String get(Identifier identifier);
}
