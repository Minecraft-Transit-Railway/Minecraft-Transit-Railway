package org.mtr.mod.client;

import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.KeyBindings;

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
		final boolean tempPressing1Negative = KeyBindings.DEBUG_1_NEGATIVE.isPressed();
		final boolean tempPressing2Negative = KeyBindings.DEBUG_2_NEGATIVE.isPressed();
		final boolean tempPressing3Negative = KeyBindings.DEBUG_3_NEGATIVE.isPressed();
		final boolean tempPressing1Positive = KeyBindings.DEBUG_1_POSITIVE.isPressed();
		final boolean tempPressing2Positive = KeyBindings.DEBUG_2_POSITIVE.isPressed();
		final boolean tempPressing3Positive = KeyBindings.DEBUG_3_POSITIVE.isPressed();
		final boolean tempPressingCategoryNegative = KeyBindings.DEBUG_ROTATE_CATEGORY_NEGATIVE.isPressed();
		final boolean tempPressingCategoryPositive = KeyBindings.DEBUG_ROTATE_CATEGORY_POSITIVE.isPressed();

		final boolean tempIsPressing = tempPressing1Negative || tempPressing2Negative || tempPressing3Negative || tempPressing1Positive || tempPressing2Positive || tempPressing3Positive || tempPressingCategoryNegative || tempPressingCategoryPositive;
		final float gameTick = InitClient.getGameTick();

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

			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity != null) {
				if (tempPressingCategoryNegative || tempPressingCategoryPositive) {
					clientPlayerEntity.sendMessage(new Text(TextHelper.literal(String.format("Category: %s", index)).data), true);
				}
				if (tempPressing1Negative || tempPressing1Positive) {
					clientPlayerEntity.sendMessage(new Text(TextHelper.literal(String.format("Category: %s - Value: %s", index, getField1(index))).data), true);
				}
				if (tempPressing2Negative || tempPressing2Positive) {
					clientPlayerEntity.sendMessage(new Text(TextHelper.literal(String.format("Category: %s - Value: %s", index, getField2(index))).data), true);
				}
				if (tempPressing3Negative || tempPressing3Positive) {
					clientPlayerEntity.sendMessage(new Text(TextHelper.literal(String.format("Category: %s - Value: %s", index, getField3(index))).data), true);
				}
			}
		}

		isPressing = tempIsPressing;
	}

	public double getField1(int category) {
		return Utilities.round(field1[Math.abs(category) % categories] * scale, 5);
	}

	public double getField2(int category) {
		return Utilities.round(field2[Math.abs(category) % categories] * scale, 5);
	}

	public double getField3(int category) {
		return Utilities.round(field3[Math.abs(category) % categories] * scale, 5);
	}
}
