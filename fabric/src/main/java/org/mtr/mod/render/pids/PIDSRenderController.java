package org.mtr.mod.render.pids;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mod.resource.PIDSData;
import org.mtr.mod.resource.PIDSModulePos;

import java.util.ArrayList;

public class PIDSRenderController {
    private final ArrayList<PIDSModule> modules;
    public final int arrivals;
    public final boolean isLegacy;

    public PIDSRenderController(String layout) {
        this.modules = loadModules(layout);
        //loop through modules and find latest arrival number
        float maxArrival = 0;
        boolean legacy = false;
        for (PIDSModule module : modules) {
            if (module instanceof TextModule) {
                maxArrival = Math.max(maxArrival, ((TextModule) module).getArrival());
            }
            if (module instanceof LegacyModule) {
                maxArrival = Math.max(maxArrival, ((LegacyModule) module).getSize());
                legacy = true;
            }
        }
        this.arrivals = (int) (maxArrival + 1);
        this.isLegacy = legacy;
    }

    public ArrayList<PIDSModule> getModules() {
        return modules;
    }

    public ArrayList<PIDSModule> loadModules(String raw) {
        PIDSData data;
        try {
            data = new PIDSData(new JsonReader(JsonParser.parseString(raw)));
        } catch (Exception e) {
            return new ArrayList<>();
        }
        ArrayList<PIDSModule> modules =  new ArrayList<>();
        for (org.mtr.mod.resource.PIDSModule module : data.getModules()) {
            PIDSModulePos pos = module.getPos();
            float x = pos.getX(), y = pos.getY(), w = pos.getW(), h = pos.getH();
            switch (module.getType()) {
                case "arrivalTime":
                    modules.add(new ArrivalTimeModule(x, y, w, h, module.getData()));
                    break;
                case "block":
                    modules.add(new BlockModule(x, y, w, h, module.getData()));
                    break;
                case "destination":
                    modules.add(new DestinationModule(x, y, w, h, module.getData()));
                    break;
                case "platformNumber":
                    modules.add(new PlatformNumberModule(x, y, w, h, module.getData()));
                    break;
                case "stopsAt":
                    modules.add(new StopsAtModule(x, y, w, h, module.getData()));
                    break;
                case "lineName":
                    modules.add(new LineNameModule(x, y, w, h, module.getData()));
                    break;
                case "text":
                    modules.add(new TextModule(x, y, w, h, module.getData()));
                    break;
                case "template":
                    modules.add(new TemplateModule(x, y, w, h, module.getData()));
                    break;
                case "time":
                    modules.add(new TimeModule(x, y, w, h, module.getData()));
                    break;
                case "trainLength":
                    modules.add(new TrainLengthModule(x, y, w, h, module.getData()));
                    break;
                case "legacy":
                    modules.add(new LegacyModule(x, y, w, h, module.getData()));
                    break;
            }
        }
        return modules;
    }
}
