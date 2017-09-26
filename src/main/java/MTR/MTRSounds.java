package MTR;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class MTRSounds {

	public static SoundEvent trainDoorclose;
	public static SoundEvent trainDoorclosetrain;
	public static SoundEvent trainDoorcloseplatform;
	public static SoundEvent trainDooropen;
	public static SoundEvent platformApgclose;
	public static SoundEvent platformApgopen;
	public static SoundEvent platformPsdclose;
	public static SoundEvent platformPsdopen;
	public static SoundEvent lightrail1Doorclose;
	public static SoundEvent lightrail1Dooropen;
	public static SoundEvent sp1900Doorclose;
	public static SoundEvent sp1900Dooropen;

	public static void init() {
		trainDoorclose = createSound("train.doorclose");
		trainDoorclosetrain = createSound("train.doorclosetrain");
		trainDoorcloseplatform = createSound("train.doorcloseplatform");
		trainDooropen = createSound("train.dooropen");
		platformApgclose = createSound("platform.apgclose");
		platformApgopen = createSound("platform.apgopen");
		platformPsdclose = createSound("platform.psdclose");
		platformPsdopen = createSound("platform.psdopen");
		lightrail1Doorclose = createSound("lightrail1.doorclose");
		lightrail1Dooropen = createSound("lightrail1.dooropen");
		sp1900Doorclose = createSound("sp1900.doorclose");
		sp1900Dooropen = createSound("sp1900.dooropen");
	}

	private static SoundEvent createSound(String soundName) {
		ResourceLocation soundID = new ResourceLocation("mtr", soundName);
		SoundEvent event = new SoundEvent(soundID).setRegistryName(soundID);
		return event;
	}
}
