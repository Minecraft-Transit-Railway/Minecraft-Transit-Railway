package org.mtr.mod;

public abstract class CustomThread extends Thread {

	private boolean started;

	@Override
	public synchronized final void start() {
		if (!started) {
			super.start();
		}
		started = true;
	}

	@Override
	public final void run() {
		while (isRunning()) {
			try {
				runTick();
			} catch (Exception e) {
				Init.logException(e);
			}
		}
	}

	protected abstract void runTick();

	protected abstract boolean isRunning();
}
