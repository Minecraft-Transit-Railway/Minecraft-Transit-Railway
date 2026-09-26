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
import java.util.Map;

/**
 * Rewrites a recipe's ingredients from the object form to the string form as the file is copied.
 *
 * <p>The recipes are written the way 1.21.1 reads them, {@code {"item": "minecraft:glass"}} and
 * {@code {"tag": "c:iron_ingots"}}, because that version accepts nothing else. From 1.21.4 an
 * ingredient is a plain identifier, {@code "minecraft:glass"}, with a {@code #} prefix for a tag,
 * and the object form is rejected: on 26.1 it is dropped without a complaint about the form
 * itself, so the ingredient list simply comes out empty and the recipe is refused as too short.
 * The rewrite is applied to every version that reads the string form and the source stays in
 * the older one.</p>
 *
 * <p>Only the two places an ingredient can sit are touched, the {@code key} map of a shaped recipe
 * and the {@code ingredients} list of a shapeless one. Results already use the {@code id} form
 * every version reads, and an ingredient that is not a single {@code item} or {@code tag} object
 * is left as it is.</p>
 *
 * <p>Items that Minecraft later renamed are mapped as well when {@link #setRenameItems} says so,
 * since an unknown identifier is rejected just as an unreadable one is and the recipe is lost
 * with it. The source keeps the names the older versions know.</p>
 *
 * <p>The work is done on the first read rather than in the constructor, because Gradle sets the
 * filter's properties after constructing it.</p>
 */
public final class RecipeIngredientFilter extends FilterReader {

	/**
	 * Items renamed by 26.1, keyed by the name the source uses. Chains gained a copper variant
	 * and the original became the iron one.
	 */
	private static final Map<String, String> RENAMED_ITEMS = Map.of(
		"minecraft:chain", "minecraft:iron_chain"
	);

	private boolean renameItems;
	private Reader rewritten;

	public RecipeIngredientFilter(Reader in) {
		super(in);
	}

	/**
	 * Whether the identifiers in {@link #RENAMED_ITEMS} are mapped to their newer names.
	 */
	public void setRenameItems(boolean renameItems) {
		this.renameItems = renameItems;
	}

	@Override
	public int read() throws IOException {
		return rewritten().read();
	}

	@Override
	public int read(char[] buffer, int offset, int length) throws IOException {
		return rewritten().read(buffer, offset, length);
	}

	private Reader rewritten() throws IOException {
		if (rewritten == null) {
			rewritten = new StringReader(rewrite(IOUtils.toString(in), renameItems));
		}
		return rewritten;
	}

	static String rewrite(String json, boolean renameItems) {
		final JsonObject recipe = JsonParser.parseString(json).getAsJsonObject();

		if (recipe.has("key")) {
			final JsonObject key = recipe.getAsJsonObject("key");
			for (final Map.Entry<String, JsonElement> entry : key.entrySet()) {
				key.add(entry.getKey(), ingredient(entry.getValue(), renameItems));
			}
		}

		if (recipe.has("ingredients")) {
			final JsonArray ingredients = recipe.getAsJsonArray("ingredients");
			final JsonArray rewrittenIngredients = new JsonArray();
			ingredients.forEach(element -> rewrittenIngredients.add(ingredient(element, renameItems)));
			recipe.add("ingredients", rewrittenIngredients);
		}

		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(recipe);
	}

	private static JsonElement ingredient(JsonElement element, boolean renameItems) {
		if (element.isJsonObject()) {
			final JsonObject object = element.getAsJsonObject();
			if (object.size() == 1 && object.has("item")) {
				final String item = object.get("item").getAsString();
				return new JsonPrimitive(renameItems ? RENAMED_ITEMS.getOrDefault(item, item) : item);
			}
			if (object.size() == 1 && object.has("tag")) {
				return new JsonPrimitive("#" + object.get("tag").getAsString());
			}
		}
		return element;
	}
}
