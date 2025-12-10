package uhc.data.nbt.entity.data;

import uhc.data.resource.BooleanNbtProperty;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) holding the configuration for the entire Firework Rocket Entity NBT.
 */
public class FireworkRocketData {
    private final byte flightDuration;
    private final List<FireworkStarData> explosions;
    // Map for storing general boolean properties like GLOWING, SILENT, etc.
    private final Map<BooleanNbtProperty, Boolean> booleanProperties;

    public FireworkRocketData(
            byte flightDuration,
            List<FireworkStarData> explosions,
            Map<BooleanNbtProperty, Boolean> booleanProperties) {

        this.flightDuration = flightDuration;
        this.explosions = explosions;
        this.booleanProperties = booleanProperties;
    }

    public byte getFlightDuration() { return flightDuration; }
    public List<FireworkStarData> getExplosions() { return explosions; }
    public Map<BooleanNbtProperty, Boolean> getBooleanProperties() { return booleanProperties; }
}