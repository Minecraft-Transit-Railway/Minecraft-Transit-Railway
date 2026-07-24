package org.mtr.mod.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Random;

public final class RenderRailVisibilityTest {

	private static final float[] SINE_TABLE = new float[65536];

	static {
		for (int i = 0; i < SINE_TABLE.length; i++) {
			SINE_TABLE[i] = (float) Math.sin(i * Math.PI * 2 / SINE_TABLE.length);
		}
	}

	@Test
	public void testDistanceBoundaries() {
		final double[] renderDistances = {16, 32, 224, 512};
		for (final double renderDistance : renderDistances) {
			assertEquivalent(renderDistance, 0, 0, 0, 0, 0, renderDistance, 0, 0);
			assertEquivalent(Math.nextDown(renderDistance), 0, 0, 0, 0, 0, renderDistance, 0, 0);
			assertEquivalent(Math.nextUp(renderDistance), 0, 0, 0, 0, 0, renderDistance, 0, 0);
			assertEquivalent(-renderDistance, 0, 0, 0, 0, 0, renderDistance, 180, 0);
			assertEquivalent(Math.nextUp(-renderDistance), 0, 0, 0, 0, 0, renderDistance, 180, 0);
			assertEquivalent(Math.nextDown(-renderDistance), 0, 0, 0, 0, 0, renderDistance, 180, 0);
		}

		final double[] nearDistances = {Math.nextDown(32), 32, Math.nextUp(32)};
		for (final double distance : nearDistances) {
			assertEquivalent(distance, 0, 0, 0, 0, 0, 224, 0, 0);
			assertEquivalent(0, 0, distance, 0, 0, 0, 224, 90, 0);
		}

		final Random random = new Random(0x424F554E44415259L);
		for (int i = 0; i < 100000; i++) {
			final boolean nearBoundary = random.nextBoolean();
			final double renderDistance = nearBoundary ? 224 : 16 * (2 + random.nextInt(31));
			final double threshold = nearBoundary ? 32 : renderDistance;
			final double angle = random.nextDouble() * Math.PI * 2;
			double distance = threshold;
			final int adjacentSteps = random.nextInt(17) - 8;
			for (int step = 0; step < Math.abs(adjacentSteps); step++) {
				distance = Math.nextAfter(distance, adjacentSteps < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
			}
			assertEquivalent(Math.cos(angle) * distance, 0, Math.sin(angle) * distance, 0, 0, 0, renderDistance, 0, 0);
		}
	}

	@Test
	public void testFacingBoundaries() {
		assertEquivalent(64, 0, Math.nextDown(0D), 0, 0, 0, 224, 0, 0);
		assertEquivalent(64, 0, 0, 0, 0, 0, 224, 0, 0);
		assertEquivalent(64, 0, Math.nextUp(0D), 0, 0, 0, 224, 0, 0);
		assertEquivalent(64, Math.nextDown(0D), 0, 0, 0, 0, 224, 0, 90);
		assertEquivalent(64, 0, 0, 0, 0, 0, 224, 0, 90);
		assertEquivalent(64, Math.nextUp(0D), 0, 0, 0, 0, 224, 0, 90);

		final float[] yaws = {-1080, -360, -180, -90, -45, 0, 45, 90, 180, 360, 1080};
		final float[] pitches = {-90, -89.999F, -45, 0, 45, 89.999F, 90};
		for (final float yaw : yaws) {
			for (final float pitch : pitches) {
				assertEquivalent(64, 0, 0, 0, 0, 0, 224, yaw, pitch);
				assertEquivalent(0, 0, 64, 0, 0, 0, 224, yaw, pitch);
				assertEquivalent(-64, 0, 0, 0, 0, 0, 224, yaw, pitch);
				assertEquivalent(0, 0, -64, 0, 0, 0, 224, yaw, pitch);
				assertEquivalent(64, Math.nextDown(0D), 0, 0, 0, 0, 224, yaw, pitch);
				assertEquivalent(64, Math.nextUp(0D), 0, 0, 0, 0, 224, yaw, pitch);
			}
		}
	}

	@Test
	public void testCoordinatesAndRandomizedInputs() {
		assertEquivalent(-1000000, 255, 1000000, -1000064, -64, 1000000, 224, 0, 0);
		assertEquivalent(30000000, -2048, -30000000, 29999800, 4096, -30000000, 512, 180, -89.999F);
		assertEquivalent(-30000000, 4096, 30000000, -29999800, -2048, 30000000, 512, -180, 89.999F);

		final Random random = new Random(0x4D54525241494CL);
		for (int i = 0; i < 100000; i++) {
			final double cameraX = randomCoordinate(random);
			final double cameraY = randomCoordinate(random) / 64;
			final double cameraZ = randomCoordinate(random);
			final double x = cameraX + randomOffset(random);
			final double y = cameraY + randomOffset(random);
			final double z = cameraZ + randomOffset(random);
			final double renderDistance = 16 * (2 + random.nextInt(31));
			final float yaw = -1080 + random.nextFloat() * 2160;
			final float pitch = -90 + random.nextFloat() * 180;
			assertEquivalent(x, y, z, cameraX, cameraY, cameraZ, renderDistance, yaw, pitch);
		}
	}

	private static double randomCoordinate(Random random) {
		return (random.nextDouble() - 0.5) * 60000000;
	}

	private static double randomOffset(Random random) {
		return (random.nextDouble() - 0.5) * 2048;
	}

	private static void assertEquivalent(double x, double y, double z, double cameraX, double cameraY, double cameraZ, double renderDistance, float yawDegrees, float pitchDegrees) {
		final float yaw = (float) Math.toRadians(yawDegrees);
		final float pitch = (float) Math.toRadians(pitchDegrees);
		final float yawCos = minecraftCos(yaw);
		final float yawSin = minecraftSin(yaw);
		final float pitchCos = minecraftCos(pitch);
		final float pitchSin = minecraftSin(pitch);
		final boolean actual = RailVisibility.shouldRender(
				x, y, z,
				cameraX, cameraY, cameraZ,
				renderDistance, renderDistance * renderDistance,
				yawCos, yawSin,
				pitchCos, pitchSin
		);
		final boolean expected = legacyShouldRender(x, y, z, cameraX, cameraY, cameraZ, renderDistance, yaw, pitch);
		Assertions.assertEquals(expected, actual, () -> String.format(
				"x=%s y=%s z=%s camera=(%s,%s,%s) renderDistance=%s yaw=%s pitch=%s",
				x, y, z, cameraX, cameraY, cameraZ, renderDistance, yawDegrees, pitchDegrees
		));
	}

	private static boolean legacyShouldRender(double x, double y, double z, double cameraX, double cameraY, double cameraZ, double renderDistance, float yaw, float pitch) {
		final double differenceX = x - cameraX;
		final double differenceY = y - cameraY;
		final double differenceZ = z - cameraZ;
		final double distanceToCamera = Math.sqrt(differenceX * differenceX + differenceZ * differenceZ);
		if (distanceToCamera <= renderDistance) {
			if (distanceToCamera < 32) {
				return true;
			}
			final double yawRotatedZ = differenceZ * minecraftCos(yaw) - differenceX * minecraftSin(yaw);
			return yawRotatedZ * minecraftCos(pitch) - differenceY * minecraftSin(pitch) > 0;
		}
		return false;
	}

	private static float minecraftSin(float value) {
		return SINE_TABLE[(int) (value * 10430.378F) & 65535];
	}

	private static float minecraftCos(float value) {
		return SINE_TABLE[(int) (value * 10430.378F + 16384) & 65535];
	}
}
