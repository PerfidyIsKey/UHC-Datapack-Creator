package uhc.data.nbt.entity.mobs;

import uhc.arguments.item.SingleItemStack;
import uhc.data.nbt.tags.*;
import uhc.resource.gameplay.GameModeId;
import uhc.resource.dimension.DimensionId;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 👤 **Player NBT Builder**
 * <p>
 * Specialized builder for Player entities. Handles unique player data structures
 * such as the 'abilities' compound, hunger, experience, and inventory.
 * </p>
 */
public class PlayerNBT extends MobNBT<PlayerNBT> {

    /**
     * Private constructor for the Player NBT builder.
     * @param root The underlying CompoundTag where data is stored.
     */
    private PlayerNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Creates a new Player NBT builder with an empty root compound.
     * @return A new instance of PlayerNBT.
     */
    public static PlayerNBT create() {
        return new PlayerNBT(CompoundTag.create());
    }

    /**
     * @return The root CompoundTag containing the built player data.
     */
    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🕊️ Abilities (Nesting Helper) ---

    /**
     * Internal helper to retrieve or initialize the 'abilities' compound.
     * This compound stores player capabilities like flight and invulnerability.
     * @return The "abilities" CompoundTag.
     */
    private CompoundTag abilities() {
        CompoundTag abilities = (CompoundTag) root().get("abilities");
        if (abilities == null) {
            abilities = CompoundTag.create("abilities");
            root().put(abilities);
        }
        return abilities;
    }

    /** Sets whether the player is currently flying. */
    public PlayerNBT flying(boolean flying) {
        abilities().put(new ByteTag("flying", (byte) (flying ? 1 : 0)));
        return this;
    }

    /** Sets the player's flight speed (default: 0.05). */
    public PlayerNBT flySpeed(float speed) {
        abilities().put(new FloatTag("flySpeed", speed));
        return this;
    }

    /** If true, the player can break blocks instantly (Creative mode feature). */
    public PlayerNBT instabuild(boolean instabuild) {
        abilities().put(new ByteTag("instabuild", (byte) (instabuild ? 1 : 0)));
        return this;
    }

    /** If true, the player takes no damage from any source. */
    public PlayerNBT invulnerable(boolean invuln) {
        abilities().put(new ByteTag("invulnerable", (byte) (invuln ? 1 : 0)));
        return this;
    }

    /** If false, the player cannot modify the world. */
    public PlayerNBT mayBuild(boolean mayBuild) {
        abilities().put(new ByteTag("mayBuild", (byte) (mayBuild ? 1 : 0)));
        return this;
    }

    /** If true, the player is allowed to toggle flight. */
    public PlayerNBT mayfly(boolean mayfly) {
        abilities().put(new ByteTag("mayfly", (byte) (mayfly ? 1 : 0)));
        return this;
    }

    /** Sets the player's walking speed (default: 0.1). */
    public PlayerNBT walkSpeed(float speed) {
        abilities().put(new FloatTag("walkSpeed", speed));
        return this;
    }

    // --- 🍔 Metabolism (Hunger & Health) ---

    /** Sets the hunger level (0-20). */
    public PlayerNBT foodLevel(int level) {
        root().put(new IntTag("foodLevel", Math.max(0, Math.min(20, level))));
        return this;
    }

    /** Sets hunger exhaustion (decreases saturation/food level). */
    public PlayerNBT foodExhaustionLevel(float level) {
        root().put(new FloatTag("foodExhaustionLevel", level));
        return this;
    }

    /** Sets hunger saturation (prevents food level from dropping). */
    public PlayerNBT foodSaturationLevel(float level) {
        root().put(new FloatTag("foodSaturationLevel", level));
        return this;
    }

    /** Sets ticks until next hunger-related event (e.g., healing or starving). */
    public PlayerNBT foodTickTimer(int ticks) {
        root().put(new IntTag("foodTickTimer", ticks));
        return this;
    }

    // --- 📈 Progression (XP & Score) ---

    /** Sets the player's current XP level. */
    public PlayerNBT xpLevel(int level) {
        root().put(new IntTag("XpLevel", Math.max(0, level)));
        return this;
    }

    /** Sets XP progress percentage toward the next level (0.0 to 1.0). */
    public PlayerNBT xpP(float progressPercentage) {
        root().put(new FloatTag("XpP", Math.max(0f, Math.min(1f, progressPercentage))));
        return this;
    }

    /** Sets the seed for the next enchantment table options. */
    public PlayerNBT xpSeed(int seed) {
        root().put(new IntTag("XpSeed", seed));
        return this;
    }

