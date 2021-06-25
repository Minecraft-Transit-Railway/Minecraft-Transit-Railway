import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This is a class for generating lots of JSON files and is not directly used for the mod.
 **/

public class FileGenerator {

	private static final Path OUTPUT_PATH = Paths.get(System.getProperty("user.home")).resolve("desktop/generated");
	private static final Path INPUT_PATH = Paths.get(System.getProperty("user.home")).resolve("desktop/input.json");

	/**
	 * Available placeholders:<br/>
	 * %i to replace by index<br/>
	 * %&lt;m&gt;% to replace by m*index
	 * %&lt;m+c&gt;% to replace by m*index+c
	 *
	 * @param args Arguments to generate the file: Output file name, Range
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Not enough arguments");
			return;
		}

		final String fileName = args[0];
		final int range = Integer.parseInt(args[1]);
		final String jsonString = String.join("\n", Files.readAllLines(INPUT_PATH));

		FileUtils.cleanDirectory(OUTPUT_PATH.toFile());
		Files.createDirectories(OUTPUT_PATH);
		for (int i = 0; i < range; i++) {
			Files.write(OUTPUT_PATH.resolve(formatString(fileName, i) + ".json"), formatString(jsonString, i).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		}
	}

	private static String formatString(String string, int index) {
		final String[] stringSplit = string.replace("%i", String.valueOf(index)).split("%");
		final StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < stringSplit.length; i++) {
			if (i % 2 == 1) {
				final String[] argumentSplit = stringSplit[i].split("\\+");
				final String result = String.valueOf(Float.parseFloat(argumentSplit[0]) * index + (argumentSplit.length > 1 ? Float.parseFloat(argumentSplit[1]) : 0));
				stringBuilder.append(result.endsWith(".0") ? result.substring(0, result.length() - 2) : result);
			} else {
				stringBuilder.append(stringSplit[i]);
			}
		}

		return stringBuilder.toString();
	}
}
