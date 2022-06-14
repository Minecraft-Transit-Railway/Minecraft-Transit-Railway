package mtr.sound.bve;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BveTrainSoundConfig {

    public String audioBaseName;

    public SoundCfg soundCfg;
    public MotorDataBase motorData;

    public float doorCloseSoundTime = 5;
    public float regenerationLimit = 10F / 3.6F;

    public BveTrainSoundConfig(ResourceManager manager, String configBaseName, String audioBaseName) {
        this.audioBaseName = audioBaseName;
        soundCfg = new SoundCfg(readResource(manager, new ResourceLocation(configBaseName + "/sound.cfg")));
        motorData = new MotorData5(manager, configBaseName);
    }

    public static String readResource(ResourceManager manager, ResourceLocation location) {
        try {
            List<Resource> resources = manager.getResources(location);
            if (resources.size() < 1) return "";
            InputStream iStream = resources.get(0).getInputStream();
            return IOUtils.toString(iStream, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return "";
        }
    }

}