    /** Sets the total experience points earned. */
    public PlayerNBT xpTotal(int total) {
        root().put(new IntTag("XpTotal", Math.max(0, total)));
        return this;
    }

    /** Sets the score displayed upon the player's death. */
    public PlayerNBT score(int score) {
        root().put(new IntTag("Score", score));
        return this;
    }

    // --- 🌍 World & Survival State ---

    /** Sets the player's current dimension (e.g., "minecraft:overworld"). */
    public PlayerNBT dimension(DimensionId dimension) {
        Objects.requireNonNull(dimension, "DimensionId cannot be null");
        root().put(new StringTag("Dimension", dimension.toString()));
        return this;
    }

    /** Sets the player's current GameMode. */
    public PlayerNBT playerGameType(GameModeId mode) {
        Objects.requireNonNull(mode, "GameMode cannot be null");
        root().put(new IntTag("playerGameType", mode.index()));
        return this;
    }

    /** Sets the previous GameMode (used for switching back from Spectator). */
    public PlayerNBT previousPlayerGameType(GameModeId type) {
        Objects.requireNonNull(type, "Previous GameMode cannot be null");
        root().put(new IntTag("previousPlayerGameType", type.index()));
        return this;
    }

    /** Records where the player last died. */
    public PlayerNBT lastDeathLocation(DimensionId dimension, int x, int y, int z) {
        Objects.requireNonNull(dimension, "Death dimension cannot be null");
        CompoundTag death = CompoundTag.create("LastDeathLocation");
        death.put(new StringTag("dimension", dimension.toString()));
        death.put(new IntArrayTag("pos", new int[]{x, y, z}));
        root().put(death);
        return this;
    }

    /** Sets whether the player has seen the end-game credits. */
    public PlayerNBT seenCredits(boolean seen) {
        root().put(new ByteTag("seenCredits", (byte) (seen ? 1 : 0)));
        return this;
    }

    /** Sets ticks until the player wakes up from a bed. */
    public PlayerNBT sleepTimer(short ticks) {
        root().put(new ShortTag("SleepTimer", ticks));
        return this;
    }

    // --- ⚙️ Physics & Explosion Impact ---

    /** Tracks the origin of the last explosion that affected the player. */
    public PlayerNBT currentExplosionImpactPos(double x, double y, double z) {
        ListTag pos = new ListTag("current_explosion_impact_pos");
        pos.add(new DoubleTag("", x));
        pos.add(new DoubleTag("", y));
        pos.add(new DoubleTag("", z));
        root().put(pos);
        return this;
    }

    /** The position where the player entered the Nether (for portal logic). */
    public PlayerNBT enteredNetherPos(double x, double y, double z) {
        ListTag pos = new ListTag("entered_nether_pos");
        pos.add(new DoubleTag("", x));
        pos.add(new DoubleTag("", y));
        pos.add(new DoubleTag("", z));
        root().put(pos);
        return this;
    }

    /** If true, the player won't take fall damage from a pending explosion knockback. */
    public PlayerNBT ignoreFallDamageFromCurrentExplosion(boolean ignore) {
        root().put(new ByteTag("ignore_fall_damage_from_current_explosion", (byte) (ignore ? 1 : 0)));
        return this;
    }

    // --- 📦 Inventory & Selected Items ---

    /** Sets the full inventory ListTag (items must include 'Slot' byte). */
    public PlayerNBT inventory(ListTag items) {
        Objects.requireNonNull(items, "Inventory ListTag cannot be null");
        items.setName("Inventory");
        root().put(items);
        return this;
    }

    /** Sets the items stored in the player's Ender Chest. */
    public PlayerNBT enderItems(ListTag items) {
        Objects.requireNonNull(items, "EnderItems ListTag cannot be null");
        items.setName("EnderItems");
        root().put(items);
        return this;
    }

    /** Sets the active hotbar slot (0-8). */
    public PlayerNBT selectedItemSlot(int slot) {
        root().put(new IntTag("SelectedItemSlot", Math.max(0, Math.min(8, slot))));
        return this;
    }

    /** Sets the data of the item currently held in the player's hand. */
    public PlayerNBT selectedItem(SingleItemStack stack) {
        Objects.requireNonNull(stack, "SelectedItem stack cannot be null");
        CompoundTag itemData = stack.toNbt();
        itemData.setName("SelectedItem");
        root().put(itemData);
        return this;
    }

    // --- 🧩 Complex Entities & Tracking ---

