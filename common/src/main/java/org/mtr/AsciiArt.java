package org.mtr.mod;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Random;

public final class AsciiArt {

	private static final ObjectArrayList<ObjectArrayList<String>> ART = new ObjectArrayList<>();

	static {
		final ObjectArrayList<String> big = new ObjectArrayList<>();
		addToList(big, ":  __  __ _                            __ _    ");
		addToList(big, ": |  ?/  (_)                          / _| |   ");
		addToList(big, ": | ?  / |_ _ __   ___  ___ _ __ __ _| |_| |_  ");
		addToList(big, ": | |?/| | | '_ ? / _ ?/ __| '__/ _` |  _| __| ");
		addToList(big, ": | |  | | | | | |  __/ (__| | | (_| | | | |_  ");
		addToList(big, ": |_|__|_|_|_| |_|?___|?___|_|_ ?__,_|_|  ?__| ");
		addToList(big, ": |__   __|                (_) |               ");
		addToList(big, ":    | |_ __ __ _ _ __  ___ _| |_              ");
		addToList(big, ":    | | '__/ _` | '_ ?/ __| | __|             ");
		addToList(big, ":    | | | | (_| | | | ?__ ? | |_              ");
		addToList(big, ":    |_|_|  ?__,_|_| |_|___/_|?__|             ");
		addToList(big, ":  _____       _ _                             ");
		addToList(big, ": |  __ ?     (_) |                            ");
		addToList(big, ": | |__) |__ _ _| |_      ____ _ _   _         ");
		addToList(big, ": |  _  // _` | | ? ? /? / / _` | | | |        ");
		addToList(big, ": | | ? ? (_| | | |? V  V / (_| | |_| |        ");
		addToList(big, ": |_|  ?_?__,_|_|_| ?_/?_/ ?__,_|?__, |        ");
		addToList(big, ":                                 __/ |        ");
		addToList(big, ":                                |___/ (v....) ");
		addToList(big, ":                                              ");
		ART.add(big);

		final ObjectArrayList<String> train = new ObjectArrayList<>();
		addToList(train, ":  __  __     _                                                __    _     ");
		addToList(train, ": |  ?/  |   (_)    _ _      ___     __       _ _   __ _      / _|  | |_   ");
		addToList(train, ": | |?/| |   | |   | ' ?    / -_)   / _|     | '_| / _` |    |  _|  |  _|  ");
		addToList(train, ": |_|__|_|  _|_|_  |_||_|   ?___|   ?__|_   _|_|_  ?__,_|   _|_|_   _?__|  ");
		addToList(train, ": _|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^| ");
		addToList(train, ": ^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-' ");
		addToList(train, ":   _____                                     _      _                     ");
		addToList(train, ":  |_   _|    _ _   __ _    _ _      ___     (_)    | |_                   ");
		addToList(train, ":    | |     | '_| / _` |  | ' ?    (_-<     | |    |  _|                  ");
		addToList(train, ":   _|_|_   _|_|_  ?__,_|  |_||_|   /__/_   _|_|_   _?__|                  ");
		addToList(train, ": _|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|                 ");
		addToList(train, ": ^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'                 ");
		addToList(train, ":    ___              _       _                      _  _                  ");
		addToList(train, ":   | _ ?   __ _     (_)     | |   __ __ __ __ _    | || |                 ");
		addToList(train, ":   |   /  / _` |    | |     | |   ? V  V // _` |    ?_, |                 ");
		addToList(train, ":   |_|_?  ?__,_|   _|_|_   _|_|_   ?_/?_/ ?__,_|   _|__/                  ");
		addToList(train, ": _|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_|^^^^^|_| ^^^^|                 ");
		addToList(train, ": ^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-'^`-0-0-' (v............) ");
		addToList(train, ":                                                                          ");
		ART.add(train);
	}

	private static void addToList(ObjectArrayList<String> list, String string) {
		list.add(string.replace("?", "\\").replace("^", "\"").replaceAll("\\(v\\.+\\)", String.format("(%s)", Keys.MOD_VERSION)));
	}

	public static void print() {
		ART.get(new Random().nextInt(ART.size())).forEach(Init.LOGGER::info);
	}
}
