import Enums.WorldShape;

public class Constant {
    // Admin entity
    public static final String admin = "@e[type=marker]";
    public static final String adminSingle = "@e[type=marker,limit=1]";

    // Tick speed
    public static final int tickFrequencyShort = 20;
    public static final int tickFrequencyMed = 4;
    public static final int tickFrequencyLong = 1;

    // World
    public static final int worldHeight = 257;
    public static final int worldBottom = -64;
    public static final WorldShape worldShape = WorldShape.square;
}
