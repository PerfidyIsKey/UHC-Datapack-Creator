package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;
import uhc.resource.item.components.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 👤 **Profile Component Implementation**
 * <p>
 * Manages the "minecraft:profile" component for player heads and skins.
 * Handles automatic switching between simple string profiles and complex
 * property-based profiles with UUIDs and Base64 textures.
 * </p>
 */
public class ProfileComponent implements ItemComponent {

    private String name;
    private UUID id;
    private String texturePath;
    private String capePath;
    private PlayerModel model;
    private final List<Property> properties = new ArrayList<>();

    public ProfileComponent(String name) {
        this.name = name;
    }

    public ProfileComponent(UUID id) {
        this.id = id;
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

    public ProfileComponent texture(String path) {
        this.texturePath = path;
        return this;
    }

    public ProfileComponent cape(String path) {
        this.capePath = path;
        return this;
    }

    public ProfileComponent model(PlayerModel modelType) {
        this.model = modelType;
        return this;
    }

    /**
     * Adds a property (like 'textures') to the profile.
     * @param name The property name (e.g., "textures").
     * @param value The Base64 encoded texture data.
     * @param signature The optional Mojang signature.
     */
    public ProfileComponent addProperty(String name, String value, String signature) {
        this.properties.add(new Property(name, value, signature));
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.PROFILE;
    }

    @Override
    public NBTTag toNbt() {
        String resourceLocation = getId().getResourceLocation();

        // 1. Simple Mode: If only name is set, return a StringTag.
        // This results in: profile: "Username"
        if (name != null && id == null && texturePath == null && properties.isEmpty() && model == null && capePath == null) {
            return new StringTag(resourceLocation, name);
        }

        // 2. Detailed Mode: Return a CompoundTag.
        CompoundTag root = CompoundTag.create(resourceLocation);

        if (name != null) root.put(new StringTag("name", name));
        if (id != null) root.put(new IntArrayTag("id", uuidToIntArray(id)));
        if (texturePath != null) root.put(new StringTag("texture", texturePath));
        if (capePath != null) root.put(new StringTag("cape", capePath));

        // Error Catch: Null check for model to avoid NPE
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
            this.name = n; this.value = v; this.signature = s;
        }
    }
}