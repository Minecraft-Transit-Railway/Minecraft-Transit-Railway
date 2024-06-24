package mtr.data;

public class TrainDelay {

	private int currentDelayCounter;
	private int delayTicks;
	private long lastDelayTime;

	private static final int CURRENT_DELAY_RESET_MILLIS = 1000;
	private static final int TOTAL_DELAY_RESET_MILLIS = 300000;

	public void delaying() {
		final long millis = System.currentTimeMillis();
		if (millis - lastDelayTime > CURRENT_DELAY_RESET_MILLIS) {
			currentDelayCounter = 0;
		}
		if (millis - lastDelayTime > TOTAL_DELAY_RESET_MILLIS) {
			delayTicks = currentDelayCounter;
		}

		currentDelayCounter++;
		delayTicks = Math.max(currentDelayCounter, delayTicks);
		lastDelayTime = millis;
	}

	public int getDelayTicks() {
		return delayTicks;
	}

	public long getLastDelayTime() {
		return lastDelayTime;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() - lastDelayTime > TOTAL_DELAY_RESET_MILLIS + CURRENT_DELAY_RESET_MILLIS;
	}
}
