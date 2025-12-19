package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.text.ColorType;
import uhc.text.HexColor;
import uhc.text.TextColor;

import java.util.Objects;

/**
 * 📍 **Waypoint Command Builder**
 * <p>
 * Provides a fluent API for the {@code /waypoint} command, specifically for
 * modifying the visual appearance of waypoint entities.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /waypoint modify <entity> color <color_name | hex #RRGGBB | reset>}
 * </p>
 */
public class WaypointCommand implements MinecraftCommand {
    private final Entity waypointEntity;
    private ColorType color;

    private WaypointCommand(Entity waypointEntity) {
        // Enforce non-nullability for the target entity
        this.waypointEntity = Objects.requireNonNull(waypointEntity, "Waypoint must be associated with a non-null Entity.");
    }

    /**
     * Initializes a new Waypoint command builder.
     * @param waypointEntity The entity (usually a marker or armor stand) representing the waypoint.
     * @return A new WaypointCommand instance.
     */
    public static WaypointCommand create(Entity waypointEntity) {
        return new WaypointCommand(waypointEntity);
    }

    /**
     * Sets the waypoint color using a custom hexadecimal code.
     * <p>The generated command will include the {@code hex} prefix automatically.</p>
     * @param hex The HexColor instance (e.g., #FF5555).
     * @return The current builder instance.
     */
    public WaypointCommand color(HexColor hex) {
        this.color = Objects.requireNonNull(hex, "HexColor cannot be null.");
        return this;
    }

    /**
     * Sets the waypoint color using a standard Minecraft color name.
     * @param color The TextColor constant (e.g., RED, GOLD, AQUA).
     * @return The current builder instance.
     */
    public WaypointCommand color(TextColor color) {
        this.color = Objects.requireNonNull(color, "TextColor cannot be null.");
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * <p>If no color is specified, the command defaults to {@code reset}.</p>
     * @return The formatted command (e.g., "waypoint modify @e[...] color hex #00FF00").
     * @throws IllegalStateException if an unknown ColorType implementation is encountered.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("waypoint modify ");

        // Base structure: waypoint modify <entity> color
        sb.append(waypointEntity).append(" color ");

        if (color == null) {
            // Default behavior if color() was never called
            sb.append("reset");
        } else if (color instanceof HexColor) {
            // Hex format requires the 'hex' keyword before the value
            sb.append("hex ").append(color);
        } else if (color instanceof TextColor) {
            // Named colors (red, blue, etc.) are appended directly
            sb.append(color);
        } else {
            // Error Catching: Protects against unexpected implementations of ColorType
            throw new IllegalStateException("Unsupported ColorType implementation: " + color.getClass().getName());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}