package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.PIDSModuleSchema;

public final class PIDSModule extends PIDSModuleSchema {
    public PIDSModule(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public String getType() {
        return type;
    }

    public String getAlign() {
        return align;
    }

    public long getColor() {
        return color;
    }

    public long getArrival() {
        return arrival;
    }
}
