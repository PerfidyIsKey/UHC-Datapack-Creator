package uhc.modules;

import uhc.arguments.entity.Entity;
import uhc.arguments.itemstack.SimpleItemStack;
import uhc.arguments.targetselector.TargetSelector;
import uhc.command.commands.ItemCommand;
import uhc.arguments.item.ItemTargetEntity;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.command.util.ItemSlot;
import uhc.resource.ItemId;
import uhc.core.MinecraftConstants;
import uhc.command.commands.Comment; // Import the Comment class

/**
 * 🗑️ **Clear Ender Chest Module**
 * <p>
 * Defines and registers the function necessary to forcefully clear the contents
 * of every player's Ender Chest, typically used at the start or end of a game
 * to ensure a clean slate.
 * </p>
 */
public class ClearEnderChestModule implements DatapackModule {

    /**
     * Creates and registers the {@code clear_enderchest} function to the custom namespace.
     * <p>
     * This function iterates through all 27 slots of the Ender Chest and replaces their
     * contents with air, effectively clearing them for all players.
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
            throw new IllegalArgumentException("The Datapack object cannot be null during module registration.");
        }
        if (customNamespaceName == null || customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("The custom namespace name cannot be null or blank.");
        }

        // --- 2. Get the required namespace ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);

        // Safety Check: Verify successful creation/retrieval of required namespace
        if (customNamespace == null) {
            throw new IllegalStateException("Failed to obtain the necessary custom namespace ('" + customNamespaceName + "'). Cannot register ClearEnderChest components.");
        }


        // --- 3. Create the Function component ---
        // Function path: data/{customNamespaceName}/function/util/clear_enderchest.mcfunction
        FunctionPath functionPath = FunctionPath.CLEAR_ENDERCHEST;
        Function currentFunction = new Function(functionPath);

        // Add the commands/lines to the function in sequence, using the Comment class for documentation.
        currentFunction.addLine(Comment.create("Clears all 27 slots of every player's Ender Chest."));
        currentFunction.addLine(Comment.create("Uses /item replace to substitute item stacks with air."));

        // Loop through all slots (0 to 26) defined by MinecraftConstants.CHEST_SLOTS (27)
        for (int i = 0; i < MinecraftConstants.CHEST_SLOTS; i++) {
            // Generates command: /item replace entity @a enderchest.<slot_number> with minecraft:air 1
            currentFunction.addLine(ItemCommand.create(ItemCommand.ItemAction.REPLACE_WITH,
                            ItemTargetEntity.create(Entity.ofSelector(TargetSelector.ALL_PLAYERS)))
                    .slot(ItemSlot.ENDERCHEST.withSlotNumber(i))
                    .replaceWith(SimpleItemStack.create(ItemId.AIR), 1));
        }

        // Register the function component with the custom namespace
        customNamespace.addComponent(currentFunction);
    }
}