package org.mtr.resource;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.LiftResourceSchema;

public final class LiftResource extends LiftResourceSchema {

	public LiftResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return Text.translatable(name).getString();
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public Identifier getTexture() {
		return CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
	}
}
