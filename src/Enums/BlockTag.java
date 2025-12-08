package Enums;

public enum BlockTag {
    RECORD_ITEM("RecordItem");

    private final String name;

    BlockTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

