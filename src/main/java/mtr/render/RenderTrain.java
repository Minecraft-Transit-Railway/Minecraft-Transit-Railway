package mtr.render;

import org.lwjgl.opengl.GL11;

import mtr.MTRUtilities;
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

		final EntityTrain entitySibling = entity.getSiblingClient();
		if (entitySibling == null)
			return;

		final Midpoint midpointSibling = new Midpoint(entity, entitySibling, partialTicks);
		final int trainType = entity.getTrainTypeClient();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 1.5, z);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		final Entity entityConnection = entity.getConnectionClient();
		if (entityConnection != null && entityConnection instanceof EntityTrain)
			renderConnection(entity, (EntityTrain) entityConnection, midpointSibling);

		GlStateManager.translate(midpointSibling.midX, midpointSibling.midY, midpointSibling.midZ);
		GlStateManager.rotate((float) Math.toDegrees(midpointSibling.angleYaw) + (trainType < 0 ? 180 : 0), 0, 1, 0);
		GlStateManager.rotate((float) Math.toDegrees(midpointSibling.anglePitch) * Math.signum(trainType), 1, 0, 0);
		GlStateManager.scale(-1, -1, 1);

		bindEntityTexture(entity);
		render(entity, EnumTrainType.getByDirection(trainType), entity.getLeftDoorClient(), entity.getRightDoorClient());
		entitySibling.getLeftDoorClient();
		entitySibling.getRightDoorClient();

		GlStateManager.disableLighting();

		renderLights();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	protected void renderConnection(final T entity, final EntityTrain entityConnection, final Midpoint midpoint) {
		final Vec3d[] begin = new Vec3d[8], end = new Vec3d[8];
		begin[0] = entity.connectionVectorClient[0] = getConnectionVector(entity, midpoint, 1, -0.25);
		begin[1] = entity.connectionVectorClient[1] = getConnectionVector(entity, midpoint, -1, -0.25);
		begin[2] = entity.connectionVectorClient[2] = getConnectionVector(entity, midpoint, 1, 2.25);
		begin[3] = entity.connectionVectorClient[3] = getConnectionVector(entity, midpoint, -1, 2.25);
		begin[4] = entity.connectionVectorClient[4] = getConnectionVector(entity, midpoint, 0.75, 0);
		begin[5] = entity.connectionVectorClient[5] = getConnectionVector(entity, midpoint, -0.75, 0);
		begin[6] = entity.connectionVectorClient[6] = getConnectionVector(entity, midpoint, 0.75, 2);
		begin[7] = entity.connectionVectorClient[7] = getConnectionVector(entity, midpoint, -0.75, 2);

		final T connectionTrain = (T) entityConnection;
		boolean valid = true;
		for (int i = 0; i < 8; i++) {
			if (connectionTrain.connectionVectorClient[i] == null) {
				valid = false;
				break;
			}
			end[i] = connectionTrain.connectionVectorClient[i].addVector(connectionTrain.posX - entity.posX, connectionTrain.posY - entity.posY, connectionTrain.posZ - entity.posZ);
		}
		if (connectionTrain.connectionVectorClient != null && valid) {
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();

			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

			bufferBuilder.pos(end[0].x, end[0].y, end[0].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end[2].x, end[2].y, end[2].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin[3].x, begin[3].y, begin[3].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin[1].x, begin[1].y, begin[1].z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(end[5].x, end[5].y, end[5].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end[7].x, end[7].y, end[7].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin[6].x, begin[6].y, begin[6].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin[4].x, begin[4].y, begin[4].z).color(255, 255, 255, 255).endVertex();

			bufferBuilder.pos(begin[2].x, begin[2].y, begin[2].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin[3].x, begin[3].y, begin[3].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end[2].x, end[2].y, end[2].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end[3].x, end[3].y, end[3].z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(begin[7].x, begin[7].y, begin[7].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin[6].x, begin[6].y, begin[6].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end[7].x, end[7].y, end[7].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end[6].x, end[6].y, end[6].z).color(255, 255, 255, 255).endVertex();

			bufferBuilder.pos(begin[1].x, begin[1].y, begin[1].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(begin[0].x, begin[0].y, begin[0].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end[1].x, end[1].y, end[1].z).color(128, 128, 128, 255).endVertex();
			bufferBuilder.pos(end[0].x, end[0].y, end[0].z).color(128, 128, 128, 255).endVertex();

			bufferBuilder.pos(begin[4].x, begin[4].y, begin[4].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(begin[5].x, begin[5].y, begin[5].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end[4].x, end[4].y, end[4].z).color(255, 255, 255, 255).endVertex();
			bufferBuilder.pos(end[5].x, end[5].y, end[5].z).color(255, 255, 255, 255).endVertex();

			tessellator.draw();

			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}
	}

	protected abstract void render(T entity, EnumTrainType trainType, float leftDoor, float rightDoor);

	protected abstract void renderLights();

	private Vec3d getConnectionVector(T train, Midpoint midpoint, double xOffset, double yOffset) {
		final float zOffsetBegin = train.getSiblingSpacing() / 2F + train.getEndSpacing() - 0.25F;
		Vec3d vector = new Vec3d(xOffset, yOffset, zOffsetBegin);
		vector = vector.rotatePitch(midpoint.anglePitch).rotateYaw(midpoint.angleYaw + (float) Math.PI);
		vector = vector.addVector(midpoint.midX, midpoint.midY - 0.5, midpoint.midZ);
		return vector;
	}

	private class Midpoint {

		private final double midX, midY, midZ;
		private final float angleYaw, anglePitch;

		private Midpoint(Entity entity1, Entity entity2, float partialTicks) {
			final double x1 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * partialTicks;
			final double y1 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * partialTicks;
			final double z1 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * partialTicks;
			final double x2 = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * partialTicks;
			final double y2 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * partialTicks;
			final double z2 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * partialTicks;
			midX = (x2 - x1) / 2D;
			midY = (y2 - y1) / 2D;
			midZ = (z2 - z1) / 2D;
			angleYaw = (float) MTRUtilities.angleBetweenPoints(x2, z2, x1, z1);
			anglePitch = (float) Math.asin((y1 - y2) / MTRUtilities.distanceBetweenPoints(x1, z1, x2, z2));
		}
	}
}
