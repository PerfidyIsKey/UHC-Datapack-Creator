import Enums.EntityType;
import Enums.WorldShape;

public class Constant {
    // Unit conversion
    public static final int secPerMinute = 60;

    // Admin entity
    public static final String admin = "@n[type=" + EntityType.MARKER +"]";
    public static final String adminSingle = "@e[type=" + EntityType.MARKER + ",limit=1]";

    // Tick speed
    public static final int tickFrequencyShort = 20;
    public static final int tickFrequencyMed = 4;
    public static final int tickFrequencyLong = 1;

    // World
    public static final int worldHeight = 257;
    public static final int worldBottom = -64;
    public static final WorldShape worldShape = WorldShape.square;
}
