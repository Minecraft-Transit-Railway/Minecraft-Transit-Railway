package mtr.render;

import mtr.block.BlockLiftPanel1;
import com.mojang.blaze3d.systems.RenderSystem;
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
    private float tickElapsed;
    private float nextTarget = 0;
    private float currentTarget;
    private int currentLang;
    private final float INCREMENT = 0.17F;
    private final float SPEED = 0.01F;
    private EntityLift.LiftDirection direction = EntityLift.LiftDirection.UP;

    public RenderLiftPanel(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BlockLiftPanel1.TileEntityLiftPanel entity, float delta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int combinedOverlay) {
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

        tickElapsed += delta;

        if(tickElapsed >= 100) {
            if(direction == EntityLift.LiftDirection.UP) {
                nextTarget -= INCREMENT;
            } else {
                nextTarget += INCREMENT;
            }
            currentLang++;
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

                renderLiftDisplay(matrices, vertexConsumers, delta, facing, pos, liftDisplay.getA()[1], liftDisplay.getB(), maxWidth, 0.3125F);
            }
            matrices.translate(maxWidth, 0, 0);
        });

        if(tickElapsed >= 100) {
            tickElapsed = 0;
        }

        matrices.popPose();
    }

    public void renderLiftDisplay(PoseStack matrices, MultiBufferSource vertexConsumers, float delta, Direction facing, BlockPos pos, String floorNumber, EntityLift.LiftDirection liftDirection, float maxWidth, float height) {
        if (!RenderTrains.shouldNotRender(pos, Math.min(16, RenderTrains.maxTrainRenderDistance), null)) {
            Font textRenderer = Minecraft.getInstance().font;
            if(textRenderer == null) return;
            int textPadding = textRenderer.lineHeight + 2;

            if (liftDirection != EntityLift.LiftDirection.NONE) {
                boolean goingUp = direction == EntityLift.LiftDirection.UP;
                uvShift += (0.02 * delta) % 2;
                IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), -0.06F, 0.65F, maxWidth / 8.0F, maxWidth / 8.0F, 0.0F, (goingUp ? 0.0F : 1.0F) + uvShift, 1.0F, (goingUp ? 1.0F : 0.0F) + uvShift, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
                IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(ARROW_TEXTURE, true)), maxWidth / 1.3F, 0.65F, maxWidth / 8.0F, maxWidth / 8.0F, 0.0F, (goingUp ? 0.0F : 1.0F) + uvShift, 1.0F, (goingUp ? 1.0F : 0.0F) + uvShift, Direction.UP, ARGB_WHITE, MAX_LIGHT_GLOWING);
            }

            matrices.pushPose();
            matrices.translate(0.5F, 0.6F, 0);
            matrices.scale(0.017F, 0.017F,0.017F);
            IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getLiftPanelDisplay(floorNumber, 16755200, 0.4F).resourceLocation)), 0, 0, 100, (textPadding * floorNumber.split("\\|").length) * 10, 0, 0, 1, 1, facing, ARGB_WHITE, MAX_LIGHT_GLOWING);
            matrices.popPose();

//            for(String language : loopedFloorNumber) {
//                language = language.toUpperCase();
//                matrices.pushPose();
//                matrices.translate(0.5F, 0.65F, 0);
//
//                if(direction == EntityLift.LiftDirection.UP && nextTarget < currentTarget) {
//                    currentTarget -= SPEED * delta;
//                } else if (direction == EntityLift.LiftDirection.DOWN && nextTarget > currentTarget) {
//                    currentTarget += SPEED * delta;
//                } else {
//                    if(currentLang != 0 && currentLang >= loopedFloorNumber.length - 1) {
//                        nextTarget = 0;
//                        currentTarget = 0;
//                        currentLang = 0;
//                    }
//                }
//
//                matrices.translate(0, currentTarget, 0);
//                matrices.scale(0.017F, 0.017F,0.017F);
//                textRenderer.draw(matrices, language, maxWidth - textRenderer.width(language) / 2F, textPadding * j, 16755200);
//                matrices.popPose();
//                j++;
//            }
            RenderSystem.disableScissor();
        }
    }
}
