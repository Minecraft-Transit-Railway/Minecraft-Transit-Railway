package org.mtr.core.data;

import org.mtr.core.generated.SignResourceSchema;
import org.mtr.core.serializers.ReaderBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;

public final class SignResource extends SignResourceSchema {

	public final boolean hasCustomText;

	public SignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		hasCustomText = !customText.isEmpty();
	}

	public void print() {
		Init.LOGGER.info(String.format("%s %s", id, customText.isEmpty() ? "" : String.format("(%s)", getCustomText().getString())));
	}

	public String getId() {
		return id;
	}

	public Identifier getTexture() {
		return CustomResourceTools.formatIdentifier(textureResource, "png");
	}

	public boolean getFlipTexture() {
		return flipTexture;
	}

	public MutableText getCustomText() {
		return TextHelper.translatable(customText);
	}

	public boolean getFlipCustomText() {
		return flipCustomText;
	}

	public boolean getSmall() {
		return small;
	}

	public int getBackgroundColor() {
		return CustomResourceTools.colorStringToInt(backgroundColor);
	}
}
