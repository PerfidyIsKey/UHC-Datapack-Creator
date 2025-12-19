package uhc.functions;

import uhc.arguments.entity.Entity;
import uhc.arguments.itemstack.SimpleItemStack;
import uhc.arguments.entity.TargetSelector;
import uhc.command.commands.ItemCommand;
import uhc.arguments.item.ItemTargetEntity;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.arguments.slot.ItemSlot;
import uhc.resource.ItemId;
import uhc.core.MinecraftConstants;
import uhc.command.commands.Comment;

/**
 * 🗑️ **Clear Ender Chest Function**
 * <p>
 * Defines and registers the function necessary to forcefully clear the contents
 * of every player's Ender Chest, typically used at the start or end of a game
 * to ensure a clean slate.
 * </p>
 * This function utilizes a loop to replace every storage slot with air.
 */
public class ClearEnderChestFunction implements DatapackFunction { // Refactored class name

    /**
     * Creates and registers the {@code clear_enderchest} function to the custom namespace.
     * <p>
     * This function uses the {@code /item replace} command in a loop to iterate through
     * all 27 slots of the Ender Chest for all players ({@code @a}) and replaces their
     * contents with air, effectively clearing them.
     * </p>
     * @param datapack The main {@code Datapack} object used to retrieve or create namespaces.
     * @param customNamespaceName The name of the custom namespace (e.g., "uhc_core_pack") where the function is stored.
     * @throws IllegalArgumentException If {@code datapack} is null or {@code customNamespaceName} is invalid.
     * @throws IllegalStateException If the required {@code Namespace} cannot be obtained from the {@code Datapack} container.
     */
    @Override
    public void register(Datapack datapack, String customNamespaceName) {

        // --- 1. Parameter Validation ---
        if (datapack == null) {
            throw new IllegalArgumentException("The Datapack object cannot be null during function registration.");
        }
        if (customNamespaceName == null || customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("The custom namespace name cannot be null or blank.");
        }

        // --- 2. Get the required namespace ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);

        // Safety Check: Verify successful creation/retrieval of required namespace
        if (customNamespace == null) {
            // Updated error message to reflect the class name
            throw new IllegalStateException("Failed to obtain the necessary custom namespace ('" + customNamespaceName + "'). Cannot register ClearEnderChestFunction components.");
        }


        // --- 3. Create the Function component ---
        // Function path example: data/{customNamespaceName}/function/util/clear_enderchest.mcfunction
        FunctionPath functionPath = FunctionPath.CLEAR_ENDERCHEST;
        Function currentFunction = new Function(functionPath);

        // Add header comments to the generated function file.
        currentFunction.addLine(Comment.create("--- Clear Ender Chest Function ---"));
        currentFunction.addLine(Comment.create("Target: All online players (@a)"));
        currentFunction.addLine(Comment.create("Action: Replace contents of all 27 Ender Chest slots with minecraft:air."));

        // Loop through all 27 slots (0 to 26) of the Ender Chest.
        for (int i = 0; i < MinecraftConstants.CHEST_SLOTS; i++) {
            // Generates command: /item replace entity @a enderchest.<slot_number> with minecraft:air 1
            currentFunction.addLine(ItemCommand.create(ItemCommand.ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS)))
                    // Slot is set to "enderchest.i"
                    .slot(ItemSlot.ENDERCHEST.withSlotNumber(i))
                    // Item is replaced with 1 count of minecraft:air
                    .replaceWith(SimpleItemStack.create(ItemId.AIR), 1));
        }

        currentFunction.addLine(Comment.create("--- Ender Chest clearing complete ---"));

        // Register the function component with the custom namespace
        customNamespace.addComponent(currentFunction);
    }
}