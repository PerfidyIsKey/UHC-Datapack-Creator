package uhc.data.nbt.blockentity.state;

/**
 * 💿 **Has Record Block State**
 * <p>
 * Represents the {@code has_record} property for Jukeboxes.
 * Uses a Boolean wrapper to determine if the Jukebox contains a disc.
 * </p>
 */
public class HasRecordState implements BlockState {

    private final boolean hasRecord;

    /**
     * Primary constructor.
     * @param hasRecord true if a record is present, false otherwise.
     */
    private HasRecordState(boolean hasRecord) {
        this.hasRecord = hasRecord;
    }

    /**
     * Static factory method for cleaner fluent API usage.
     */
    public static HasRecordState of(boolean hasRecord) {
        return new HasRecordState(hasRecord);
    }

    /**
     * @return The Minecraft property key "has_record".
     */
    @Override
    public String getKey() {
        return "has_record";
    }

    /**
     * @return The boolean value as a string ("true" or "false").
     */
    @Override
    public String getValue() {
        return Boolean.toString(hasRecord);
    }

    /**
     * @return The raw boolean value.
     */
    public boolean asBoolean() {
        return hasRecord;
    }
}