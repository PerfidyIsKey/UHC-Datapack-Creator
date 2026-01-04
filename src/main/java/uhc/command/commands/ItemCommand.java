package uhc.command.commands;

import uhc.arguments.block.BlockPos;
import uhc.arguments.entity.Entity;
import uhc.arguments.item.ItemStack;
import uhc.arguments.item.slot.ItemSlot;
import uhc.arguments.item.slot.SpecificItemSlot;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🔨 **Minecraft /item Command Factory**
 * <p>
 * This class provides a centralized, type-safe API for constructing Minecraft {@code /item}
 * commands. It supports the full syntax for {@code modify}, {@code replace ... with},
 * and {@code replace ... from} across both block and entity targets.
 * </p>
 * <p>
 * <b>Strict Policy:</b> This factory implements a fail-fast mechanism. It rejects null
 * parameters and invalid stack counts immediately to prevent the generation of
 * broken datapack logic.
 * </p>
 */
public class ItemCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The final, immutable Minecraft command string generated during static instantiation.
     */
    private final String command;

    /**
     * Private constructor to enforce the use of static factory methods and store the
     * validated command string.
     * * @param command The validated and fully assembled Minecraft command string.
     */
    private ItemCommand(String command) {
        this.command = command;
    }

    // =================================================================================
    // 1. MODIFY ACTIONS (item modify ...)
    // =================================================================================

    /**
     * Generates a command to apply an item modifier to a block's inventory slot.
     * <p>Syntax: {@code item modify block <pos> <slot> <modifier>}</p>
     * * @param pos      The world coordinates of the target block (non-null).
     * @param slot     The specific inventory slot to modify (non-null).
     * @param modifier The resource location or SNBT string of the modifier (non-null/blank).
     * @return A validated {@link ItemCommand} instance.
     * @throws NullPointerException if pos or slot is null.
     */
    public static ItemCommand modifyBlock(BlockPos pos, SpecificItemSlot slot, String modifier) {
        validateModifier(modifier);
        return new ItemCommand(String.format("item modify block %s %s %s",
                Objects.requireNonNull(pos, "Item Command Error: BlockPos cannot be null."),
                Objects.requireNonNull(slot, "Item Command Error: Target slot cannot be null."),
                modifier));
    }

    /**
     * Generates a command to apply an item modifier to an entity's inventory slot.
     * <p>Syntax: {@code item modify entity <targets> <slot> <modifier>}</p>
     * * @param targets  The entity selector or UUID (non-null).
     * @param slot     The specific inventory slot to modify (non-null).
     * @param modifier The resource location or SNBT string of the modifier (non-null/blank).
     * @return A validated {@link ItemCommand} instance.
     * @throws NullPointerException if targets or slot is null.
     */
    public static ItemCommand modifyEntity(Entity targets, SpecificItemSlot slot, String modifier) {
        validateModifier(modifier);
        return new ItemCommand(String.format("item modify entity %s %s %s",
                Objects.requireNonNull(targets, "Item Command Error: Entity targets cannot be null."),
                Objects.requireNonNull(slot, "Item Command Error: Target slot cannot be null."),
                modifier));
    }

    // =================================================================================
    // 2. REPLACE WITH ACTIONS (item replace ... with ...)
    // =================================================================================

    /**
     * Replaces the item in a block's slot with a specific item (count defaults to 1).
     * * @param pos  Target block position (non-null).
     * @param slot Target slot (non-null).
     * @param item The item to place (non-null).
     * @return A validated {@link ItemCommand} instance.
     */
    public static ItemCommand replaceBlockWith(BlockPos pos, SpecificItemSlot slot, ItemStack item) {
        return replaceBlockWith(pos, slot, item, null);
    }

    /**
     * Replaces the item in a block's slot with a specific item and explicit count.
     * * @param pos   Target block position (non-null).
     * @param slot  Target slot (non-null).
     * @param item  The item to place (non-null).
     * @param count The stack size [1-99].
     * @return A validated {@link ItemCommand} instance.
     */
    public static ItemCommand replaceBlockWith(BlockPos pos, SpecificItemSlot slot, ItemStack item, Integer count) {
        return new ItemCommand(assembleReplaceWith("block",
                Objects.requireNonNull(pos, "Item Command Error: BlockPos cannot be null.").toString(),
                slot, item, count));
    }

    /**
     * Replaces the item in an entity's slot with a specific item (count defaults to 1).
     * * @param targets Target entity (non-null).
     * @param slot    Target slot (non-null).
     * @param item    The item to place (non-null).
     * @return A validated {@link ItemCommand} instance.
     */
    public static ItemCommand replaceEntityWith(Entity targets, SpecificItemSlot slot, ItemStack item) {
        return replaceEntityWith(targets, slot, item, null);
    }

    /**
     * Replaces the item in an entity's slot with a specific item and explicit count.
     * * @param targets Target entity (non-null).
     * @param slot    Target slot (non-null).
     * @param item    The item to place (non-null).
     * @param count   The stack size [1-99].
     * @return A validated {@link ItemCommand} instance.
     */
    public static ItemCommand replaceEntityWith(Entity targets, SpecificItemSlot slot, ItemStack item, Integer count) {
        return new ItemCommand(assembleReplaceWith("entity",
                Objects.requireNonNull(targets, "Item Command Error: Entity targets cannot be null.").toString(),
                slot, item, count));
    }

    // =================================================================================
    // 3. REPLACE FROM ACTIONS (item replace ... from ...)
    // =================================================================================

    // --- From Block Sources ---

    public static ItemCommand replaceBlockFromBlock(BlockPos targetPos, SpecificItemSlot targetSlot, BlockPos sourcePos, ItemSlot sourceSlot) {
        return replaceBlockFromBlock(targetPos, targetSlot, sourcePos, sourceSlot, null);
    }

    public static ItemCommand replaceBlockFromBlock(BlockPos targetPos, SpecificItemSlot targetSlot, BlockPos sourcePos, ItemSlot sourceSlot, String modifier) {
        return new ItemCommand(assembleReplaceFrom("block", targetPos.toString(), targetSlot, "block", sourcePos.toString(), sourceSlot, modifier));
    }

    public static ItemCommand replaceEntityFromBlock(Entity targets, SpecificItemSlot targetSlot, BlockPos sourcePos, ItemSlot sourceSlot) {
        return replaceEntityFromBlock(targets, targetSlot, sourcePos, sourceSlot, null);
    }

    public static ItemCommand replaceEntityFromBlock(Entity targets, SpecificItemSlot targetSlot, BlockPos sourcePos, ItemSlot sourceSlot, String modifier) {
        return new ItemCommand(assembleReplaceFrom("entity", targets.toString(), targetSlot, "block", sourcePos.toString(), sourceSlot, modifier));
    }

    // --- From Entity Sources ---

    public static ItemCommand replaceBlockFromEntity(BlockPos targetPos, SpecificItemSlot targetSlot, Entity sourceTarget, ItemSlot sourceSlot) {
        return replaceBlockFromEntity(targetPos, targetSlot, sourceTarget, sourceSlot, null);
    }

    public static ItemCommand replaceBlockFromEntity(BlockPos targetPos, SpecificItemSlot targetSlot, Entity sourceTarget, ItemSlot sourceSlot, String modifier) {
        return new ItemCommand(assembleReplaceFrom("block", targetPos.toString(), targetSlot, "entity", sourceTarget.toString(), sourceSlot, modifier));
    }

    public static ItemCommand replaceEntityFromEntity(Entity targets, SpecificItemSlot targetSlot, Entity sourceTarget, ItemSlot sourceSlot) {
        return replaceEntityFromEntity(targets, targetSlot, sourceTarget, sourceSlot, null);
    }

    public static ItemCommand replaceEntityFromEntity(Entity targets, SpecificItemSlot targetSlot, Entity sourceTarget, ItemSlot sourceSlot, String modifier) {
        return new ItemCommand(assembleReplaceFrom("entity", targets.toString(), targetSlot, "entity", sourceTarget.toString(), sourceSlot, modifier));
    }

    // =================================================================================
    // INTERNAL HELPERS & VALIDATION (Fail-Fast Architecture)
    // =================================================================================

    /**
     * Internal helper to assemble the 'replace with' variant of the command.
     */
    private static String assembleReplaceWith(String type, String target, SpecificItemSlot slot, ItemStack item, Integer count) {
        Objects.requireNonNull(slot, "Item Command Error: Target slot cannot be null.");
        Objects.requireNonNull(item, "Item Command Error: Replacement item stack cannot be null.");

        StringBuilder sb = new StringBuilder("item replace ")
                .append(type).append(" ").append(target).append(" ")
                .append(slot).append(" with ").append(item.build());

        if (count != null) {
            if (count < 1 || count > 99) {
                throw new IllegalArgumentException("Item Command Error: Stack count [" + count + "] is out of bounds (1-99).");
            }
            sb.append(" ").append(count);
        }
        return sb.toString();
    }

    /**
     * Internal helper to assemble the 'replace from' variant of the command.
     */
    private static String assembleReplaceFrom(String targetType, String target, SpecificItemSlot targetSlot, String sourceType, String source, ItemSlot sourceSlot, String modifier) {
        Objects.requireNonNull(targetSlot, "Item Command Error: Target slot cannot be null.");
        Objects.requireNonNull(sourceSlot, "Item Command Error: Source slot cannot be null.");

        StringBuilder sb = new StringBuilder("item replace ")
                .append(targetType).append(" ").append(target).append(" ")
                .append(targetSlot).append(" from ")
                .append(sourceType).append(" ").append(source).append(" ")
                .append(sourceSlot);

        if (modifier != null && !modifier.isBlank()) {
            sb.append(" ").append(modifier);
        }
        return sb.toString();
    }

    /**
     * Validates that a modifier string is neither null nor empty.
     * * @param modifier The string to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private static void validateModifier(String modifier) {
        if (modifier == null || modifier.isBlank()) {
            throw new IllegalArgumentException("Item Command Error: Modifier string cannot be null or blank.");
        }
    }

    // =================================================================================
    // CORE CONTRACT
    // =================================================================================

    /**
     * Returns the pre-constructed Minecraft command string.
     * @return A non-null, validated command.
     */
    @Override
    public String generate() {
        return this.command;
    }

    /**
     * Returns the command string.
     * @return Result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}