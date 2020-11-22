package mtr.model;

import com.mojang.datafixers.util.Pair;
import mtr.block.BlockAPGDoor;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class APGDoorModel extends CustomBlockModelBase {

	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/black")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_side_light")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/apg_door_bottom")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/apg_door_top"))
	};
	private static final int SPRITE_COUNT = SPRITE_IDS.length;

	private final Sprite[] SPRITES = new Sprite[SPRITE_COUNT];

	@Override
	protected Mesh bake(BlockState state, Function<SpriteIdentifier, Sprite> textureGetter) {
		for (int i = 0; i < SPRITE_COUNT; i++) {
			SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
		}

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		if (renderer != null) {
			MeshBuilder builder = renderer.meshBuilder();
			QuadEmitter emitter = builder.getEmitter();

			final Direction facing = state.get(BlockAPGDoor.FACING);
			final boolean side = state.get(BlockAPGDoor.SIDE) == BlockAPGDoor.EnumPSDAPGDoorSide.RIGHT;
			final float open = (side ? -1F : 1F) * state.get(BlockAPGDoor.OPEN) / BlockAPGDoor.MAX_OPEN_VALUE;
			final boolean top = state.get(BlockAPGDoor.TOP);

			emitter.square(facing, 0, 0, 1, 1, 0.0625F);
			emitter.spriteBake(0, SPRITES[top ? 3 : 2], MutableQuadView.BAKE_LOCK_UV + (side ? 0 : MutableQuadView.BAKE_FLIP_U));
			emitter.square(facing, open, 0, 1 + open, 1, 0.0625F);
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();

			emitter.square(facing.getOpposite(), 0, 0, 1, 1, 0.875F);
			emitter.spriteBake(0, SPRITES[(top ? 3 : 2)], MutableQuadView.BAKE_LOCK_UV + (side ? MutableQuadView.BAKE_FLIP_U : 0));
			emitter.square(facing.getOpposite(), -open, 0, 1 - open, 1, 0.875F);
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();

			emitter.square(facing.rotateYClockwise(), 0.875F, 0, 0.9375F, top ? 0.5F : 1, open);
			emitter.spriteBake(0, SPRITES[side ? 1 : 0], MutableQuadView.BAKE_LOCK_UV);
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();

			emitter.square(facing.rotateYCounterclockwise(), 0.0625F, 0, 0.125F, top ? 0.5F : 1, -open);
			emitter.spriteBake(0, SPRITES[side ? 0 : 1], MutableQuadView.BAKE_LOCK_UV);
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();

			if (top) {
				switch (facing) {
					case NORTH:
						emitter.square(Direction.UP, 0, 0.5F, 1, 0.53125F, 0.5F);
						break;
					case EAST:
						emitter.square(Direction.UP, 0.5F, 0, 0.53125F, 1, 0.5F);
						break;
					case SOUTH:
						emitter.square(Direction.UP, 0, 0.46875F, 1, 0.5F, 0.5F);
						break;
					case WEST:
						emitter.square(Direction.UP, 0.53125F, 0, 0.5F, 1, 0.5F);
						break;
				}
				emitter.spriteBake(0, SPRITES[3], MutableQuadView.BAKE_LOCK_UV + facing.getHorizontal() + (side ? 0 : MutableQuadView.BAKE_FLIP_U));
				switch (facing) {
					case NORTH:
						emitter.square(Direction.UP, -open, 0.875F, 1 - open, 0.9375F, 0.5F);
						break;
					case EAST:
						emitter.square(Direction.UP, 0.875F, open, 0.9375F, 1 + open, 0.5F);
						break;
					case SOUTH:
						emitter.square(Direction.UP, open, 0.0625F, 1 + open, 0.125F, 0.5F);
						break;
					case WEST:
						emitter.square(Direction.UP, 0.0625F, -open, 0.125F, 1 - open, 0.5F);
						break;
				}
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
			}

			return builder.build();
		} else {
			return null;
		}
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(SPRITE_IDS);
	}

	@Override
	public Sprite getSprite() {
		return SPRITES[0];
	}

	@Override
	protected Block getBlock() {
		return new BlockAPGDoor();
	}
}
