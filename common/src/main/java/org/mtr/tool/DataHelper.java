package org.mtr.tool;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.generated.lang.TranslationProvider;

public final class DataHelper {

	public static String getNameOrUntitled(NameColorDataBase nameColorDataBase) {
		return getNameOrUntitled(nameColorDataBase.getName());
	}

	public static String getNameOrUntitled(String name) {
		return name.isEmpty() ? TranslationProvider.GUI_MTR_UNTITLED.getString() : name;
	}
}
