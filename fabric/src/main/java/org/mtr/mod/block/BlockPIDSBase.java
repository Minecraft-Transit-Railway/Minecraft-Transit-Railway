package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.packet.PacketOpenPIDSConfigScreen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class BlockPIDSBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public final int maxArrivals;
	public final BiPredicate<World, BlockPos> canStoreData;
	public final BiFunction<World, BlockPos, BlockPos> getBlockPosWithData;

	public BlockPIDSBase(int maxArrivals, BiPredicate<World, BlockPos> canStoreData, BiFunction<World, BlockPos, BlockPos> getBlockPosWithData) {
		super(BlockHelper.createBlockSettings(true, blockState -> 5));
		this.maxArrivals = maxArrivals;
		this.canStoreData = canStoreData;
		this.getBlockPosWithData = getBlockPosWithData;
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos newBlockPos = getBlockPosWithData.apply(world, pos);
			final BlockEntity entity = world.getBlockEntity(newBlockPos);
			if (entity != null && entity.data instanceof BlockEntityBase) {
				((BlockEntityBase) entity.data).markDirty2();
				Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenPIDSConfigScreen(newBlockPos, ((BlockEntityBase) entity.data).maxArrivals));
			}
		});
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		public final int maxArrivals;
		public final BiPredicate<World, BlockPos> canStoreData;
		public final BiFunction<World, BlockPos, BlockPos> getBlockPosWithData;

		public final String[] messages;
		private final boolean[] hideArrival;
		private final LongAVLTreeSet platformIds = new LongAVLTreeSet();
		private int displayPage;
		private static final String KEY_MESSAGE = "message";
		private static final String KEY_HIDE_ARRIVAL = "hide_arrival";
		private static final String KEY_PLATFORM_IDS = "platform_ids";
		private static final String KEY_DISPLAY_PAGE = "display_page";

		public BlockEntityBase(int maxArrivals, BiPredicate<World, BlockPos> canStoreData, BiFunction<World, BlockPos, BlockPos> getBlockPosWithData, BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
			this.maxArrivals = maxArrivals;
			this.canStoreData = canStoreData;
			this.getBlockPosWithData = getBlockPosWithData;
			messages = new String[maxArrivals];
			hideArrival = new boolean[maxArrivals];
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			for (int i = 0; i < maxArrivals; i++) {
				messages[i] = compoundTag.getString(KEY_MESSAGE + i);
			}
			for (int i = 0; i < maxArrivals; i++) {
				hideArrival[i] = compoundTag.getBoolean(KEY_HIDE_ARRIVAL + i);
			}
			platformIds.clear();
			final long[] platformIdsArray = compoundTag.getLongArray(KEY_PLATFORM_IDS);
			for (final long platformId : platformIdsArray) {
				platformIds.add(platformId);
			}
			displayPage = compoundTag.getInt(KEY_DISPLAY_PAGE);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			for (int i = 0; i < maxArrivals; i++) {
				compoundTag.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
			}
			for (int i = 0; i < maxArrivals; i++) {
				compoundTag.putBoolean(KEY_HIDE_ARRIVAL + i, hideArrival[i]);
			}
			compoundTag.putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(platformIds));
			compoundTag.putInt(KEY_DISPLAY_PAGE, displayPage);
		}

		public void setData(String[] messages, boolean[] hideArrival, LongAVLTreeSet platformIds, int displayPage) {
			System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
			System.arraycopy(hideArrival, 0, this.hideArrival, 0, Math.min(hideArrival.length, this.hideArrival.length));
			this.platformIds.clear();
			this.platformIds.addAll(platformIds);
			this.displayPage = displayPage;
			markDirty2();
		}

		public int getDisplayPage() {
			return displayPage;
		}

		public LongAVLTreeSet getPlatformIds() {
			return platformIds;
		}

		public String getMessage(int index) {
			if (index >= 0 && index < maxArrivals) {
				if (messages[index] == null) {
					messages[index] = "";
				}
				return messages[index];
			} else {
				return "";
			}
		}

		public boolean getHideArrival(int index) {
			if (index >= 0 && index < maxArrivals) {
				return hideArrival[index];
			} else {
				return false;
			}
		}
	}
}
