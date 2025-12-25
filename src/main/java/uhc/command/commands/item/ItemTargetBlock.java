package uhc.command.commands.item;

import uhc.arguments.block.BlockPos;

/**
 * 🧱 **Item Target: Block**
 * <p>
 * Implements the {@code ItemTarget} interface specifically for targeting a container block
 * (like a chest, dispenser, or furnace) in the {@code /item} command.
 * </p>
 * <p>
 * This class generates the command segment: {@code block <x> <y> <z>}
 * </p>
 */
public class ItemTargetBlock implements ItemTarget {

    /** The coordinates of the target container block. */
    private final BlockPos pos;

    /**
     * Private constructor to enforce use of the static factory method {@code create()}.
     * @param pos The internal {@code BlockPos} argument representing the block coordinates.
     */
    private ItemTargetBlock(BlockPos pos) {
        this.pos = pos;
    }

    /**
     * Factory method to create a new {@code ItemTargetBlock} instance.
     * @param pos The {@code BlockPos} object defining the coordinates of the target block.
     * @return A new instance of {@code ItemTargetBlock}.
     * @throws IllegalArgumentException if the provided {@code pos} object is null.
     */
    public static ItemTargetBlock create(BlockPos pos) {
        if (pos == null) {
            // CRITICAL ERROR CATCH: Ensure the BlockPos argument is not null.
            throw new IllegalArgumentException("The BlockPos argument cannot be null for ItemTargetBlock.");
        }
        // Using the private constructor
        return new ItemTargetBlock(pos);
    }

    /**
     * Generates the command segment required by the {@code /item} command for block targets.
     * @return The command string segment: {@code block <x> <y> <z>}
     */
    @Override
    public String getCommandString() {
        // The BlockPos object handles converting its internal data (coordinates) into the final string format.
        return "block " + pos.toString();
    }
}