plugins {
	id("dev.kikugie.stonecutter")
	id("net.neoforged.moddev") version "2.0.146" apply false
}

stonecutter active "1.21.4-fabric"

stonecutter parameters {
	constants.match(node.metadata.project.substringAfterLast("-"), "fabric", "neoforge")

	// Minecraft 1.21.11 renamed ResourceLocation back to Identifier and left it in the same
	// net.minecraft.resources package, so rewriting the token alone covers the imports, every usage
	// and the Javadoc links. That keeps roughly 150 sites free of version guards.
	//
	// The replacement is declared only for the newer targets instead of relying on a reversible
	// direction. Reversing it would rewrite unrelated names that merely contain the word, such as
	// formatIdentifier, along with a log message that mentions it in prose.
	if (current.parsed >= "26.1") {
		replacements {
			// The direction is fixed forwards; the guard above already restricts this to 26.1 and newer.
			string(true) { replace("ResourceLocation", "Identifier") }

			// Pure package moves. The type names are unchanged, so only the import lines differ and the
			// call sites throughout the code need no attention at all.
			string(true) { replace("net.minecraft.Util", "net.minecraft.util.Util") }
			string(true) { replace("net.minecraft.world.level.GameRules", "net.minecraft.world.level.gamerules.GameRules") }
			string(true) { replace("net.minecraft.client.renderer.RenderType", "net.minecraft.client.renderer.rendertype.RenderType") }
			string(true) { replace("net.minecraft.client.renderer.block.model.BakedQuad", "net.minecraft.client.resources.model.geometry.BakedQuad") }

			// LightTexture both moved package and was renamed, but LightCoordsUtil kept pack(...) and
			// block(...) with identical signatures. The import and the call sites are therefore rewritten
			// by two rules: the first matches only the import, which ends in a semicolon, and the second
			// only the static calls, which are followed by a dot.
			string(true) { replace("net.minecraft.client.renderer.LightTexture", "net.minecraft.util.LightCoordsUtil") }
			string(true) { replace("LightTexture.", "LightCoordsUtil.") }

			// NeoForge dropped the bus attribute and the Bus enum from @EventBusSubscriber; events now
			// declare which bus they belong to themselves. Only the attribute needs removing, and it is
			// stripped here rather than guarded in the four subscriber classes because those files sit
			// entirely inside a comment-toggled "if neoforge" block, and nesting a second condition inside
			// one is a pattern this codebase does not use anywhere.
			string(true) { replace(", bus = EventBusSubscriber.Bus.GAME", "") }
			string(true) { replace(", bus = EventBusSubscriber.Bus.MOD", "") }
		}
	}
}
