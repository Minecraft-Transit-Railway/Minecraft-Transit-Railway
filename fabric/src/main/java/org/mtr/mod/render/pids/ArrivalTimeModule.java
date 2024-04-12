package org.mtr.mod.render.pids;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.mod.packet.PacketFetchArrivals;

import java.util.ArrayList;
import java.util.Objects;

public class ArrivalTimeModule extends TextModule {
    public final String type = "arrivalTime";
    private String minuteTemplate = "%s min";
    private String secondTemplate = "%s sec";
    private String mixedTemplate = "%s:%s";
    private boolean showMixed = false;

    public ArrivalTimeModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        template = "%s";
        data.unpackString("mode", (value) -> showMixed = Objects.equals(value, "b"));
        data.unpackString("secText", (value) -> secondTemplate = value);
        data.unpackString("minText", (value) -> minuteTemplate = value);
        data.unpackString("mixText", (value) -> mixedTemplate = value);
    }

    @Override
    protected ArrayList<String> getText(ObjectImmutableList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival);
        if (arrivalResponse == null) {
            return null;
        }
        //seconds until arrival
        final long time = (arrivalResponse.getArrival() - PacketFetchArrivals.getMillisOffset() - System.currentTimeMillis()) / 1000;
        if (time >= 60 && !showMixed) {
            final int minutes = (int) Math.floor((double) time / 60);
            text.add(String.valueOf(minutes));
            template = minuteTemplate;
        } else if (time < 60 && time >= 1 && !showMixed) {
            text.add(String.valueOf(time));
            template = secondTemplate;
        } else if (time >= 1) {
            final int minutes = (int) Math.floor((double) time / 60);
            final int seconds = (int) time % 60;
            text.add(String.valueOf(minutes));
            text.add(String.valueOf(seconds));
            template = mixedTemplate;
        } else {
            text.add("");
            template = "%s";
        }
        return text;
    }
}
