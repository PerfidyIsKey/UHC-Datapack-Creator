import Enums.WorldShape;
import arguments.ColumnPos;
import arguments.Entity;
import arguments.coordinate.Vec2;
import arguments.targetselector.SelectorArgumentsBuilder;
import arguments.targetselector.TargetSelector;
import shared.EntityType;

public class Constant {
    // Unit conversion
    public static final int secPerMinute = 60;

    // Admin entity
    public static final String adminOld = "@n[type=" + EntityType.MARKER +"]";
    public static final Entity admin = Entity.ofSelector(
            TargetSelector.NEAREST_ENTITY,
            SelectorArgumentsBuilder.create()
                    .type(EntityType.MARKER)
    );

    // Tick speed
    public static final int tickFrequencyShort = 20;
    public static final int tickFrequencyMed = 4;
    public static final int tickFrequencyLong = 1;

    // World
    public static final int worldHeight = 257;
    public static final int worldBottom = -64;
    public static final WorldShape worldShape = WorldShape.square;
    public static final Vec2 spawnCenterDouble = Vec2.absolute(0, 0);
    public static final ColumnPos spawnCenterInt = ColumnPos.absolute(0, 0);
}
