package mtr.model;

import com.mojang.datafixers.util.Pair;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
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
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class PSDTopModel extends CustomBlockModelBase implements IBlock {

	public static final float END_FRONT_OFFSET = 1 / (MathHelper.SQUARE_ROOT_OF_TWO * 16);

	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_top")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/psd_top_edge")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/light_off")),
			new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("mtr:block/light_on"))
	};
	private static final int SPRITE_COUNT = SPRITE_IDS.length;
	private static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
	private static final float ROOT_TWO_SCALED = MathHelper.SQUARE_ROOT_OF_TWO / 16;
	private static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / MathHelper.SQUARE_ROOT_OF_TWO;

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

			final boolean airLeft = state.get(BlockPSDTop.AIR_LEFT);
			final boolean airRight = state.get(BlockPSDTop.AIR_RIGHT);
			final BlockPSDTop.EnumDoorLight doorLight = state.get(BlockPSDTop.DOOR_LIGHT);
			final Direction facing = state.get(BlockPSDTop.FACING);
			final EnumSide side = state.get(SIDE_EXTENDED);

			final boolean isMiddleOrSingle = side == EnumSide.MIDDLE || side == EnumSide.SINGLE;
			final boolean isRight = side == EnumSide.RIGHT;

			// back
			emitter.square(facing, airRight ? 0.625F : 0, 0, airLeft ? 0.375F : 1, 1, 0);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? 0 : MutableQuadView.BAKE_FLIP_U));
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();
			// front
			emitter.square(facing.getOpposite(), airLeft ? 0.625F : 0, 0.0625F, airRight ? 0.375F : 1, 1, 0.625F);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();
			// top
			emitter.square(facing, airLeft ? 0.625F : 0, 0, airRight ? 0.375F : 1, 0.375F, 0.5F);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
			customShape(emitter, facing, airRight ? 0.375F : 1, 1, 0.375F, airRight ? 0.375F : 1, 1, 0, airLeft ? 0.625F : 0, 1, 0, airLeft ? 0.625F : 0, 1, 0.375F);
			// bottom
			emitter.square(facing, airLeft ? 0.625F : 0, 0, airRight ? 0.375F : 1, 0.3125F, 0.5F);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
			customShape(emitter, facing, airRight ? 0.375F : 1, 0, 0, airRight ? 0.375F : 1, 0, 0.3125F, airLeft ? 0.625F : 0, 0, 0.3125F, airLeft ? 0.625F : 0, 0, 0);
			// top curve
			emitter.square(facing, airLeft ? 0.625F : 0, 0.03125F, airRight ? 0.375F : 1, 0.0625F, 0.5F);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
			customShape(emitter, facing, airRight ? 0.375F : 1, 0, 0.3125F, airRight ? 0.375F : 1, BOTTOM_DIAGONAL_OFFSET, 0.375F - BOTTOM_DIAGONAL_OFFSET, airLeft ? 0.625F : 0, BOTTOM_DIAGONAL_OFFSET, 0.375F - BOTTOM_DIAGONAL_OFFSET, airLeft ? 0.625F : 0, 0, 0.3125F);
			// bottom curve
			emitter.square(facing, airLeft ? 0.625F : 0, 0, airRight ? 0.375F : 1, 0.03125F, 0.5F);
			emitter.spriteBake(0, SPRITES[isMiddleOrSingle ? 0 : 1], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
			customShape(emitter, facing, airRight ? 0.375F : 1, BOTTOM_DIAGONAL_OFFSET, 0.375F - BOTTOM_DIAGONAL_OFFSET, airRight ? 0.375F : 1, 0.0625F, 0.375F, airLeft ? 0.625F : 0, 0.0625F, 0.375F, airLeft ? 0.625F : 0, BOTTOM_DIAGONAL_OFFSET, 0.375F - BOTTOM_DIAGONAL_OFFSET);
			// light
			if (doorLight != BlockPSDTop.EnumDoorLight.NONE) {
				final boolean isOn = doorLight == BlockPSDTop.EnumDoorLight.ON;
				// front
				emitter.square(facing.getOpposite(), isRight ? 0 : 0.9375F, 0, isRight ? 0.0625F : 1, 0.0625F, 0.625F);
				emitter.spriteBake(0, SPRITES[isOn ? 3 : 2], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// right
				emitter.square(facing.rotateYClockwise(), 0.625F, 0, 0.6875F, 0.0625F, isRight ? 0.9375F : 0);
				emitter.spriteBake(0, SPRITES[isOn ? 3 : 2], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.lightmap(0, 15);
				emitter.emit();
				// left
				emitter.square(facing.rotateYCounterclockwise(), 0.3125F, 0, 0.375F, 0.0625F, isRight ? 0 : 0.9375F);
				emitter.spriteBake(0, SPRITES[isOn ? 3 : 2], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// bottom
				emitter.square(facing, isRight ? 0 : 0.9375F, 0.0625F, isRight ? 0.0625F : 1, 0.125F, 0.5F);
				emitter.spriteBake(0, SPRITES[isOn ? 3 : 2], MutableQuadView.BAKE_LOCK_UV + (isRight ? MutableQuadView.BAKE_FLIP_U : 0));
				customShape(emitter, facing, isRight ? 0.0625F : 1, 0, 0.3125F, isRight ? 0.0625F : 1, 0, 0.375F, isRight ? 0 : 0.9375F, 0, 0.375F, isRight ? 0 : 0.9375F, 0, 0.3125F);
			}
			// end left
			if (airLeft) {
				// back
				emitter.square(facing, 0, 0, 1, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 0, 0, 0.625F, 0, 1, 0.625F, 0.625F, 1, 0, 0.625F, 0, 0);
				// front
				emitter.square(facing, 0, 0.0625F, 1, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.75F + END_FRONT_OFFSET, 0.0625F, 0.25F + END_FRONT_OFFSET, 0.75F + END_FRONT_OFFSET, 1, 0.25F + END_FRONT_OFFSET, END_FRONT_OFFSET, 1, 1 + END_FRONT_OFFSET, END_FRONT_OFFSET, 0.0625F, 1 + END_FRONT_OFFSET);
				// top curve
				emitter.square(facing, 0, 0.03125F, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.75F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, 0.75F + END_FRONT_OFFSET, 0.0625F, 0.25F + END_FRONT_OFFSET, END_FRONT_OFFSET, 0.0625F, 1 + END_FRONT_OFFSET, BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 1 + BOTTOM_END_DIAGONAL_OFFSET);
				// bottom curve
				emitter.square(facing, 0, 0, 1, 0.03125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.75F, 0, 0.25F, 0.75F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 1 + BOTTOM_END_DIAGONAL_OFFSET, 0, 0, 1);
				// bottom
				emitter.square(facing, 0, 0, 1, 0.3125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.625F, 0, 0, 0.625F, 0, 0.375F, 0, 0, 1, 0, 0, 0.625F);
				// top
				emitter.square(facing, 0, 0, 1, 0.3125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.625F, 1, 0.375F, 0.625F, 1, 0, 0, 1, 0.625F, 0, 1, 1);
				// top front
				emitter.square(facing, 0, 0, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.625F + ROOT_TWO_SCALED, 1, 0.375F, 0.625F, 1, 0.375F, 0, 1, 1, END_FRONT_OFFSET, 1, 1 + END_FRONT_OFFSET);
				// left side flat
				emitter.square(facing.rotateYCounterclockwise(), 0.625F, 0, 1, 1, 0);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// left side diagonal
				emitter.square(facing.rotateYCounterclockwise(), 0, 0.0625F, 0.0625F, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYCounterclockwise(), -END_FRONT_OFFSET, 0.0625F, END_FRONT_OFFSET, -END_FRONT_OFFSET, 1, END_FRONT_OFFSET, 0, 1, 0, 0, 0.0625F, 0);
				// left side diagonal square
				emitter.square(facing.rotateYCounterclockwise(), 0, 0, 0.0625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYCounterclockwise(), -BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, BOTTOM_END_DIAGONAL_OFFSET, -END_FRONT_OFFSET, 0.0625F, END_FRONT_OFFSET, 0, 0.0625F, 0, 0, 0, 0);
				// light front
				emitter.square(facing, 0.4375F, 0, 0.5625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.3125F + ROOT_TWO_SCALED, 0, 0.6875F, 0.3125F + ROOT_TWO_SCALED, 0.0625F, 0.6875F, 0.3125F, 0.0625F, 0.6875F + ROOT_TWO_SCALED, 0.3125F, 0, 0.6875F + ROOT_TWO_SCALED);
				// light bottom
				emitter.square(facing, 0.4375F, 0.0625F, 0.5625F, 0.125F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.3125F + END_FRONT_OFFSET, 0, 0.6875F - END_FRONT_OFFSET, 0.3125F + ROOT_TWO_SCALED, 0, 0.6875F, 0.3125F, 0, 0.6875F + ROOT_TWO_SCALED, 0.3125F - END_FRONT_OFFSET, 0, 0.6875F + END_FRONT_OFFSET);
				// light left
				emitter.square(facing, 0, 0, 0.0625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.3125F, 0, 0.6875F + ROOT_TWO_SCALED, 0.3125F, 0.0625F, 0.6875F + ROOT_TWO_SCALED, 0.3125F - END_FRONT_OFFSET, 0.0625F, 0.6875F + END_FRONT_OFFSET, 0.3125F - END_FRONT_OFFSET, 0, 0.6875F + END_FRONT_OFFSET);
				// light right
				emitter.square(facing, 0.9375F, 0, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.3125F + END_FRONT_OFFSET, 0, 0.6875F - END_FRONT_OFFSET, 0.3125F + END_FRONT_OFFSET, 0.0625F, 0.6875F - END_FRONT_OFFSET, 0.3125F + ROOT_TWO_SCALED, 0.0625F, 0.6875F, 0.3125F + ROOT_TWO_SCALED, 0, 0.6875F);
			} else {
				// left side top
				emitter.square(facing.rotateYCounterclockwise(), 0, 0.0625F, 0.375F, 1, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// left side bottom
				emitter.square(facing.rotateYCounterclockwise(), 0, 0, 0.3125F, 0.0625F, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// left side bottom square
				emitter.square(facing.rotateYCounterclockwise(), 0.3125F, 0, 0.375F, 0.0625F, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYCounterclockwise(), 0.625F + BOTTOM_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0, 0.625F, 0.0625F, 0, 0.6875F, 0.0625F, 0, 0.6875F, 0, 0);
			}
			// end right
			if (airRight) {
				// back
				emitter.square(facing, 0, 0, 1, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.375F, 0, 0, 0.375F, 1, 0, 1, 1, 0.625F, 1, 0, 0.625F);
				// front
				emitter.square(facing, 0, 0.0625F, 1, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1 - END_FRONT_OFFSET, 0.0625F, 1 + END_FRONT_OFFSET, 1 - END_FRONT_OFFSET, 1, 1 + END_FRONT_OFFSET, 0.25F - END_FRONT_OFFSET, 1, 0.25F + END_FRONT_OFFSET, 0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F + END_FRONT_OFFSET);
				// top curve
				emitter.square(facing, 0, 0.03125F, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1 - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 1 + BOTTOM_END_DIAGONAL_OFFSET, 1 - END_FRONT_OFFSET, 0.0625F, 1 + END_FRONT_OFFSET, 0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F + END_FRONT_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET);
				// bottom curve
				emitter.square(facing, 0, 0, 1, 0.03125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1, 0, 1, 1 - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 1 + BOTTOM_END_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, 0.25F, 0, 0.25F);
				// bottom
				emitter.square(facing, 0, 0, 1, 0.3125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1, 0, 0.625F, 1, 0, 1, 0.375F, 0, 0.375F, 0.375F, 0, 0);
				// top
				emitter.square(facing, 0, 0, 1, 0.3125F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1, 1, 1, 1, 1, 0.625F, 0.375F, 1, 0, 0.375F, 1, 0.375F);
				// top front
				emitter.square(facing, 0, 0, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing, 1 - END_FRONT_OFFSET, 1, 1 + END_FRONT_OFFSET, 1, 1, 1, 0.375F, 1, 0.375F, 0.375F - ROOT_TWO_SCALED, 1, 0.375F);
				// right side flat
				emitter.square(facing.rotateYClockwise(), 0, 0, 0.375F, 1, 0);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// right side diagonal
				emitter.square(facing.rotateYClockwise(), 0.9375F, 0.0625F, 1, 1, 0.5F);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYClockwise(), 1, 0.0625F, 0, 1, 1, 0, 1 + END_FRONT_OFFSET, 1, END_FRONT_OFFSET, 1 + END_FRONT_OFFSET, 0.0625F, END_FRONT_OFFSET);
				// right side diagonal square
				emitter.square(facing.rotateYClockwise(), 0, 0, 0.0625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[0], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing.rotateYClockwise(), 1, 0, 0, 1, 0.0625F, 0, 1 + END_FRONT_OFFSET, 0.0625F, END_FRONT_OFFSET, 1 + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, BOTTOM_END_DIAGONAL_OFFSET);
				// light front
				emitter.square(facing, 0.4375F, 0, 0.5625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.6875F, 0, 0.6875F + ROOT_TWO_SCALED, 0.6875F, 0.0625F, 0.6875F + ROOT_TWO_SCALED, 0.6875F - ROOT_TWO_SCALED, 0.0625F, 0.6875F, 0.6875F - ROOT_TWO_SCALED, 0, 0.6875F);
				// light bottom
				emitter.square(facing, 0.4375F, 0.0625F, 0.5625F, 0.125F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.6875F + END_FRONT_OFFSET, 0, 0.6875F + END_FRONT_OFFSET, 0.6875F, 0, 0.6875F + ROOT_TWO_SCALED, 0.6875F - ROOT_TWO_SCALED, 0, 0.6875F, 0.6875F - END_FRONT_OFFSET, 0, 0.6875F - END_FRONT_OFFSET);
				// light left
				emitter.square(facing, 0, 0, 0.0625F, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.6875F - ROOT_TWO_SCALED, 0, 0.6875F, 0.6875F - ROOT_TWO_SCALED, 0.0625F, 0.6875F, 0.6875F - END_FRONT_OFFSET, 0.0625F, 0.6875F - END_FRONT_OFFSET, 0.6875F - END_FRONT_OFFSET, 0, 0.6875F - END_FRONT_OFFSET);
				// light right
				emitter.square(facing, 0.9375F, 0, 1, 0.0625F, 0.5F);
				emitter.spriteBake(0, SPRITES[2], MutableQuadView.BAKE_LOCK_UV);
				customShape(emitter, facing, 0.6875F + END_FRONT_OFFSET, 0, 0.6875F + END_FRONT_OFFSET, 0.6875F + END_FRONT_OFFSET, 0.0625F, 0.6875F + END_FRONT_OFFSET, 0.6875F, 0.0625F, 0.6875F + ROOT_TWO_SCALED, 0.6875F, 0, 0.6875F + ROOT_TWO_SCALED);
			} else {
				// right side top
				emitter.square(facing.rotateYClockwise(), 0.625F, 0.0625F, 1, 1, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// right side bottom
				emitter.square(facing.rotateYClockwise(), 0.6875F, 0, 1, 0.0625F, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				emitter.spriteColor(0, -1, -1, -1, -1);
				emitter.emit();
				// right side bottom square
				emitter.square(facing.rotateYClockwise(), 0.625F, 0, 0.6875F, 0.0625F, 0);
				emitter.spriteBake(0, SPRITES[1], MutableQuadView.BAKE_LOCK_UV + MutableQuadView.BAKE_FLIP_U);
				customShape(emitter, facing.rotateYClockwise(), 0.3125F, 0, 0, 0.3125F, 0.0625F, 0, 0.375F, 0.0625F, 0, 0.375F - BOTTOM_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0);
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
		return new BlockPSDTop();
	}
}
