package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.bossbar.BossbarStyle;
import uhc.text.BossbarColor;
import uhc.text.TextComponent;

/**
 * 🚧 **Bossbar Command Builder**
 * <p>
 * Implements the fluent API for constructing the Minecraft {@code /bossbar} command,
 * covering {@code add}, {@code get}, {@code list}, {@code remove}, and {@code set} sub-commands.
 * </p>
 */
public class BossbarCommand implements MinecraftCommand {
    private String id;
    private String name; // Changed to TextComponent to support rich text in the command
    private BossbarAction action;
    private BossbarGetProperty getProperty;
    private BossbarSetProperty setProperty;

    // Set properties
    private BossbarColor color;
    private Integer max;
    private Entity targets;
    private BossbarStyle style;
    private Integer value;
    private Boolean visible;

    // --- Constructor and Factory ---

    private BossbarCommand() {}

    public static BossbarCommand create() {
        return new BossbarCommand();
    }

    // --- Primary Action Setters ---

    public BossbarCommand add() {
        this.action = BossbarAction.ADD;
        return this;
    }

    public BossbarCommand remove() {
        this.action = BossbarAction.REMOVE;
        return this;
    }

    public BossbarCommand list() {
        this.action = BossbarAction.LIST;
        return this;
    }

    public BossbarCommand get() {
        this.action = BossbarAction.GET;
        return this;
    }

    public BossbarCommand set() {
        this.action = BossbarAction.SET;
        return this;
    }


    // --- Core Property Setters ---

    public BossbarCommand id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the display name for the bossbar. Automatically wraps a String into a simple TextComponent.
     */
    public BossbarCommand name(String name) {
        // Assume TextComponent.simple(String) is available to wrap the string
        this.name = TextComponent.simple(name);
        return this;
    }


    // --- Action/Property Type Setters ---

    /**
     * Sets the specific property to retrieve (used with {@code GET}).
     */
    public BossbarCommand action(BossbarGetProperty action) {
        this.getProperty = action;
        return this;
    }

    /**
     * Sets the specific property to modify (used with {@code SET}).
     */
    public BossbarCommand setProperty(BossbarSetProperty property) {
        this.setProperty = property;
        return this;
    }

    // --- SET Property Value Setters (Fluent continuation) ---

    public BossbarCommand color(BossbarColor color) {
        this.setProperty = BossbarSetProperty.COLOR; // Infer property from setter
        this.color = color;
        return this;
    }

    public BossbarCommand max(Integer max) {
        this.setProperty = BossbarSetProperty.MAX; // Infer property from setter
        this.max = max;
        return this;
    }

    public BossbarCommand targets(Entity targets) {
        this.setProperty = BossbarSetProperty.PLAYERS; // Infer property from setter
        this.targets = targets;
        return this;
    }

    public BossbarCommand style(BossbarStyle style) {
        this.setProperty = BossbarSetProperty.STYLE; // Infer property from setter
        this.style = style;
        return this;
    }

    public BossbarCommand value(Integer value) {
        this.setProperty = BossbarSetProperty.VALUE; // Infer property from setter
        this.value = value;
        return this;
    }

    public BossbarCommand visible(Boolean visible) {
        this.setProperty = BossbarSetProperty.VISIBLE; // Infer property from setter
        this.visible = visible;
        return this;
    }

    // --- Command Generation ---

    @Override
    public String generate() {
        if (action == null) {
            throw new IllegalStateException("Bossbar command must define a sub-command (add, remove, get, list, or set).");
        }

        StringBuilder sb = new StringBuilder("bossbar ").append(action).append(" ");

        if (action == BossbarAction.LIST) {
            // "bossbar list"
            return sb.toString().trim();
        }

        if (id == null && action != BossbarAction.LIST) {
            throw new IllegalStateException("Bossbar command ID is required for sub-command: " + action.name());
        }

        sb.append(id);

        switch (action) {
            case ADD:
                // "bossbar add <id> <name>"
                if (name == null) {
                    throw new IllegalStateException("Name is required for 'BOSSBAR ADD'.");
                }
                // Name is a TextComponent, its toString() is expected to return the quoted JSON
                sb.append(" ").append(name);
                break;

            case REMOVE:
                // "bossbar remove <id>"
                // ID already appended
                break;

            case GET:
                // "bossbar get <id> (max|players|value|visible)"
                if (getProperty == null) {
                    throw new IllegalStateException("Action (max|players|value|visible) is required for 'BOSSBAR GET'.");
                }
                sb.append(" ").append(getProperty.toString());
                break;

            case SET:
                // "bossbar set <id> <property> <value>"
                if (setProperty == null) {
                    throw new IllegalStateException("Property (color|max|name|players|style|value|visible) is required for 'BOSSBAR SET'.");
                }

                sb.append(" ").append(setProperty.toString());

                switch (setProperty) {
                    case COLOR:
                        if (color == null) throw new IllegalStateException("Color is required for SET color.");
                        sb.append(" ").append(color);
                        break;
                    case MAX:
                        if (max == null) throw new IllegalStateException("Max value is required for SET max.");
                        sb.append(" ").append(max);
                        break;
                    case NAME:
                        if (name == null) throw new IllegalStateException("Name is required for SET name.");
                        // Name is a TextComponent, its toString() is expected to return the quoted JSON
                        sb.append(" ").append(name);
                        break;
                    case PLAYERS:
                        // "bossbar set <id> players [<targets>]" (targets is optional)
                        if (targets != null) {
                            sb.append(" ").append(targets);
                        }
                        break;
                    case STYLE:
                        if (style == null) throw new IllegalStateException("Style is required for SET style.");
                        sb.append(" ").append(style);
                        break;
                    case VALUE:
                        if (value == null) throw new IllegalStateException("Value is required for SET value.");
                        sb.append(" ").append(value);
                        break;
                    case VISIBLE:
                        if (visible == null) throw new IllegalStateException("Visible state is required for SET visible.");
                        sb.append(" ").append(visible);
                        break;
                }
                break;
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }

    // --- Enums ---

    /** Defines the main sub-command actions for /bossbar. */
    public enum BossbarAction {
        ADD,
        GET,
        LIST,
        REMOVE,
        SET;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /** Defines the specific property to GET from a bossbar. */
    public enum BossbarGetProperty {
        MAX,
        PLAYERS,
        VALUE,
        VISIBLE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /** Defines the property being modified by the SET sub-command. */
    public enum BossbarSetProperty {
        COLOR,
        MAX,
        NAME,
        PLAYERS,
        STYLE,
        VALUE,
        VISIBLE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}