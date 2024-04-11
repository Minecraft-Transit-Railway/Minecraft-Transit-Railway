package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.PIDSModulePosSchema;
import org.mtr.mod.generated.resource.PIDSModuleSchema;

public final class PIDSModulePos extends PIDSModulePosSchema {
    public PIDSModulePos(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public float getX() {
        return (float) x;
    }

    public float getY() {
        return (float) y;
    }

    public float getW() {
        return (float) w;
    }

    public float getH() {
        return (float) h;
    }
}
