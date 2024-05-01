package org.mtr.mod.render.pids;

import org.mtr.core.data.SimplifiedRoute;
import org.mtr.core.data.SimplifiedRoutePlatform;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderPIDS;

import java.util.ArrayList;
import java.util.Objects;

public class StopsAtModule extends TextModule {
    public final String type = "stopsAt";
    private boolean scrollText;
    private boolean showPage;
    private int stop;
    private int stopIncrement;
    private float scrollSpeed;


    public StopsAtModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
        data.unpackString("baseText", (value) -> template = value);
        data.unpackString("mode", (value) -> scrollText = Objects.equals(value, "s"));
        data.unpackBoolean("showPage", (value) -> showPage = value);
        data.unpackInt("stop", (value) -> stop = value);
        data.unpackInt("stopIncrement", (value) -> stopIncrement = value);
        data.unpackLong("scrollSpeed", (value) -> scrollSpeed = value);
    }

    @Override
    protected ArrayList<String> getText(ObjectList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival);
        if (arrivalResponse == null) {
            return null;
        }
        ObjectArrayList<SimplifiedRoutePlatform> stops = getStops(arrivalResponse);
        if (stops == null || stops.isEmpty()) {
            return null;
        }
        if (this.scrollText) {
            StringBuilder stopText = new StringBuilder();
            for (int i = 0; i < stops.size(); i++) {
                if (i != 0 && i == stops.size() - 1) {
                    stopText.append("and ");
                }
                stopText.append(stops.get(i).getStationName());
                if (i != stops.size() - 1) {
                    stopText.append(", ");
                }
            }
            text.add(stopText.toString());
        } else {
            int pages = (int) Math.ceil((double) stops.size() / stopIncrement);
            long time = 0;
            if (MinecraftClient.getInstance().getWorldMapped() != null) {
                time = MinecraftClient.getInstance().getWorldMapped().getTime();
            }
            int currentPage = (int) Math.floor((double) time / RenderPIDS.SWITCH_TEXT_TICKS) % pages;

            if (this.showPage) {
                text.add(String.valueOf(currentPage + 1));
                text.add(String.valueOf(pages));
            }

            if (stops.size() <= stop + currentPage * stopIncrement) return null;
            text.add(stops.get(stop + currentPage * stopIncrement).getStationName());
        }
        return text;
    }

    protected ObjectArrayList<SimplifiedRoutePlatform> getStops(ArrivalResponse arrival) {
        SimplifiedRoute route = null;
        for (SimplifiedRoute r : MinecraftClientData.getInstance().simplifiedRoutes) {
            if (r.getId() == arrival.getRouteId()) {
                route = r;
                break;
            }
        }
        if (route == null) {
            return null;
        }
        long currentPlatform = arrival.getPlatformId();
        ObjectArrayList<SimplifiedRoutePlatform> stops = route.getPlatforms();
        ObjectArrayList<SimplifiedRoutePlatform> stopsAt = new ObjectArrayList<>();
        boolean stopped = false;
        for (SimplifiedRoutePlatform stop : stops) {
            if (stopped) stopsAt.add(stop);
            else if (stop.getPlatformId() == currentPlatform) {
                stopped = true;
            }
        }
        return stopsAt;
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, ObjectList<ArrivalResponse> arrivals, RenderPIDS renderPIDS, BlockPIDSBase.BlockEntityBase entity, BlockPos blockPos, Direction facing) {
        if (this.scrollText) {
            final float textPadding = height * 0.1f;
            final float textSize = height - textPadding * 2;
            final float scale = textSize / 8;
            ArrayList<String> placeholders = getText(arrivals);
            if (placeholders == null || placeholders.isEmpty()) {
                return;
            }
            String text = String.format(template, placeholders.toArray());
            float width = GraphicsHolder.getTextWidth(text) * scale + this.width;
            // seconds for the text to scroll across the screen
            float animationTime = width / scrollSpeed;
            double time = ((double) System.currentTimeMillis() / 1000L) % animationTime;
            double x = (-time / animationTime) * width + this.x + this.width;
            String trimmedText = text;
            // trim text overflowing to the left
            while (x < this.x && !trimmedText.isEmpty()) {
                int charLength = RenderPIDS.getCharWidth(trimmedText.charAt(0));
                x += charLength * scale;
                trimmedText = trimmedText.substring(1);
            }
            // trim text overflowing to the right
            int[] charWidths = RenderPIDS.getCharWidths(trimmedText);
            double currentRight = x + GraphicsHolder.getTextWidth(trimmedText) * scale;
            int rightBound = trimmedText.length();
            while (currentRight > this.x + this.width && rightBound > 0) {
                rightBound--;
                currentRight -= charWidths[rightBound] * scale;
            }
            trimmedText = trimmedText.substring(0, rightBound);
            RenderPIDS.renderText(graphicsHolder, trimmedText, (float) x, this.y + textPadding, textSize, color, this.width * 2, IGui.HorizontalAlignment.LEFT, layer);
        } else {
            super.render(graphicsHolder, arrivals, renderPIDS, entity, blockPos, facing);
        }
    }
}
