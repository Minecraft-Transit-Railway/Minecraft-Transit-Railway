package mtr.data;

import mtr.item.ItemNodeModifierBase;
import mtr.item.ItemNodeModifierSelectableBlockBase;
import mtr.item.ItemRailModifier;
import mtr.item.ItemSignalModifier;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE, false, true, 0),
	BOAT(1, false, true, 0),
	CABLE_CAR(1, true, false, -5);

	public final int maxLength;
	public final boolean continuousMovement;
	public final boolean hasPitch;
	public final int railOffset;

	TransportMode(int maxLength, boolean continuousMovement, boolean hasPitch, int railOffset) {
		this.maxLength = maxLength;
		this.continuousMovement = continuousMovement;
		this.hasPitch = hasPitch;
		this.railOffset = railOffset;
	}

	public boolean canUse(ItemNodeModifierBase item) {
		return !continuousMovement || (!(item instanceof ItemRailModifier) || ((ItemRailModifier) item).forContinuousMovement()) && !(item instanceof ItemSignalModifier) && !(item instanceof ItemNodeModifierSelectableBlockBase);
	}
}
