package uhc.data.nbt.entity.projectiles;

import uhc.data.nbt.entity.EntityNBT;
import uhc.data.nbt.tags.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 🏹 **Projectile NBT Builder**
 * <p>
 * Specialized builder for Projectile entities such as Arrows, Fireballs, and Tridents.
 * This class manages the state required for projectile physics, owner attribution,
 * and game event triggering.
 * </p>
 */
public class ProjectileNBT extends EntityNBT<ProjectileNBT> {

    private ProjectileNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Projectile NBT builder with an empty root structure.
     * @return A new instance of ProjectileNBT.
     */
    public static ProjectileNBT create() {
        return new ProjectileNBT(CompoundTag.create());
    }

    @Override
    public CompoundTag root() {
        return build();
    }

    // --- 🚀 Physics & State ---

    /**
     * Defines if the projectile has ticked at least once since being fired.
     * <p>
     * <b>Vanilla Logic:</b> If true, the game prevents the {@code projectile_shoot}
     * game event from firing again.
     * </p>
     * @param hasBeenShot true if the projectile is already in flight.
     * @return This builder instance.
     */
    public ProjectileNBT hasBeenShot(boolean hasBeenShot) {
        root().put(new ByteTag("HasBeenShot", (byte) (hasBeenShot ? 1 : 0)));
        return this;
    }

    /**
     * Defines if the projectile has left the owner's bounding box.
     * <p>
     * <b>Vanilla Logic:</b> Projectiles cannot collide with their owner until they
     * have "left" the owner. To save space, this tag is removed from the NBT if false.
     * </p>
     * @param leftOwner true if collision with the owner is now enabled.
     * @return This builder instance.
     */
    public ProjectileNBT leftOwner(boolean leftOwner) {
        if (leftOwner) {
            root().put(new ByteTag("LeftOwner", (byte) 1));
        } else {
            // Cleanup: Minecraft NBT specification implies absence equals false here.
            root().remove("LeftOwner");
        }
        return this;
    }

    // --- 👤 Ownership ---

    /**
     * Sets the owner of this projectile using their UUID.
     * <p>
     * The UUID is automatically converted from a 128-bit Java UUID into a
     * 4-element Integer Array required by Minecraft's modern NBT format.
     * </p>
     * @param ownerUUID The UUID of the entity that shot/threw this projectile.
     * @throws NullPointerException if ownerUUID is null.
     * @return This builder instance.
     */
    public ProjectileNBT owner(UUID ownerUUID) {
        Objects.requireNonNull(ownerUUID, "Owner UUID cannot be null. Use removeOwner() if needed.");

        long mostSig = ownerUUID.getMostSignificantBits();
        long leastSig = ownerUUID.getLeastSignificantBits();

        // Convert 128-bit UUID to four 32-bit signed integers
        int[] uuidArray = new int[] {
                (int) (mostSig >> 32),
                (int) mostSig,
                (int) (leastSig >> 32),
                (int) leastSig
        };

        root().put(new IntArrayTag("Owner", uuidArray));
        return this;
    }

    /**
     * Removes the 'Owner' tag entirely from the projectile.
     * Use this for projectiles summoned without a specific source.
     * @return This builder instance.
     */
    public ProjectileNBT removeOwner() {
        root().remove("Owner");
        return this;
    }
}