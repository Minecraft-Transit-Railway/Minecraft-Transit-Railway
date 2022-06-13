package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.client.TrainClientRegistry;
import mtr.data.*;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelCableCarGrip;
import mtr.model.ModelTrainBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;

public class JonModelTrainRenderer extends TrainRendererBase implements IGui {

    public ModelTrainBase model;
    public String textureId;

    public JonModelTrainRenderer() {
        // A constructor without arguments is used by createTrainInstance via reflection.
    }

    public JonModelTrainRenderer(ModelTrainBase model, String textureId) {
        this.model = model;
        this.textureId = textureId;
    }

    protected void copyFrom(TrainRendererBase srcBase) {
        JonModelTrainRenderer src = (JonModelTrainRenderer)srcBase;
        this.model = src.model;
        this.textureId = src.textureId;
    }

    private static final EntityModel<Minecart> MODEL_MINECART = UtilitiesClient.getMinecartModel();
    private static final EntityModel<Boat> MODEL_BOAT = UtilitiesClient.getBoatModel();
    private static final Map<Long, FakeBoat> BOATS = new HashMap<>();
    private static final ModelCableCarGrip MODEL_CABLE_CAR_GRIP = new ModelCableCarGrip();

    private static class FakeBoat extends Boat {

        private float progress;

        public FakeBoat() {
            super(EntityType.BOAT, null);
        }

        @Override
        public float getRowingTime(int paddle, float newProgress) {
            progress += newProgress;
            return progress;
        }
    }

    @Override
    public void renderCar(int carIndex, double x, double y, double z, float yaw, float pitch, boolean isTranslucentBatch,
                          boolean doorLeftOpen, boolean doorRightOpen) {
        final BlockPos posAverage = getPosAverage(x, y, z);
        if (RenderTrains.shouldNotRender(posAverage, UtilitiesClient.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null))
            return;

        String trainId = train.trainId;
        TrainType baseTrainType = train.baseTrainType;
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);

        if (model == null && isTranslucentBatch) {
            return;
        }

        matrices.pushPose();
        matrices.translate(x, y, z);
        matrices.mulPose(Vector3f.YP.rotation((float) Math.PI + yaw));
        matrices.mulPose(Vector3f.XP.rotation((float) Math.PI + (baseTrainType.transportMode.hasPitch ? pitch : 0)));

        final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

        final boolean isEnd1Head = carIndex == 0;
        final boolean isEnd2Head = carIndex == train.trainCars - 1;
        final boolean head1IsFront = !train.reversed;
        final float doorValue = Math.abs(train.rawDoorValue);
        final float doorLeftValue = doorLeftOpen ? doorValue : 0;
        final float doorRightValue = doorRightOpen ? doorValue : 0;
        final boolean opening = train.rawDoorValue > 0;
        final boolean lightsOn = train.isOnRoute;

        if (model == null || textureId == null) {
            final boolean isBoat = baseTrainType.transportMode == TransportMode.BOAT;

            matrices.translate(0, isBoat ? 0.875 : 0.5, 0);
            matrices.mulPose(Vector3f.YP.rotationDegrees(90));

            final EntityModel<? extends Entity> model = isBoat ? MODEL_BOAT : MODEL_MINECART;
            final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(resolveTexture(trainProperties, textureId -> textureId + ".png")));

