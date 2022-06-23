package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.MTRClient;
import mtr.client.Config;
import mtr.client.TrainClientRegistry;
import mtr.data.*;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelBogie;
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
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;

public class JonModelTrainRenderer extends TrainRendererBase implements IGui {

    public ModelTrainBase model;
    public String textureId;
    public String gangwayConnectionId;
    public String trainBarrierId;

    public JonModelTrainRenderer() {
        // A constructor without arguments is used by createTrainInstance via reflection.
    }

    public JonModelTrainRenderer(ModelTrainBase model, String textureId, String gangwayConnectionId, String trainBarrierId) {
        this.model = model;
        this.textureId = textureId;
        this.gangwayConnectionId = gangwayConnectionId;
        this.trainBarrierId = trainBarrierId;
    }

    protected void copyFrom(TrainRendererBase srcBase) {
        JonModelTrainRenderer src = (JonModelTrainRenderer)srcBase;
        this.model = src.model;
        this.textureId = src.textureId;
        this.gangwayConnectionId = src.gangwayConnectionId;
        this.trainBarrierId = src.trainBarrierId;
    }

    private static final EntityModel<Minecart> MODEL_MINECART = UtilitiesClient.getMinecartModel();
    private static final EntityModel<Boat> MODEL_BOAT = UtilitiesClient.getBoatModel();
    private static final Map<Long, FakeBoat> BOATS = new HashMap<>();
    private static final ModelCableCarGrip MODEL_CABLE_CAR_GRIP = new ModelCableCarGrip();
    private static final ModelBogie MODEL_BOGIE = new ModelBogie();

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
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

        if (model == null && isTranslucentBatch) {
            return;
        }

        matrices.pushPose();
        matrices.translate(x, y, z);
        matrices.mulPose(Vector3f.YP.rotation((float) Math.PI + yaw));
        matrices.mulPose(Vector3f.XP.rotation((float) Math.PI + (train.transportMode.hasPitch ? pitch : 0)));

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
            final boolean isBoat = train.transportMode == TransportMode.BOAT;

            matrices.translate(0, isBoat ? 0.875 : 0.5, 0);
            matrices.mulPose(Vector3f.YP.rotationDegrees(90));

