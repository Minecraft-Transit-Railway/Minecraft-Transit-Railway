package mapper;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.MTR;
import mtr.item.ItemNodeModifierBase;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

import java.util.function.Function;

public interface UtilitiesClient {

	static void registerNodeModifierPredicate(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier(MTR.MOD_ID + ":selected"), (itemStack, clientWorld, livingEntity, i) -> itemStack.getOrCreateNbt().contains(ItemNodeModifierBase.TAG_POS) ? 1 : 0);
	}

	static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> factory) {
		BlockEntityRendererRegistry.INSTANCE.register(type, context -> factory.apply(null));
	}

	static void beginDrawingRectangle(BufferBuilder buffer) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	}

	static void finishDrawingRectangle() {
	}

	static void beginDrawingTexture(Identifier textureId) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, textureId);
	}

	static void setScreen(MinecraftClient client, ScreenMapper screen) {
		client.setScreen(screen);
	}

	static EntityModel<MinecartEntity> getMinecartModel() {
		return new MinecartEntityModel<>(MinecartEntityModel.getTexturedModelData().createModel());
	}

	static Matrix3f getNormal(MatrixStack.Entry entry) {
		return entry.getNormalMatrix();
	}

	static Matrix4f getModel(MatrixStack.Entry entry) {
		return entry.getPositionMatrix();
	}
}
