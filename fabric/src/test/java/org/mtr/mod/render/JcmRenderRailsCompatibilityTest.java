package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.client.MinecraftClientData;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class JcmRenderRailsCompatibilityTest {

	@Test
	public void preservesOcclusionTaskInjectionDescriptor() {
		assertDescriptor(
				"lambda$render$1",
				Runnable.class,
				MinecraftClientData.RailWrapper.class,
				Vec3d.class,
				OcclusionCullingInstance.class
		);
	}

	@Test
	public void preservesRailCancellationInjectionDescriptor() {
		assertDescriptor(
				"lambda$render$5",
				void.class,
				boolean.class,
				ObjectArraySet.class,
				ClientWorld.class,
				Rail.class
		);
	}

	@Test
	public void preservesRailStatsInjectionDescriptor() {
		assertDescriptor(
				"lambda$renderRailStats$24",
				void.class,
				BlockPos.class,
				double.class,
				BlockPos.class,
				String.class,
				String.class,
				String.class,
				String.class,
				String.class,
				String.class,
				GraphicsHolder.class,
				Vector3d.class
		);
	}

	@Test
	public void preservesStrictRenderLocalCapturePrefix() throws Exception {
		Path renderRailsPath = Path.of("src", "main", "java", "org", "mtr", "mod", "render", "RenderRails.java");
		if (!Files.exists(renderRailsPath)) {
			renderRailsPath = Path.of("fabric").resolve(renderRailsPath);
		}
		Assertions.assertTrue(Files.exists(renderRailsPath), "RenderRails source must be available for JCM local capture verification");
		final String source = Files.readString(renderRailsPath);
		final int holdingRailRelatedIndex = source.indexOf("final boolean holdingRailRelated = isHoldingRailRelated(clientPlayerEntity);");
		final int railsToRenderIndex = source.indexOf("final ObjectArrayList<Rail> railsToRender = new ObjectArrayList<>();", holdingRailRelatedIndex);
		Assertions.assertTrue(holdingRailRelatedIndex >= 0 && railsToRenderIndex > holdingRailRelatedIndex, "JCM expects railsToRender immediately after holdingRailRelated in the render local table");
		Assertions.assertFalse(source.substring(holdingRailRelatedIndex, railsToRenderIndex).contains("optimizedRailRendering"), "extra render locals break JCM's CAPTURE_FAILSOFT injection");
		Assertions.assertFalse(source.contains("shouldBypassOcclusionCulling"), "batched rails must retain JCM visibility selection");
		Assertions.assertTrue(source.contains("cullingTasks.add(occlusionCullingInstance ->"), "the original JCM culling lambda must remain present");
	}

	private static void assertDescriptor(String methodName, Class<?> returnType, Class<?>... parameterTypes) {
		final Method method = Arrays.stream(RenderRails.class.getDeclaredMethods())
				.filter(candidate -> candidate.getName().equals(methodName))
				.findFirst()
				.orElse(null);
		Assertions.assertNotNull(method, "JCM injection target is missing: " + methodName);
		Assertions.assertArrayEquals(parameterTypes, method.getParameterTypes(), "JCM injection descriptor changed: " + methodName);
		Assertions.assertEquals(returnType, method.getReturnType(), "JCM injection return type changed: " + methodName);
	}
}
