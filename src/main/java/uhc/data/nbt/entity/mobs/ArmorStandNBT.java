package uhc.data.nbt.entity.mobs;

import uhc.data.nbt.entity.MobNBT;
import uhc.data.nbt.tags.*;
import uhc.resource.entity.mobs.armor_stand.DisabledSlots;

/**
 * 🛡️ **ArmorStand NBT Builder**
 * <p>
 * Specialized builder for Armor Stand entities.
 * Inherits from MobNBT with recursive generics. Certain mob-specific methods
 * are disabled as they are inapplicable to the Armor Stand entity type.
 * </p>
 */
public class ArmorStandNBT extends MobNBT<ArmorStandNBT> {

    private ArmorStandNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new ArmorStand NBT builder.
     * @return A new ArmorStandNBT instance.
     */
    public static ArmorStandNBT create() {
        return new ArmorStandNBT(CompoundTag.create());
    }

    // --- Interaction Control ---

    /**
     * Disables all interactions (Place, Replace, Remove) for the specified slots.
     * @param slots The slots to fully lock.
     * @return This builder for chaining.
     */
    public ArmorStandNBT disabledSlots(DisabledSlots... slots) {
        int bitfield = 0;
        for (DisabledSlots slot : slots) {
            bitfield |= slot.getBitfield(true, true, true);
        }
        return disabledSlots(bitfield);
    }

    /**
     * Fully locks every slot on the Armor Stand.
     * @return This builder for chaining.
     */
    public ArmorStandNBT disableAllSlots() {
        return disabledSlots(DisabledSlots.disableAll());
    }

    /**
     * Set a raw bitfield for 'DisabledSlots' if custom logic is needed.
     */
    public ArmorStandNBT disabledSlots(int bitfield) {
        root().put(new IntTag("DisabledSlots", bitfield));
        return this;
    }

    // --- Visual Attributes ---

    public ArmorStandNBT invisible(boolean invisible) {
        root().put(new ByteTag("Invisible", (byte) (invisible ? 1 : 0)));
        return this;
    }

    public ArmorStandNBT marker(boolean marker) {
        root().put(new ByteTag("Marker", (byte) (marker ? 1 : 0)));
        return this;
    }

    public ArmorStandNBT noBasePlate(boolean noBasePlate) {
        root().put(new ByteTag("NoBasePlate", (byte) (noBasePlate ? 1 : 0)));
        return this;
    }

    public ArmorStandNBT showArms(boolean showArms) {
        root().put(new ByteTag("ShowArms", (byte) (showArms ? 1 : 0)));
        return this;
    }

    public ArmorStandNBT small(boolean small) {
        root().put(new ByteTag("Small", (byte) (small ? 1 : 0)));
        return this;
    }

    // --- Pose Logic ---

    /**
     * Internal helper to build the rotation list for armor stand parts.
     * Uses the CompoundTag 'get' and 'put' methods correctly.
     */
    private ArmorStandNBT setPosePart(String part, float x, float y, float z) {
        // Correctly retrieve the 'Pose' tag using your CompoundTag logic
        CompoundTag pose = (CompoundTag) root().get("Pose");

        if (pose == null) {
            pose = CompoundTag.create("Pose");
            root().put(pose);
        }

        // Initialize ListTag with the part name (e.g. "Head")
        ListTag rotationList = new ListTag(part);
        rotationList.add(new FloatTag("", x));
        rotationList.add(new FloatTag("", y));
        rotationList.add(new FloatTag("", z));

        pose.put(rotationList);
        return this;
    }

    public ArmorStandNBT poseHead(float x, float y, float z) { return setPosePart("Head", x, y, z); }
    public ArmorStandNBT poseBody(float x, float y, float z) { return setPosePart("Body", x, y, z); }
    public ArmorStandNBT poseLeftArm(float x, float y, float z) { return setPosePart("LeftArm", x, y, z); }
    public ArmorStandNBT poseRightArm(float x, float y, float z) { return setPosePart("RightArm", x, y, z); }
    public ArmorStandNBT poseLeftLeg(float x, float y, float z) { return setPosePart("LeftLeg", x, y, z); }
    public ArmorStandNBT poseRightLeg(float x, float y, float z) { return setPosePart("RightLeg", x, y, z); }

    // --- Blocked MobNBT Methods ---

    @Override
    public ArmorStandNBT noAI(boolean noAI) {
        throw new UnsupportedOperationException("ArmorStands do not use the NoAI tag.");
    }

    @Override
    public ArmorStandNBT leftHanded(boolean leftHanded) {
        throw new UnsupportedOperationException("ArmorStands do not use the LeftHanded tag.");
    }

    @Override
    public ArmorStandNBT canPickUpLoot(boolean canPickUpLoot) {
        throw new UnsupportedOperationException("ArmorStands use DisabledSlots instead of CanPickUpLoot.");
    }

    @Override
    public ArmorStandNBT persistenceRequired(boolean persistenceRequired) {
        throw new UnsupportedOperationException("ArmorStands are persistent by default.");
    }

    @Override
    public ArmorStandNBT leash(CompoundTag leash) {
        throw new UnsupportedOperationException("ArmorStands cannot be leashed.");
    }

    @Override
    public ArmorStandNBT leash(int x, int y, int z) {
        throw new UnsupportedOperationException("ArmorStands cannot be leashed.");
    }

    @Override
    public ArmorStandNBT leash(java.util.UUID uuid) {
        throw new UnsupportedOperationException("ArmorStands cannot be leashed.");
    }
}