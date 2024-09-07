package org.mtr.mod.config;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.generated.config.ServerSchema;

public final class Server extends ServerSchema {

	public Server(ReaderBase readerBase) {
		super(readerBase);
		updateData(readerBase);
	}

	public int getWebserverPort() {
		return (int) webserverPort;
	}

	public boolean getUseThreadedSimulation() {
		return useThreadedSimulation;
	}
}
