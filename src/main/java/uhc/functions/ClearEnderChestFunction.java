package uhc.functions;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.command.commands.ItemCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.arguments.item.slot.ItemSlot;
import uhc.resource.item.ItemId;
import uhc.core.MinecraftConstants;
import uhc.command.commands.Comment;

import java.util.Objects;

/**
 * 🗑️ **Clear Ender Chest Function**
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
     * @param namespace The target {@link Namespace} where the function component will be stored (non-null).
     * @throws NullPointerException if {@code datapack} or {@code namespace} is null.
     * @throws RuntimeException     if any error occurs during the command assembly or slot iteration.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Parameter Validation ---
        // Ensuring parameters meet the contract requirements before proceeding.
        Objects.requireNonNull(datapack, "ClearEnderChest Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "ClearEnderChest Error: Target Namespace cannot be null.");

        // --- 2. Function Component Initialization ---
        // Acquiring the standardized path for this utility function.
        final FunctionPath path = Objects.requireNonNull(FunctionPath.CLEAR_ENDERCHEST,
                "Registry Error: FunctionPath.CLEAR_ENDERCHEST is not defined in the system.");

        final Function currentFunction = new Function(path);

        // --- 3. Logic Assembly & Loop Execution ---
        try {
            // Utilize the inherited default method from DatapackFunction for a consistent header style.
            this.appendStandardHeader(currentFunction, "Utility: Clear All Ender Chests");

            // Document the intent within the function file for datapack transparency.
            currentFunction.addLine(Comment.create("Target: All online players (@a)"));
            currentFunction.addLine(Comment.create("Action: Replace 27 slots with air."));

            // Perform the iterative command generation.
            // We use the constant CHEST_SLOTS to ensure compatibility with Minecraft's container size.
            for (int i = 0; i < MinecraftConstants.CHEST_SLOTS; i++) {

                // Construct the command: /item replace entity @a enderchest.<i> with minecraft:air 1
                // We add the command object directly to the function's list.
                currentFunction.addLine(this.buildClearSlotCommand(i));
            }

            // Append footer for file readability.
            currentFunction.addLine(Comment.create(" "));
            currentFunction.addLine(Comment.create("--- Ender Chest clearing complete ---"));

            // Registration of the finalized component into the provided namespace.
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // Provide a clear error message that differentiates between registry issues and assembly issues.
            throw new RuntimeException("CRITICAL: Failed to assemble ClearEnderChest commands. " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Builds a single {@link ItemCommand} for a specific Ender Chest slot.
     * <p>
     * This method encapsulates the replacement logic for index {@code i}, replacing
     * the content with {@code minecraft:air}.
     * </p>
     *
     * @param slotIndex The index of the slot to clear (0-26).
     * @return A constructed {@link ItemCommand} object ready for line-entry.
     * @throws RuntimeException if the slot assignment or item creation fails.
     */
    private ItemCommand buildClearSlotCommand(int slotIndex) {
        return ItemCommand.replaceEntityWith(Entity.ofSelector(TargetSelector.ALL_PLAYERS),
                ItemSlot.ENDERCHEST.withSlotNumber(slotIndex),
                ItemStack.create(ItemId.AIR),
                1);
    }
}