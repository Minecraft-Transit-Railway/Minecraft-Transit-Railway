package org.mtr.mod.client;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

final class DynamicTextureGenerationTracker {

	private final Object2ObjectLinkedOpenHashMap<String, Token> activeGenerations = new Object2ObjectLinkedOpenHashMap<>();
	private final Object2LongArrayMap<String> retryTimes = new Object2LongArrayMap<>();

	Token start(String key) {
		final Token token = new Token();
		activeGenerations.put(key, token);
		return token;
	}

	boolean isActive(String key) {
		return activeGenerations.containsKey(key);
	}

	boolean isCurrent(String key, Token token) {
		return activeGenerations.get(key) == token;
	}

	boolean isRetryBlocked(String key, long currentTimeMillis) {
		if (!retryTimes.containsKey(key)) {
			return false;
		}
		if (retryTimes.getLong(key) > currentTimeMillis) {
			return true;
		}
		retryTimes.removeLong(key);
		return false;
	}

	void completeSuccess(String key, Token token) {
		if (isCurrent(key, token)) {
			activeGenerations.remove(key);
			retryTimes.removeLong(key);
		}
	}

	void completeFailure(String key, Token token, long retryTime) {
		if (isCurrent(key, token)) {
			activeGenerations.remove(key);
			retryTimes.put(key, retryTime);
		}
	}

	void refresh() {
		activeGenerations.clear();
		retryTimes.clear();
	}

	static final class Token {
	}
}
