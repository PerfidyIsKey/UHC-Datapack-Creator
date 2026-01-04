package uhc.functions.util;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.command.commands.ItemCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.arguments.item.slot.ItemSlot;
import uhc.functions.DatapackFunction;
import uhc.resource.item.ItemId;
import uhc.core.MinecraftConstants;
import uhc.command.commands.Comment;

import java.util.Objects;

/**
 * 🗑️ **Clear Ender Chest Function Module**
 * <p>
 * This class handles the automated generation of an {@code .mcfunction} file
 * designed to purge the Ender Chest contents of all online players.
 * </p>
 * <p>
 * <b>Operational Logic:</b>
 * It iterates through the total number of container slots (defined by {@link MinecraftConstants#CHEST_SLOTS})
 * and generates a discrete {@code /item replace} command for every index, ensuring
 * no items persist between game sessions.
 * </p>
 */
public class ClearEnderChestFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The header title used for generating the standardized documentation block. */
    private static final String HEADER_TITLE = "Utility: Clear All Ender Chests";

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Executes the registration of the Ender Chest clearing logic into the Datapack.
     * <p>
     * <b>Strict Validation:</b>
     * This method enforces a "Fail-Fast" architecture. If any required resource
     * (Datapack, Namespace, or internal Constants) is missing, it will throw a
     * descriptive exception rather than generating a malformed function.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException if {@code datapack} or {@code namespace} is null.
     * @throws RuntimeException     if slot counts are invalid or assembly fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Infrastructure Validation ---
        Objects.requireNonNull(datapack, "ClearEnderChest Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "ClearEnderChest Error: Target Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.CLEAR_ENDERCHEST,
                "Registry Error: FunctionPath.CLEAR_ENDERCHEST is not defined in the system.");

        final Function currentFunction = new Function(path);

        // --- 2. Pre-Assembly Range Validation ---
        if (MinecraftConstants.CHEST_SLOTS <= 0) {
            throw new RuntimeException("Configuration Error: MinecraftConstants.CHEST_SLOTS must be greater than 0.");
        }

        // --- 3. Command Assembly ---
        try {
            this.appendStandardHeader(currentFunction, HEADER_TITLE);

            // Re-using the same Entity object for all iterations to improve memory efficiency
            final Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

            currentFunction.addLine(Comment.create("Target: All online players (@a)"));
            currentFunction.addLine(Comment.create("Action: Replacing " + MinecraftConstants.CHEST_SLOTS + " slots with air."));

            // Perform the iterative command generation
            for (int i = 0; i < MinecraftConstants.CHEST_SLOTS; i++) {
                currentFunction.addLine(this.buildClearSlotCommand(allPlayers, i));
            }

            this.addSafeLine(currentFunction, "");
            this.addSafeLine(currentFunction, "--- Ender Chest clearing complete ---");

            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // No-fallback: We prefer a build-time crash over a broken in-game function
            throw new RuntimeException("CRITICAL: Failed to assemble ClearEnderChest at [" + path + "]. " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Builds a single {@link ItemCommand} for a specific Ender Chest slot.
     * <p>
     * Encapsulates the logic for replacing a slot's content with {@code minecraft:air}.
     * </p>
     *
     * @param target    The entity target (all players).
     * @param slotIndex The index of the slot to clear (0-based).
     * @return A constructed {@link ItemCommand} object.
     * @throws NullPointerException if target is null.
     */
    private ItemCommand buildClearSlotCommand(Entity target, int slotIndex) {
        return ItemCommand.replaceEntityWith(
                Objects.requireNonNull(target, "Command Builder Error: Target entity cannot be null."),
                ItemSlot.ENDERCHEST.withSlotNumber(slotIndex),
                ItemStack.create(ItemId.AIR),
                1
        );
    }
}