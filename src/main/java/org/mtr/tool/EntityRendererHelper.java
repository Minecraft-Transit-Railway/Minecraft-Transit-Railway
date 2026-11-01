package org.mtr.tool;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
//? if >= 26.1 {
/*import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
*///? }

/**
 * Draws a whole entity from inside the mod's own render queue. Both the passengers sitting in a
 * vehicle and the driver shown in the cab need this, and from 26.1 there is no longer one call
 * behind it, so the versions are reconciled here rather than at each of the two call sites.
 */
public final class EntityRendererHelper {

	/**
	 * Renders one entity wherever the given matrix stack currently points.
	 *
	 * <p>From 26.1 an entity renderer no longer draws. It reads the entity into a render state and
	 * submits that state, and the level renderer draws every submission together later in the frame.
	 * The submission is made into the shared store rather than a store of the mod's own, so that
	 * these entities are drawn in the same pass as every other entity and sort against them
	 * correctly. Nothing is flushed here for the same reason: flushing would draw whatever the level
	 * renderer had queued up to that point along with it.
	 *
	 * @param light the packed light coordinates to light the entity with. The render state carries
	 *              these itself now instead of them being passed alongside it, so they are written
	 *              onto the state after the entity has been read into it and would otherwise be
	 *              overwritten by the brightness at the entity's own position, which for a passenger
	 *              is wherever the underlying player really is rather than inside the vehicle.
	 */
	public static void render(Entity entity, PoseStack matrixStack, int light) {
		final Minecraft minecraftClient = Minecraft.getInstance();
//? if >= 26.1 {
		/*final EntityRenderDispatcher entityRenderDispatcher = minecraftClient.getEntityRenderDispatcher();
		final EntityRenderState entityRenderState = entityRenderDispatcher.extractEntity(entity, minecraftClient.getDeltaTracker().getGameTimeDeltaPartialTick(false));
		entityRenderState.lightCoords = light;
		entityRenderDispatcher.submit(
			entityRenderState,
			minecraftClient.gameRenderer.getGameRenderState().levelRenderState.cameraRenderState,
			0,
			0,
			0,
			matrixStack,
			minecraftClient.gameRenderer.getFeatureRenderDispatcher().getSubmitNodeStorage()
		);
*///? } else if >= 1.21.4 {
		minecraftClient.getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, matrixStack, minecraftClient.renderBuffers().bufferSource(), light);
//? } else {
		/*minecraftClient.getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, matrixStack, minecraftClient.renderBuffers().bufferSource(), light);
*///? }
	}

	private EntityRendererHelper() {
	}
}
