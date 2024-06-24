package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.generated.resource.SignResourceSchema;

public final class SignResource extends SignResourceSchema {

	public final boolean hasCustomText;

	public SignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		hasCustomText = !customText.isEmpty();
	}

	public String getId() {
		return id;
	}

	public Identifier getTexture() {
		return CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
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
