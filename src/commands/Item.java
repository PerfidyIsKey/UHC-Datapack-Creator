package commands;

import uhc.arguments.item.ItemStack;
import uhc.command.commands.ItemCommand;
import uhc.arguments.slot.ItemSlot;
import uhc.arguments.slot.SpecificItemSlot;
import uhc.arguments.item.ItemTarget;

/**
 * Fluent builder for the Minecraft /item command.
 * The structure is: item <action> <target> <slot> ...
 * * Usage example: Item.target(entity).slot(slot).replaceWith(item, 1).build();
 */
public class Item {
    // Mandatory shared arguments
    private final ItemTarget target;
    private final ItemCommand.ItemAction action;
    private SpecificItemSlot targetSlot;

    // Fields for MODIFY
    private String modifyModifier;

    // Fields for REPLACE WITH
    private ItemStack replaceItem;
    private Integer replaceCount;

    // Fields for REPLACE FROM
    private ItemTarget source;
    private SpecificItemSlot sourceSlot;
    private String replaceFromModifier;

    // Private constructor enforces starting with the static factory method.
    private Item(ItemCommand.ItemAction action, ItemTarget target) {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null.");
        }
        this.action = action;
        this.target = target;
    }

    // --- Factory Start ---

    /**
     * Starts the item command chain by specifying the destination target (block or entity).
     * @param target The destination target.
     */
    public static Item create(ItemCommand.ItemAction action, ItemTarget target) {
        return new Item(action, target);
    }

    // --- Chain 1: Set Target Slot ---

    /**
     * Specifies the inventory slot on the target to be modified/replaced.
     * Must be called before any action (modify/replace).
     * @param targetSlot The destination slot, which can be a base slot or a numbered slot.
     */
    // Change the parameter type to the new interface
    public Item slot(SpecificItemSlot targetSlot) {
        if (targetSlot == null) {
            throw new IllegalArgumentException("Target slot cannot be null.");
        }
        this.targetSlot = targetSlot;
        return this;
    }

    // --- Chain 2a: item modify ---

    /**
     * Specifies the 'item modify' action.
     * @param modifier The item modifier (resource location or SNBT).
     */
    public Item modify(String modifier) {
        if (this.action != ItemCommand.ItemAction.MODIFY) {
            throw new IllegalStateException("Cannot call modify() if the command was not initialized with action ItemAction.MODIFY.");
        }

        requireSlotSet();
        this.modifyModifier = modifier;
        return this;
    }

    // --- Chain 2b: item replace ... with ---

    /**
     * Specifies the 'item replace ... with' action with an item stack.
     * @param item The item stack to place.
     * @param count The optional count (1-99). Null to omit.
     */
    public Item replaceWith(ItemStack item, Integer count) {
        if (this.action != ItemCommand.ItemAction.REPLACE_WITH) {
            throw new IllegalStateException("Cannot call replaceWith() if the command was not initialized with action ItemAction.REPLACE_WITH.");
        }

        requireSlotSet();
        if (item == null) {
            throw new IllegalArgumentException("'replaceWith' item cannot be null.");
        }
        if (count != null && (count < 1 || count > 99)) {
            throw new IllegalArgumentException("Count must be between 1 and 99.");
        }
        this.replaceItem = item;
        this.replaceCount = count;
        return this;
    }

    // Auxiliary method for count=1 (no count argument)
    public Item replaceWith(ItemStack item) {
        return replaceWith(item, null);
    }

    // --- Chain 2c: item replace ... from ---

    /**
     * Specifies the 'item replace ... from' action.
     * @param source The source block or entity to copy items from.
     * @param sourceSlot The source slot.
     * @param modifier The optional item modifier (resource location or SNBT). Null to omit.
     */
    public Item replaceFrom(ItemTarget source, ItemSlot sourceSlot, String modifier) {
        if (this.action != ItemCommand.ItemAction.REPLACE_FROM) {
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

    // Auxiliary method for no modifier
    public Item replaceFrom(ItemTarget source, ItemSlot sourceSlot) {
        return replaceFrom(source, sourceSlot, null);
    }

    // --- Validation Helper ---
    private void requireSlotSet() {
        if (targetSlot == null) {
            throw new IllegalStateException("The target slot must be set using .slot() before specifying an action.");
        }
    }

    // --- Finalizer ---

    /**
     * Finalizes the builder and creates the command string.
     */
    public String build() {
        if (action == null) {
            throw new IllegalStateException("An action (modify, replaceWith, or replaceFrom) must be specified.");
        }

        StringBuilder sb = new StringBuilder("item ");

        switch (action) {
            case MODIFY:
                // item modify <target> <slot> <modifier>
                sb.append("modify ").append(target.getCommandString())
                        .append(" ").append(targetSlot.getCommandString())
                        .append(" ").append(modifyModifier);
                break;

            case REPLACE_WITH:
                // item replace <target> <slot> with <item> [<count>]
                sb.append("replace ").append(target.getCommandString())
                        .append(" ").append(targetSlot.getCommandString())
                        .append(" with ").append(replaceItem.build());

                if (replaceCount != null) {
                    sb.append(" ").append(replaceCount);
                }
                break;

            case REPLACE_FROM:
                // item replace <target> <slot> from <source> <sourceSlot> [<modifier>]
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
}