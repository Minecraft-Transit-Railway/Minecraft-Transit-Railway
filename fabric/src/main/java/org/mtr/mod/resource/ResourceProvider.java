package org.mtr.mod.resource;

import org.mtr.mapping.holder.Identifier;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

@FunctionalInterface
public interface ResourceProvider {

	@Nonnull
	String get(Identifier identifier);

	default byte[] getBytes(Identifier identifier) {
		return get(identifier).getBytes(StandardCharsets.UTF_8);
	}
}
