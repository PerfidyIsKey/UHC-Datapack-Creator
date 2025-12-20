package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.tags.*;
import uhc.text.TextComponent;
import java.util.Objects;

/**
 * 👤 **Mannequin NBT Builder**
 * <p>
 * Specialized builder for Mannequin (NPC) entities.
 * </p>
 * <p>
 * Mannequins are unique "static" entities that do not possess standard mob AI.
 * They utilize a {@code profile} compound for skin/cape textures and specific
 * {@code pose} strings for animations. To maintain data integrity, this class
 * explicitly forbids standard mob behaviors like pathfinding and loot drops.
 * </p>
 */
public class MannequinNBT extends MobNBT<MannequinNBT> {

    /**
     * Private constructor for internal builder use.
     * @param root The root CompoundTag for the entity.
     */
    private MannequinNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Creates a new Mannequin NBT builder starting with an empty root tag.
     * @return A new instance of MannequinNBT.
     */
    public static MannequinNBT create() {
        return new MannequinNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🎭 Profile & Appearance ---

    /**
     * Internal helper to retrieve or initialize the 'profile' compound tag.
     * This compound stores skin, cape, and model information.
     */
    private CompoundTag profile() {
        CompoundTag profile = (CompoundTag) root().get("profile");
        if (profile == null) {
            profile = CompoundTag.create("profile");
            root().put(profile);
        }
        return profile;
    }

    /**
     * Configures the mannequin's primary skin texture and model type.
     * * @param texture The namespaced ID of the skin (e.g., "entity/player/slim/alex").
     * Relative to the 'textures' folder with .png suffix.
     * @param model   The model type: "wide" (Steve) or "slim" (Alex). Defaults to slim.
     * @return This builder instance for chaining.
     */
    public MannequinNBT texture(String texture, String model) {
        Objects.requireNonNull(texture, "Texture resource location cannot be null.");
        profile().put(new StringTag("texture", texture));
        profile().put(new StringTag("model", Objects.requireNonNullElse(model, "slim").toLowerCase()));
        return this;
    }

    /**
     * Sets an optional cape texture for the mannequin.
     * @param capeTexture Namespaced ID of the cape texture.
     * @return This builder instance for chaining.
     */
    public MannequinNBT cape(String capeTexture) {
        Objects.requireNonNull(capeTexture, "Cape texture cannot be null.");
        profile().put(new StringTag("cape", capeTexture));
        return this;
    }

    /**
     * Sets an optional custom elytra texture for the mannequin.
     * @param elytraTexture Namespaced ID of the elytra texture.
     * @return This builder instance for chaining.
     */
    public MannequinNBT elytra(String elytraTexture) {
        Objects.requireNonNull(elytraTexture, "Elytra texture cannot be null.");
        profile().put(new StringTag("elytra", elytraTexture));
        return this;
    }

    /**
     * Configures which outer skin layers of the player model should be hidden.
     * * @param layers Array of layers: "cape", "jacket", "left_sleeve", "right_sleeve",
     * "left_pants_leg", "right_pants_leg", "hat".
     * @return This builder instance for chaining.
     */
    public MannequinNBT hiddenLayers(String... layers) {
        ListTag list = new ListTag("hidden_layers");
        for (String layer : layers) {
            list.add(new StringTag("", layer.toLowerCase()));
        }
        root().put(list);
        return this;
    }

    // --- 💃 Pose & Physics ---

    /**
     * Defines which hand is the mannequin's dominant hand.
     * @param hand "left" or "right".
     * @return This builder instance for chaining.
     */
    public MannequinNBT mainHand(String hand) {
        Objects.requireNonNull(hand, "Main hand must be 'left' or 'right'.");
        root().put(new StringTag("main_hand", hand.toLowerCase()));
        return this;
    }

    /**
     * Sets the static animation pose of the mannequin.
     * @param pose Valid values: "standing", "crouching", "swimming", "fall_flying", "sleeping".
     * @return This builder instance for chaining.
     */
    public MannequinNBT pose(String pose) {
        Objects.requireNonNull(pose, "Pose cannot be null.");
        root().put(new StringTag("pose", pose.toLowerCase()));
        return this;
    }

    /**
     * When true, the mannequin cannot be moved by entities, liquids, or explosions.
     * @param immovable True to freeze position.
     * @return This builder instance for chaining.
     */
    public MannequinNBT immovable(boolean immovable) {
        root().put(new ByteTag("immovable", (byte) (immovable ? 1 : 0)));
        return this;
    }

    // --- 📝 Labeling ---

    /**
     * Sets a custom description shown where a player's scoreboard 'below_name' would appear.
     * @param text The TextComponent representing the label.
     * @return This builder instance for chaining.
     */
    public MannequinNBT description(TextComponent text) {
        Objects.requireNonNull(text, "Description cannot be null. Use hideDescription(true) to clear.");
        root().put(new StringTag("description", text.toString()));
        return this;
    }

    /**
     * Toggles whether the description label is visible to players.
     * @param hide True to hide the label entirely.
     * @return This builder instance for chaining.
     */
    public MannequinNBT hideDescription(boolean hide) {
        root().put(new ByteTag("hide_description", (byte) (hide ? 1 : 0)));
        return this;
    }

    // --- 🛑 Forbidden Inherited Methods ---

    @Override
    public MannequinNBT canPickUpLoot(boolean can) {
        throw new UnsupportedOperationException("Mannequins do not process inventory pickup logic.");
    }
    
    public MannequinNBT deathLootTable(String table) {
        throw new UnsupportedOperationException("Mannequins do not drop loot via tables.");
    }

    public MannequinNBT deathLootTableSeed(long seed) {
        throw new UnsupportedOperationException("Mannequins do not use loot seeds.");
    }

    public MannequinNBT dropChances(float... chances) {
        throw new UnsupportedOperationException("Mannequins do not use the drop_chances array.");
    }

    @Override
    public MannequinNBT homePos(int x, int y, int z) {
        throw new UnsupportedOperationException("Mannequins are stationary entities.");
    }

    public MannequinNBT homeRadius(float radius) {
        throw new UnsupportedOperationException("Mannequins do not use AI pathing radii.");
    }

    @Override
    public MannequinNBT leash(CompoundTag leash) {
        throw new UnsupportedOperationException("Mannequins cannot be leashed.");
    }

    @Override
    public MannequinNBT leftHanded(boolean left) {
        throw new UnsupportedOperationException("Mannequins use mainHand(String) for orientation.");
    }

    @Override
    public MannequinNBT noAI(boolean noAI) {
        throw new UnsupportedOperationException("Mannequins are static and do not have an AI goal selector.");
    }

    @Override
    public MannequinNBT persistenceRequired(boolean req) {
        throw new UnsupportedOperationException("Mannequins are persistent by default.");
    }
}