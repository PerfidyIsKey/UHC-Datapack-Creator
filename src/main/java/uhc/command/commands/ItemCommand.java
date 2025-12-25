package uhc.command.commands;

import uhc.arguments.item.ItemTarget;
import uhc.arguments.item.ItemStack;
import uhc.arguments.slot.ItemSlot; // Note: Used for sourceSlot argument in replaceFrom
import uhc.arguments.slot.SpecificItemSlot; // Note: Used for targetSlot argument
import uhc.command.MinecraftCommand;

/**
 * 🔨 **Fluent Builder for the Minecraft /item Command**
 * <p>
 * This class constructs the complex {@code /item} command in a type-safe, fluent manner.
 * The core command structure is: {@code /item <action> <target> <slot> ...}
 * </p>
 * <p>
 * Usage example for replacing an item:
 * {@code ItemCommand.create(ItemAction.REPLACE_WITH, entityTarget).slot(ItemSlot.MAINHAND).replaceWith(diamond, 1).generate();}
 * </p>
 */
public class ItemCommand implements MinecraftCommand {

    // --- Mandatory Shared Arguments ---

    /** The destination target (entity or block location) where the operation occurs. */
    private final ItemTarget target;
    /** The overall action of the command (modify, replace with, or replace from). */
    private final ItemAction action;
    /** The specific inventory slot on the target to be modified/replaced. This field is required for all actions. */
    private SpecificItemSlot targetSlot;

    // --- Fields for Specific Actions ---

    // Fields for MODIFY: item modify <target> <slot> <modifier>
    private String modifyModifier;

    // Fields for REPLACE WITH: item replace <target> <slot> with <item> [<count>]
    private ItemStack replaceItem;
    private Integer replaceCount;

    // Fields for REPLACE FROM: item replace <target> <slot> from <source> <sourceSlot> [<modifier>]
    private ItemTarget source;
    private ItemSlot sourceSlot; // Retained ItemSlot type as per original code for functionality integrity.
    private String replaceFromModifier;

    // Private constructor enforces starting with the static factory method.
    private ItemCommand(ItemAction action, ItemTarget target) {
        if (target == null) {
            // CRITICAL ERROR CATCH: Ensure the initial target is valid.
            throw new IllegalArgumentException("Target cannot be null when initializing ItemCommand.");
        }
        if (action == null) {
            // CRITICAL ERROR CATCH: Ensure the initial action is valid.
            throw new IllegalArgumentException("Action cannot be null when initializing ItemCommand.");
        }
        this.action = action;
        this.target = target;
    }

    // --- Factory Start ---

    /**
     * Initializes the builder by specifying the desired action and the destination target
     * (the entity or block location where the item operation will occur).
     * @param action The intended action (MODIFY, REPLACE_WITH, or REPLACE_FROM).
     * @param target The destination block or entity target.
     * @return A new ItemCommand builder instance.
     */
    public static ItemCommand create(ItemAction action, ItemTarget target) {
        return new ItemCommand(action, target);
    }

    // --- Chain 1: Set Target Slot ---

    /**
     * Specifies the inventory slot on the target to be modified or replaced.
     * This method must be called before calling the action-specific methods (e.g., {@code modify()}, {@code replaceWith()}, etc.).
     * @param targetSlot The destination slot (must be a specific slot or a slot range defined by {@code SpecificItemSlot}).
     * @return The current builder instance.
     * @throws IllegalArgumentException if the provided target slot is null.
     */
    public ItemCommand slot(SpecificItemSlot targetSlot) {
        if (targetSlot == null) {
            throw new IllegalArgumentException("Target slot cannot be null.");
        }
        this.targetSlot = targetSlot;
        return this;
    }

    // --- Chain 2a: item modify ---

    /**
     * Completes the 'item modify' action by specifying the item modifier.
     * @param modifier The item modifier (a data pack resource location or SNBT string).
     * @return The current builder instance.
     * @throws IllegalStateException if the command was not initialized with {@code ItemAction.MODIFY} or if the target slot was not set.
     */
    public ItemCommand modify(String modifier) {
        if (this.action != ItemAction.MODIFY) {
            throw new IllegalStateException("Cannot call modify() if the command was not initialized with action ItemAction.MODIFY.");
        }

        requireSlotSet();
        this.modifyModifier = modifier;
        return this;
    }

    // --- Chain 2b: item replace ... with ---

    /**
     * Completes the 'item replace ... with' action with an item stack and an optional count.
     * @param item The item stack to place.
     * @param count The optional count (1 to 99). Use {@code null} to omit the count argument (defaulting to 1).
     * @return The current builder instance.
     * @throws IllegalStateException if the command was not initialized with {@code ItemAction.REPLACE_WITH} or if the target slot was not set.
     * @throws IllegalArgumentException if the item is null or the count is outside the valid Minecraft stack range (1-99).
     */
    public ItemCommand replaceWith(ItemStack item, Integer count) {
        if (this.action != ItemAction.REPLACE_WITH) {
            throw new IllegalStateException("Cannot call replaceWith() if the command was not initialized with action ItemAction.REPLACE_WITH.");
        }

        requireSlotSet();
        if (item == null) {
            throw new IllegalArgumentException("'replaceWith' item cannot be null.");
        }
        // Minecraft stack count limit is 1 to 99
        if (count != null && (count < 1 || count > 99)) {
            throw new IllegalArgumentException("Count must be between 1 and 99.");
        }
        this.replaceItem = item;
        this.replaceCount = count;
        return this;
    }

