package org.mtr.item;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.block.BlockNode;
import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.core.tool.Vector;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketApplyRailAction;
import org.mtr.registry.DataComponentTypes;
import org.mtr.registry.RegistryClient;

import java.util.List;

//? if < 1.21.4 {
/*import net.minecraft.world.InteractionResultHolder;
*///? }

public abstract class ItemNodeModifierSelectableBlockBase extends ItemNodeModifierBase {

	private final boolean canSaveBlock;
	@Getter
	private final int height;
	private final int width;
	@Getter
	private final int radius;

	public ItemNodeModifierSelectableBlockBase(boolean canSaveBlock, int height, int width, Item.Properties settings) {
		super(true, false, false, true, settings);
		this.canSaveBlock = canSaveBlock;
		this.height = height;
		this.width = width;
		radius = width / 2;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (canSaveBlock) {
			final Level world = context.getLevel();
			if (!world.isClientSide()) {
				final Player playerEntity = context.getPlayer();
				if (playerEntity != null && playerEntity.isShiftKeyDown()) {
					final BlockState state = world.getBlockState(context.getClickedPos());
					final BlockState neighborState;
					if (state.getBlock() instanceof BlockNode || state.getBlock() == getSavedState(context.getItemInHand()).getBlock()) {
						neighborState = Blocks.AIR.defaultBlockState();
					} else {
						neighborState = state;
					}
					playerEntity.displayClientMessage(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getText(Component.translatable(neighborState.getBlock().getDescriptionId()).getString()), true);
					context.getItemInHand().set(DataComponentTypes.BLOCK_ID.get(), Block.getId(neighborState));
					return InteractionResult.SUCCESS;
				}
			}
		}

		if (context.getLevel().isClientSide()) {
			final Player player = context.getPlayer();
			if (player != null && !player.isShiftKeyDown()) {
				final BlockPos startPos = context.getItemInHand().get(DataComponentTypes.START_POS.get());
				if (startPos != null && clickCondition(context)) {
					RegistryClient.sendPacketToServer(new PacketApplyRailAction(startPos, context.getClickedPos()));
				}
			}
		}

		return super.useOn(context);
	}

