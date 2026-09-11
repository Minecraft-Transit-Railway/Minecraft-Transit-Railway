package org.mtr;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.IOUtils;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Rewrites a recipe's ingredients from the object form to the string form as the file is copied.
 *
 * <p>The recipes are written the way 1.21.1 reads them, {@code {"item": "minecraft:glass"}} and
 * {@code {"tag": "c:iron_ingots"}}, because that version accepts nothing else. From 26.1 an
 * ingredient is a plain identifier, {@code "minecraft:glass"}, with a {@code #} prefix for a tag,
 * and the object form is dropped without a complaint about the form itself: the ingredient list
 * simply comes out empty and the recipe is rejected as too short. 1.21.4 reads both, so the
 * rewrite is applied only where it is needed and the source stays in the older form.</p>
 *
 * <p>Only the two places an ingredient can sit are touched, the {@code key} map of a shaped recipe
 * and the {@code ingredients} list of a shapeless one. Results already use the {@code id} form
 * every version reads, and an ingredient that is not a single {@code item} or {@code tag} object
 * is left as it is.</p>
 *
 * <p>Items that Minecraft renamed on the way are mapped as well, since an unknown identifier is
 * rejected just as an unreadable one is and the recipe is lost with it. The source keeps the
 * names the older versions know.</p>
 */
public final class RecipeIngredientFilter extends FilterReader {

	/**
	 * Items renamed by 26.1, keyed by the name the source uses. Chains gained a copper variant
	 * and the original became the iron one.
	 */
	private static final Map<String, String> RENAMED_ITEMS = Map.of(
		"minecraft:chain", "minecraft:iron_chain"
	);

	public RecipeIngredientFilter(Reader in) {
		super(new StringReader(rewrite(read(in))));
	}

	private static String read(Reader in) {
		try {
			return IOUtils.toString(in);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	static String rewrite(String json) {
		final JsonObject recipe = JsonParser.parseString(json).getAsJsonObject();

		if (recipe.has("key")) {
			final JsonObject key = recipe.getAsJsonObject("key");
			for (final Map.Entry<String, JsonElement> entry : key.entrySet()) {
				key.add(entry.getKey(), ingredient(entry.getValue()));
			}
		}

		if (recipe.has("ingredients")) {
			final JsonArray ingredients = recipe.getAsJsonArray("ingredients");
			final JsonArray rewritten = new JsonArray();
			ingredients.forEach(element -> rewritten.add(ingredient(element)));
			recipe.add("ingredients", rewritten);
		}

		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(recipe);
	}

	private static JsonElement ingredient(JsonElement element) {
		if (element.isJsonObject()) {
			final JsonObject object = element.getAsJsonObject();
			if (object.size() == 1 && object.has("item")) {
				final String item = object.get("item").getAsString();
				return new JsonPrimitive(RENAMED_ITEMS.getOrDefault(item, item));
			}
			if (object.size() == 1 && object.has("tag")) {
				return new JsonPrimitive("#" + object.get("tag").getAsString());
			}
		}
		return element;
	}
}