    /** Sets data for the vehicle the player is riding. */
    public PlayerNBT rootVehicle(CompoundTag vehicleData) {
        Objects.requireNonNull(vehicleData, "RootVehicle compound cannot be null");
        vehicleData.setName("RootVehicle");
        root().put(vehicleData);
        return this;
    }

    /** Sets the entity sitting on the player's left shoulder (e.g., Parrot). */
    public PlayerNBT shoulderEntityLeft(CompoundTag entity) {
        Objects.requireNonNull(entity, "ShoulderEntity compound cannot be null");
        entity.setName("ShoulderEntityLeft");
        root().put(entity);
        return this;
    }

    /** Sets the entity sitting on the player's right shoulder. */
    public PlayerNBT shoulderEntityRight(CompoundTag entity) {
        Objects.requireNonNull(entity, "ShoulderEntity compound cannot be null");
        entity.setName("ShoulderEntityRight");
        root().put(entity);
        return this;
    }

    /** Tracks the player's status regarding Warden spawning/agitation. */
    public PlayerNBT wardenSpawnTracker(int warningLevel, int cooldown, int ticksSinceLast) {
        CompoundTag tracker = CompoundTag.create("warden_spawn_tracker");
        tracker.put(new IntTag("warning_level", Math.max(0, Math.min(3, warningLevel))));
        tracker.put(new IntTag("cooldown_ticks", Math.max(0, cooldown)));
        tracker.put(new IntTag("ticks_since_last_warning", Math.max(0, ticksSinceLast)));
        root().put(tracker);
        return this;
    }

    /** Sets known recipes and book configuration. */
    public PlayerNBT recipeBook(CompoundTag recipeData) {
        Objects.requireNonNull(recipeData, "RecipeBook compound cannot be null");
        recipeData.setName("recipeBook");
        root().put(recipeData);
        return this;
    }

    // --- 🏁 Final Versioning & Respawn ---

    /** Sets the DataVersion for migration/compatibility. */
    public PlayerNBT dataVersion(int version) {
        root().put(new IntTag("DataVersion", version));
        return this;
    }

    /** Sets detailed respawn parameters (e.g., Bed or Respawn Anchor data). */
    public PlayerNBT respawn(int[] pos, DimensionId dimension, float yaw, float pitch, boolean forced) {
        Objects.requireNonNull(dimension, "Respawn dimension cannot be null");
        Objects.requireNonNull(pos, "Respawn position cannot be null");
        if (pos.length != 3) throw new IllegalArgumentException("Position array must contain exactly 3 integers [x, y, z].");

        CompoundTag respawn = CompoundTag.create("respawn");
        respawn.put(new IntArrayTag("pos", pos));
        respawn.put(new StringTag("dimension", dimension.toString()));
        respawn.put(new FloatTag("yaw", yaw));
        respawn.put(new FloatTag("pitch", pitch));
        respawn.put(new ByteTag("forced", (byte) (forced ? 1 : 0)));

        root().put(respawn);
        return this;
    }

    // --- 🛑 Blocked Inherited Methods ---

    /** @throws UnsupportedOperationException Players use usernames, not CustomName tags. */
    @Override
    public PlayerNBT customName(TextComponent name) {
        throw new UnsupportedOperationException("Players do not use CustomName; they use usernames.");
    }

    /** @throws UnsupportedOperationException Tag not applicable to players. */
    @Override
    public PlayerNBT customNameVisible(boolean visible) {
        throw new UnsupportedOperationException("Players do not use CustomNameVisible.");
    }

    /** @throws UnsupportedOperationException Players do not use the NBT 'Glowing' tag for effects. */
    @Override
    public PlayerNBT glowing(boolean glowing) {
        throw new UnsupportedOperationException("Glowing tag is not applicable to Player NBT files.");
    }

    /** @throws UnsupportedOperationException Players pick up loot by default. */
    @Override
    public PlayerNBT canPickUpLoot(boolean canPickUpLoot) {
        throw new UnsupportedOperationException("Players pick up loot by default.");
    }

    /** @throws UnsupportedOperationException Players are persistent by default. */
    @Override
    public PlayerNBT persistenceRequired(boolean persistenceRequired) {
        throw new UnsupportedOperationException("Players are persistent by default.");
    }

    /** @throws UnsupportedOperationException Players cannot be leashed. */
    @Override
    public PlayerNBT leash(CompoundTag leash) {
        throw new UnsupportedOperationException("Players cannot be leashed.");
    }

    /** @throws UnsupportedOperationException Players do not use mob-specific drop chances. */
    @Override
    public PlayerNBT dropChances(float... chances) {
        throw new UnsupportedOperationException("Players do not use drop_chances.");
    }
}