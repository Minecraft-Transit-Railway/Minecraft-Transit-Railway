package mtr.model;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CustomBlockModelBase implements UnbakedModel, BakedModel, FabricBakedModel {

	private final Map<ImmutableMap<Property<?>, Comparable<?>>, Mesh> meshMap = new HashMap<>();

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		getBlock().getStateManager().getStates().forEach(blockState -> meshMap.put(blockState.getEntries(), bake(blockState, textureGetter)));
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public ModelTransformation getTransformation() {
		return null;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return null;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(meshMap.get(blockState.getEntries()));
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
	}

	protected abstract Mesh bake(BlockState state, Function<SpriteIdentifier, Sprite> textureGetter);

	protected abstract Block getBlock();

	protected static void customShape(QuadEmitter emitter, Direction facing, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		switch (facing) {
			case NORTH:
				emitter.pos(2, x1, y1, z1);
				emitter.pos(3, x2, y2, z2);
				emitter.pos(0, x3, y3, z3);
				emitter.pos(1, x4, y4, z4);
				break;
			case EAST:
				emitter.pos(2, 1 - z1, y1, x1);
				emitter.pos(3, 1 - z2, y2, x2);
				emitter.pos(0, 1 - z3, y3, x3);
				emitter.pos(1, 1 - z4, y4, x4);
				break;
			case SOUTH:
				emitter.pos(2, 1 - x1, y1, 1 - z1);
				emitter.pos(3, 1 - x2, y2, 1 - z2);
				emitter.pos(0, 1 - x3, y3, 1 - z3);
				emitter.pos(1, 1 - x4, y4, 1 - z4);
				break;
			case WEST:
				emitter.pos(2, z1, y1, 1 - x1);
				emitter.pos(3, z2, y2, 1 - x2);
				emitter.pos(0, z3, y3, 1 - x3);
				emitter.pos(1, z4, y4, 1 - x4);
				break;
		}
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}
}