    /**
     * Auxiliary method for 'item replace ... with' when count is defaulted to 1 (omits the count argument).
     * @param item The item stack to place.
     * @return The current builder instance.
     */
    public ItemCommand replaceWith(ItemStack item) {
        return replaceWith(item, null);
    }

    // --- Chain 2c: item replace ... from ---

    /**
     * Completes the 'item replace ... from' action by specifying the source item location.
     * @param source The source block or entity target to copy items from.
     * @param sourceSlot The source slot (e.g., {@code ItemSlot.CHEST}).
     * @param modifier The optional item modifier (resource location or SNBT). Use {@code null} to omit.
     * @return The current builder instance.
     * @throws IllegalStateException if the command was not initialized with {@code ItemAction.REPLACE_FROM} or if the target slot was not set.
     * @throws IllegalArgumentException if the source or sourceSlot are null.
     */
    public ItemCommand replaceFrom(ItemTarget source, ItemSlot sourceSlot, String modifier) {
        if (this.action != ItemAction.REPLACE_FROM) {
            throw new IllegalStateException("Cannot call replaceFrom() if the command was not initialized with action ItemAction.REPLACE_FROM.");
        }

        requireSlotSet();
        if (source == null || sourceSlot == null) {
            throw new IllegalArgumentException("Source and sourceSlot cannot be null for 'replaceFrom'.");
        }
        this.source = source;
        this.sourceSlot = sourceSlot;
        this.replaceFromModifier = modifier;
        return this;
    }

    /**
     * Auxiliary method for 'item replace ... from' when no modifier is specified.
     * @param source The source block or entity target.
     * @param sourceSlot The source slot.
     * @return The current builder instance.
     */
    public ItemCommand replaceFrom(ItemTarget source, ItemSlot sourceSlot) {
        return replaceFrom(source, sourceSlot, null);
    }

    // --- Validation Helper ---

    /**
     * Internal check to ensure the required destination slot has been specified.
     * @throws IllegalStateException if {@code targetSlot} is null.
     */
    private void requireSlotSet() {
        if (targetSlot == null) {
            throw new IllegalStateException("The target slot must be set using .slot() before specifying an action.");
        }
    }

    /**
     * Internal check to ensure all necessary fields for the chosen action are set before generating the command.
     * @throws IllegalStateException if required fields are missing for the current action.
     */
    private void validateActionFieldsSet() {
        switch (action) {
            case MODIFY:
                if (modifyModifier == null || modifyModifier.isBlank()) {
                    throw new IllegalStateException("Missing required 'modifier' for ItemAction.MODIFY.");
                }
                break;
            case REPLACE_WITH:
                if (replaceItem == null) {
                    throw new IllegalStateException("Missing required 'replaceItem' for ItemAction.REPLACE_WITH.");
                }
                // replaceCount is optional (can be null)
                break;
            case REPLACE_FROM:
                if (source == null || sourceSlot == null) {
                    throw new IllegalStateException("Missing required 'source' and/or 'sourceSlot' for ItemAction.REPLACE_FROM.");
                }
                // replaceFromModifier is optional (can be null or blank)
                break;
        }
    }

    // --- Finalizer ---

    /**
     * Finalizes the builder, validates all required arguments for the specified action, and creates the final command string.
     * @return The complete Minecraft {@code /item} command string.
     * @throws IllegalStateException if any mandatory argument (slot, item, modifier, source, etc.) is missing.
     */
    @Override
    public String generate() {
        // 1. Check if the destination slot is set.
        requireSlotSet();

        // 2. Check if all fields required by the specific action are set.
        validateActionFieldsSet();

        StringBuilder sb = new StringBuilder("item ");

        switch (action) {
            case MODIFY:
                // Structure: item modify <target> <slot> <modifier>
                sb.append("modify ").append(target.getCommandString())
                        .append(" ").append(targetSlot.getCommandString())
                        .append(" ").append(modifyModifier);
                break;

            case REPLACE_WITH:
                // Structure: item replace <target> <slot> with <item> [<count>]
                sb.append("replace ").append(target.getCommandString())
                        .append(" ").append(targetSlot.getCommandString())
                        .append(" with ").append(replaceItem.build());

                if (replaceCount != null) {
                    sb.append(" ").append(replaceCount);
                }
                break;

            case REPLACE_FROM:
                // Structure: item replace <target> <slot> from <source> <sourceSlot> [<modifier>]
                sb.append("replace ").append(target.getCommandString())
                        .append(" ").append(targetSlot.getCommandString())
                        .append(" from ").append(source.getCommandString())
                        .append(" ").append(sourceSlot.getCommandString());

                if (replaceFromModifier != null && !replaceFromModifier.trim().isEmpty()) {
                    sb.append(" ").append(replaceFromModifier);
                }
                break;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        // Ensures the command is generated if toString() is called (e.g., for logging)
        return generate();
    }

    /**
     * ⚙️ **Item Command Actions**
     * <p>
     * Defines the three main actions available in the Minecraft {@code /item} command syntax.
     * The chosen action determines the structure and required arguments for the rest of the command.
     * </p>
     */
    public enum ItemAction {
        /**
         * Corresponds to {@code /item modify ...}
         * <p>
         * Used to apply a pre-defined item modifier to the item stack in the target slot.
         * </p>
         */
        MODIFY,

        /**
         * Corresponds to {@code /item replace ... with ...}
         * <p>
         * Used to replace the item stack in the target slot with a specified new item stack.
         * </p>
         */
        REPLACE_WITH,

        /**
         * Corresponds to {@code /item replace ... from ...}
         * <p>
         * Used to replace the item stack in the target slot with an item stack copied from a source slot.
         * </p>
         */
        REPLACE_FROM;
    }
}

