package mtr.model;

import com.mojang.datafixers.util.Pair;
import mtr.block.BlockAPGDoor;
import mtr.block.IBlock;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
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

public class APGDoorModel extends CustomBlockModelBase implements IBlock {

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

			final Direction facing = IBlock.getStatePropertySafe(state, BlockAPGDoor.FACING);
			final boolean side = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
			final float open = (side ? -1F : 1F) * IBlock.getStatePropertySafe(state, BlockAPGDoor.OPEN) / BlockAPGDoor.MAX_OPEN_VALUE;
			final boolean top = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;

			emitter.square(facing, 0, 0, 1, 1, 0.0625F);
			emitter.spriteBake(0, SPRITES[top ? 3 : 2], MutableQuadView.BAKE_LOCK_UV + (side ? 0 : MutableQuadView.BAKE_FLIP_U));
			emitter.square(facing, open, 0, 1 + open, 1, 0.0625F);
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();

			emitter.square(facing.getOpposite(), 0, 0, 1, 1, 0.875F);
			emitter.spriteBake(0, SPRITES[top ? 3 : 2], MutableQuadView.BAKE_LOCK_UV + (side ? MutableQuadView.BAKE_FLIP_U : 0));
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

			drawThinTexture(emitter, facing, side);
			if (top) {
				customShape(emitter, facing, -open, 0.5F, 0.0625F, -open, 0.5F, 0.125F, 1 - open, 0.5F, 0.125F, 1 - open, 0.5F, 0.0625F);
			} else {
				customShape(emitter, facing, -open, 0.375F, 0, -open, 0.4375F, 0.0625F, 1 - open, 0.4375F, 0.0625F, 1 - open, 0.375F, 0);

				drawThinTexture(emitter, facing, side);
				emitter.square(facing, open, 0, 1 + open, 0.375F, 0);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();

				emitter.square(facing.rotateYClockwise(), 0.9375F, 0, 1, 0.375F, open);
				emitter.spriteBake(0, SPRITES[side ? 1 : 0], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();

				emitter.square(facing.rotateYClockwise(), 0.9375F, 0.375F, 1, 0.4375F, open);
				emitter.spriteBake(0, SPRITES[side ? 1 : 0], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYClockwise(), 0.0625F, 0.375F, open, 0.0625F, 0.4375F, open, 0.0625F, 0.375F, open, 0, 0.375F, open);

				emitter.square(facing.rotateYCounterclockwise(), 0, 0, 0.0625F, 0.375F, -open);
				emitter.spriteBake(0, SPRITES[side ? 0 : 1], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();

				emitter.square(facing.rotateYCounterclockwise(), 0, 0.375F, 0.0625F, 0.4375F, -open);
				emitter.spriteBake(0, SPRITES[side ? 0 : 1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYCounterclockwise(), 0.9375F, 0.375F, -open, 0.9375F, 0.4375F, -open, 0.9375F, 0.375F, -open, 1, 0.375F, -open);
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
		return SPRITES[3];
	}

	@Override
	protected Block getBlock() {
		return new BlockAPGDoor();
	}

	private void drawThinTexture(QuadEmitter emitter, Direction facing, boolean side) {
		emitter.square(facing, 0, 0.46875F, 1, 0.5F, 0);
		emitter.spriteBake(0, SPRITES[3], MutableQuadView.BAKE_LOCK_UV + (side ? 0 : MutableQuadView.BAKE_FLIP_U));
	}
}
