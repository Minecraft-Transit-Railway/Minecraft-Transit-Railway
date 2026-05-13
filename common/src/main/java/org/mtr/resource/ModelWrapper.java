package org.mtr.resource;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.ModelWrapperSchema;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class ModelWrapper extends ModelWrapperSchema {

	public ModelWrapper(String id, ObjectArrayList<String> modelParts) {
		super(id);
		this.modelParts.addAll(modelParts);
	}

	public ModelWrapper(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public String getId() {
		return id;
	}
}
