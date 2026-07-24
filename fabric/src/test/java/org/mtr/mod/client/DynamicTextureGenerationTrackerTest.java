package org.mtr.mod.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class DynamicTextureGenerationTrackerTest {

	@Test
	public void testCurrentGenerationLifecycle() {
		final DynamicTextureGenerationTracker tracker = new DynamicTextureGenerationTracker();
		final DynamicTextureGenerationTracker.Token token = tracker.start("key");

		Assertions.assertTrue(tracker.isActive("key"));
		Assertions.assertTrue(tracker.isCurrent("key", token));

		tracker.completeSuccess("key", token);

		Assertions.assertFalse(tracker.isActive("key"));
		Assertions.assertFalse(tracker.isRetryBlocked("key", 100));
	}

	@Test
	public void testRefreshSupersedesQueuedCompletion() {
		final DynamicTextureGenerationTracker tracker = new DynamicTextureGenerationTracker();
		final DynamicTextureGenerationTracker.Token staleToken = tracker.start("key");
		tracker.refresh();
		final DynamicTextureGenerationTracker.Token currentToken = tracker.start("key");

		tracker.completeFailure("key", staleToken, 200);

		Assertions.assertTrue(tracker.isCurrent("key", currentToken));
		Assertions.assertFalse(tracker.isRetryBlocked("key", 100));

		tracker.completeSuccess("key", staleToken);

		Assertions.assertTrue(tracker.isCurrent("key", currentToken));
	}

	@Test
	public void testFailureBackoffExpires() {
		final DynamicTextureGenerationTracker tracker = new DynamicTextureGenerationTracker();
		final DynamicTextureGenerationTracker.Token token = tracker.start("key");
		tracker.completeFailure("key", token, 200);

		Assertions.assertFalse(tracker.isActive("key"));
		Assertions.assertTrue(tracker.isRetryBlocked("key", 199));
		Assertions.assertFalse(tracker.isRetryBlocked("key", 200));
		Assertions.assertFalse(tracker.isRetryBlocked("key", 201));
	}

	@Test
	public void testRefreshClearsFailureBackoff() {
		final DynamicTextureGenerationTracker tracker = new DynamicTextureGenerationTracker();
		final DynamicTextureGenerationTracker.Token token = tracker.start("key");
		tracker.completeFailure("key", token, Long.MAX_VALUE);

		tracker.refresh();

		Assertions.assertFalse(tracker.isRetryBlocked("key", 0));
	}
}
