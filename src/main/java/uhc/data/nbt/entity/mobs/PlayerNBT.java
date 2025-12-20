package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.MobNBT;
import uhc.data.nbt.tags.*;
import uhc.game.GameModeId;
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

    private PlayerNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Creates a new Player NBT builder with an empty root compound.
     */
    public static PlayerNBT create() {
        return new PlayerNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🕊️ Abilities (Nesting Helper) ---

    /**
     * Internal helper to retrieve or initialize the 'abilities' compound.
     */
    private CompoundTag abilities() {
        CompoundTag abilities = (CompoundTag) root().get("abilities");
        if (abilities == null) {
            abilities = CompoundTag.create("abilities");
            root().put(abilities);
        }
        return abilities;
    }

    public PlayerNBT flying(boolean flying) {
        abilities().put(new ByteTag("flying", (byte) (flying ? 1 : 0)));
        return this;
    }

    public PlayerNBT flySpeed(float speed) {
        abilities().put(new FloatTag("flySpeed", speed));
        return this;
    }

    public PlayerNBT instabuild(boolean instabuild) {
        abilities().put(new ByteTag("instabuild", (byte) (instabuild ? 1 : 0)));
        return this;
    }

    public PlayerNBT invulnerable(boolean invuln) {
        abilities().put(new ByteTag("invulnerable", (byte) (invuln ? 1 : 0)));
        return this;
    }

    public PlayerNBT mayBuild(boolean mayBuild) {
        abilities().put(new ByteTag("mayBuild", (byte) (mayBuild ? 1 : 0)));
        return this;
    }

    public PlayerNBT mayfly(boolean mayfly) {
        abilities().put(new ByteTag("mayfly", (byte) (mayfly ? 1 : 0)));
        return this;
    }

    public PlayerNBT walkSpeed(float speed) {
        abilities().put(new FloatTag("walkSpeed", speed));
        return this;
    }

    // --- 🍔 Metabolism (Hunger & Health) ---

    public PlayerNBT foodLevel(int level) {
        root().put(new IntTag("foodLevel", Math.max(0, Math.min(20, level))));
        return this;
    }

    public PlayerNBT foodExhaustionLevel(float level) {
        root().put(new FloatTag("foodExhaustionLevel", level));
        return this;
    }

    public PlayerNBT foodSaturationLevel(float level) {
        root().put(new FloatTag("foodSaturationLevel", level));
        return this;
    }

    public PlayerNBT foodTickTimer(int ticks) {
        root().put(new IntTag("foodTickTimer", ticks));
        return this;
    }

    // --- 📈 Progression (XP & Score) ---

    public PlayerNBT xpLevel(int level) {
        root().put(new IntTag("XpLevel", Math.max(0, level)));
        return this;
    }

    public PlayerNBT xpP(float progressPercentage) {
        root().put(new FloatTag("XpP", Math.max(0f, Math.min(1f, progressPercentage))));
        return this;
    }

    public PlayerNBT xpSeed(int seed) {
        root().put(new IntTag("XpSeed", seed));
        return this;
    }

    public PlayerNBT xpTotal(int total) {
        root().put(new IntTag("XpTotal", Math.max(0, total)));
        return this;
    }

    public PlayerNBT score(int score) {
        root().put(new IntTag("Score", score));
        return this;
    }

    // --- 🌍 World & Survival State ---

    public PlayerNBT dimension(DimensionId dimension) {
        Objects.requireNonNull(dimension, "DimensionId cannot be null");
        root().put(new StringTag("Dimension", dimension.toString()));
        return this;
    }

    public PlayerNBT playerGameType(GameModeId mode) {
        Objects.requireNonNull(mode, "GameMode cannot be null");
        root().put(new IntTag("playerGameType", mode.index()));
        return this;
    }

    public PlayerNBT previousPlayerGameType(GameModeId type) {
        root().put(new IntTag("previousPlayerGameType", type.index()));
        return this;
    }

    public PlayerNBT lastDeathLocation(DimensionId dimension, int x, int y, int z) {
        Objects.requireNonNull(dimension, "Death dimension cannot be null");
        CompoundTag death = CompoundTag.create("LastDeathLocation");
        death.put(new StringTag("dimension", dimension.toString()));
        death.put(new IntArrayTag("pos", new int[]{x, y, z}));
        root().put(death);
        return this;
    }

    public PlayerNBT seenCredits(boolean seen) {
        root().put(new ByteTag("seenCredits", (byte) (seen ? 1 : 0)));
        return this;
    }

    public PlayerNBT sleepTimer(short ticks) {
        root().put(new ShortTag("SleepTimer", ticks));
        return this;
    }

    // --- ⚙️ Physics & Explosion Impact ---

    public PlayerNBT currentExplosionImpactPos(double x, double y, double z) {
        ListTag pos = new ListTag("current_explosion_impact_pos");
        pos.add(new DoubleTag("", x));
        pos.add(new DoubleTag("", y));
        pos.add(new DoubleTag("", z));
        root().put(pos);
        return this;
    }

    public PlayerNBT enteredNetherPos(double x, double y, double z) {
        ListTag pos = new ListTag("entered_nether_pos");
        pos.add(new DoubleTag("", x));
        pos.add(new DoubleTag("", y));
        pos.add(new DoubleTag("", z));
        root().put(pos);
        return this;
    }

    public PlayerNBT ignoreFallDamageFromCurrentExplosion(boolean ignore) {
        root().put(new ByteTag("ignore_fall_damage_from_current_explosion", (byte) (ignore ? 1 : 0)));
        return this;
    }

    // --- 📦 Inventory & Selected Items ---

    public PlayerNBT inventory(ListTag items) {
        Objects.requireNonNull(items, "Inventory ListTag cannot be null");
        items.setName("Inventory");
        root().put(items);
        return this;
    }

    public PlayerNBT enderItems(ListTag items) {
        Objects.requireNonNull(items, "EnderItems ListTag cannot be null");
        items.setName("EnderItems");
        root().put(items);
        return this;
    }

    public PlayerNBT selectedItemSlot(int slot) {
        root().put(new IntTag("SelectedItemSlot", Math.max(0, Math.min(8, slot))));
        return this;
    }

    public PlayerNBT selectedItem(CompoundTag itemData) {
        Objects.requireNonNull(itemData, "SelectedItem compound cannot be null");
        itemData.setName("SelectedItem");
        root().put(itemData);
        return this;
    }

    // --- 🧩 Complex Entities & Tracking ---

    public PlayerNBT rootVehicle(CompoundTag vehicleData) {
        Objects.requireNonNull(vehicleData, "RootVehicle compound cannot be null");
        vehicleData.setName("RootVehicle");
        root().put(vehicleData);
        return this;
    }

    public PlayerNBT shoulderEntityLeft(CompoundTag entity) {
        Objects.requireNonNull(entity, "ShoulderEntity compound cannot be null");
        entity.setName("ShoulderEntityLeft");
        root().put(entity);
        return this;
    }

    public PlayerNBT shoulderEntityRight(CompoundTag entity) {
        Objects.requireNonNull(entity, "ShoulderEntity compound cannot be null");
        entity.setName("ShoulderEntityRight");
        root().put(entity);
        return this;
    }

    public PlayerNBT wardenSpawnTracker(int warningLevel, int cooldown, int ticksSinceLast) {
        CompoundTag tracker = CompoundTag.create("warden_spawn_tracker");
        tracker.put(new IntTag("warning_level", Math.max(0, Math.min(3, warningLevel))));
        tracker.put(new IntTag("cooldown_ticks", Math.max(0, cooldown)));
        tracker.put(new IntTag("ticks_since_last_warning", Math.max(0, ticksSinceLast)));
        root().put(tracker);
        return this;
    }

    public PlayerNBT recipeBook(CompoundTag recipeData) {
        Objects.requireNonNull(recipeData, "RecipeBook compound cannot be null");
        recipeData.setName("recipeBook");
        root().put(recipeData);
        return this;
    }

    // --- 🏁 Final Versioning & Respawn ---

    public PlayerNBT dataVersion(int version) {
        root().put(new IntTag("DataVersion", version));
        return this;
    }

    public PlayerNBT respawn(int[] pos, DimensionId dimension, float yaw, float pitch, boolean forced) {
        Objects.requireNonNull(dimension, "Respawn dimension cannot be null");
        Objects.requireNonNull(pos, "Respawn position cannot be null");

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

    public PlayerNBT customName(TextComponent name) {
        throw new UnsupportedOperationException("Players do not use CustomName; they use usernames.");
    }

    @Override
    public PlayerNBT customNameVisible(boolean visible) {
        throw new UnsupportedOperationException("Players do not use CustomNameVisible.");
    }

    @Override
    public PlayerNBT glowing(boolean glowing) {
        throw new UnsupportedOperationException("Glowing tag is not applicable to Player NBT files.");
    }

    @Override
    public PlayerNBT canPickUpLoot(boolean canPickUpLoot) {
        throw new UnsupportedOperationException("Players pick up loot by default.");
    }

    @Override
    public PlayerNBT persistenceRequired(boolean persistenceRequired) {
        throw new UnsupportedOperationException("Players are persistent by default.");
    }

    @Override
    public PlayerNBT leash(CompoundTag leash) {
        throw new UnsupportedOperationException("Players cannot be leashed.");
    }

    public PlayerNBT dropChances(float... chances) {
        throw new UnsupportedOperationException("Players do not use drop_chances.");
    }
}