package org.mtr.mod.render.pids;

import java.util.List;

public class PIDSRenderController {
    private List<PIDSModule> modules;
    protected final float width;
    protected final float height;

    public PIDSRenderController(float width, float height) {
        this.width = width;
        this.height = height;
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
}
