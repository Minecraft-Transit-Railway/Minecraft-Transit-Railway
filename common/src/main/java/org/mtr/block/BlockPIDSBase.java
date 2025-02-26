package org.mtr.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
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
		super(Blocks.createDefaultBlockSettings(true, blockState -> 5).nonOpaque());
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
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenPIDSConfigScreen(newBlockPos, ((BlockEntityBase) entity.data).maxArrivals));
			}
		});
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		public final int maxArrivals;
		public final BiPredicate<World, BlockPos> canStoreData;
		public final BiFunction<World, BlockPos, BlockPos> getBlockPosWithData;

		private final String[] messages;
		private final boolean[] hideArrivalArray;
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
			for (int i = 0; i < maxArrivals; i++) {
				messages[i] = "";
			}
			hideArrivalArray = new boolean[maxArrivals];
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			for (int i = 0; i < maxArrivals; i++) {
				messages[i] = compoundTag.getString(KEY_MESSAGE + i);
				hideArrivalArray[i] = compoundTag.getBoolean(KEY_HIDE_ARRIVAL + i);
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
				compoundTag.putBoolean(KEY_HIDE_ARRIVAL + i, hideArrivalArray[i]);
			}
			compoundTag.putLongArray(KEY_PLATFORM_IDS, new ArrayList<>(platformIds));
			compoundTag.putInt(KEY_DISPLAY_PAGE, displayPage);
		}

		public void setData(String[] messages, boolean[] hideArrivalArray, LongAVLTreeSet platformIds, int displayPage) {
			System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
			System.arraycopy(hideArrivalArray, 0, this.hideArrivalArray, 0, Math.min(hideArrivalArray.length, this.hideArrivalArray.length));
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
				return hideArrivalArray[index];
			} else {
				return false;
			}
		}

		public abstract boolean showArrivalNumber();

		public abstract boolean alternateLines();

		public abstract int textColorArrived();

		public abstract int textColor();
	}
}
