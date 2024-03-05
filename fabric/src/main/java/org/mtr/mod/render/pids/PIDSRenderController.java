package org.mtr.mod.render.pids;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mod.Init;
import org.mtr.mod.resource.PIDSData;

import java.util.List;

public class PIDSRenderController {
    private List<PIDSModule> modules;
    protected final float width;
    protected final float height;

    public PIDSRenderController(float width, float height, String defaultLayout) {
        this.width = width;
        this.height = height;
        loadModules(defaultLayout);
    }

    public List<PIDSModule> getModules() {
        return modules;
    }

    public void addModule(PIDSModule module) {
        modules.add(module);
    }

    public void clearModules() {
        modules.clear();
    }

    public void loadModules(String raw) {
        PIDSData data;
        try {
            data = new PIDSData(new JsonReader(JsonParser.parseString(raw)));
        } catch (Exception e) {
            Init.LOGGER.error("", e);
            data = new PIDSData(new JsonReader(new JsonObject()));
        }
    }
}
