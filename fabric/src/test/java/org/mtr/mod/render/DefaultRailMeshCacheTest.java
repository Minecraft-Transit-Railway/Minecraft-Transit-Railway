package org.mtr.mod.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.render.model.Mesh;
import org.mtr.mapping.render.vertex.Vertex;
import org.mtr.mapping.render.vertex.VertexAttributeMapping;
import org.mtr.mapping.render.vertex.VertexAttributeSource;
import org.mtr.mapping.render.vertex.VertexAttributeType;
import org.mtr.mod.data.IGui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public final class DefaultRailMeshCacheTest {

	private static final String CACHE_CLASS_NAME = "org.mtr.mod.render.DefaultRailMeshCache";

	@Test
	public void onlyNormalTrainDefaultTwoDimensionalRailsAreEligible() {
		final Method isEligible = requirePackageApi(
				"isEligible",
				boolean.class,
				TransportMode.class,
				Iterable.class,
				boolean.class
		);

		Assertions.assertTrue(invokeBoolean(isEligible, true, TransportMode.TRAIN, Collections.singletonList("default"), false));
		Assertions.assertFalse(invokeBoolean(isEligible, false, TransportMode.TRAIN, Collections.singletonList("default"), false), "coloured or flashing rails must stay on the dynamic path");
		Assertions.assertFalse(invokeBoolean(isEligible, true, TransportMode.BOAT, Collections.singletonList("default"), false), "only TRAIN rails use the cached default geometry");
		Assertions.assertFalse(invokeBoolean(isEligible, true, TransportMode.TRAIN, Collections.singletonList("custom"), false), "custom-only styles need their resource renderers");
		Assertions.assertFalse(invokeBoolean(isEligible, true, TransportMode.TRAIN, Arrays.asList("default", "custom"), false), "mixed styles must preserve custom resource rendering");
		Assertions.assertFalse(invokeBoolean(isEligible, true, TransportMode.TRAIN, Collections.singletonList("default"), true), "defaultRail3D replaces the two-dimensional default rail");
	}

	@Test
	public void cellBatchingRetainsVoxelOcclusionSelection() throws Exception {
		Path renderRailsPath = Path.of("src", "main", "java", "org", "mtr", "mod", "render", "RenderRails.java");
		if (!Files.exists(renderRailsPath)) {
			renderRailsPath = Path.of("fabric").resolve(renderRailsPath);
		}
		Assertions.assertTrue(Files.exists(renderRailsPath), "RenderRails source must be available for culling integration verification");
		final String renderRailsSource = Files.readString(renderRailsPath);
		Assertions.assertFalse(renderRailsSource.contains("shouldBypassOcclusionCulling"), "cached rails must continue through JCM culling");
		Assertions.assertTrue(renderRailsSource.contains("cullingTasks.add(occlusionCullingInstance ->"), "the JCM culling lambda must remain present");
		Assertions.assertTrue(renderRailsSource.contains("if (cullingTasks != null)"), "the original JCM culling batch scheduling guard must remain present");
	}

	@Test
	public void splitsSixtyFourMeterPagesAtOneHundredTwentyEightHalfMeterSegments() {
		Assertions.assertEquals(64D, requireNumericConstant("PAGE_LENGTH_METERS").doubleValue());
		Assertions.assertEquals(128, requireNumericConstant("SEGMENTS_PER_PAGE").intValue());

		final Method pageIndexForSegment = requirePackageApi("pageIndexForSegment", int.class);
		Assertions.assertEquals(0, invokeInt(pageIndexForSegment, 0));
		Assertions.assertEquals(0, invokeInt(pageIndexForSegment, 127));
		Assertions.assertEquals(1, invokeInt(pageIndexForSegment, 128));
		Assertions.assertEquals(1, invokeInt(pageIndexForSegment, 255));
		Assertions.assertEquals(2, invokeInt(pageIndexForSegment, 256));

		final Method pageCountForSegments = requirePackageApi("pageCountForSegments", int.class);
		Assertions.assertEquals(0, invokeInt(pageCountForSegments, 0));
		Assertions.assertEquals(1, invokeInt(pageCountForSegments, 1));
		Assertions.assertEquals(1, invokeInt(pageCountForSegments, 128));
		Assertions.assertEquals(2, invokeInt(pageCountForSegments, 129));
		Assertions.assertEquals(2, invokeInt(pageCountForSegments, 256));
		Assertions.assertEquals(3, invokeInt(pageCountForSegments, 257));
	}

	@Test
	public void geometryFingerprintChangesWithEveryGeometryInput() {
		final Method geometryFingerprint = requirePackageApi(
				"geometryFingerprint",
				double.class,
				double.class,
				Rail.Shape.class,
				double.class,
				double.class
		);
		final long baseline = invokeLong(geometryFingerprint, 0D, 0D, Rail.Shape.QUADRATIC, 0D, 128D);

		Assertions.assertNotEquals(baseline, invokeLong(geometryFingerprint, 0.5D, 0D, Rail.Shape.QUADRATIC, 0D, 128D), "start angle must invalidate cached geometry");
		Assertions.assertNotEquals(baseline, invokeLong(geometryFingerprint, 0D, 0.5D, Rail.Shape.QUADRATIC, 0D, 128D), "end angle must invalidate cached geometry");
		Assertions.assertNotEquals(baseline, invokeLong(geometryFingerprint, 0D, 0D, Rail.Shape.TWO_RADII, 0D, 128D), "shape must invalidate cached geometry");
		Assertions.assertNotEquals(baseline, invokeLong(geometryFingerprint, 0D, 0D, Rail.Shape.QUADRATIC, 64D, 128D), "vertical radius must invalidate cached geometry");
		Assertions.assertNotEquals(baseline, invokeLong(geometryFingerprint, 0D, 0D, Rail.Shape.QUADRATIC, 0D, 129D), "length must invalidate cached geometry");
	}

	@Test
	public void cachedVertexLayoutKeepsPerVertexLightmap() {
		final VertexAttributeMapping mapping = (VertexAttributeMapping) requireStaticField("RAIL_MAPPING");
		Assertions.assertEquals(28, mapping.strideVertex);
		Assertions.assertEquals(VertexAttributeSource.VERTEX_BUFFER, mapping.sources.get(VertexAttributeType.POSITION));
		Assertions.assertEquals(VertexAttributeSource.GLOBAL, mapping.sources.get(VertexAttributeType.COLOR));
		Assertions.assertEquals(VertexAttributeSource.VERTEX_BUFFER, mapping.sources.get(VertexAttributeType.UV_TEXTURE));
		Assertions.assertEquals(VertexAttributeSource.GLOBAL, mapping.sources.get(VertexAttributeType.UV_OVERLAY));
		Assertions.assertEquals(VertexAttributeSource.VERTEX_BUFFER, mapping.sources.get(VertexAttributeType.UV_LIGHTMAP));
		Assertions.assertEquals(VertexAttributeSource.VERTEX_BUFFER, mapping.sources.get(VertexAttributeType.NORMAL));
		Assertions.assertEquals(VertexAttributeSource.GLOBAL, mapping.sources.get(VertexAttributeType.MATRIX_MODEL));
	}

	@Test
	public void writesLegacyDoubleSidedNonDegenerateRailQuadWithSignedTextureOffsets() {
		final Method appendSegmentVertices = requirePackageApi(
				"appendSegmentVertices",
				Consumer.class,
				double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class,
				float.class, int.class
		);
		final List<Vertex> vertices = new ArrayList<>();
		final int light = 0x00F000A0;

		// x1 + z1 = 301, so the legacy texture offset is +0.25.
		invoke(
				appendSegmentVertices,
				(Consumer<Vertex>) vertices::add,
				100D, 60D, 200D,
				101D, 200D, 103D, 200D,
				103D, 204D, 101D, 204D,
				61D, 62D,
				0.065625F, light
		);
		assertNonDegenerateDoubleSidedQuad(vertices, 0.4375F, 0.5625F, light);

		vertices.clear();
		// x1 + z1 = -301, so Java's signed remainder gives a -0.25 offset.
		invoke(
				appendSegmentVertices,
				(Consumer<Vertex>) vertices::add,
				-102D, 60D, -200D,
				-101D, -200D, -99D, -200D,
				-99D, -196D, -101D, -196D,
				61D, 62D,
				0.065625F, light
		);
		assertNonDegenerateDoubleSidedQuad(vertices, -0.0625F, 0.0625F, light);
	}

	@Test
	public void keepsPageLocalFloatPrecisionNearBothWorldBorders() {
		final Method appendSegmentVertices = requirePackageApi(
				"appendSegmentVertices",
				Consumer.class,
				double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class,
				float.class, int.class
		);

		assertPageLocalPrecision(appendSegmentVertices, 29_999_960D);
		assertPageLocalPrecision(appendSegmentVertices, -29_999_960D);
	}

	@Test
	public void preparedPageBoundsCoverLightBlockAcrossSectionBoundary() throws ReflectiveOperationException {
		final Class<?> builderClass = Arrays.stream(requireCacheClass().getDeclaredClasses())
				.filter(candidate -> candidate.getSimpleName().equals("PreparedPageBuilder"))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Missing PreparedPageBuilder"));
		final java.lang.reflect.Constructor<?> constructor = builderClass.getDeclaredConstructor(double.class, double.class, double.class);
		constructor.setAccessible(true);
		final Object builder = constructor.newInstance(2.25D, 15.92D, -3.75D);
		final Method addSegment = builderClass.getDeclaredMethod(
				"addSegment",
				double.class, double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class
		);
		addSegment.setAccessible(true);

		final double x1 = 2.25D;
		final double y1 = 15.92D;
		final double z1 = -3.75D;
		addSegment.invoke(builder, x1, z1, 3.25D, z1, 3.25D, -2.75D, x1, -2.75D, y1, y1);

		final int lightBlockX = (int) Math.floor(x1);
		final int lightBlockY = (int) Math.floor(y1 + requireNumericConstant("LIGHT_REFERENCE_OFFSET").doubleValue());
		final int lightBlockZ = (int) Math.floor(z1);
		final double minX = requireDoubleField(builder, "minX");
		final double minY = requireDoubleField(builder, "minY");
		final double minZ = requireDoubleField(builder, "minZ");
		final double maxX = requireDoubleField(builder, "maxX");
		final double maxY = requireDoubleField(builder, "maxY");
		final double maxZ = requireDoubleField(builder, "maxZ");

		Assertions.assertEquals(16, lightBlockY, "the regression fixture must cross the Y section boundary");
		Assertions.assertAll(
				() -> Assertions.assertTrue(minX <= lightBlockX && maxX >= lightBlockX + 1, "bounds must cover the sampled light block on X"),
				() -> Assertions.assertTrue(minY <= lightBlockY && maxY >= lightBlockY + 1, "bounds must cover the sampled light block on Y"),
				() -> Assertions.assertTrue(minZ <= lightBlockZ && maxZ >= lightBlockZ + 1, "bounds must cover the sampled light block on Z")
		);

		final int sectionX = Math.floorDiv(lightBlockX, 16);
		final int sectionY = Math.floorDiv(lightBlockY, 16);
		final int sectionZ = Math.floorDiv(lightBlockZ, 16);
		Assertions.assertTrue(
				intersects(
						minX, minY, minZ, maxX, maxY, maxZ,
						sectionX * 16D, sectionY * 16D, sectionZ * 16D,
						(sectionX + 1) * 16D, (sectionY + 1) * 16D, (sectionZ + 1) * 16D
				),
				"a light update for the sampled section must intersect the prepared page bounds"
		);
	}

	@Test
	public void pageVisibilityIsConservativeAtDistanceAndCameraPlaneBoundaries() {
		final Method isPageVisible = requirePackageApi(
				"isPageVisible",
				double.class, double.class, double.class,
				double.class, double.class, double.class,
				double.class, double.class, double.class,
				double.class,
				float.class, float.class, float.class, float.class
		);

		Assertions.assertTrue(invokeBoolean(isPageVisible, -1D, -1D, -16D, 1D, 1D, -8D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "near pages stay visible even behind the camera");
		Assertions.assertTrue(invokeBoolean(isPageVisible, -1D, -1D, 63D, 1D, 1D, 65D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "pages in front of the camera render");
		Assertions.assertFalse(invokeBoolean(isPageVisible, -1D, -1D, -65D, 1D, 1D, -63D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "pages wholly behind the camera are culled outside the near radius");
		Assertions.assertTrue(invokeBoolean(isPageVisible, 63D, -1D, -1D, 65D, 1D, 1D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "a page crossing the camera plane must not be falsely culled");
		Assertions.assertFalse(invokeBoolean(isPageVisible, 225D, -1D, 0D, 230D, 1D, 5D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "pages beyond render distance are culled");
		Assertions.assertTrue(invokeBoolean(isPageVisible, 223D, -1D, 0D, 225D, 1D, 5D, 0D, 0D, 0D, 224D, 1F, 0F, 1F, 0F), "pages touching render distance stay visible");
	}

	@Test
	public void proactivePrewarmHooksRunAfterRailDiscoveryAndRetainTheRail() throws Exception {
		final Method finishFrame = requireCacheClass().getDeclaredMethod("finishFrame", ClientWorld.class);
		Assertions.assertAll(
				() -> Assertions.assertTrue(Modifier.isPublic(finishFrame.getModifiers()), "finishFrame must be callable from MainRenderer"),
				() -> Assertions.assertTrue(Modifier.isStatic(finishFrame.getModifiers()), "finishFrame must be static"),
				() -> Assertions.assertEquals(void.class, finishFrame.getReturnType())
		);

		Path mainRendererPath = Path.of("src", "main", "java", "org", "mtr", "mod", "render", "MainRenderer.java");
		if (!Files.exists(mainRendererPath)) {
			mainRendererPath = Path.of("fabric").resolve(mainRendererPath);
		}
		Assertions.assertTrue(Files.exists(mainRendererPath), "MainRenderer source must be available for hook verification");
		final String mainRendererSource = Files.readString(mainRendererPath);
		final int beginFrameIndex = mainRendererSource.indexOf("DefaultRailMeshCache.beginFrame(clientWorld, renderingShadows);");
		final int renderRailsIndex = mainRendererSource.indexOf("RenderRails.render();", beginFrameIndex);
		final int finishFrameIndex = mainRendererSource.indexOf("DefaultRailMeshCache.finishFrame(clientWorld);", renderRailsIndex);
		final int scheduleRenderIndex = mainRendererSource.indexOf("DefaultRailMeshCache.scheduleRender();", finishFrameIndex);
		Assertions.assertTrue(
				beginFrameIndex >= 0 && renderRailsIndex > beginFrameIndex && finishFrameIndex > renderRailsIndex && scheduleRenderIndex > finishFrameIndex,
				"MainRenderer must run beginFrame -> rail discovery -> background finishFrame upload -> scheduleRender"
		);

		final Class<?> preparingEntryClass = requireNestedClass("PreparingEntry");
		final Field railField = preparingEntryClass.getDeclaredField("rail");
		Assertions.assertAll(
				() -> Assertions.assertEquals(Rail.class, railField.getType(), "PreparingEntry must retain the Rail used by proactive preparation"),
				() -> Assertions.assertTrue(Modifier.isFinal(railField.getModifiers()), "the prepared Rail identity must not change while its future is running"),
				() -> Assertions.assertNotNull(preparingEntryClass.getDeclaredConstructor(Rail.class, long.class, Future.class), "PreparingEntry constructor must capture Rail, fingerprint, and future")
		);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void missingPageStateSeparatesVisibleBlockingFromBackgroundPrewarm() throws ReflectiveOperationException {
		setStaticDouble("cameraX", 0);
		setStaticDouble("cameraY", 0);
		setStaticDouble("cameraZ", 0);
		setStaticDouble("renderDistance", 224);
		setStaticFloat("yawCos", 1);
		setStaticFloat("yawSin", 0);
		setStaticFloat("pitchCos", 1);
		setStaticFloat("pitchSin", 0);

		final Class<?> preparedPageClass = requireNestedClass("PreparedPage");
		final Class<?> preparedRailClass = requireNestedClass("PreparedRail");
		final Class<?> cacheEntryClass = requireNestedClass("CacheEntry");
		final Class<?> uploadedPageClass = requireNestedClass("UploadedPage");
		final java.lang.reflect.Constructor<?> preparedPageConstructor = preparedPageClass.getDeclaredConstructor(
				double.class, double.class, double.class,
				double.class, double.class, double.class,
				double.class, double.class, double.class,
				double[].class, int.class, long.class
		);
		preparedPageConstructor.setAccessible(true);
		final Object visiblePage = preparedPageConstructor.newInstance(0D, 0D, 64D, -1D, -1D, 63D, 1D, 1D, 65D, new double[0], 0, 0L);
		final Object offscreenPage = preparedPageConstructor.newInstance(0D, 0D, -64D, -1D, -1D, -65D, 1D, 1D, -63D, new double[0], 0, 0L);

		final java.lang.reflect.Constructor<?> preparedRailConstructor = preparedRailClass.getDeclaredConstructor(ObjectArrayList.class, long.class);
		preparedRailConstructor.setAccessible(true);
		final ObjectArrayList<Object> visiblePreparedPages = new ObjectArrayList<>();
		visiblePreparedPages.add(visiblePage);
		final ObjectArrayList<Object> offscreenPreparedPages = new ObjectArrayList<>();
		offscreenPreparedPages.add(offscreenPage);
		final Object visiblePreparedRail = preparedRailConstructor.newInstance(visiblePreparedPages, 0L);
		final Object offscreenPreparedRail = preparedRailConstructor.newInstance(offscreenPreparedPages, 0L);

		final java.lang.reflect.Constructor<?> cacheEntryConstructor = cacheEntryClass.getDeclaredConstructor(Rail.class, long.class, preparedRailClass);
		cacheEntryConstructor.setAccessible(true);
		final Object visibleEntry = cacheEntryConstructor.newInstance(null, 1L, visiblePreparedRail);
		final Object offscreenEntry = cacheEntryConstructor.newInstance(null, 2L, offscreenPreparedRail);
		final Method hasMissingVisiblePages = cacheEntryClass.getDeclaredMethod("hasMissingVisiblePages");
		hasMissingVisiblePages.setAccessible(true);
		final Method hasMissingPages = cacheEntryClass.getDeclaredMethod("hasMissingPages");
		hasMissingPages.setAccessible(true);

		Assertions.assertTrue(invokeInstanceBoolean(hasMissingVisiblePages, visibleEntry), "a visible null page must block the cached path until upload");
		Assertions.assertTrue(invokeInstanceBoolean(hasMissingPages, visibleEntry), "a visible null page must be available to foreground or background upload");
		Assertions.assertFalse(invokeInstanceBoolean(hasMissingVisiblePages, offscreenEntry), "an offscreen null page must not block already visible cached pages");
		Assertions.assertTrue(invokeInstanceBoolean(hasMissingPages, offscreenEntry), "an offscreen null page must still be discovered by proactive background upload");

		final java.lang.reflect.Constructor<?> uploadedPageConstructor = uploadedPageClass.getDeclaredConstructor(preparedPageClass, Mesh.class, OptimizedModel.class);
		uploadedPageConstructor.setAccessible(true);
		final Object uploadedPage = uploadedPageConstructor.newInstance(visiblePage, null, null);
		final Field pagesField = cacheEntryClass.getDeclaredField("pages");
		pagesField.setAccessible(true);
		((ObjectArrayList<Object>) pagesField.get(visibleEntry)).set(0, uploadedPage);
		Assertions.assertFalse(invokeInstanceBoolean(hasMissingVisiblePages, visibleEntry), "a filled visible page must no longer be reported missing");
		Assertions.assertFalse(invokeInstanceBoolean(hasMissingPages, visibleEntry), "a fully filled entry must not be selected for background upload");
	}

	@Test
	public void identicalGeometryAndUvsRemainDistinctAcrossLightValues() {
		final Method appendSegmentVertices = requirePackageApi(
				"appendSegmentVertices",
				Consumer.class,
				double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class, double.class, double.class,
				double.class, double.class,
				float.class, int.class
		);
		final List<Vertex> first = new ArrayList<>();
		final List<Vertex> second = new ArrayList<>();
		invoke(appendSegmentVertices, (Consumer<Vertex>) first::add, 0D, 0D, 0D, -1D, 0D, 1D, 0D, 1D, 0.5D, -1D, 0.5D, 0D, 0D, 0.065625F, 0x00F000A0);
		invoke(appendSegmentVertices, (Consumer<Vertex>) second::add, 0D, 0D, 0D, -1D, 0D, 1D, 0D, 1D, 0.5D, -1D, 0.5D, 0D, 0D, 0.065625F, 0x00A000F0);

		Assertions.assertEquals(8, first.size());
		Assertions.assertEquals(first.size(), second.size());
		for (int i = 0; i < first.size(); i++) {
			final Vertex firstVertex = first.get(i);
			final Vertex secondVertex = second.get(i);
			Assertions.assertAll(
					() -> Assertions.assertEquals(firstVertex.position, secondVertex.position, "geometry must be identical"),
					() -> Assertions.assertEquals(firstVertex.u, secondVertex.u, "U must be identical"),
					() -> Assertions.assertEquals(firstVertex.v, secondVertex.v, "V must be identical"),
					() -> Assertions.assertNotEquals(firstVertex.light, secondVertex.light, "the fixture must use distinct packed light"),
					() -> Assertions.assertNotEquals(firstVertex, secondVertex, "RawMesh.distinct must not merge differently lit vertices"),
					() -> Assertions.assertEquals(1, firstVertex.normal.getY(), 0),
					() -> Assertions.assertEquals(1, secondVertex.normal.getY(), 0),
					() -> Assertions.assertTrue(Math.abs(firstVertex.normal.getX()) < Float.MIN_NORMAL && Math.abs(firstVertex.normal.getZ()) < Float.MIN_NORMAL, "encoded normal must remain visually equivalent to UP"),
					() -> Assertions.assertTrue(Math.abs(secondVertex.normal.getX()) < Float.MIN_NORMAL && Math.abs(secondVertex.normal.getZ()) < Float.MIN_NORMAL, "encoded normal must remain visually equivalent to UP")
			);
		}
	}

	private static Class<?> requireCacheClass() {
		try {
			return Class.forName(CACHE_CLASS_NAME);
		} catch (ClassNotFoundException exception) {
			return Assertions.fail("Missing default rail mesh cache class: " + CACHE_CLASS_NAME, exception);
		}
	}

	private static Class<?> requireNestedClass(String simpleName) {
		return Arrays.stream(requireCacheClass().getDeclaredClasses())
				.filter(candidate -> candidate.getSimpleName().equals(simpleName))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Missing nested cache class: " + simpleName));
	}

	private static Method requirePackageApi(String name, Class<?>... parameterTypes) {
		final Method method;
		try {
			method = requireCacheClass().getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException exception) {
			return Assertions.fail("Missing default rail mesh cache API: " + name + Arrays.toString(parameterTypes), exception);
		}
		Assertions.assertTrue(Modifier.isStatic(method.getModifiers()), name + " must be static");
		Assertions.assertFalse(Modifier.isPrivate(method.getModifiers()), name + " must be package-visible or public");
		method.setAccessible(true);
		return method;
	}

	private static Number requireNumericConstant(String name) {
		final Field field;
		try {
			field = requireCacheClass().getDeclaredField(name);
			field.setAccessible(true);
			final Object value = field.get(null);
			if (value instanceof Number) {
				return (Number) value;
			}
			return Assertions.fail(name + " must be numeric");
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			return Assertions.fail("Missing default rail mesh cache constant: " + name, exception);
		}
	}

	private static Object requireStaticField(String name) {
		try {
			final Field field = requireCacheClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(null);
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			return Assertions.fail("Missing default rail mesh cache field: " + name, exception);
		}
	}

	private static void setStaticDouble(String name, double value) throws ReflectiveOperationException {
		final Field field = requireCacheClass().getDeclaredField(name);
		field.setAccessible(true);
		field.setDouble(null, value);
	}

	private static void setStaticFloat(String name, float value) throws ReflectiveOperationException {
		final Field field = requireCacheClass().getDeclaredField(name);
		field.setAccessible(true);
		field.setFloat(null, value);
	}

	private static double requireDoubleField(Object instance, String name) {
		try {
			final Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.getDouble(instance);
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			return Assertions.fail("Missing double field: " + name, exception);
		}
	}

	private static void assertNonDegenerateDoubleSidedQuad(List<Vertex> vertices, float v1, float v2, int light) {
		Assertions.assertEquals(8, vertices.size());
		assertVertex(vertices.get(0), 1, 1.065625F, 0, 0, v2, light);
		assertVertex(vertices.get(1), 3, 1.065625F + IGui.SMALL_OFFSET, 0, 1, v2, light);
		assertVertex(vertices.get(2), 3, 2.065625F, 4, 1, v1, light);
		assertVertex(vertices.get(3), 1, 2.065625F + IGui.SMALL_OFFSET, 4, 0, v1, light);
		assertVertex(vertices.get(4), 3, 1.065625F + IGui.SMALL_OFFSET, 0, 0, v2, light);
		assertVertex(vertices.get(5), 1, 1.065625F, 0, 1, v2, light);
		assertVertex(vertices.get(6), 1, 2.065625F + IGui.SMALL_OFFSET, 4, 1, v1, light);
		assertVertex(vertices.get(7), 3, 2.065625F, 4, 0, v1, light);

		final float firstWinding = windingY(vertices.get(0), vertices.get(1), vertices.get(2));
		final float secondWinding = windingY(vertices.get(4), vertices.get(5), vertices.get(6));
		Assertions.assertNotEquals(0, firstWinding, "the golden quad must have non-zero area");
		Assertions.assertEquals(-Math.signum(firstWinding), Math.signum(secondWinding), "the back face must reverse the front-face winding");
	}

	private static void assertPageLocalPrecision(Method appendSegmentVertices, double worldOrigin) {
		final List<Vertex> vertices = new ArrayList<>();
		invoke(
				appendSegmentVertices,
				(Consumer<Vertex>) vertices::add,
				worldOrigin, 64D, worldOrigin,
				worldOrigin + 0.125D, worldOrigin + 0.25D,
				worldOrigin + 1.875D, worldOrigin + 0.25D,
				worldOrigin + 1.875D, worldOrigin + 4.75D,
				worldOrigin + 0.125D, worldOrigin + 4.75D,
				64.375D, 64.875D,
				0.065625F, 0x00F000A0
		);

		Assertions.assertEquals(8, vertices.size());
		assertPosition(vertices.get(0), 0.125F, 0.440625F, 0.25F);
		assertPosition(vertices.get(1), 1.875F, 0.440625F + IGui.SMALL_OFFSET, 0.25F);
		assertPosition(vertices.get(2), 1.875F, 0.940625F, 4.75F);
		assertPosition(vertices.get(3), 0.125F, 0.940625F + IGui.SMALL_OFFSET, 4.75F);
		assertPosition(vertices.get(4), 1.875F, 0.440625F + IGui.SMALL_OFFSET, 0.25F);
		assertPosition(vertices.get(5), 0.125F, 0.440625F, 0.25F);
		assertPosition(vertices.get(6), 0.125F, 0.940625F + IGui.SMALL_OFFSET, 4.75F);
		assertPosition(vertices.get(7), 1.875F, 0.940625F, 4.75F);
	}

	private static void assertPosition(Vertex vertex, float x, float y, float z) {
		Assertions.assertAll(
				() -> Assertions.assertEquals(x, vertex.position.getX(), 0.00001F),
				() -> Assertions.assertEquals(y, vertex.position.getY(), 0.00001F),
				() -> Assertions.assertEquals(z, vertex.position.getZ(), 0.00001F)
		);
	}

	private static float windingY(Vertex first, Vertex second, Vertex third) {
		final float firstX = second.position.getX() - first.position.getX();
		final float firstZ = second.position.getZ() - first.position.getZ();
		final float secondX = third.position.getX() - first.position.getX();
		final float secondZ = third.position.getZ() - first.position.getZ();
		return firstZ * secondX - firstX * secondZ;
	}

	private static boolean intersects(
			double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
			double otherMinX, double otherMinY, double otherMinZ, double otherMaxX, double otherMaxY, double otherMaxZ
	) {
		return minX <= otherMaxX && maxX >= otherMinX && minY <= otherMaxY && maxY >= otherMinY && minZ <= otherMaxZ && maxZ >= otherMinZ;
	}

	private static void assertVertex(Vertex vertex, float x, float y, float z, float u, float v, int light) {
		Assertions.assertAll(
				() -> Assertions.assertEquals(x, vertex.position.getX(), 0.00001F),
				() -> Assertions.assertEquals(y, vertex.position.getY(), 0.00001F),
				() -> Assertions.assertEquals(z, vertex.position.getZ(), 0.00001F),
				() -> Assertions.assertEquals(0, vertex.normal.getX(), 0.00001F),
				() -> Assertions.assertEquals(1, vertex.normal.getY(), 0.00001F),
				() -> Assertions.assertEquals(0, vertex.normal.getZ(), 0.00001F),
				() -> Assertions.assertEquals(u, vertex.u, 0.00001F),
				() -> Assertions.assertEquals(v, vertex.v, 0.00001F),
				() -> Assertions.assertEquals(light, vertex.light)
		);
	}

	private static boolean invokeBoolean(Method method, Object... arguments) {
		final Object value = invoke(method, arguments);
		return value instanceof Boolean ? (Boolean) value : Assertions.fail(method.getName() + " must return boolean");
	}

	private static boolean invokeInstanceBoolean(Method method, Object instance, Object... arguments) {
		try {
			final Object value = method.invoke(instance, arguments);
			return value instanceof Boolean ? (Boolean) value : Assertions.fail(method.getName() + " must return boolean");
		} catch (IllegalAccessException | InvocationTargetException exception) {
			return Assertions.fail("Default rail mesh cache instance API failed: " + method.getName(), exception);
		}
	}

	private static int invokeInt(Method method, Object... arguments) {
		final Object value = invoke(method, arguments);
		return value instanceof Number ? ((Number) value).intValue() : Assertions.fail(method.getName() + " must return an integer");
	}

	private static long invokeLong(Method method, Object... arguments) {
		final Object value = invoke(method, arguments);
		return value instanceof Number ? ((Number) value).longValue() : Assertions.fail(method.getName() + " must return a long");
	}

	private static Object invoke(Method method, Object... arguments) {
		try {
			return method.invoke(null, arguments);
		} catch (IllegalAccessException | InvocationTargetException exception) {
			return Assertions.fail("Default rail mesh cache API failed: " + method.getName(), exception);
		}
	}
}
