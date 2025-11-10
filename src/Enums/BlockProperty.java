package Enums;

public enum BlockProperty {
    HAS_RECORD("has_record", Boolean.class);

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
