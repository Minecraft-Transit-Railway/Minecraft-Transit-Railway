package mapper;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.MTR;
import mtr.item.ItemNodeModifierBase;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface UtilitiesClient {

	static void registerNodeModifierPredicate(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemNodeModifierBase.TAG_POS) ? 1 : 0);
	}

	static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> factory) {
		BlockEntityRendererRegistry.INSTANCE.register(type, context -> factory.apply(null));
	}

	static void beginDrawingRectangle(BufferBuilder buffer) {
		RenderSystem.disableTexture();
		buffer.begin(7, VertexFormats.POSITION_COLOR);
	}

	static void finishDrawingRectangle() {
		RenderSystem.enableTexture();
	}

	static void beginDrawingTexture(Identifier textureId) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(textureId);
	}

	static void setScreen(MinecraftClient client, ScreenMapper screen) {
		client.openScreen(screen);
	}

	static EntityModel<MinecartEntity> getMinecartModel() {
		return new MinecartEntityModel<>();
	}
}
