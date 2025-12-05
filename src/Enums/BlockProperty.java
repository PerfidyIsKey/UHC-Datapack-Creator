package Enums;

import shared.StructureBlockMode;

public enum BlockProperty {
    FACING("facing", Direction.class),
    HAS_RECORD("has_record", Boolean.class),
    MODE("mode", StructureBlockMode.class),
    WATERLOGGED("waterlogged", Boolean.class);

    private final String name;
    private final Class<?> type;

    BlockProperty(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
