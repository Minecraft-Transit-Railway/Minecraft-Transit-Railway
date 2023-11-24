package org.mtr.mod.data;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.TransportMode;
import org.mtr.mod.client.ClientData;

public class DataConverter extends NameColorDataBase {

	public DataConverter(String name, int color) {
		super(TransportMode.TRAIN, ClientData.getInstance());
		this.name = name;
		this.color = color;
	}

	@Override
	public boolean isValid() {
		return false;
	}
}
