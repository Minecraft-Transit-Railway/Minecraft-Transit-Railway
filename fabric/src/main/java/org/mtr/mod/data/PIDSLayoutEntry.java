package org.mtr.mod.data;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mod.render.pids.PIDSRenderController;
import org.mtr.mod.resource.PIDSData;

public class PIDSLayoutEntry {
    public final String data;
    public final PIDSLayoutMetadata metadata;

    public PIDSLayoutEntry(String data) {
        PIDSData pidsData = new PIDSData(new JsonReader(JsonParser.parseString(data)));
        this.metadata = new PIDSLayoutMetadata(pidsData.getID(), pidsData.getName(), pidsData.getDescription(), pidsData.getAuthor(), pidsData.getModules().size(), new PIDSRenderController(data).arrivals);
        this.data = data;
    }

    public static class PIDSLayoutMetadata {
        public final String id;
        public final String name;
        public final String description;
        public final String author;
        public final int moduleCount;
        public final int arrivalCount;

        public PIDSLayoutMetadata(String id, String name, String description, String author, int moduleCount, int arrivalCount) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.author = author;
            this.moduleCount = moduleCount;
            this.arrivalCount = arrivalCount;
        }
    }
}
