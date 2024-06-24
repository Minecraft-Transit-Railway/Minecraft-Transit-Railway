package mtr.client;

import mtr.KeyMappings;
import mtr.MTRClient;
import mtr.data.RailwayData;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class DebugKeys {

	private int index;
	private boolean isPressing;
	private float lastPressedTicks;
	private final int categories;
	private final float scale;
	private final int[] field1;
	private final int[] field2;
	private final int[] field3;

	public DebugKeys(int categories, float scale) {
		this.categories = categories;
		this.scale = scale;
		field1 = new int[categories];
		field2 = new int[categories];
		field3 = new int[categories];
	}

	public void tick() {
		final boolean tempPressing1Negative = KeyMappings.DEBUG_1_NEGATIVE.isDown();
		final boolean tempPressing2Negative = KeyMappings.DEBUG_2_NEGATIVE.isDown();
		final boolean tempPressing3Negative = KeyMappings.DEBUG_3_NEGATIVE.isDown();
		final boolean tempPressing1Positive = KeyMappings.DEBUG_1_POSITIVE.isDown();
		final boolean tempPressing2Positive = KeyMappings.DEBUG_2_POSITIVE.isDown();
		final boolean tempPressing3Positive = KeyMappings.DEBUG_3_POSITIVE.isDown();
		final boolean tempPressingCategoryNegative = KeyMappings.DEBUG_ROTATE_CATEGORY_NEGATIVE.isDown();
		final boolean tempPressingCategoryPositive = KeyMappings.DEBUG_ROTATE_CATEGORY_POSITIVE.isDown();

		final boolean tempIsPressing = tempPressing1Negative || tempPressing2Negative || tempPressing3Negative || tempPressing1Positive || tempPressing2Positive || tempPressing3Positive || tempPressingCategoryNegative || tempPressingCategoryPositive;
		final float gameTick = MTRClient.getGameTick();

		final boolean shouldIncrement;
		final boolean fastPress;
		if (!isPressing && tempIsPressing) {
			lastPressedTicks = gameTick;
			shouldIncrement = true;
			fastPress = false;
		} else {
			shouldIncrement = tempIsPressing && gameTick - lastPressedTicks > 10;
			fastPress = true;
		}

		if (shouldIncrement) {
			index = (categories + index + (tempPressingCategoryNegative ? -1 : tempPressingCategoryPositive ? 1 : 0)) % categories;
			field1[index] += (fastPress ? 10 : 1) * (tempPressing1Negative ? -1 : tempPressing1Positive ? 1 : 0);
			field2[index] += (fastPress ? 10 : 1) * (tempPressing2Negative ? -1 : tempPressing2Positive ? 1 : 0);
			field3[index] += (fastPress ? 10 : 1) * (tempPressing3Negative ? -1 : tempPressing3Positive ? 1 : 0);

			final LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				if (tempPressingCategoryNegative || tempPressingCategoryPositive) {
					player.displayClientMessage(Text.literal(String.format("Category: %s", index)), true);
				}
				if (tempPressing1Negative || tempPressing1Positive) {
					player.displayClientMessage(Text.literal(String.format("Category: %s - Value: %s", index, getField1(index))), true);
				}
				if (tempPressing2Negative || tempPressing2Positive) {
					player.displayClientMessage(Text.literal(String.format("Category: %s - Value: %s", index, getField2(index))), true);
				}
				if (tempPressing3Negative || tempPressing3Positive) {
					player.displayClientMessage(Text.literal(String.format("Category: %s - Value: %s", index, getField3(index))), true);
				}
			}
		}

		isPressing = tempIsPressing;
	}

	public float getField1(int category) {
		return RailwayData.round(field1[Math.abs(category) % categories] * scale, 5);
	}

	public float getField2(int category) {
		return RailwayData.round(field2[Math.abs(category) % categories] * scale, 5);
	}

	public float getField3(int category) {
		return RailwayData.round(field3[Math.abs(category) % categories] * scale, 5);
	}
}
