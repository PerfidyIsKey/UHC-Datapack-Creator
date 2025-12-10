package uhc.data.nbt.blockentity;

public enum BlockEntityType {
    CHEST("minecraft:chest"),
    JUKEBOX("minecraft:jukebox"),
    SIGN("minecraft:sign"),
    STRUCTURE_BLOCK("minecraft:structure_block");

    private final String nbtId;

    BlockEntityType(String nbtId) {
        this.nbtId = nbtId;
    }

    public String getNbtId() {
        return nbtId;
    }

    // Utility method to look up the type from the NBT string id
    public static BlockEntityType fromNbtId(String id) {
        for (BlockEntityType type : values()) {
            if (type.nbtId.equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown Block Entity ID: " + id);
    }
}