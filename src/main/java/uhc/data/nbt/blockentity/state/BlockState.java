package uhc.data.nbt.blockentity.state;

/**
 * 🧱 **Block State Property Interface**
 * <p>
 * A pure descriptor for block properties (facing, lit, layers, etc.).
 * These are strictly used for block configuration and are not NBT tags themselves.
 * </p>
 */
public interface BlockState {
    /** * @return The property key defined by Minecraft (e.g., "facing", "waterlogged").
     */
    String getKey();

    /** * @return The property value as a string (e.g., "north", "true", "4").
     */
    String getValue();
}