            final EntityModel<? extends Entity> model = isBoat ? MODEL_BOAT : MODEL_MINECART;
            final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.renderType(resolveTexture(trainProperties.baseTrainType, textureId, textureId -> textureId + ".png")));

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
            model.render(matrices, vertexConsumers, resolveTexture(trainProperties.baseTrainType, textureId, textureId -> textureId + ".png"), light, doorLeftValue, doorRightValue, opening, carIndex, train.trainCars, head1IsFront, lightsOn, isTranslucentBatch, renderDetails);

            if (trainProperties.bogiePosition != 0) {
                if (trainProperties.isJacobsBogie) {
                    if (carIndex == 0) {
                        MODEL_BOGIE.render(matrices, vertexConsumers, light, -(int)(trainProperties.bogiePosition * 16F));
                    } else if (carIndex == train.trainCars - 1) {
                        MODEL_BOGIE.render(matrices, vertexConsumers, light, (int)(trainProperties.bogiePosition * 16F));
                    }
                } else {
                    MODEL_BOGIE.render(matrices, vertexConsumers, light, (int)(trainProperties.bogiePosition * 16F));
                    MODEL_BOGIE.render(matrices, vertexConsumers, light, -(int)(trainProperties.bogiePosition * 16F));
                }
            }
        }

        if (train.transportMode == TransportMode.CABLE_CAR) {
            matrices.translate(0, TransportMode.CABLE_CAR.railOffset + 0.5, 0);
            if (!train.transportMode.hasPitch) {
                matrices.mulPose(Vector3f.XP.rotation(pitch));
            }
            if (trainId.endsWith("_RHT")) {
                matrices.mulPose(Vector3f.YP.rotationDegrees(180));
            }
            MODEL_CABLE_CAR_GRIP.render(matrices, vertexConsumers, light);
        }

        matrices.popPose();
    }

    @Override
    public void renderConnection(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch) {
        final BlockPos posAverage = getPosAverage(x, y, z);
        if (RenderTrains.shouldNotRender(posAverage, UtilitiesClient.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null))
            return;

        String trainId = train.trainId;
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

        final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

        if (trainProperties.hasGangwayConnection) {
            final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(trainProperties, true, "exterior")));
            drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
            drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
            drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos3, prevPos2, light);
            drawTexture(matrices, vertexConsumerExterior, prevPos1, thisPos4, thisPos1, prevPos4, light);

            final boolean lightsOn = train.isOnRoute;
            final int lightOnLevel = lightsOn ? RenderTrains.MAX_LIGHT_INTERIOR : light;
            final VertexConsumer vertexConsumerSide = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, true, "side")));
            drawTexture(matrices, vertexConsumerSide, thisPos3, prevPos2, prevPos1, thisPos4, lightOnLevel);
            drawTexture(matrices, vertexConsumerSide, prevPos3, thisPos2, thisPos1, prevPos4, lightOnLevel);
            drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, true, "roof"))), prevPos2, thisPos3, thisPos2, prevPos3, lightOnLevel);
            drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getInterior(getConnectorTextureString(trainProperties, true, "floor"))), prevPos4, thisPos1, thisPos4, prevPos1, lightOnLevel);
        }

        if (trainProperties.isJacobsBogie) {
            matrices.pushPose();
            matrices.translate(x, y, z);
            matrices.mulPose(Vector3f.YP.rotation((float) Math.PI + yaw));
            matrices.mulPose(Vector3f.XP.rotation((float) Math.PI + (train.transportMode.hasPitch ? pitch : 0)));
            MODEL_BOGIE.render(matrices, vertexConsumers, light, 0);
            matrices.popPose();
        }
    }

    @Override
    public void renderBarrier(Vec3 prevPos1, Vec3 prevPos2, Vec3 prevPos3, Vec3 prevPos4, Vec3 thisPos1, Vec3 thisPos2, Vec3 thisPos3, Vec3 thisPos4, double x, double y, double z, float yaw, float pitch) {
        if (StringUtils.isEmpty(trainBarrierId)) return;

        final BlockPos posAverage = getPosAverage(x, y, z);
        if (RenderTrains.shouldNotRender(posAverage, UtilitiesClient.getRenderDistance() * (Config.trainRenderDistanceRatio() + 1), null))
            return;

        String trainId = train.trainId;
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

        final int light = LightTexture.pack(world.getBrightness(LightLayer.BLOCK, posAverage), world.getBrightness(LightLayer.SKY, posAverage));

        final VertexConsumer vertexConsumerExterior = vertexConsumers.getBuffer(MoreRenderLayers.getExterior(getConnectorTextureString(trainProperties, false, "exterior")));
        drawTexture(matrices, vertexConsumerExterior, thisPos2, prevPos3, prevPos4, thisPos1, light);
        drawTexture(matrices, vertexConsumerExterior, prevPos2, thisPos3, thisPos4, prevPos1, light);
        drawTexture(matrices, vertexConsumerExterior, thisPos3, prevPos2, prevPos1, thisPos4, light);
        drawTexture(matrices, vertexConsumerExterior, prevPos3, thisPos2, thisPos1, prevPos4, light);
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

    private ResourceLocation getConnectorTextureString(TrainClientRegistry.TrainProperties trainProperties, boolean isConnector, String partName) {
        return resolveTexture(trainProperties.baseTrainType, isConnector ? gangwayConnectionId : trainBarrierId, textureId -> String.format("%s_%s_%s.png", textureId, isConnector ? "connector" : "barrier", partName));
    }

    public ResourceLocation resolveTexture(String baseTrainType, String textureId, Function<String, String> formatter) {
        final String textureString = formatter.apply(textureId);
        final ResourceLocation id = new ResourceLocation(textureString);
        final boolean available;

        if (!RenderTrains.AVAILABLE_TEXTURES.contains(textureString) && !RenderTrains.UNAVAILABLE_TEXTURES.contains(textureString)) {
            available = UtilitiesClient.hasResource(id);
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
            final TrainRendererBase renderer = TrainClientRegistry.getTrainProperties(train.trainId).renderer;
            if (renderer instanceof JonModelTrainRenderer) {
                JonModelTrainRenderer jonModelTrainRenderer = (JonModelTrainRenderer) renderer;
                final String baseTextureId = jonModelTrainRenderer.textureId;
                return new ResourceLocation(baseTextureId == null ? "mtr:textures/block/transparent.png" : formatter.apply(textureId));
            } else {
                return new ResourceLocation("mtr:textures/block/transparent.png");
            }
        }
    }

    private static void drawTexture(PoseStack matrices, VertexConsumer vertexConsumer, Vec3 pos1, Vec3 pos2, Vec3 pos3, Vec3 pos4, int light) {
        mtr.client.IDrawing.drawTexture(matrices, vertexConsumer, (float) pos1.x, (float) pos1.y, (float) pos1.z, (float) pos2.x, (float) pos2.y, (float) pos2.z, (float) pos3.x, (float) pos3.y, (float) pos3.z, (float) pos4.x, (float) pos4.y, (float) pos4.z, 0, 0, 1, 1, Direction.UP, -1, light);
    }

}
