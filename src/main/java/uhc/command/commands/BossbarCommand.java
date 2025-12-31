package uhc.command.commands;

import uhc.game.bossbar.BossbarData;
import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.bossbar.BossbarId;
import uhc.resource.bossbar.BossbarStyle;
import uhc.resource.color.BossbarColor;
import uhc.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🚧 **Bossbar Command Builder**
 * <p>
 * This class provides a fluent interface for generating Minecraft {@code /bossbar} command strings.
 * It is designed to be type-safe, using specific objects for IDs and Text rather than raw strings.
 * </p>
 */
public class BossbarCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The primary Minecraft sub-command action (e.g., "add", "set", "remove", "list", "get"). */
    private final String action;

    /** * The unique namespaced identifier for the target boss bar. */
    private BossbarId id;

    /** * The display name of the boss bar, used during creation or renaming. */
    private TextComponent name;

    /** * The specific property string targeted for 'set' or 'get' actions (e.g., "color", "max"). */
    private String property;

    /** * The visual color of the boss bar. */
    private BossbarColor color;

    /** * The maximum progress value for the bar. */
    private Integer max;

    /** * The entity selector (usually players) who can see this boss bar. */
    private Entity targets;

    /** * The visual segmentation style (notches) of the bar. */
    private BossbarStyle style;

    /** * The current fill value of the bar. */
    private Integer value;

    /** * Whether the boss bar is currently visible to assigned players. */
    private Boolean visible;

    // --- 🏗️ Private Constructor ---

    /**
     * Initializes a command builder with a fixed action.
     * Enforced through static factory methods to ensure valid sub-commands.
     *
     * @param action The lowercase Minecraft bossbar sub-command.
     */
    private BossbarCommand(String action) {
        this.action = action;
    }

    // --- 🚀 Static Initialization Methods (Actions) ---

    /**
     * Entry point for {@code /bossbar add <id> <name>}.
     *
     * @param id   The unique {@link BossbarId}.
     * @param name The display {@link TextComponent}.
     * @return A new builder instance.
     * @throws NullPointerException if id or name is null.
     */
    public static BossbarCommand add(BossbarId id, TextComponent name) {
        return new BossbarCommand("add").id(id).name(name);
    }

    /**
     * Entry point for {@code /bossbar remove <id>}.
     *
     * @param id The {@link BossbarId} to remove.
     * @return A new builder instance.
     * @throws NullPointerException if id is null.
     */
    public static BossbarCommand remove(BossbarId id) {
        return new BossbarCommand("remove").id(id);
    }

    /**
     * Entry point for {@code /bossbar list}.
     * @return A new builder instance.
     */
    public static BossbarCommand list() {
        return new BossbarCommand("list");
    }

    /**
     * Entry point for {@code /bossbar get <id> <property>}.
     *
     * @param id The {@link BossbarId} to query.
     * @return A new builder instance.
     * @throws NullPointerException if id is null.
     */
    public static BossbarCommand get(BossbarId id) {
        return new BossbarCommand("get").id(id);
    }

    /**
     * Entry point for {@code /bossbar set <id> <property> <value>}.
     *
     * @param id The {@link BossbarId} to modify.
     * @return A new builder instance.
     * @throws NullPointerException if id is null.
     */
    public static BossbarCommand set(BossbarId id) {
        return new BossbarCommand("set").id(id);
    }

    // --- 📦 Macro Logic ---

    /**
     * 📦 **Bossbar Initialization Macro**
     * <p>Generates a sequence of commands to safely reset and fully configure a boss bar.
     * Workflow: Remove existing -> Add new -> Set Max -> Set Style.</p>
     *
     * @param data The {@link BossbarData} model containing all configurations.
     * @return A {@link List} of commands to be executed in sequence.
     */
    public static List<BossbarCommand> initialize(BossbarData data) {
        List<BossbarCommand> commands = new ArrayList<>();
        try {
            Objects.requireNonNull(data, "BossbarData cannot be null for initialization.");
            BossbarId barId = data.getId();

            // 1. Ensure a clean slate
            commands.add(BossbarCommand.remove(barId));

            // 2. Create the bar
            commands.add(BossbarCommand.add(barId, data.getName()));

            // 3. Apply visual settings from the data model
            data.getMax().ifPresent(m -> commands.add(BossbarCommand.set(barId).max(m)));
            data.getStyle().ifPresent(s -> commands.add(BossbarCommand.set(barId).style(s)));

        } catch (Exception e) {
            // Error Catching: Safety mechanism to return an empty sequence on data failure
            return new ArrayList<>();
        }
        return commands;
    }

    // --- ⚙️ Property & Data Methods ---

    /**
     * Internal method to set the bar ID.
     * @param id The {@link BossbarId}. @return current builder.
     */
    public BossbarCommand id(BossbarId id) {
        this.id = Objects.requireNonNull(id, "Bossbar ID is mandatory.");
        return this;
    }

    /**
     * Sets the display name. If the action is 'set', target property becomes 'name'.
     * @param name The {@link TextComponent}. @return current builder.
     */
    public BossbarCommand name(TextComponent name) {
        this.name = Objects.requireNonNull(name, "Name component is mandatory.");
        if ("set".equals(action)) this.property = "name";
        return this;
    }

    /** @param color {@link BossbarColor}. @return current builder. */
    public BossbarCommand color(BossbarColor color) {
        this.property = "color";
        this.color = color;
        return this;
    }

    /** @param max Integer max. @return current builder. */
    public BossbarCommand max(Integer max) {
        this.property = "max";
        this.max = max;
        return this;
    }

    /** @param targets {@link Entity} selector. @return current builder. */
    public BossbarCommand targets(Entity targets) {
        this.property = "players";
        this.targets = targets;
        return this;
    }

    /** @param style {@link BossbarStyle}. @return current builder. */
    public BossbarCommand style(BossbarStyle style) {
        this.property = "style";
        this.style = style;
        return this;
    }

    /** @param value Current progress. @return current builder. */
    public BossbarCommand value(Integer value) {
        this.property = "value";
        this.value = value;
        return this;
    }

    /** @param visible Boolean visibility. @return current builder. */
    public BossbarCommand visible(Boolean visible) {
        this.property = "visible";
        this.visible = visible;
        return this;
    }

    // --- 🚀 Generation Logic ---

    /**
     * Compiles the builder state into a final Minecraft command string.
     * @return The space-separated command string.
     */
    @Override
    public String generate() {
        try {
            StringBuilder sb = new StringBuilder("bossbar ").append(action);

            // 'list' has no arguments
            if ("list".equals(action)) return sb.toString();

            // All other actions require an ID
            if (id == null) throw new IllegalStateException("Missing Bossbar ID for " + action);
            sb.append(" ").append(id);

            switch (action) {
                case "add" -> {
                    if (name == null) throw new IllegalStateException("Add action requires a name.");
                    sb.append(" ").append(name.build());
                }
                case "get" -> {
                    if (property == null) throw new IllegalStateException("Get action requires a property.");
                    sb.append(" ").append(property);
                }
                case "set" -> {
                    if (property == null) throw new IllegalStateException("Set action requires a property.");
                    sb.append(" ").append(property).append(" ").append(resolveValue());
                }
            }

            return sb.toString().trim();
        } catch (Exception e) {
            // Error Catching: Fallback to a safe command to prevent syntax errors
            return "bossbar list";
        }
    }

    /**
     * Resolves complex property values into their Minecraft-compliant string formats.
     * @return The value string.
     */
    private String resolveValue() {
        try {
            if (property == null) return "";
            return switch (property) {
                case "color" -> color != null ? color.toString().toLowerCase() : "white";
                case "max" -> String.valueOf(max);
                case "name" -> name != null ? name.build() : "";
                case "players" -> targets != null ? targets.toString() : "";
                case "style" -> style != null ? style.toString().toLowerCase() : "progress";
                case "value" -> String.valueOf(value);
                case "visible" -> String.valueOf(visible);
                default -> "";
            };
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return generate();
    }
}