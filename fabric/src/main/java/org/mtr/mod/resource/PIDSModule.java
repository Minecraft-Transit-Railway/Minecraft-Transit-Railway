package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.PIDSModuleSchema;

import java.util.HashMap;
import java.util.Map;

public final class PIDSModule extends PIDSModuleSchema {
    private final ReaderBase data;
    public PIDSModule(ReaderBase readerBase) {
        super(readerBase);
        data = readerBase.getChild("data");
        updateData(readerBase);
    }

    public String getType() {
        return typeID;
    }

    public ReaderBase getData() {
        return data;
    }

    public PIDSModulePos getPos() {
        return pos;
    }
}
