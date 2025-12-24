package uhc.data.nbt.blockentity.state;

/**
 * 🌊 **Waterlogged Block State**
 * <p>
 * Represents the {@code waterlogged} property for blocks that can be submerged.
 * When true, the block behaves as a water source while maintaining its
 * original block geometry.
 * </p>
 */
public class WaterloggedState implements BlockState {

    private final boolean waterlogged;

    /**
     * Primary constructor.
     * @param waterlogged true if the block contains water, false otherwise.
     */
    public WaterloggedState(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }

    /**
     * Static factory method for fluent API usage.
     * @param waterlogged the state value.
     * @return A new WaterloggedState instance.
     */
    public static WaterloggedState of(boolean waterlogged) {
        return new WaterloggedState(waterlogged);
    }

    /**
     * @return The Minecraft property key "waterlogged".
     */
    @Override
    public String getKey() {
        return "waterlogged";
    }

    /**
     * @return The boolean value as a string ("true" or "false").
     */
    @Override
    public String getValue() {
        return Boolean.toString(waterlogged);
    }

    /**
     * @return The raw boolean value.
     */
    public boolean asBoolean() {
        return waterlogged;
    }
}