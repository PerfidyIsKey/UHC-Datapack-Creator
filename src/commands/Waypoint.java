package commands;

import arguments.Entity;
import shared.ColorType;
import shared.HexColor;
import shared.TextColor;

/**
 * Represents a command to modify the color of a waypoint associated with a specific entity.
 * This class uses the Builder pattern to construct the final command string.
 * <p>
 * Final command format: {@code waypoint modify <entity_selector> color <color_name | hex #RRGGBB | reset>}
 */
public class Waypoint {
    private final Entity waypointEntity;
    private ColorType color;

    /**
     * Private constructor to enforce starting the chain with the static factory method.
     * @param waypointEntity The entity whose waypoint color is being modified.
     * @throws IllegalArgumentException if the {@code waypointEntity} is null.
     */
    private Waypoint(Entity waypointEntity) {
        if (waypointEntity == null) {
            throw new IllegalArgumentException("Waypoint must be associated with a non-null Entity.");
        }
        this.waypointEntity = waypointEntity;
    }

    /**
     * Static factory method to begin defining the waypoint color modification command.
     * @param waypointEntity The target entity. Cannot be null.
     * @return A new Waypoint command builder instance.
     */
    public static Waypoint create(Entity waypointEntity) {
        return new Waypoint(waypointEntity);
    }

    /**
     * Sets the waypoint color using a hexadecimal color code. This will result in the "hex #RRGGBB" format in the command.
     * @param hex The HexColor instance containing the hex string (e.g., #RRGGBB). Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the hex color is null.
     */
    public Waypoint color(HexColor hex) {
        if (hex == null) {
            throw new IllegalArgumentException("HexColor cannot be null.");
        }
        this.color = hex;
        return this;
    }

    /**
     * Sets the waypoint color using a standard Minecraft text color name. This will result in the "color_name" format in the command.
     * @param color The TextColor enum constant (e.g., TextColor.RED). Cannot be null.
     * @return This builder for chaining.
     * @throws IllegalArgumentException if the TextColor is null.
     */
    public Waypoint color(TextColor color) {
        if (color == null) {
            throw new IllegalArgumentException("TextColor cannot be null.");
        }
        this.color = color;
        return this;
    }

    /**
     * Finalizes the command and builds the complete string.
     * @return The complete waypoint modify command string.
     * @throws IllegalStateException if the stored ColorType is not a known implementation (HexColor or TextColor).
     */
    public String build() {
        // Start with the base command and the entity selector
        StringBuilder sb = new StringBuilder("waypoint modify ");
        sb.append(waypointEntity.toString()).append(" ").append("color").append(" ");

        if (color != null) {
            // Check if the stored ColorType requires the "hex" prefix
            if (color instanceof HexColor) {
                // HexColor requires the prefix: "hex #RRGGBB"
                sb.append("hex").append(" ").append(color);
            }
            // Check if the stored ColorType is a standard name
            else if (color instanceof TextColor) {
                // TextColor uses the name directly: "red"
                sb.append(color);
            } else {
                // Fail-safe check for any future unsupported ColorType implementations
                throw new IllegalStateException("Unsupported ColorType implementation: " + color.getClass().getName());
            }
        } else {
            // If the user didn't call a color() method, default to reset
            sb.append("reset");
        }

        return sb.toString();
    }
}