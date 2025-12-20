package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;
import uhc.resource.item.components.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 👤 **Profile Component Implementation**
 * <p>
 * Manages the "minecraft:profile" data component (1.20.5+).
 * This component is used for player heads to define the skin, cape, and
 * player model (Slim/Classic).
 * </p>
 */
public class ProfileComponent implements ItemComponent {

    private String name;
    private UUID id;
    private String texturePath;
    private String capePath;
    private PlayerModel model;
    private final List<Property> properties = new ArrayList<>();

    private ProfileComponent(String name) {
        this.name = name;
    }

    private ProfileComponent(UUID id) {
        this.id = id;
    }

    /**
     * Creates a profile based on a simple player name.
     * @param name The Minecraft username.
     */
    public static ProfileComponent create(String name) {
        return new ProfileComponent(name);
    }

    /**
     * Creates a profile based on a Player UUID.
     * @param id The player's unique ID.
     */
    public static ProfileComponent create(UUID id) {
        return new ProfileComponent(id);
    }

    // --- 🛠️ Fluent Setters ---

    public ProfileComponent name(String name) {
        this.name = name;
        return this;
    }

    public ProfileComponent id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Note: In vanilla, custom textures are usually handled via {@link #addProperty(String, String, String)}.
     */
    public ProfileComponent texture(String path) {
        this.texturePath = path;
        return this;
    }

    public ProfileComponent cape(String path) {
        this.capePath = path;
        return this;
    }

    /**
     * Defines the model type (e.g., SLIM for Alex-style skins).
     */
    public ProfileComponent model(PlayerModel modelType) {
        this.model = modelType;
        return this;
    }

    /**
     * Adds a skin property.
     * @param name The property name (usually "textures").
     * @param value The Base64 encoded JSON texture data.
     * @param signature The optional Mojang cryptographic signature.
     */
    public ProfileComponent addProperty(String name, String value, String signature) {
        this.properties.add(new Property(name, value, signature));
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.PROFILE;
    }

    /**
     * Converts the profile into the required NBT structure.
     * <p>
     * <b>Format 1 (Simple):</b> StringTag containing just the name.
     * <b>Format 2 (Complex):</b> CompoundTag containing name, id (IntArray), and properties.
     * </p>
     */
    @Override
    public NBTTag toNbt() {
        String resourceLocation = getId().getResourceLocation();

        // 1. Simple Mode: profile: "Username"
        if (name != null && id == null && texturePath == null && properties.isEmpty() && model == null && capePath == null) {
            return new StringTag(resourceLocation, name);
        }

        // 2. Detailed Mode: CompoundTag
        CompoundTag root = CompoundTag.create(resourceLocation);

        if (name != null) root.put(new StringTag("name", name));

        // In modern NBT, UUIDs are stored as an IntArray of 4 ints.
        if (id != null) root.put(new IntArrayTag("id", uuidToIntArray(id)));

        if (texturePath != null) root.put(new StringTag("texture", texturePath));
        if (capePath != null) root.put(new StringTag("cape", capePath));

        if (model != null) {
            root.put(new StringTag("model", model.getNbtValue()));
        }

        if (!properties.isEmpty()) {
            ListTag propsList = new ListTag("properties");
            for (Property prop : properties) {
                CompoundTag propTag = CompoundTag.create("");
                propTag.put(new StringTag("name", prop.name));
                propTag.put(new StringTag("value", prop.value));
                if (prop.signature != null) {
                    propTag.put(new StringTag("signature", prop.signature));
                }
                propsList.add(propTag);
            }
            root.put(propsList);
        }

        return root;
    }

    /**
     * Splits a UUID into the 4-int array format required by Minecraft.
     */
    private int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return new int[] {
                (int) (most >> 32), (int) most,
                (int) (least >> 32), (int) least
        };
    }

    private static class Property {
        String name, value, signature;
        Property(String n, String v, String s) {
            this.name = Objects.requireNonNull(n);
            this.value = Objects.requireNonNull(v);
            this.signature = s;
        }
    }
}