package mapper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import me.shedaniel.architectury.registry.BlockEntityRenderers;
import me.shedaniel.architectury.registry.ItemPropertiesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public abstract class UtilitiesClient {

	public static void registerItemModelPredicate(String id, Item item, String tag) {
		ItemPropertiesRegistry.register(item, new ResourceLocation(id), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(tag) ? 1 : 0);
	}

	public static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> factory) {
		BlockEntityRenderers.registerRenderer(type, context -> factory.apply(null));
	}

	public static void beginDrawingRectangle(BufferBuilder buffer) {
		RenderSystem.disableTexture();
		buffer.begin(7, DefaultVertexFormat.POSITION_COLOR);
	}

	public static void finishDrawingRectangle() {
		RenderSystem.enableTexture();
	}

	public static void beginDrawingTexture(ResourceLocation textureId) {
		Minecraft.getInstance().getTextureManager().bind(textureId);
	}

	public static void setScreen(Minecraft client, ScreenMapper screen) {
		client.setScreen(screen);
	}

	public static EntityModel<Minecart> getMinecartModel() {
		return new MinecartModel<>();
	}

	public static Matrix3f getNormal(PoseStack.Pose entry) {
		return entry.normal();
	}

	public static Matrix4f getModel(PoseStack.Pose entry) {
		return entry.pose();
	}
}
