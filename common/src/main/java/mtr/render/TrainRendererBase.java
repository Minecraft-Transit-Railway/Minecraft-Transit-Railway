package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.data.TrainClient;
import mtr.mappings.Utilities;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public abstract class TrainRendererBase {

    TrainClient train;

    PoseStack matrices;
    MultiBufferSource vertexConsumers;
    Vec3 viewOffset;

    protected static Minecraft client;
    protected static Camera camera;
    protected static Entity cameraEntity;
    protected static Level world;
    protected static LocalPlayer player;
    protected static float lastFrameDuration;

    public final TrainRendererBase createTrainInstance(TrainClient train) {
        try {
            TrainRendererBase newInstance = this.getClass().getDeclaredConstructor().newInstance();
            newInstance.copyFrom(this);
            newInstance.train = train;
            return newInstance;
        } catch (Exception ex) {
            // This should not happen
            ex.printStackTrace();
            return null;
        }
    }

    protected abstract void copyFrom(TrainRendererBase src);

    public static void setupStaticInfo() {
        client = Minecraft.getInstance();
        camera = client.gameRenderer.getMainCamera();
        cameraEntity = client.cameraEntity;
        world = client.level;
        player = client.player;
        lastFrameDuration = MTRClient.getLastFrameDuration();
    }

    public void setupRender(PoseStack matrices, MultiBufferSource vertexConsumers) {
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;

        this.viewOffset = train.offset.isEmpty() ? null : new Vec3(train.offset.get(3), train.offset.get(4), train.offset.get(5));

        final float cameraYaw = camera.getYRot();
        final Vec3 cameraOffset = camera.isDetached() ? player.getEyePosition(client.getFrameTime()) : camera.getPosition();
        final boolean secondF5 = Math.abs(Utilities.getYaw(player) - cameraYaw) > 90;

        matrices.pushPose();
        if (viewOffset != null) {
            matrices.translate(cameraOffset.x, cameraOffset.y, cameraOffset.z);
            matrices.mulPose(Vector3f.YP.rotationDegrees(Utilities.getYaw(player) - cameraYaw + (secondF5 ? 180 : 0)));
            matrices.translate(-viewOffset.x, -viewOffset.y, -viewOffset.z);
        }
    }

    public void finishRender() {
        matrices.popPose();
    }

    public abstract void renderCar(int carIndex, double x, double y, double z, float yaw, float pitch, boolean isTranslucentBatch,
                                 boolean doorLeftOpen, boolean doorRightOpen);

    public abstract void renderConnection(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4,
                                 double x, double y, double z, float yaw);

    public abstract void renderRidingPlayer(UUID playerId, Vec3 playerPositionOffset);

    protected BlockPos getPosAverage(double x, double y, double z) {
        final boolean noOffset = viewOffset == null;
        final Entity camera = cameraEntity;
        final Vec3 cameraPos = camera == null ? null : camera.position();
        return new BlockPos(x + (noOffset || cameraPos == null ? 0 : cameraPos.x), y + (noOffset || cameraPos == null ? 0 : cameraPos.y), z + (noOffset || cameraPos == null ? 0 : cameraPos.z));
    }
}
