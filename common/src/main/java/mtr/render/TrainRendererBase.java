package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.data.TrainClient;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public abstract class TrainRendererBase {

	protected TrainClient train;
	protected PoseStack matrices;
	protected MultiBufferSource vertexConsumers;
	private Vec3 viewOffset;

	protected static Minecraft client;
	protected static Camera camera;
	protected static Entity cameraEntity;
	protected static Level world;
	protected static LocalPlayer player;
	protected static float lastFrameDuration;

	public void setupRender(TrainClient train, PoseStack matrices, MultiBufferSource vertexConsumers, EntitySeat entity, float tickDelta) {
		this.train = train;
		this.matrices = matrices;
		this.vertexConsumers = vertexConsumers;

		viewOffset = train.offset.isEmpty() ? null : new Vec3(train.offset.get(3), train.offset.get(4), train.offset.get(5));

		final float cameraYaw = camera.getYRot();
		final Vec3 cameraOffset = camera.isDetached() ? player.getEyePosition(client.getFrameTime()) : camera.getPosition();
		final boolean secondF5 = Math.abs(Utilities.getYaw(player) - cameraYaw) > 90;
		final double entityX = entity == null ? 0 : Mth.lerp(tickDelta, entity.xOld, entity.getX());
		final double entityY = entity == null ? 0 : Mth.lerp(tickDelta, entity.yOld, entity.getY());
		final double entityZ = entity == null ? 0 : Mth.lerp(tickDelta, entity.zOld, entity.getZ());

		matrices.pushPose();
		if (viewOffset != null) {
			final double offsetX;
			final double offsetY;
			final double offsetZ;
			if (MTRClient.isVivecraft() && entity != null) {
				offsetX = entityX;
				offsetY = entityY;
				offsetZ = entityZ;
			} else {
				offsetX = cameraOffset.x;
				offsetY = cameraOffset.y;
				offsetZ = cameraOffset.z;
			}
			matrices.translate(offsetX, offsetY, offsetZ);
			matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (secondF5 ? 180 : 0)));
			matrices.translate(-viewOffset.x, -viewOffset.y, -viewOffset.z);
		}
	}

	public void finishRender() {
		matrices.popPose();
	}

	protected BlockPos getPosAverage(double x, double y, double z) {
		final boolean noOffset = viewOffset == null;
		final Entity camera = cameraEntity;
		final Vec3 cameraPos = camera == null ? null : camera.position();
		return new BlockPos(x + (noOffset || cameraPos == null ? 0 : cameraPos.x), y + (noOffset || cameraPos == null ? 0 : cameraPos.y), z + (noOffset || cameraPos == null ? 0 : cameraPos.z));
	}

	public abstract void renderCar(int carIndex, double x, double y, double z, float yaw, float pitch, boolean isTranslucentBatch, boolean doorLeftOpen, boolean doorRightOpen);

	public abstract void renderConnection(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch);

	public abstract void renderBarrier(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch);

	public abstract void renderRidingPlayer(UUID playerId, Vec3 playerPositionOffset);

	public static void setupStaticInfo() {
		client = Minecraft.getInstance();
		camera = client.gameRenderer.getMainCamera();
		cameraEntity = client.cameraEntity;
		world = client.level;
		player = client.player;
		lastFrameDuration = MTRClient.getLastFrameDuration();
	}
}
