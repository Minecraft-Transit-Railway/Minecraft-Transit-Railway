package MTR.client;

import java.util.List;

import MTR.EntityTrainBase;
import MTR.MathTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

// https://wiki.mcjty.eu/modding/index.php/Render_Block_TESR_/_OBJ-1.9
public class RenderTrain<T extends EntityTrainBase> extends Render<T> {

	private IBakedModel bakedModelBogie, bakedModelTrain;

	public RenderTrain(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		float yaw = entity.prevRotationYaw
				+ (float) MathTools.angleDifference(entity.rotationYaw, entity.prevRotationYaw) * partialTicks;
		float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		GlStateManager.rotate(yaw, 0, 1, 0);
		GlStateManager.rotate(pitch, 0, 0, 1);
		renderBogie2(entity);
		GlStateManager.popMatrix();

		double midX = 0, midY = 0, midZ = 0, angleYaw = 0, anglePitch = 0;
		GlStateManager.pushMatrix();
		try {
			int wheelID = entity.mtrWheelID;
			int connectedID = entity.mtrConnectedID;
			boolean front = wheelID < 0;
			wheelID = Math.abs(wheelID);
			T entityWheel = (T) entity.worldObj.getEntityByID(wheelID);
			double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
			double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
			double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
			double x2 = entityWheel.lastTickPosX + (entityWheel.posX - entityWheel.lastTickPosX) * partialTicks;
			double y2 = entityWheel.lastTickPosY + (entityWheel.posY - entityWheel.lastTickPosY) * partialTicks;
			double z2 = entityWheel.lastTickPosZ + (entityWheel.posZ - entityWheel.lastTickPosZ) * partialTicks;
			midX = (x2 - x1) / 2D;
			midY = (y2 - y1) / 2D;
			midZ = (z2 - z1) / 2D;
			angleYaw = MathTools.angleBetweenPoints(x2, z2, x1, z1);
			anglePitch = Math.asin((y1 - y2) / MathTools.distanceBetweenPoints(x1, z1, x2, z2));

			GlStateManager.translate(x, y + 1.5, z);
			GlStateManager.translate(midX, midY, midZ);
			if (entity.isBeingRidden()) {
				// Minecraft.getMinecraft().thePlayer.rotationYaw = (float)
				// -Math.toDegrees(angleYaw);
				// Minecraft.getMinecraft().thePlayer.prevRotationYaw = (float)
				// -Math.toDegrees(angleYaw);
				// Minecraft.getMinecraft().getRenderViewEntity().rotationYaw = (float)
				// -Math.toDegrees(angleYaw);
				// Minecraft.getMinecraft().getRenderViewEntity().prevRotationYaw = (float)
				// -Math.toDegrees(angleYaw);
				// Minecraft.getMinecraft().entityRenderer.updateCameraAndRender(partialTicks,
				// nanoTime);
			}
			GlStateManager.rotate((float) Math.toDegrees(angleYaw) + (front ? 0 : 180), 0, 1, 0);
			GlStateManager.rotate((float) Math.toDegrees(anglePitch) * (front ? 1 : -1), 1, 0, 0);

			// bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			// vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
			// renderQuads(vertexbuffer, getModelTrain().getQuads(null, null, 0));
			// tessellator.draw();

			bindEntityTexture(entity);
			GlStateManager.scale(-1, -1, 1);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			float leftDoor = entity.leftDoor / 60F;
			float rightDoor = entity.rightDoor / 60F;
			render(entity, leftDoor, rightDoor);

			// GlStateManager.pushMatrix();
			// Tessellator tessellator = Tessellator.getInstance();
			// VertexBuffer vertexBuffer = tessellator.getBuffer();
			// bindTexture(getConnectionTexture());
			// vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			// vertexBuffer.pos(x2, y2, z2).tex(u, v).endVertex();
			//
			// GlStateManager.popMatrix();
		} catch (Exception e) {
		}
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}

	protected ResourceLocation getTrainObj() {
		return null;
	}

	protected ResourceLocation getConnectionTexture() {
		return null;
	}

	protected void render(T entity, float leftDoor, float rightDoor) {
	}

	protected void renderBogie(T entity) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
		// for (EnumFacing enumfacing : EnumFacing.values())
		// renderQuads(vertexbuffer, getModel().getQuads(null, enumfacing, 0));
		renderQuads(vertexbuffer, getModelBogie().getQuads(null, null, 0));
		tessellator.draw();
	}

	protected void renderBogie2(T entity) {
	}

	private IBakedModel getModelBogie() {
		if (bakedModelBogie == null) {
			IModel modelBogie;
			try {
				modelBogie = ModelLoaderRegistry.getModel(new ResourceLocation("MTR:obj/Bogie.obj"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelBogie = modelBogie.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
					location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelBogie;
	}

	private IBakedModel getModelTrain() {
		if (bakedModelTrain == null) {
			IModel modelTrain;
			try {
				modelTrain = ModelLoaderRegistry.getModel(getTrainObj());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			bakedModelTrain = modelTrain.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
					location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		}
		return bakedModelTrain;
	}

	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads) {
		int i = 0;
		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, -1);
		}
	}
}
