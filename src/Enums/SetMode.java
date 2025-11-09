package Enums;

public enum SetMode {
    DESTROY("destroy"),
    KEEP("keep"),
    REPLACE("replace"),
    STRICT("strict");

    private final String name;

    SetMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
