package mtr.sound;

import mtr.data.TrainClient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class TrainSoundBase {

	protected final TrainClient train;

	protected TrainSoundBase(TrainClient train) {
		this.train = train;
	}

	public abstract void playNearestCar(Level world, BlockPos pos, int carIndex);

	public abstract void playAllCars(Level world, BlockPos pos, int carIndex);

	public abstract void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex);
}
