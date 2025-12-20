package uhc.data.nbt.tags;

import uhc.data.nbt.util.TagFactory;
import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the TAG_Compound NBT structure (Type ID: 10).
 * This tag acts as a container for named NBT tags, similar to a JSON object or a Map,
 * where the key is the child tag's name.
 */
public class CompoundTag implements NBTTag {

    private String name;

    /** Stores the actual named NBT tags contained within this compound. */
    private final Map<String, NBTTag> value = new LinkedHashMap<>();

    // --- Private Constructors (Used by static factories and deserialization) ---

    /**
     * Private constructor for creating an unnamed compound tag (used for deserialization or root tags).
     */
    protected CompoundTag() {
        this.name = "";
    }

    /**
     * Private constructor for creating a named compound tag.
     * @param name The name of this compound tag.
     */
    protected CompoundTag(String name) {
        // Ensures the name is non-null, defaulting to an empty string if null is passed.
        this.name = Objects.requireNonNullElse(name, "");
    }

    // --- Public Factory Methods (Preferred way to create instances) ---

    /**
     * Creates a new unnamed CompoundTag.
     * @return A new CompoundTag instance.
     */
    public static CompoundTag create() {
        return new CompoundTag();
    }

    /**
     * Creates a new named CompoundTag.
     * @param name The name to assign to the new compound tag.
     * @return A new CompoundTag instance.
     */
    public static CompoundTag create(String name) {
        return new CompoundTag(name);
    }

    // --- NBTTag Interface Implementation ---

    @Override
    public byte getTypeId() {
        return TagType.TAG_COMPOUND;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = Objects.requireNonNullElse(name, "");
    }

    /**
     * Gets the map containing the key-value pairs of the tags inside this compound.
     * @return The internal map of tags.
     */
    public Map<String, NBTTag> getValue() {
        return value;
    }

    /**
     * Adds an NBT tag to the compound. The tag's {@code getName()} method is used as the key.
     * * @param t The NBTTag to add. It must have a non-null, non-empty name.
     * @return The CompoundTag instance for method chaining (fluent interface).
     * @throws IllegalArgumentException if the tag does not have a name or the name is empty.
     */
    public CompoundTag put(NBTTag t) {
        // Enforce that tags inside a compound must be named
        if (t.getName() == null || t.getName().isEmpty()) {
            throw new IllegalArgumentException("Compound entries must have a non-empty name.");
        }
        value.put(t.getName(), t);
        return this;
    }

    /**
     * Retrieves an NBT tag by its assigned name within this compound.
     * @param name The name (key) of the tag to retrieve.
     * @return The NBTTag associated with the name, or {@code null} if not found.
     */
    public NBTTag get(String name) {
        return value.get(name);
    }

    /**
     * Removes an NBT tag from the compound by its name.
     * @param name The name of the tag to remove.
     * @return The CompoundTag instance for method chaining.
     */
    public CompoundTag remove(String name) {
        value.remove(name);
        return this;
    }

    /**
     * Checks if a tag with the specified name exists in this compound.
     * @param name The name to look for.
     * @return true if the tag exists, false otherwise.
     */
    public boolean contains(String name) {
        return value.containsKey(name);
    }

    // --- Serialization and Deserialization (Standard NBT Format) ---

    /**
     * Writes the payload (the map of inner tags followed by TAG_END) to the data output.
     */
    @Override
    public void writePayload(DataOutput out) throws IOException {
        for (NBTTag t : value.values()) {
            // Write tag type
            out.writeByte(t.getTypeId());

            // TAG_END marks the end of the compound; it has no name or payload.
            if (t.getTypeId() == TagType.TAG_END) continue;

            // Write name (UTF) and then the tag's payload recursively
            out.writeUTF(t.getName());
            t.writePayload(out);
        }
        // Write the final TAG_END marker (0 byte)
        out.writeByte(TagType.TAG_END);
    }

    /**
     * Reads the payload (a sequence of tags until TAG_END is encountered) from the data input.
     */
    @Override
    public void readPayload(DataInput in, int depth) throws IOException {
        while (true) {
            byte type = in.readByte();
            if (type == TagType.TAG_END) return; // Stop reading at the end marker

            String name = in.readUTF();
            NBTTag t = TagFactory.create(type);
            t.setName(name);
            t.readPayload(in, depth + 1); // Recurse for the tag's payload
            value.put(name, t);
        }
    }
}