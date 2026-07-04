package org.mtr.resource;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.LiftResourceSchema;

public final class LiftResource extends LiftResourceSchema implements Comparable<LiftResource> {

	public LiftResource(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return Component.translatable(name).getString();
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public ResourceLocation getTexture() {
		return CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
	}

	@Override
	public int compareTo(LiftResource liftResource) {
		return liftResource.getName().compareTo(getName());
	}
}
