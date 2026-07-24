package org.mtr.mod.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.mod.block.IBlock.DoubleBlockHalf;
import org.mtr.mod.block.IBlock.EnumSide;

public final class RenderAPGGlassTest {

	@Test
	public void onlyExtendedUpperHalvesProduceRouteRendering() {
		Assertions.assertTrue(RenderAPGGlass.shouldRenderState(DoubleBlockHalf.UPPER, EnumSide.LEFT));
		Assertions.assertTrue(RenderAPGGlass.shouldRenderState(DoubleBlockHalf.UPPER, EnumSide.MIDDLE));
		Assertions.assertFalse(RenderAPGGlass.shouldRenderState(DoubleBlockHalf.UPPER, EnumSide.SINGLE));
		Assertions.assertFalse(RenderAPGGlass.shouldRenderState(DoubleBlockHalf.LOWER, EnumSide.LEFT));
	}
}
