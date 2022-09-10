package mtr.render;

import mtr.MTRClient;
import mtr.block.BlockLiftPanel1;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.block.BlockLiftButtons;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.entity.EntityLift;
import mtr.item.ItemLiftButtonsLinkModifier;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

import static mtr.data.IGui.*;

public class RenderLiftPanel extends BlockEntityRendererMapper<BlockLiftPanel1.TileEntityLiftPanel> {

    private static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("mtr:textures/block/lift_arrow.png");
    private float uvShift = 0;
    private float nextUV = 0;
    private float currentUV = 0;
    private float tickElapsed = 0;
    private final float SPEED = 0.04F;
    private EntityLift.LiftDirection direction = EntityLift.LiftDirection.UP;

    public RenderLiftPanel(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BlockLiftPanel1.TileEntityLiftPanel entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int combinedOverlay) {
        final Level world = entity.getLevel();
        if (world == null) {
            return;
        }

        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final BlockPos pos = entity.getBlockPos();
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
            return;
        }

        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING).getOpposite();
        final boolean holdingLinker = Utilities.isHolding(player, item -> item instanceof ItemLiftButtonsLinkModifier || Block.byItem(item) instanceof BlockLiftButtons);

        matrices.pushPose();
        matrices.translate(0.5, 0, 0.5);

        final Map<BlockPos, Tuple<String[], EntityLift.LiftDirection>> liftDisplays = new HashMap<>();
        final List<BlockPos> liftPositions = new ArrayList<>();
        entity.forEachTrackPosition(world, (trackPosition, trackFloorTileEntity) -> {
            if (holdingLinker) {
                final Direction trackFacing = IBlock.getStatePropertySafe(world, trackPosition, HorizontalDirectionalBlock.FACING);
                IDrawing.drawLine(matrices, vertexConsumers, trackPosition.getX() - pos.getX() + trackFacing.getStepX() / 2F, trackPosition.getY() - pos.getY() + 0.5F, trackPosition.getZ() - pos.getZ() + trackFacing.getStepZ() / 2F, facing.getStepX() / 2F, 0.25F, facing.getStepZ() / 2F, 0xFF, 0xFF, 0xFF);
            }

            final EntityLift entityLift = trackFloorTileEntity.getEntityLift();
            if (entityLift != null) {
                final BlockPos liftPos = new BlockPos(entityLift.getX(), 0, entityLift.getZ());
                liftPositions.add(liftPos);
                liftDisplays.put(liftPos, new Tuple<>(entityLift.getCurrentFloorDisplay(), entityLift.getLiftDirectionClient()));
            }
        });
        liftPositions.sort(Comparator.comparingInt(checkPos -> facing.getStepX() * (checkPos.getZ() - pos.getZ()) - facing.getStepZ() * (checkPos.getX() - pos.getX())));

        matrices.mulPose(Vector3f.YN.rotationDegrees(facing.toYRot()));
        matrices.translate(0, 0, 0.4375 - SMALL_OFFSET);

        final float maxWidth = 1.2F;
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrices.translate(maxWidth * (0.5 - liftPositions.size() / 2F), 0, 0);
        IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/black.png"))), -0.10F, -0.27F, maxWidth * liftPositions.size(), 0.23F, Direction.UP, light);
        matrices.translate(0, -0.875, -SMALL_OFFSET);

        liftPositions.forEach(liftPosition -> {
            final Tuple<String[], EntityLift.LiftDirection> liftDisplay = liftDisplays.get(liftPosition);
            if (liftDisplay != null) {

                if(liftDisplay.getB() != EntityLift.LiftDirection.NONE) {
                    direction = liftDisplay.getB();
                }

                renderLiftDisplay(matrices, vertexConsumers, tickDelta, facing, pos, liftDisplay.getA()[1], liftDisplay.getB(), maxWidth, 0.3125F);
            }
            matrices.translate(maxWidth, 0, 0);
        });

        matrices.popPose();
    }

    public void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, float tickDelta, Direction facing, BlockPos pos, String floorNumber, EntityLift.LiftDirection liftDirection, float maxWidth, float height) {
        if (!RenderTrains.shouldNotRender(pos, Math.min(16, RenderTrains.maxTrainRenderDistance), null)) {
            Font textRenderer = Minecraft.getInstance().font;
            if(textRenderer == null) return;
            float lineHeight = (float) (1.0 / floorNumber.split("\\|").length);

            //TODO: Tick Delta not reliable enough
            tickElapsed += Minecraft.getInstance().getDeltaFrameTime();
            boolean goingUp = direction == EntityLift.LiftDirection.UP;

            if(tickElapsed >= 70) {
                nextUV += lineHeight;
                tickElapsed = 0;
            }

            if(nextUV > currentUV) {
                currentUV += Math.min(SPEED * tickDelta, nextUV);
            } else if(nextUV % lineHeight != 0) {
                nextUV = 0;
                currentUV = 0;
            }

            if (liftDirection != EntityLift.LiftDirection.NONE) {
                uvShift += (0.02 * tickDelta) % 2;
                IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -0.06F, 0.65F, maxWidth / 8.0F, maxWidth / 8.0F, 0.0F, (goingUp ? 0.0F : 1.0F) + uvShift, 1.0F, (goingUp ? 1.0F : 0.0F) + uvShift, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
                IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), maxWidth / 1.3F, 0.65F, maxWidth / 8.0F, maxWidth / 8.0F, 0.0F, (goingUp ? 0.0F : 1.0F) + uvShift, 1.0F, (goingUp ? 1.0F : 0.0F) + uvShift, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
            }

            matrices.pushPose();
            matrices.translate(0.07F, 0.63F, 0);
            matrices.scale(0.017F, 0.017F,0.017F);
            IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ClientData.DATA_CACHE.getLiftPanelDisplay(floorNumber.toUpperCase(), 16755200).resourceLocation, true)), 0, 0.5F, 50, 10, 0,  goingUp ? (0 - currentUV) : (0 + currentUV), 1, goingUp ? (lineHeight - currentUV) : (lineHeight + currentUV), facing, ARGB_WHITE, MAX_LIGHT_GLOWING);
            matrices.popPose();
        }
    }
}
