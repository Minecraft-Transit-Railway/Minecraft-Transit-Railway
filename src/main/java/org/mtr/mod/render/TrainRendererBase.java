package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.Config;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TrainRendererBase {

	protected static Camera camera;
	protected static ClientWorld clientWorld;
	protected static float lastFrameDuration;
	protected static GraphicsHolder graphicsHolder;

	protected static boolean isTranslucentBatch;

	private static Entity cameraEntity;
	private static ClientPlayerEntity clientPlayerEntity;
	private static Vector3d playerEyePosition;

	public abstract void renderCar(int carIndex, double x, double y, double z, float yaw, float pitch, boolean doorLeftOpen, boolean doorRightOpen);

	public abstract void renderConnection(Vector3d prevPos1, Vector3d prevPos2, Vector3d prevPos3, Vector3d prevPos4, Vector3d thisPos1, Vector3d thisPos2, Vector3d thisPos3, Vector3d thisPos4, double x, double y, double z, float yaw, float pitch);

	public abstract void renderBarrier(Vector3d prevPos1, Vector3d prevPos2, Vector3d prevPos3, Vector3d prevPos4, Vector3d thisPos1, Vector3d thisPos2, Vector3d thisPos3, Vector3d thisPos4, double x, double y, double z, float yaw, float pitch);

	public static void renderRidingPlayer(Vector3d viewOffset, UUID playerId, Vector3d playerPositionOffset) {
		final BlockPos posAverage = applyAverageTransform(viewOffset, playerPositionOffset.getXMapped(), playerPositionOffset.getYMapped(), playerPositionOffset.getZMapped());
		if (posAverage == null) {
			return;
		}
		graphicsHolder.translate(0, RenderTrains.PLAYER_RENDER_OFFSET, 0);
		final PlayerEntity renderPlayer = clientWorld.getPlayerByUuid(playerId);
		if (renderPlayer != null && (!playerId.equals(clientPlayerEntity.getUuid()) || camera.isThirdPerson())) {
			graphicsHolder.renderEntity(new Entity(renderPlayer.data), playerPositionOffset.getXMapped(), playerPositionOffset.getYMapped(), playerPositionOffset.getZMapped(), 0, 1, 0xF000F0);
		}
		graphicsHolder.pop();
	}

	public static void setupStaticInfo(GraphicsHolder graphicsHolder2) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		camera = minecraftClient.getGameRendererMapped().getCamera();
		clientWorld = minecraftClient.getWorldMapped();
		clientPlayerEntity = minecraftClient.getPlayerMapped();
		lastFrameDuration = minecraftClient.getLastFrameDuration();
		graphicsHolder = graphicsHolder2;
		cameraEntity = minecraftClient.getCameraEntityMapped();
		playerEyePosition = clientPlayerEntity == null ? Vector3d.getZeroMapped() : clientPlayerEntity.getCameraPosVec(minecraftClient.getTickDelta());
	}

	public static void setBatch(boolean isTranslucentBatch) {
		TrainRendererBase.isTranslucentBatch = isTranslucentBatch;
	}

	public static BlockPos applyAverageTransform(@Nullable Vector3d viewOffset, double x, double y, double z) {
		final boolean noOffset = viewOffset == null;
		final Vector3d cameraPos = cameraEntity == null ? null : cameraEntity.getPos();
		final BlockPos posAverage = InitClient.newBlockPos(x + (noOffset || cameraPos == null ? 0 : cameraPos.getXMapped()), y + (noOffset || cameraPos == null ? 0 : cameraPos.getYMapped()), z + (noOffset || cameraPos == null ? 0 : cameraPos.getZMapped()));

		if (RenderTrains.shouldNotRender(posAverage, MinecraftClientHelper.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null)) {
			return null;
		}

		graphicsHolder.push();
		if (viewOffset != null) {
			final Vector3d cameraOffset = camera.isThirdPerson() ? playerEyePosition : camera.getPos();
			final float cameraYaw = camera.getYaw();
			graphicsHolder.translate(cameraOffset.getXMapped(), cameraOffset.getYMapped(), cameraOffset.getZMapped());
			graphicsHolder.rotateYDegrees(EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) - cameraYaw + (Math.abs(EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) - cameraYaw) > 90 ? 180 : 0));
			graphicsHolder.translate(-viewOffset.getXMapped(), -viewOffset.getYMapped(), -viewOffset.getZMapped());
		}

		return posAverage;
	}
}