	@Override
	//? if >= 1.21.4 {
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
	//? } else {
	/*public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
	*///? }
		final ItemStack stack = player.getItemInHand(hand);
		if (hasWallSideMode() && player.isShiftKeyDown() && player.pick(player.blockInteractionRange(), 0, false).getType() == HitResult.Type.MISS) {
			if (!world.isClientSide()) {
				final int next = (stack.getOrDefault(DataComponentTypes.WALL_SIDE.get(), 0) + 1) % 3;
				stack.set(DataComponentTypes.WALL_SIDE.get(), next);
				player.displayClientMessage(getWallSideComponent(next), true);
			}
			//? if >= 1.21.4 {
			return InteractionResult.SUCCESS;
			//? } else {
			/*return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());*/
			//? }
		}
		return super.use(world, player, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if (height > 0) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_HEIGHT.getMutableText(height).withStyle(ChatFormatting.GRAY));
		}
		tooltip.add(TranslationProvider.TOOLTIP_MTR_RAIL_ACTION_WIDTH.getMutableText(width).withStyle(ChatFormatting.GRAY));

		if (canSaveBlock) {
			final BlockState state = getSavedState(stack);
			final String[] textSplit = (state.isAir() ? TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_SELECT_MATERIAL : TranslationProvider.TOOLTIP_MTR_SHIFT_RIGHT_CLICK_TO_CLEAR).getString(MTRClient.getShiftText(), Component.translatable(org.mtr.registry.Blocks.RAIL_NODE.get().getDescriptionId())).split("\\|");
			for (String text : textSplit) {
				tooltip.add(Component.literal(text).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
			}
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_MATERIAL.getMutableText(Component.translatable(state.getBlock().getDescriptionId()).getString()).withStyle(ChatFormatting.GREEN));
		}

		if (hasWallSideMode()) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_USE_IN_AIR_TO_CHANGE_SIDE.getMutableText(MTRClient.getUseText()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
			tooltip.add(Component.translatable("tooltip.mtr.wall_side", getWallSideComponent(stack.getOrDefault(DataComponentTypes.WALL_SIDE.get(), 0))).withStyle(ChatFormatting.GREEN));
		}

		super.appendHoverText(stack, context, tooltip, type);
	}

	protected boolean hasWallSideMode() {
		return false;
	}

	private static Component getWallSideComponent(int side) {
		return switch (side) {
			case 1 -> Component.translatable("tooltip.mtr.wall_side_left");
			case 2 -> Component.translatable("tooltip.mtr.wall_side_right");
			default -> Component.translatable("tooltip.mtr.wall_side_both");
		};
	}

	@Override
	protected final void onConnect(Level world, ItemStack itemStack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Angle facingStart, Angle facingEnd, @Nullable ServerPlayer serverPlayerEntity) {
		if (serverPlayerEntity != null) {
			getRail(world, posStart, posEnd, serverPlayerEntity, rail -> onConnect(rail, serverPlayerEntity, itemStack, radius, height, 1, 1, posStart, posEnd));
		}
	}

	@Override
	protected final void onRemove(Level world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayer serverPlayerEntity) {
	}

	@Override
	protected void onEndClick(UseOnContext context, BlockPos posEnd) {
		context.getItemInHand().remove(DataComponentTypes.TRANSPORT_MODE.get());
	}

	protected BlockState getSavedState(ItemStack stack) {
		final Integer blockId = stack.get(DataComponentTypes.BLOCK_ID.get());
		return blockId == null ? Blocks.AIR.defaultBlockState() : Block.stateById(blockId);
	}

	protected abstract void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height, int batchIndex, int batchTotal, BlockPos posStart, BlockPos posEnd);

	protected static int resolveWallSide(Rail rail, BlockPos posStart, BlockPos posEnd, int wallSide) {
		if (wallSide == 0) {
			return wallSide;
		}
		final Vector positionAtStartOfRail = rail.railMath.getPosition(0, false);
		final Vector start = new Vector(posStart.getX(), posStart.getY(), posStart.getZ());
		final Vector end = new Vector(posEnd.getX(), posEnd.getY(), posEnd.getZ());
		final boolean matchesClickOrder = positionAtStartOfRail.distanceTo(start) <= positionAtStartOfRail.distanceTo(end);
		return matchesClickOrder ? wallSide : (wallSide == 1 ? 2 : 1);
	}

	public static void processRailActions(ServerPlayer serverPlayerEntity, ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> railPairs) {
		final ItemStack itemStack = serverPlayerEntity.getMainHandItem();
		if (!(itemStack.getItem() instanceof ItemNodeModifierSelectableBlockBase item)) {
			return;
		}

		final int capturedRadius = item.radius;
		final int capturedHeight = item.height;
		final int batchTotal = railPairs.size();
		for (int i = 0; i < railPairs.size(); i++) {
			final ObjectObjectImmutablePair<BlockPos, BlockPos> pair = railPairs.get(i);
			final int batchIndex = i + 1;
			getRail(
				serverPlayerEntity.serverLevel(),
				pair.left(),
				pair.right(),
				serverPlayerEntity,
				rail -> item.onConnect(rail, serverPlayerEntity, itemStack, capturedRadius, capturedHeight, batchIndex, batchTotal, pair.left(), pair.right())
			);
		}
	}

	/**
	 * BFS over a rail graph (client cache or a server-fetched snapshot) to find a path of rail
	 * segments connecting startPosition to endPosition.
	 */
	@Nullable
	public static ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> findRailPath(Object2ObjectOpenHashMap<Position, Object2ObjectOpenHashMap<Position, Rail>> positionsToRail, Position startPosition, Position endPosition) {
		if (!positionsToRail.containsKey(startPosition)) {
			return null;
		}

		// BFS to find any path from start to end through connected rail positions
		final Object2ObjectOpenHashMap<Position, Position> parentMap = new Object2ObjectOpenHashMap<>();
		final ObjectArrayList<Position> queue = new ObjectArrayList<>();
		queue.add(startPosition);
		parentMap.put(startPosition, startPosition);

		boolean found = false;
		for (int queueIndex = 0; queueIndex < queue.size(); queueIndex++) {
			final Position current = queue.get(queueIndex);
			if (current.equals(endPosition)) {
				found = true;
				break;
			}

			final Object2ObjectOpenHashMap<Position, Rail> neighbors = positionsToRail.getOrDefault(current, new Object2ObjectOpenHashMap<>());
			for (final Position neighbor : neighbors.keySet()) {
				if (!parentMap.containsKey(neighbor)) {
					parentMap.put(neighbor, current);
					queue.add(neighbor);
				}
			}
		}

		if (!found) {
			return null;
		}

		final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> path = new ObjectArrayList<>();
		Position current = endPosition;
		while (!parentMap.get(current).equals(current)) {
			final Position previous = parentMap.get(current);
			path.add(0, new ObjectObjectImmutablePair<>(MTR.positionToBlockPos(previous), MTR.positionToBlockPos(current)));
			current = previous;
		}
		return path;
	}
}
