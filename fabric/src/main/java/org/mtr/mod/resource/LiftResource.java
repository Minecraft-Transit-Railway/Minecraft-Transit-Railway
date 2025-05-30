package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.generated.resource.LiftResourceSchema;

public final class LiftResource extends LiftResourceSchema {

    public LiftResource(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public String getId() {
        return id;
    }

    public Identifier getTexture() {
        return CustomResourceTools.formatIdentifierWithDefault(textureResource, "png");
    }

    public MutableText getName() {
        return TextHelper.translatable(name);
    }

    public int getColor() {
        return CustomResourceTools.colorStringToInt(color);
    }
}
