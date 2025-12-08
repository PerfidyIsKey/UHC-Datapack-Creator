package nbt.entity.data;

import shared.BooleanNbtProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent builder for creating FireworkRocketData DTOs via method chaining.
 * Example: FireworkRocketDataBuilder.create().setFlightDuration(3).addStar(...).build();
 */
public class FireworkRocketDataBuilder {
    private byte flightDuration = (byte)1;
    private final List<FireworkStarData> explosions = new ArrayList<>();
    private final Map<BooleanNbtProperty, Boolean> booleanProperties = new HashMap<>();

    private FireworkRocketDataBuilder() {}

    public static FireworkRocketDataBuilder create() {
        return new FireworkRocketDataBuilder();
    }

    /** Sets the rocket's flight duration (1-3 is standard, 1 is default). */
    public FireworkRocketDataBuilder setFlightDuration(int duration) {
        if (duration < 1 || duration > 3) {
            System.err.println("Warning: Flight duration outside typical range (1-3) is not recommended.");
        }
        this.flightDuration = (byte)duration;
        return this;
    }

    /** Adds a configured Firework Star DTO to the rocket's payload. */
    public FireworkRocketDataBuilder addStar(FireworkStarData starData) {
        this.explosions.add(starData);
        return this;
    }

    /** Adds a common boolean property (like GLOWING or SILENT) to the entity NBT. */
    public FireworkRocketDataBuilder setProperty(BooleanNbtProperty property, boolean value) {
        this.booleanProperties.put(property, value);
        return this;
    }

    /** Convenience method for setting boolean properties to true. */
    public FireworkRocketDataBuilder setProperty(BooleanNbtProperty property) {
        return setProperty(property, true);
    }

    public FireworkRocketData buildData() {
        return new FireworkRocketData(flightDuration, explosions, booleanProperties);
    }
}