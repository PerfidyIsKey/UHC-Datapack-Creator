package uhc.data.nbt.blockentity.traits;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.StringTag;
import uhc.text.TextComponent;
import java.util.Objects;

/**
 * 🏷️ **Renamable NBT Trait**
 * <p>
 * This trait provides a standardized way to apply a custom name to Block Entities
 * that support it. In Minecraft, this changes the name displayed in the block's
 * GUI (e.g., the title at the top of a Chest, Furnace, or Hopper).
 * </p>
 * <p>
 * <b>Version Compatibility:</b> Since 1.20.5, block entity names are stored
 * as JSON text components within a String tag.
 * </p>
 * * @param <T> The type of the builder implementing this trait, used for fluent chaining.
 */
public interface RenamableNBT<T extends RenamableNBT<T>> {

    /**
     * Provides access to the builder's internal root {@link CompoundTag}.
     * <p>
     * Implementations of this trait (like {@code ChestBlockEntityNBT}) must
     * return their underlying NBT storage here.
     * </p>
     * @return The internal NBT root.
     */
    CompoundTag root();

    /**
     * Sets the {@code CustomName} of the block entity.
     * <p>
     * <b>NBT Key:</b> {@code "CustomName"} <br>
     * <b>NBT Type:</b> {@link StringTag} (JSON formatted string)
     * </p>
     * * @param component The {@link TextComponent} representing the name.
     * Can include colors, bold, and italics.
     * @return {@code (T)} The current builder instance to allow for continued chaining.
     * @throws NullPointerException if the provided component is null.
     */
    @SuppressWarnings("unchecked")
    default T customName(TextComponent component) {
        Objects.requireNonNull(component, "CustomName cannot be null. Use a blank TextComponent if you wish to clear it.");

        // Minecraft requires the CustomName to be a serialized JSON string.
        // Your TextComponent.toString() is expected to produce valid JSON.
        root().put(new StringTag("CustomName", component.toString()));

        return (T) this;
    }

    /**
     * Removes the custom name from the block entity, resetting it to its default name.
     * * @return {@code (T)} The current builder instance.
     */
    @SuppressWarnings("unchecked")
    default T removeCustomName() {
        root().remove("CustomName");
        return (T) this;
    }
}