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
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Angle;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketApplyRailAction;
import org.mtr.registry.DataComponentTypes;
import org.mtr.registry.RegistryClient;

import java.util.List;

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

		final Player player = context.getPlayer();
		if (context.getLevel().isClientSide() && player != null && !player.isShiftKeyDown()) {
			final BlockPos startPos = context.getItemInHand().get(DataComponentTypes.START_POS.get());
			if (startPos != null && clickCondition(context)) {
				final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> path = findRailPath(startPos, context.getClickedPos());
				if (path != null && path.size() > 1) {
					RegistryClient.sendPacketToServer(new PacketApplyRailAction(path));
				}
			}
		}

		return super.useOn(context);
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		if (hasWallSideMode() && player.isShiftKeyDown() && player.pick(player.blockInteractionRange(), 0, false).getType() == HitResult.Type.MISS) {
			if (!world.isClientSide()) {
				final ItemStack stack = player.getItemInHand(hand);
				final int next = (stack.getOrDefault(DataComponentTypes.WALL_SIDE.get(), 0) + 1) % 3;
				stack.set(DataComponentTypes.WALL_SIDE.get(), next);
				player.displayClientMessage(getWallSideComponent(next), true);
			}
			return InteractionResult.SUCCESS;
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
			tooltip.add(Component.translatable("tooltip.mtr.wall_side", getWallSideComponent(stack.getOrDefault(DataComponentTypes.WALL_SIDE.get(), 0))).withStyle(ChatFormatting.GRAY));
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
			getRail(world, posStart, posEnd, serverPlayerEntity, rail -> onConnect(rail, serverPlayerEntity, itemStack, radius, height));
		}
	}

	@Override
	protected final void onRemove(Level world, BlockPos posStart, BlockPos posEnd, @Nullable ServerPlayer serverPlayerEntity) {
	}

	protected BlockState getSavedState(ItemStack stack) {
		final Integer blockId = stack.get(DataComponentTypes.BLOCK_ID.get());
		return blockId == null ? Blocks.AIR.defaultBlockState() : Block.stateById(blockId);
	}

	public abstract void onConnect(Rail rail, ServerPlayer serverPlayerEntity, ItemStack itemStack, int radius, int height);

	@Nullable
	private static ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> findRailPath(BlockPos startBlockPos, BlockPos endBlockPos) {
		final Position startPosition = MTR.blockPosToPosition(startBlockPos);
		final Position endPosition = MTR.blockPosToPosition(endBlockPos);

		if (!MinecraftClientData.getInstance().positionsToRail.containsKey(startPosition)) {
			return null;
		}

		final Object2ObjectOpenHashMap<Position, Position> parentMap = new Object2ObjectOpenHashMap<>();
		final ObjectArrayList<Position> queue = new ObjectArrayList<>();
		queue.add(startPosition);
		parentMap.put(startPosition, startPosition);

		boolean found = false;
		int queueIndex = 0;
		while (queueIndex < queue.size()) {
			final Position current = queue.get(queueIndex++);
			if (current.equals(endPosition)) {
				found = true;
				break;
			}
			MinecraftClientData.getInstance().positionsToRail.getOrDefault(current, new Object2ObjectOpenHashMap<>()).keySet().forEach(neighbor -> {
				if (!parentMap.containsKey(neighbor)) {
					parentMap.put(neighbor, current);
					queue.add(neighbor);
				}
			});
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
