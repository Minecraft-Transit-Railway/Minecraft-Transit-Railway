package mtr.sound;

import mtr.data.TrainClient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class TrainSoundBase {

    TrainClient train;

    public final TrainSoundBase createTrainInstance(TrainClient train) {
        try {
            TrainSoundBase newInstance = this.getClass().getDeclaredConstructor().newInstance();
            newInstance.copyFrom(this);
            newInstance.train = train;
            return newInstance;
        } catch (Exception ex) {
            // This should not happen
            ex.printStackTrace();
            return null;
        }
    }

    protected abstract void copyFrom(TrainSoundBase src);

    public abstract void playElapseSound(Level world, BlockPos pos);

    public abstract void playDoorSound(Level world, BlockPos pos);
}