            if (isBoat) {
                if (!BOATS.containsKey(train.id)) {
                    BOATS.put(train.id, new FakeBoat());
                }
                MODEL_BOAT.setupAnim(BOATS.get(train.id), (train.getSpeed() + Train.ACCELERATION_DEFAULT) * (doorLeftValue == 0 && doorRightValue == 0 ? lastFrameDuration : 0), 0, -0.1F, 0, 0);
            } else {
                model.setupAnim(null, 0, 0, -0.1F, 0, 0);
            }

            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        } else {
            final boolean renderDetails = MTRClient.isReplayMod() || posAverage.distSqr(camera.getBlockPosition()) <= RenderTrains.DETAIL_RADIUS_SQUARED;
            model.render(matrices, vertexConsumers, resolveTexture(trainProperties, textureId -> textureId + ".png"), light, doorLeftValue, doorRightValue, opening, isEnd1Head, isEnd2Head, head1IsFront, lightsOn, isTranslucentBatch, renderDetails);
        }

        if (baseTrainType.transportMode == TransportMode.CABLE_CAR) {
            matrices.translate(0, TransportMode.CABLE_CAR.railOffset + 0.5, 0);
            if (!baseTrainType.transportMode.hasPitch) {
                matrices.mulPose(Vector3f.XP.rotation(pitch));
            }
            if (baseTrainType.toString().endsWith("_RHT")) {
                matrices.mulPose(Vector3f.YP.rotationDegrees(180));
            }
            MODEL_CABLE_CAR_GRIP.render(matrices, vertexConsumers, light);
        }

        matrices.popPose();
    }

    @Override
    public void renderConnection(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw) {
        final BlockPos posAverage = getPosAverage(x, y, z);
        if (RenderTrains.shouldNotRender(posAverage, UtilitiesClient.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null))
            return;

        String trainId = train.trainId;
        TrainType baseTrainType = train.baseTrainType;
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId, baseTrainType);

        final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

        final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(trainProperties, "exterior")));
        drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
        drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
        drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos3, prevPos2, light);
        drawTexture(matrices, vertexConsumerExterior, prevPos1, thisPos4, thisPos1, prevPos4, light);

        final boolean lightsOn = train.isOnRoute;
        final int lightOnLevel = lightsOn ? RenderTrains.MAX_LIGHT_INTERIOR : light;
        final VertexConsumer vertexConsumerSide = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "side")));
        drawTexture(matrices, vertexConsumerSide, thisPos3, prevPos2, prevPos1, thisPos4, lightOnLevel);
        drawTexture(matrices, vertexConsumerSide, prevPos3, thisPos2, thisPos1, prevPos4, lightOnLevel);
        drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "roof"))), prevPos2, thisPos3, thisPos2, prevPos3, lightOnLevel);
        drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, "floor"))), prevPos4, thisPos1, thisPos4, prevPos1, lightOnLevel);
    }

    @Override
    public void renderRidingPlayer(UUID playerId, Vec3 playerPositionOffset) {
        final EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        matrices.pushPose();
        matrices.translate(0, RenderTrains.PLAYER_RENDER_OFFSET, 0);
        final Player renderPlayer = world.getPlayerByUUID(playerId);
        if (renderPlayer != null && (!playerId.equals(player.getUUID()) || camera.isDetached())) {
            entityRenderDispatcher.render(renderPlayer, playerPositionOffset.x, playerPositionOffset.y, playerPositionOffset.z, 0, 1, matrices, vertexConsumers, 0xF000F0);
        }
        matrices.popPose();
    }

    private ResourceLocation getConnectorTextureString(TrainClientRegistry.TrainProperties trainProperties, String connectorPart) {
        return resolveTexture(trainProperties, textureId -> textureId + "_connector_" + connectorPart + ".png");
    }

    public ResourceLocation resolveTexture(TrainClientRegistry.TrainProperties trainProperties, Function<String, String> formatter) {
        final String textureString = formatter.apply(textureId);
        final ResourceLocation id = new ResourceLocation(textureString);
        final boolean available;

        if (!RenderTrains.AVAILABLE_TEXTURES.contains(textureString) && !RenderTrains.UNAVAILABLE_TEXTURES.contains(textureString)) {
            available = Minecraft.getInstance().getResourceManager().hasResource(id);
            (available ? RenderTrains.AVAILABLE_TEXTURES : RenderTrains.UNAVAILABLE_TEXTURES).add(textureString);
            if (!available) {
                System.out.println("Texture " + textureString + " not found, using default");
            }
        } else {
            available = RenderTrains.AVAILABLE_TEXTURES.contains(textureString);
        }

        if (available) {
            return id;
        } else {
            final TrainRendererBase renderer = TrainClientRegistry.getTrainProperties(trainProperties.baseTrainType.toString(), trainProperties.baseTrainType).renderer;
            if (renderer instanceof JonModelTrainRenderer jonModelTrainRenderer) {
                final String textureId = jonModelTrainRenderer.textureId;
                return new ResourceLocation(textureId == null ? "mtr:textures/block/transparent.png" : formatter.apply(textureId));
            } else {
                return new ResourceLocation("mtr:textures/block/transparent.png");
            }
        }
    }

    private static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, Vec3 pos1, Vec3 pos2, Vec3 pos3, Vec3 pos4, int light) {
        IDrawing.drawTexture(matrices, vertexConsumer, (float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, (float) pos3.x, (float) pos3.y, (float) pos3.z, (float) pos4.x, (float) pos4.y, (float) pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
    }

}
