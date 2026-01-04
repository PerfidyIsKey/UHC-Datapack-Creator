package uhc.functions.startup;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.arguments.item.slot.ItemSlot;
import uhc.command.commands.ClearCommand;
import uhc.command.commands.ItemCommand;
import uhc.command.Comment;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.data.nbt.item.components.CustomNameComponent;
import uhc.data.nbt.item.components.LoreComponent;
import uhc.data.nbt.item.components.PotionContentsComponent;
import uhc.functions.DatapackFunction;
import uhc.resource.effect.EffectId;
import uhc.resource.item.ItemId;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 🧪 **Startup Potions Function Component**
 * <p>
 * This class orchestrates the generation of the {@code start_potions.mcfunction} logic.
 * It is responsible for equipping administrative users with custom splash potions
 * configured with specific NBT data to act as match-control triggers.
 * </p>
 */
public class StartPotionsFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The descriptive header title utilized for the auto-generated file documentation. */
    private static final String FUNCTION_TITLE = "Startup: Administrator Control Potions";

    /** * The target entity for the item replacement commands, locked to the command sender. */
    private static final Entity TARGET = Entity.ofSelector(TargetSelector.SENDER);

    /** * The standard duration for potion effects applied to the item tooltips/NBT. */
    private static final int DEFAULT_DURATION = 200;

    /** * The standard amplifier for potion effects. */
    private static final int DEFAULT_AMPLIFIER = 0;

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Registers the potion equipment logic within the datapack infrastructure.
     * <p>
     * This method follows a strict fail-fast policy. If the {@link FunctionPath} is missing
     * or the namespace is invalid, it throws an immediate error to prevent corrupt datapack generation.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for component persistence (non-null).
     * @throws NullPointerException if {@code datapack} or {@code namespace} is null.
     * @throws RuntimeException     if logic assembly or path resolution fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        // Infrastructure Validation
        Objects.requireNonNull(datapack, "StartPotions Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "StartPotions Error: Target Namespace cannot be null.");

        // Resolve Path from Registry
        final FunctionPath path = Objects.requireNonNull(FunctionPath.START_POTIONS,
                "Registry Error: FunctionPath.START_POTIONS is not defined in the system.");

        final Function currentFunction = new Function(path);

        try {
            // A. Header and Initialization
            this.appendStandardHeader(currentFunction, FUNCTION_TITLE);
            currentFunction.addLine(Comment.create("Equips the admin with state-control splash potions."));

            // B. Clear Phase
            currentFunction.addLine(Comment.create("--- Preparation: Clearing Hotbar ---"));
            currentFunction.addLine(ClearCommand.create().targets(TARGET));
            currentFunction.addLine(Comment.create(" "));

            // C. Assembly Phase
            this.equipControlPotions(currentFunction);

            // D. Structural Cleanup
            currentFunction.addLine(Comment.create(" "));
            currentFunction.addLine(Comment.create("--- End of equipment sequence ---"));

            // Finalize Component
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // Rethrow with clear context to ensure developer visibility
            throw new RuntimeException("CRITICAL: Failed to assemble StartPotions at [" + path + "]. Details: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Assembly Logic ---

    /**
     * Aggregates all potion-giving commands into the provided function.
     * <p>
     * Categorizes potions into Mode Controls, Management, and Social tools
     * using {@link Comment} dividers for readability in the generated file.
     * </p>
     *
     * @param function The function component currently being assembled (non-null).
     * @throws NullPointerException if {@code function} is null.
     */
    private void equipControlPotions(Function function) {
        Objects.requireNonNull(function, "Assembly Error: Cannot equip potions to a null function.");

        // --- Category: Mode Controls ---
        function.addLine(Comment.create("Logic: Administrative Mode Switches"));
        function.addLine(this.buildPotionCommand(0, "808080", EffectId.SPEED,
                "Developer Mode", "Set operational mode to Developer Mode."));
        function.addLine(this.buildPotionCommand(5, "CC3333", EffectId.STRENGTH,
                "Survival Mode", "Set operational mode to Ready to Play."));
        function.addLine(Comment.create(" "));

        // --- Category: Player/Team Management ---
        function.addLine(Comment.create("Logic: Team and Player Distribution"));
        function.addLine(this.buildPotionCommand(1, "FF9933", EffectId.WEAKNESS,
                "Assign Teams", "Assign players to teams."));
        function.addLine(this.buildPotionCommand(4, "00CC66", EffectId.POISON,
                "Spread Players", "Spread players across the map."));
        function.addLine(Comment.create(" "));

        // --- Category: Social & Predictive Tools ---
        function.addLine(Comment.create("Logic: Social and Prediction Tools"));
        function.addLine(this.buildPotionCommand(2, "6633CC", EffectId.SLOW_FALLING,
                "Predictions", "Who will win this season?"));
        function.addLine(this.buildPotionCommand(3, "3399FF", EffectId.INVISIBILITY,
                "Into Calls", "Allow players to gather in their Discord channel."));
        function.addLine(Comment.create(" "));

        // --- Category: Match Activation ---
        function.addLine(Comment.create("Logic: Official Match Start"));
        function.addLine(this.buildPotionCommand(6, "00FF7F", EffectId.SLOWNESS,
                "Start Game", "Start the game. Good luck!"));
    }

    /**
     * Constructs a specific {@link ItemCommand} to replace a hotbar slot with a custom potion.
     * <p>
     * This method builds the intricate NBT structure for the splash potion, including
     * custom colors, effects, lore, and display names.
     * </p>
     *
     * @param slotNumber The hotbar slot index (0-8).
     * @param hexColor   The HEX string for the potion liquid color.
     * @param effect     The {@link EffectId} to represent the potion's visual/mechanical theme.
     * @param name       The custom display name of the item.
     * @param lore       The descriptive text for the item's tooltip.
     * @return A fully prepared {@link ItemCommand}.
     * @throws RuntimeException if any component of the potion builder fails.
     */
    private ItemCommand buildPotionCommand(int slotNumber, String hexColor, EffectId effect, String name, String lore) {
        try {
            // Build the NBT-wrapped ItemStack
            final ItemStack potion = ItemStack.create(ItemId.SPLASH_POTION)
                    .with(PotionContentsComponent.create()
                            .customColor(hexColor)
                            .addEffect(PotionContentsComponent.CustomEffect.create(effect, DEFAULT_DURATION, DEFAULT_AMPLIFIER)
                                    .particles(false)
                                    .icon(false)
                                    .ambient(false)))
                    .with(LoreComponent.create(TextComponent.text(lore)))
                    .with(CustomNameComponent.create(TextComponent.text(name)));

            // Return the execution command
            return ItemCommand.replaceEntityWith(TARGET, ItemSlot.HOTBAR.withSlotNumber(slotNumber), potion);

        } catch (Exception e) {
            // No-fallback: ensure the specific potion that failed is identified
            throw new RuntimeException("Potion Builder Failure: Could not construct admin potion [" + name +
                    "] for slot [" + slotNumber + "]. Reason: " + e.getMessage(), e);
        }
    }
}