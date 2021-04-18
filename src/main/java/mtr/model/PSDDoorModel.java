package mtr.model;

import com.mojang.datafixers.util.Pair;
import mtr.block.BlockPSDDoor;
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

public class PSDDoorModel extends CustomBlockModelBase implements IBlock {

	public final int style;

	public PSDDoorModel(int style) {
		this.style = style;
	}

	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/black")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_side_light")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_bottom")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_bottom_back")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_bottom")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_bottom_back")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_top")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_top_back")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_top")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_top_back"))
	};
	private static final int SPRITE_COUNT = SPRITE_IDS.length;

	private static final SpriteIdentifier[] SPRITE_IDS_2 = new SpriteIdentifier[]{
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/black")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_side_light")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_bottom_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_bottom_back_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_bottom_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_bottom_back_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_top_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_top_back_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_top_2")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_door_end_top_back_2"))
	};
	private static final int SPRITE_COUNT_2 = SPRITE_IDS_2.length;

	private final Sprite[] SPRITES = new Sprite[SPRITE_COUNT];
	private final Sprite[] SPRITES_2 = new Sprite[SPRITE_COUNT_2];

	@Override
	protected Mesh bake(BlockState state, Function<SpriteIdentifier, Sprite> textureGetter) {
		for (int i = 0; i < SPRITE_COUNT; i++) {
			if (style == 1) {
				SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
			} else {
				SPRITES_2[i] = textureGetter.apply(SPRITE_IDS_2[i]);
			}
		}

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		if (renderer != null) {
			MeshBuilder builder = renderer.meshBuilder();
			QuadEmitter emitter = builder.getEmitter();

			final boolean end = IBlock.getStatePropertySafe(state, BlockPSDDoor.END);
			final Direction facing = IBlock.getStatePropertySafe(state, BlockPSDDoor.FACING);
			final boolean side = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
			final float open = (side ? -1F : 1F) * IBlock.getStatePropertySafe(state, BlockPSDDoor.OPEN) / BlockPSDDoor.MAX_OPEN_VALUE;
			final boolean top = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;

			if (end) {
				createCube(emitter, facing, side, open / 2, top, 0, 0.5F, 0.125F, 4, 1);
				createCube(emitter, facing, side, open, top, 0.5F, 1, 0, 4, 0);
			} else {
				createCube(emitter, facing, side, open, top, 0, 1, 0, 2, 0);
			}

			return builder.build();
		} else {
			return null;
		}
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(style == 1 ? SPRITE_IDS : SPRITE_IDS_2);
	}

	@Override
	public Sprite getSprite() {
		return style == 1 ? SPRITES[2] : SPRITES_2[2];
	}

	@Override
	protected Block getBlock() {
		return new BlockPSDDoor(style);
	}

	private void createCube(QuadEmitter emitter, Direction facing, boolean side, float open, boolean top, float outer, float inner, float depth, int mainTextureIndex, int innerTextureIndex) {
		final float x1 = side ? 1 - inner : outer;
		final float x2 = side ? 1 - outer : inner;

		emitter.square(facing, 1 - x2, 0, 1 - x1, 1, depth);
		emitter.spriteBake(0, style == 1 ? SPRITES[(top ? 5 : 1) + mainTextureIndex] : SPRITES_2[(top ? 5 : 1) + mainTextureIndex], MutableQuadView.BAKE_LOCK_UV + (side ? MutableQuadView.BAKE_FLIP_U : 0));
		emitter.square(facing, 1 - x2 + open, 0, 1 - x1 + open, 1, depth);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();

		emitter.square(facing.getOpposite(), x1, 0, x2, 1, 0.875F - depth);
		emitter.spriteBake(0, style == 1 ? SPRITES[(top ? 4 : 0) + mainTextureIndex] : SPRITES_2[(top ? 4 : 0) + mainTextureIndex], MutableQuadView.BAKE_LOCK_UV + (side ? MutableQuadView.BAKE_FLIP_U : 0));
		emitter.square(facing.getOpposite(), x1 - open, 0, x2 - open, 1, 0.875F - depth);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();

		emitter.square(facing.rotateYClockwise(), 0.875F - depth, 0, 1 - depth, 1, 1 - x2 + open);
		emitter.spriteBake(0, style == 1 ? SPRITES[side ? 0 : innerTextureIndex] : SPRITES_2[side ? 0 : innerTextureIndex], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();

		emitter.square(facing.rotateYCounterclockwise(), depth, 0, 0.125F + depth, 1, x1 - open);
		emitter.spriteBake(0, style == 1 ? SPRITES[side ? innerTextureIndex : 0] : SPRITES_2[side ? innerTextureIndex : 0], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}
}
