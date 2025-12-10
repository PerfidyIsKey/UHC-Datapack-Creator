package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.ByteTag;

/**
 * Sign Block Entity: Contains front_text, back_text (both Compound Tags), and is_waxed (ByteTag).
 */
public class SignEntity extends CompoundTag implements BlockEntity {

    public enum SignDataKey {
        FRONT_TEXT("front_text"),
        BACK_TEXT("back_text"),
        IS_WAXED("is_waxed"); // ByteTag: 1b or 0b

        private final String nbtName;
        SignDataKey(String nbtName) { this.nbtName = nbtName; }
        public String getNbtName() { return nbtName; }
    }

    private final SignSide frontSide;
    private final SignSide backSide;

    private SignEntity() {
        // PASS 'this' (the current SignEntity instance) to SignSide constructor
        this.frontSide = new SignSide(SignDataKey.FRONT_TEXT.getNbtName(), this);
        this.backSide = new SignSide(SignDataKey.BACK_TEXT.getNbtName(), this);

        initializeDefaultTags();
    }

    public static SignEntity create() {
        return new SignEntity();
    }

    private void initializeDefaultTags() {

        // Add the two sides
        this.put(frontSide);
        this.put(backSide);

        // Add the wax status
        this.put(new ByteTag(SignDataKey.IS_WAXED.getNbtName(), (byte)0)); // Default to unwaxed (0b)
    }

    /** Gets the SignSide helper for the front face. */
    public SignSide getFrontSide() {
        return frontSide;
    }

    /** Gets the SignSide helper for the back face. */
    public SignSide getBackSide() {
        return backSide;
    }

    /** Sets the waxed status (0b or 1b) and enables chaining. */
    public SignEntity setWaxed(boolean waxed) {
        ((ByteTag) get(SignDataKey.IS_WAXED.getNbtName())).setValue(waxed ? (byte)1 : (byte)0);
        return this;
    }
}