package org.mtr.resource;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.SignResourceSchema;

public final class SignResource extends SignResourceSchema {

	public final boolean hasCustomText;
	// Default signs (the ones bundled with Minecraft Transit Railway) have IDs starting with "!"
	public final boolean isDefault;
	public final String signId;

	public SignResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
		hasCustomText = !customText.isEmpty();
		isDefault = id.startsWith("!");
		signId = isDefault ? id.substring(1) : id;
	}

	public Identifier getTexture() {
		return CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
	}

	public boolean getFlipTexture() {
		return flipTexture;
	}

	public MutableText getCustomText() {
		return Text.translatable(customText);
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
