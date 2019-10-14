package mtr.render;

import org.lwjgl.opengl.GL11;

import mtr.MathTools;
import mtr.Midpoint;
import mtr.entity.EntityTrain;
import mtr.entity.EntityTrain.EnumTrainType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// https://wiki.mcjty.eu/modding/index.php/Render_Block_TESR_/_OBJ-1.9
@SideOnly(Side.CLIENT)
public abstract class RenderTrain<T extends EntityTrain> extends Render<T> {

	protected ModelBase modelBogie = new ModelMinecart();

	public RenderTrain(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 0.375, z);
		GlStateManager.rotate(-entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(180 + entity.rotationPitch, 0, 0, 1);
		bindTexture(new ResourceLocation("textures/entity/minecart.png"));
		modelBogie.render(entity, 0, 0, -0.1F, 0, 0, 0.0625F);
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		final Entity entitySibling = entity.world.getEntityByID(entity.getSiblingIDClient());
		if (entitySibling == null || !(entitySibling instanceof EntityTrain))
			return;
		entity.midpointClient = getMidpointClient(entity, entitySibling, partialTicks);
		final int trainType = entity.getTrainTypeClient();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 1.5, z);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		final Entity entityConnection = entity.world.getEntityByID(entity.getConnectionIDClient());
		if (entityConnection != null && entityConnection instanceof EntityTrain && ((EntityTrain) entityConnection).midpointClient != null) {
			final EntityTrain connectionTrain = (EntityTrain) entityConnection;
			final Tuple<Vec3d, Vec3d> begin = getConnectionVector(entity);
			final Tuple<Vec3d, Vec3d> end = getConnectionVectors(connectionTrain, connectionTrain.posX - entity.posX, connectionTrain.posY - entity.posY, connectionTrain.posZ - entity.posZ);
			final Vec3d begin1 = begin.getFirst(), begin2 = begin.getSecond();
			final Vec3d end1 = end.getFirst(), end2 = end.getSecond();

			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();
			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

			bufferBuilder.pos(end1.x, end1.y, end1.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end1.x, end1.y + 2, end1.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin2.x, begin2.y + 2, begin2.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin2.x, begin2.y, begin2.z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(end2.x, end2.y, end2.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end2.x, end2.y + 2, end2.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin1.x, begin1.y + 2, begin1.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin1.x, begin1.y, begin1.z).color(255, 255, 255, 255).endVertex();

			bufferBuilder.pos(begin1.x, begin1.y + 2, begin1.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin2.x, begin2.y + 2, begin2.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end1.x, end1.y + 2, end1.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end2.x, end2.y + 2, end2.z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(begin2.x, begin2.y + 2, begin2.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin1.x, begin1.y + 2, begin1.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end2.x, end2.y + 2, end2.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end1.x, end1.y + 2, end1.z).color(255, 255, 255, 255).endVertex();

			bufferBuilder.pos(begin2.x, begin2.y, begin2.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin1.x, begin1.y, begin1.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end2.x, end2.y, end2.z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end1.x, end1.y, end1.z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(begin1.x, begin1.y, begin1.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin2.x, begin2.y, begin2.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end1.x, end1.y, end1.z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end2.x, end2.y, end2.z).color(255, 255, 255, 255).endVertex();

			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}

		GlStateManager.translate(entity.midpointClient.midX, entity.midpointClient.midY, entity.midpointClient.midZ);
		GlStateManager.rotate((float) Math.toDegrees(entity.midpointClient.angleYaw) + (trainType < 0 ? 180 : 0), 0, 1, 0);
		GlStateManager.rotate((float) Math.toDegrees(entity.midpointClient.anglePitch) * Math.signum(trainType), 1, 0, 0);
		GlStateManager.scale(-1, -1, 1);
		bindEntityTexture(entity);
		final float leftDoor = entity.getLeftDoor() / 60F;
		final float rightDoor = entity.getRightDoor() / 60F;
		render(entity, EnumTrainType.getByDirection(trainType), leftDoor, rightDoor);
		GlStateManager.disableLighting();
		renderLights();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	protected abstract void render(T entity, EnumTrainType trainType, float leftDoor, float rightDoor);

	protected abstract void renderLights();

	private Midpoint getMidpointClient(Entity entity1, Entity entity2, float partialTicks) {
		final double x1 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * partialTicks;
		final double y1 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * partialTicks;
		final double z1 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * partialTicks;
		final double x2 = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * partialTicks;
		final double y2 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * partialTicks;
		final double z2 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * partialTicks;
		final double midX = (x2 - x1) / 2D;
		final double midY = (y2 - y1) / 2D;
		final double midZ = (z2 - z1) / 2D;
		final float angleYaw = (float) MathTools.angleBetweenPoints(x2, z2, x1, z1);
		final float anglePitch = (float) Math.asin((y1 - y2) / MathTools.distanceBetweenPoints(x1, z1, x2, z2));
		return new Midpoint(midX, midY, midZ, angleYaw, anglePitch);
	}

	private Tuple<Vec3d, Vec3d> getConnectionVector(EntityTrain train) {
		final float zOffsetBegin = train.getSiblingSpacing() / 2F + train.getEndSpacing() - 0.5F;
		Vec3d vector1 = new Vec3d(1, 0, zOffsetBegin);
		Vec3d vector2 = new Vec3d(-1, 0, zOffsetBegin);
		vector1 = vector1.rotatePitch(train.midpointClient.anglePitch).rotateYaw(train.midpointClient.angleYaw + (float) Math.PI);
		vector2 = vector2.rotatePitch(train.midpointClient.anglePitch).rotateYaw(train.midpointClient.angleYaw + (float) Math.PI);
		vector1 = vector1.addVector(train.midpointClient.midX, train.midpointClient.midY - 0.5, train.midpointClient.midZ);
		vector2 = vector2.addVector(train.midpointClient.midX, train.midpointClient.midY - 0.5, train.midpointClient.midZ);
		return new Tuple<Vec3d, Vec3d>(vector1, vector2);
	}

	private Tuple<Vec3d, Vec3d> getConnectionVectors(EntityTrain train, double offsetX, double offsetY, double offsetZ) {
		final Tuple<Vec3d, Vec3d> vectors = getConnectionVector(train);
		return new Tuple<Vec3d, Vec3d>(vectors.getFirst().addVector(offsetX, offsetY, offsetZ), vectors.getSecond().addVector(offsetX, offsetY, offsetZ));
	}
}
