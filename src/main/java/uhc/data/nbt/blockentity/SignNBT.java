package uhc.data.nbt.blockentity;

import uhc.data.nbt.tags.*;
import uhc.resource.block.BlockId;
import uhc.text.DyeColor;
import uhc.text.TextComponent;
import java.util.Objects;

/**
 * 🪧 **Sign NBT Builder**
 * <p>
 * Specialized builder for Sign block entities (Standing, Wall, and Hanging signs).
 * Manages dual-sided text, glowing effects, and waxing states.
 * </p>
 */
public class SignNBT extends BlockEntityNBT<SignNBT> {

    private SignNBT(CompoundTag root) {
        super(root);
    }

    /**
     * Initializes a new Sign NBT builder.
     * @return A new instance of SignNBT.
     */
    public static SignNBT create() {
        return new SignNBT(CompoundTag.create());
    }

    /**
     * Sets whether the sign is waxed (locked with honeycomb).
     * @param waxed True if the text cannot be edited by players.
     */
    public SignNBT waxed(boolean waxed) {
        build().put(new ByteTag("is_waxed", (byte) (waxed ? 1 : 0)));
        return this;
    }

    /**
     * Configures the front side of the sign.
     * @param side A configured SignSideNBT object.
     */
    public SignNBT frontText(SignSideNBT side) {
        CompoundTag tag = side.build();
        tag.setName("front_text");
        build().put(tag);
        return this;
    }

    /**
     * Configures the back side of the sign.
     * @param side A configured SignSideNBT object.
     */
    public SignNBT backText(SignSideNBT side) {
        CompoundTag tag = side.build();
        tag.setName("back_text");
        build().put(tag);
        return this;
    }

    /**
     * 📝 **SignSideNBT Internal Helper**
     * Manages the data for a single face of a sign.
     */
    public static class SignSideNBT {
        private final CompoundTag sideRoot = CompoundTag.create();

        public static SignSideNBT create() {
            return new SignSideNBT();
        }

        public SignSideNBT glowing(boolean glow) {
            sideRoot.put(new ByteTag("has_glowing_text", (byte) (glow ? 1 : 0)));
            return this;
        }

        public SignSideNBT color(DyeColor color) {
            sideRoot.put(new StringTag("color", Objects.requireNonNullElse(color, DyeColor.BLACK).toString()));
            return this;
        }

        /**
         * Sets the four lines of text for this side.
         * @param lines Array of 4 TextComponents.
         */
        public SignSideNBT messages(TextComponent... lines) {
            ListTag list = new ListTag("messages");
            // Signs always expect exactly 4 lines in the list
            for (int i = 0; i < 4; i++) {
                String content = (i < lines.length && lines[i] != null) ? lines[i].toString() : "{\"text\":\"\"}";
                list.add(new StringTag("", content));
            }
            sideRoot.put(list);
            return this;
        }

        public CompoundTag build() {
            return sideRoot;
        }
    }
}