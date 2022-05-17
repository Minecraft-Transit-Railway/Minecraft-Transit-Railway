package mtr.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
        this.dataName = String.format("%s '%s' (#%d)", dataType, data.name, data.id);
        addField("ID").addValue(data.id);
        addField("Transport Mode").addValue(data.transportMode.name());
        addField("Name").addValue(data.name);
        addField("Color").addValue(String.format("#%06X", (data.color)));
        return this;
    }

    public DataDiffLogger addBasicProperties(String dataType, NameColorDataBase data, NameColorDataBase dataParent) {
        addBasicProperties(dataType, data);
        this.dataName = String.format("%s '%s' (#%d) from '%s' (#%d)", dataType, data.name, data.id, dataParent.name, dataParent.id);
        return this;
    }

    public DataDiffLogger addField(String name) {
        if (!isAddingFinalValues) fieldNames.add(name);
        return this;
    }

    public DataDiffLogger addValue(String value) {
        if (value.isEmpty()) value = "(EMPTY)";
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
        if (initiator == null || initialValues.size() == 0) return this; // At client, or nothing

        final JsonObject body = new JsonObject();
        body.add("content", JsonNull.INSTANCE);
        body.addProperty("username", initiator.getGameProfile().getName());

        final JsonArray embeds = new JsonArray();
        final JsonObject embed = new JsonObject();
        embed.addProperty("title", String.format("%s %sd", dataName, type.displayName));
        embed.addProperty("color", type.color);
        embed.addProperty("description",
                String.format("%s %sd %s at %s", initiator.getGameProfile().getName(), type.displayName, dataName, Instant.now().toString())
        );

        final JsonArray fields = new JsonArray();
        if (finalValues.size() == initialValues.size()) {
            for (int i = 0; i < fieldNames.size(); ++i) {
                if (!Objects.equals(initialValues.get(i), finalValues.get(i))) {
                    final JsonObject field = new JsonObject();
                    field.addProperty("name", fieldNames.get(i));
                    field.addProperty("value", initialValues.get(i) + " ->\n" + finalValues.get(i));
                    fields.add(field);
                }
            }
        } else {
            for (int i = 0; i < fieldNames.size(); ++i) {
                final JsonObject field = new JsonObject();
                field.addProperty("name", fieldNames.get(i));
                field.addProperty("value", initialValues.get(i));
                fields.add(field);
            }
        }

        embed.add("fields", fields);
        embeds.add(embed);
        body.add("embeds", embeds);

        String bodyStr = body.toString();
        final byte[] data = bodyStr.getBytes(StandardCharsets.UTF_8);
        threadPoolExecutor.submit(() -> {
            try {
                URL webhookUrl = new URL("https://xinghaicity.ldiorstudio.cn/discord-api/webhooks/975937759682375680/B4LkKv0P0aytEWQbOA1k1IUkLY1rP4iVQx6juKAHOAhuefiO-81XrHz1CxR32HK3-DHc");
                HttpURLConnection http = (HttpURLConnection) webhookUrl.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setFixedLengthStreamingMode(data.length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.addRequestProperty("User-Agent", "DiscordBot (https://www.zbx1425.cn, 114514)");
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

}
