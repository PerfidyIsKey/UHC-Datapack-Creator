import java.io.*;
import java.util.*;

public class ServerProperties {
    private final Properties properties = new Properties();

    // Default values
    private final Map<String, String> defaultValues = Map.<String, String>ofEntries(
            Map.entry("accepts-transfers", "false"),
            Map.entry("allow-flight", "false"),
            Map.entry("allow-nether", "true"),
            Map.entry("broadcast-console-to-ops", "true"),
            Map.entry("broadcast-rcon-to-ops", "true"),
            Map.entry("bug-report-link", ""),
            Map.entry("debug", "false"),
            Map.entry("difficulty", "hard"),
            Map.entry("enable-command-block", "true"),
            Map.entry("enable-jmx-monitoring", "false"),
            Map.entry("enable-query", "false"),
            Map.entry("enable-rcon", "false"),
            Map.entry("enable-status", "true"),
            Map.entry("enforce-secure-profile", "true"),
            Map.entry("enforce-whitelist", "false"),
            Map.entry("entity-broadcast-range-percentage", "100"),
            Map.entry("force-gamemode", "false"),
            Map.entry("function-permission-level", "4"),
            Map.entry("gamemode", "adventure"),
            Map.entry("generate-structures", "true"),
            Map.entry("generator-settings", "{}"),
            Map.entry("hardcore", "false"),
            Map.entry("hide-online-players", "false"),
            Map.entry("initial-disabled-packs", ""),
            Map.entry("initial-enabled-packs", "vanilla"),
            Map.entry("level-name", "world"),
            Map.entry("level-seed", "2751584509"),
            Map.entry("level-type", "default"),
            Map.entry("log-ips", "true"),
            Map.entry("max-chained-neighbor-updates", "1000000"),
            Map.entry("max-players", "50"),
            Map.entry("max-tick-time", "60000"),
            Map.entry("max-world-size", "29999984"),
            Map.entry("motd", "A Minecraft Server"),
            Map.entry("network-compression-threshold", "256"),
            Map.entry("online-mode", "false"),
            Map.entry("op-permission-level", "4"),
            Map.entry("pause-when-empty-seconds", "-1"),
            Map.entry("player-idle-timeout", "0"),
            Map.entry("prevent-proxy-connections", "false"),
            Map.entry("pvp", "true"),
            Map.entry("query.port", "25565"),
            Map.entry("rate-limit", "0"),
            Map.entry("rcon.password", ""),
            Map.entry("rcon.port", "25575"),
            Map.entry("region-file-compression", "deflate"),
            Map.entry("require-resource-pack", "false"),
            Map.entry("resource-pack", ""),
            Map.entry("resource-pack-id", ""),
            Map.entry("resource-pack-prompt", ""),
            Map.entry("resource-pack-sha1", ""),
            Map.entry("server-ip", ""),
            Map.entry("server-port", "25565"),
            Map.entry("simulation-distance", "5"),
            Map.entry("spawn-animals", "true"),
            Map.entry("spawn-monsters", "true"),
            Map.entry("spawn-npcs", "true"),
            Map.entry("spawn-protection", "0"),
            Map.entry("sync-chunk-writes", "true"),
            Map.entry("text-filtering-config", ""),
            Map.entry("text-filtering-version", "0"),
            Map.entry("use-native-transport", "true"),
            Map.entry("view-distance", "7"),
            Map.entry("white-list", "false")
    );

    public ServerProperties() {
        // Load default values
        properties.putAll(defaultValues);
    }

    public ServerProperties loadFromFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            properties.load(reader);
        }
        return this;
    }

    public void set(String key, Object value) {
        properties.setProperty(key, value.toString());
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public void set(String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    public void set(String key, int value) {
        set(key, Integer.toString(value));
    }

    public void saveToFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            properties.store(writer, "Minecraft Server Properties");
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
