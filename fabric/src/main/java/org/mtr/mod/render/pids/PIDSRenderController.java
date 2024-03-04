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

    public PIDSRenderController(float width, float height) {
        this.width = width;
        this.height = height;
        loadModules("{\"id\":\"test\",\"modules\":[{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":1.5,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":0},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":3.625,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":1},{\"type\":\"DestinationModule\",\"pos\":{\"x\":1.5,\"y\":5.75,\"w\":22.5,\"h\":1.75},\"align\":\"left\",\"color\":16777215,\"arrival\":2},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":1.5,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":65280,\"arrival\":0},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":3.625,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":16777215,\"arrival\":1},{\"type\":\"ArrivalTimeModule\",\"pos\":{\"x\":24.5,\"y\":5.75,\"w\":6,\"h\":1.75},\"align\":\"right\",\"color\":16753920,\"arrival\":2}]}");
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

        Init.LOGGER.info(data);
    }
}
