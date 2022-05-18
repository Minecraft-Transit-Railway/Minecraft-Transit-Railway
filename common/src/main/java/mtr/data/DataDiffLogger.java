package mtr.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import mtr.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

public class DataDiffLogger {

    public enum ActionType {
        CREATE("create", 1879305), UPDATE("update", 5814783), REMOVE("remove", 13081620);
        public final String displayName;
        public final int color;
        ActionType(String displayName, int color) {
            this.displayName = displayName;
            this.color = color;
        }
    }

    private final ActionType type;
    private final ServerPlayer initiator;
    private final ArrayList<String> fieldNames = new ArrayList<>();
    private final ArrayList<String> initialValues = new ArrayList<>();
    private final ArrayList<String> finalValues = new ArrayList<>();
    private String dataName;
    private boolean isAddingFinalValues = false;

    public DataDiffLogger(ActionType type, ServerPlayer initiator) {
        this.type = type;
        this.initiator = initiator;
    }

    public DataDiffLogger reset() {
        fieldNames.clear();
        initialValues.clear();
        fieldNames.clear();
        isAddingFinalValues = false;
        return this;
    }

    public DataDiffLogger addBasicProperties(String dataType, NameColorDataBase data) {
        this.dataName = String.format("%s '%s'(%d)", dataType, data.name, data.id);

        addField("ID").addValue(data.id);
        addField("Transport Mode").addValue(data.transportMode.name());
        addField("Name").addValue(data.name);
        addField("Color").addValue(String.format("#%06X", (data.color)));

        if (data instanceof AreaBase areaData) {
            addField("Corner 1").addValue(String.format("BlockPos{x=%d, z=%d}", areaData.corner1.getA(), areaData.corner1.getB()));
            addField("Corner 2").addValue(String.format("BlockPos{x=%d, z=%d}", areaData.corner2.getA(), areaData.corner2.getB()));
        } else if (data instanceof SavedRailBase railData) {
            var positions = railData.getOrderedPositions(new BlockPos(0, 0, 0), false);
            addField("End 1").addValue(positions.get(0).toString());
            addField("End 2").addValue(positions.get(1).toString());
        }

        return this;
    }

    public DataDiffLogger addBasicProperties(String dataType, NameColorDataBase data, NameColorDataBase dataParent) {
        addBasicProperties(dataType, data);
        if (dataParent != null) {
            this.dataName = String.format("%s '%s'(%d) from '%s'(%d)", dataType, data.name, data.id, dataParent.name, dataParent.id);
        }
        return this;
    }

    public DataDiffLogger addField(String name) {
        if (!isAddingFinalValues) fieldNames.add(name);
        return this;
    }

    public DataDiffLogger addValue(String value) {
        if (isAddingFinalValues) {
            finalValues.add(value);
        } else {
            initialValues.add(value);
        }
        return this;
    }

    public DataDiffLogger addValue(int value) {
        return addValue(Integer.toString(value));
    }

    public DataDiffLogger addValue(long value) {
        return addValue(Long.toString(value));
    }

    public DataDiffLogger addValue(float value) {
        return addValue(Float.toString(value));
    }

    public DataDiffLogger addValue(boolean value) {
        return addValue(value ? "Yes" : "No");
    }

    public DataDiffLogger addValue(int[] value) {
        return addValue(Arrays.toString(value));
    }

    public DataDiffLogger addValue(List<Long> value) {
        return addValue(Arrays.toString(value.toArray()));
    }

    public DataDiffLogger finishAddingValues() {
        isAddingFinalValues = !isAddingFinalValues;
        return this;
    }

    private static final ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    public DataDiffLogger sendReports() {
        return sendReportToDiscord();
    }

    public DataDiffLogger sendReportToDiscord() {
        if (initiator == null) return this; // At client
        if (ServerConfig.dataDiffLoggerDiscordWebhookUrl.isEmpty()) return this; // Not configured
        if (initialValues.size() == 0) return this; // Nothing to show

        final JsonObject body = new JsonObject();
        body.add("content", JsonNull.INSTANCE);
        body.addProperty("username", initiator.getGameProfile().getName());

        final JsonArray embeds = new JsonArray();
        final JsonObject embed = new JsonObject();
        embed.addProperty("title", String.format("%s %sd", dataName, type.displayName));
        embed.addProperty("color", type.color);
        embed.addProperty("description",
                String.format("%s %sd %s at %s", initiator.getGameProfile().getName(), type.displayName, dataName,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.now()))
        );

        final int INLINE_MAX_LENGTH = 30;

        final JsonArray fields = new JsonArray();
        if (finalValues.size() == initialValues.size()) {
            for (int i = 0; i < fieldNames.size(); ++i) {
                if (!Objects.equals(initialValues.get(i), finalValues.get(i))) {
                    final JsonObject field = new JsonObject();
                    field.addProperty("name", fieldNames.get(i));
                    field.addProperty("value", eliminateEmpty(initialValues.get(i)) + " ->\n" + eliminateEmpty(finalValues.get(i)));
                    if (initialValues.get(i).length() < INLINE_MAX_LENGTH && finalValues.get(i).length() < INLINE_MAX_LENGTH)
                        field.addProperty("inline", true);
                    fields.add(field);
                }
            }
        } else {
            for (int i = 0; i < fieldNames.size(); ++i) {
                final JsonObject field = new JsonObject();
                field.addProperty("name", fieldNames.get(i));
                field.addProperty("value", eliminateEmpty(initialValues.get(i)));
                if (initialValues.get(i).length() < INLINE_MAX_LENGTH)
                    field.addProperty("inline", true);
                fields.add(field);
            }
        }
        if (fields.size() == 0) return this; // Nothing to show

        embed.add("fields", fields);
        embeds.add(embed);
        body.add("embeds", embeds);

        String bodyStr = body.toString();
        final byte[] data = bodyStr.getBytes(StandardCharsets.UTF_8);
        threadPoolExecutor.submit(() -> {
            try {
                URL webhookUrl = new URL(ServerConfig.dataDiffLoggerDiscordWebhookUrl);
                HttpURLConnection http = (HttpURLConnection) webhookUrl.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setFixedLengthStreamingMode(data.length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.addRequestProperty("User-Agent", "DiscordBot (www.zbx1425.cn, 0.0.1145141919810)");
                http.connect();
                OutputStream oStream = http.getOutputStream();
                oStream.write(data);
                oStream.flush();
                http.getInputStream().close(); // Sends the data
                http.disconnect();
            } catch (Exception ex) {
                // TODO Better Logging
                ex.printStackTrace();
            }
        });

        return this;
    }

    private String eliminateEmpty(String src) {
        return src.isEmpty() ? "(EMPTY)" : src;
    }

}
