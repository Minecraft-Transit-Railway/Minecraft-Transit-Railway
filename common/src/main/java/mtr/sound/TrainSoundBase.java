package mtr.sound;

import mtr.data.TrainClient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class TrainSoundBase {

    public TrainClient train;

    public final TrainSoundBase createTrainInstance(TrainClient train) {
        try {
            TrainSoundBase newInstance = this.getClass().getDeclaredConstructor().newInstance();
            newInstance.train = train;
            newInstance.createTrainInstance(this);
            return newInstance;
        } catch (Exception ex) {
            // This should not happen
            ex.printStackTrace();
            return null;
        }
    }

    protected abstract void createTrainInstance(TrainSoundBase src);

    public abstract void playNearestCar(Level world, BlockPos pos, int carIndex);

    public abstract void playAllCars(Level world, BlockPos pos, int carIndex);

    public abstract void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex);
}
