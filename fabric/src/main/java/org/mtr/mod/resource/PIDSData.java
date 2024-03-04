package org.mtr.mod.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.resource.PIDSSchema;

public final class PIDSData extends PIDSSchema {
    public PIDSData(ReaderBase readerBase) {
        super(readerBase);
        updateData(readerBase);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList<PIDSModule> getModules() {
        return modules;
    }
}
