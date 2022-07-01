package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.client.Config;
import mtr.data.TrainClient;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public abstract class TrainRendererBase {

	protected static Camera camera;
	protected static EntityRenderDispatcher entityRenderDispatcher;
	protected static Level world;
	protected static LocalPlayer player;
	protected static float lastFrameDuration;
	protected static PoseStack matrices;
	protected static MultiBufferSource vertexConsumers;

	private static Entity cameraEntity;
	private static boolean hasEntity;
	private static double entityX;
	private static double entityY;
	private static double entityZ;
	private static Vec3 playerEyePosition;

	protected BlockPos getPosAverage(TrainClient train, double x, double y, double z) {
		final Vec3 viewOffset = train.getViewOffset();
		final boolean noOffset = viewOffset == null;
		final Vec3 cameraPos = cameraEntity == null ? null : cameraEntity.position();
		final BlockPos posAverage = new BlockPos(x + (noOffset || cameraPos == null ? 0 : cameraPos.x), y + (noOffset || cameraPos == null ? 0 : cameraPos.y), z + (noOffset || cameraPos == null ? 0 : cameraPos.z));

		if (RenderTrains.shouldNotRender(posAverage, UtilitiesClient.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null)) {
			return null;
		}

		matrices.pushPose();
		if (viewOffset != null) {
			final double offsetX;
			final double offsetY;
			final double offsetZ;
			if (MTRClient.isVivecraft() && hasEntity) {
				offsetX = entityX;
				offsetY = entityY;
				offsetZ = entityZ;
			} else {
				final Vec3 cameraOffset = camera.isDetached() ? playerEyePosition : camera.getPosition();
				offsetX = cameraOffset.x;
				offsetY = cameraOffset.y;
				offsetZ = cameraOffset.z;
			}
			final float cameraYaw = camera.getYRot();
			matrices.translate(offsetX, offsetY, offsetZ);
			matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (Math.abs(Utilities.getYaw(player) - cameraYaw) > 90 ? 180 : 0)));
			matrices.translate(-viewOffset.x, -viewOffset.y, -viewOffset.z);
		}

		return posAverage;
	}

	public abstract void renderCar(TrainClient train, int carIndex, double x, double y, double z, float yaw, float pitch, boolean isTranslucentBatch, float doorLeftValue, float doorRightValue, boolean opening, boolean head1IsFront);

	public abstract void renderConnection(TrainClient train, Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch);

	public abstract void renderBarrier(TrainClient train, Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch);

	public abstract void renderRidingPlayer(TrainClient train, UUID playerId, Vec3 playerPositionOffset);

	public static void setupStaticInfo(PoseStack matrices, MultiBufferSource vertexConsumers, EntitySeat entity, float tickDelta) {
		final Minecraft client = Minecraft.getInstance();
		camera = client.gameRenderer.getMainCamera();
		entityRenderDispatcher = client.getEntityRenderDispatcher();
		world = client.level;
		player = client.player;
		lastFrameDuration = MTRClient.getLastFrameDuration();
		TrainRendererBase.matrices = matrices;
		TrainRendererBase.vertexConsumers = vertexConsumers;
		cameraEntity = client.cameraEntity;
		hasEntity = entity != null;
		entityX = hasEntity ? Mth.lerp(tickDelta, entity.xOld, entity.getX()) : 0;
		entityY = hasEntity ? Mth.lerp(tickDelta, entity.yOld, entity.getY()) : 0;
		entityZ = hasEntity ? Mth.lerp(tickDelta, entity.zOld, entity.getZ()) : 0;
		playerEyePosition = player == null ? Vec3.ZERO : player.getEyePosition(client.getFrameTime());
	}
}
