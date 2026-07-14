package org.mtr.mod.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class RailCullingHelperTest {

	@Test
	public void chooseOcclusionCullingByExpandedCellCount() {
		Assertions.assertTrue(RailCullingHelper.canUseOcclusionCulling(0, 0, 0, 722, 0, 722));
		Assertions.assertFalse(RailCullingHelper.canUseOcclusionCulling(0, 0, 0, 723, 0, 723));
		Assertions.assertFalse(RailCullingHelper.canUseOcclusionCulling(0, 0, 0, 100, 100, 100));
	}

	@Test
	public void handleReversedAndInvalidBoundsWithoutOverflow() {
		Assertions.assertTrue(RailCullingHelper.canUseOcclusionCulling(722, 0, 722, 0, 0, 0));
		Assertions.assertFalse(RailCullingHelper.canUseOcclusionCulling(723, 0, 723, 0, 0, 0));
		Assertions.assertFalse(RailCullingHelper.canUseOcclusionCulling(-Double.MAX_VALUE, 0, 0, Double.MAX_VALUE, 0, 0));
		Assertions.assertFalse(RailCullingHelper.canUseOcclusionCulling(Double.NaN, 0, 0, 0, 0, 0));
	}

	@Test
	public void measureHorizontalDistanceToClosestPointOnBox() {
		Assertions.assertTrue(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, -1, -1, 1, 1, 0));
		Assertions.assertTrue(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, 3, 4, 5, 6, 5));
		Assertions.assertFalse(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, 3, 4, 5, 6, 4.999));
		Assertions.assertTrue(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, 5, 6, 3, 4, 5));
	}

	@Test
	public void failOpenForInvalidDistanceInputs() {
		Assertions.assertTrue(RailCullingHelper.isWithinHorizontalRenderDistance(Double.NaN, 0, 0, 0, 0, 0, 0));
		Assertions.assertTrue(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, 0, 0, Double.POSITIVE_INFINITY, 0, 0));
		Assertions.assertFalse(RailCullingHelper.isWithinHorizontalRenderDistance(0, 0, 0, 0, 0, 0, -1));
	}
}
