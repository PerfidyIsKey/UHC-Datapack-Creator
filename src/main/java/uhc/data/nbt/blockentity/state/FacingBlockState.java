package uhc.data.nbt.blockentity.state;

/**
 * Represents the 'facing' block state property.
 */
public enum FacingBlockState implements BlockState {
    NORTH, SOUTH, EAST, WEST, UP, DOWN;

    @Override
    public String getKey() {
        return "facing";
    }

    @Override
    public String getValue() {
        return name().toLowerCase();
    }
}