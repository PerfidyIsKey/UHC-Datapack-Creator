package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.bossbar.BossbarId;
import uhc.resource.bossbar.BossbarStyle;
import uhc.resource.color.BossbarColor;
import uhc.text.TextComponent;

/**
 * 🚧 **Bossbar Command Builder**
 */
public class BossbarCommand implements MinecraftCommand {
    private BossbarId id;
    private TextComponent name;
    private BossbarAction action;
    private BossbarGetProperty getProperty;
    private BossbarSetProperty setProperty;

    private BossbarColor color;
    private Integer max;
    private Entity targets;
    private BossbarStyle style;
    private Integer value;
    private Boolean visible;

    private BossbarCommand() {}

    public static BossbarCommand create() {
        return new BossbarCommand();
    }

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

    public BossbarCommand id(BossbarId id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the display name using a raw String.
     */
    public BossbarCommand name(String name) {
        this.name = TextComponent.text(name);
        return this;
    }

    /**
     * Sets the display name using a rich TextComponent.
     */
    public BossbarCommand name(TextComponent name) {
        this.name = name;
        return this;
    }

    public BossbarCommand action(BossbarGetProperty action) {
        this.getProperty = action;
        return this;
    }

    public BossbarCommand setProperty(BossbarSetProperty property) {
        this.setProperty = property;
        return this;
    }

    public BossbarCommand color(BossbarColor color) {
        this.setProperty = BossbarSetProperty.COLOR;
        this.color = color;
        return this;
    }

    public BossbarCommand max(Integer max) {
        this.setProperty = BossbarSetProperty.MAX;
        this.max = max;
        return this;
    }

    public BossbarCommand targets(Entity targets) {
        this.setProperty = BossbarSetProperty.PLAYERS;
        this.targets = targets;
        return this;
    }

    public BossbarCommand style(BossbarStyle style) {
        this.setProperty = BossbarSetProperty.STYLE;
        this.style = style;
        return this;
    }

    public BossbarCommand value(Integer value) {
        this.setProperty = BossbarSetProperty.VALUE;
        this.value = value;
        return this;
    }

    public BossbarCommand visible(Boolean visible) {
        this.setProperty = BossbarSetProperty.VISIBLE;
        this.visible = visible;
        return this;
    }

    @Override
    public String generate() {
        if (action == null) {
            throw new IllegalStateException("Bossbar command must define a sub-command.");
        }

        StringBuilder sb = new StringBuilder("bossbar ").append(action).append(" ");

        if (action == BossbarAction.LIST) {
            return sb.toString().trim();
        }

        if (id == null) {
            throw new IllegalStateException("Bossbar command ID is required.");
        }

        sb.append(id);

        switch (action) {
            case ADD:
                if (name == null) throw new IllegalStateException("Name is required for ADD.");
                sb.append(" ").append(name.build());
                break;

            case GET:
                if (getProperty == null) throw new IllegalStateException("Property required for GET.");
                sb.append(" ").append(getProperty);
                break;

            case SET:
                if (setProperty == null) throw new IllegalStateException("Property required for SET.");
                sb.append(" ").append(setProperty);

                switch (setProperty) {
                    case COLOR:
                        sb.append(" ").append(color);
                        break;
                    case MAX:
                        sb.append(" ").append(max);
                        break;
                    case NAME:
                        if (name == null) throw new IllegalStateException("Name required for SET name.");
                        sb.append(" ").append(name.build());
                        break;
                    case PLAYERS:
                        if (targets != null) sb.append(" ").append(targets);
                        break;
                    case STYLE:
                        sb.append(" ").append(style);
                        break;
                    case VALUE:
                        sb.append(" ").append(value);
                        break;
                    case VISIBLE:
                        sb.append(" ").append(visible);
                        break;
                }
                break;
            default:
                break;
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }

    public enum BossbarAction {
        ADD, GET, LIST, REMOVE, SET;
        @Override public String toString() { return name().toLowerCase(); }
    }

    public enum BossbarGetProperty {
        MAX, PLAYERS, VALUE, VISIBLE;
        @Override public String toString() { return name().toLowerCase(); }
    }

    public enum BossbarSetProperty {
        COLOR, MAX, NAME, PLAYERS, STYLE, VALUE, VISIBLE;
        @Override public String toString() { return name().toLowerCase(); }
    }
}