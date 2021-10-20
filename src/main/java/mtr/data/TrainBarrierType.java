package mtr.data;

public class TrainBarrierType {

    public static void TrainBarrier(String[] args) {
        trainbarrieradd R179 = new trainbarrieradd(true, 0.2f);
    }

    static class trainbarrieradd {
        boolean trainBarrierOption;
        public float trainBarriersLength;

        trainbarrieradd(boolean trainBarriersOption,
                        float trainBarriersLength) {
            this.trainBarrierOption = trainBarriersOption;
            this.trainBarriersLength = trainBarriersLength;
        }
        public float getSpacing() {return trainBarriersLength;}
    }
}

