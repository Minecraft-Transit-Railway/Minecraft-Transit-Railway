package mtr.data;

import mtr.item.ItemNodeModifierBase;
import mtr.item.ItemNodeModifierSelectableBlockBase;
import mtr.item.ItemRailModifier;
import mtr.item.ItemSignalModifier;

public enum TransportMode {
	TRAIN(Integer.MAX_VALUE, false),
	BOAT(1, false),
	CABLE_CAR(1, true);

	public final int maxLength;
	public final boolean continuousMovement;

	TransportMode(int maxLength, boolean continuousMovement) {
		this.maxLength = maxLength;
		this.continuousMovement = continuousMovement;
	}

	public boolean canUse(ItemNodeModifierBase item) {
		return !continuousMovement || (!(item instanceof ItemRailModifier) || ((ItemRailModifier) item).isOneWayOrSavedRail()) && !(item instanceof ItemSignalModifier) && !(item instanceof ItemNodeModifierSelectableBlockBase);
	}
}
