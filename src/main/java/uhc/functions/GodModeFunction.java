package uhc.functions;

import uhc.arguments.entity.Entity;
import uhc.arguments.itemstack.ComponentItemStack;
import uhc.arguments.itemstack.components.*;
import uhc.arguments.itemstack.components.attributes.AttributeDisplayTag;
import uhc.arguments.itemstack.components.attributes.AttributeModifierEntry;
import uhc.arguments.entity.TargetSelector;
import uhc.attribute.AttributeId;
import uhc.attribute.AttributeOperation;
import uhc.attribute.AttributeSlot;
import uhc.attribute.AttributeTooltipDisplayType;
import uhc.command.commands.EffectCommand;
import uhc.command.commands.ItemCommand;
import uhc.command.commands.Comment; // <-- NEW: Import the Comment command class
import uhc.arguments.item.ItemTargetEntity;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.command.util.ItemSlot;
import uhc.resource.EffectId;
import uhc.resource.EnchantmentId;
import uhc.resource.ItemId;
import uhc.text.HexColor;
import uhc.text.TextColor;
import uhc.text.TextComponent;

import java.util.List;
import java.util.Map;

/**
 * 🛡️ **God Mode Module**
 * <p>
 * Defines and registers the Minecraft function required to instantly grant a player
 * "God Mode" status. This typically involves applying extremely powerful effects
 * and replacing their primary weapon with a heavily customized item.
 * </p>
 */
public class GodModeFunction implements DatapackFunction {

    /**
     * Creates and registers the {@code god_mode} function to the custom namespace.
     * <p>
     * This function performs two main actions on the command sender (@s):
     * <ul>
     * <li>Gives permanent Resistance V (Amplifier 4).</li>
     * <li>Replaces the item in the main hand with an unbreakable Trident ("The Impaler")
     * that has max-level enchantments and massive attribute modifiers.</li>
     * </ul>
     * </p>
     *
     * @param datapack            The main {@code Datapack} object used to retrieve or create namespaces.
     * @param customNamespaceName The name of the custom namespace (e.g., "uhc_core_pack") where the function is stored.
     * @throws IllegalArgumentException If {@code datapack} is null or {@code customNamespaceName} is invalid.
     * @throws IllegalStateException    If the required {@code Namespace} cannot be obtained from the {@code Datapack} container.
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

        // --- 2. Namespace Retrieval ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);

        // Safety Check: Verify successful creation/retrieval of required namespace
        if (customNamespace == null) {
            throw new IllegalStateException("Failed to obtain the necessary custom namespace ('" + customNamespaceName + "'). Cannot register GodModeModule components.");
        }


        // --- 3. Create the Function component and add commands ---
        // Function path is resolved by the Datapack core, typically resulting in
        // data/{customNamespaceName}/function/util/god_mode.mcfunction
        FunctionPath functionPath = FunctionPath.GOD_MODE;
        Function currentFunction = new Function(functionPath);

        // Add a header comment to the function file for clarity
        currentFunction.addLine(Comment.create("--- God Mode Activation Function ---"));
        currentFunction.addLine(Comment.create("Targets: Command Sender (@s)"));
        currentFunction.addLine(Comment.create(""));

        // SECTION 1: Apply Permanent God-Tier Effect
        currentFunction.addLine(Comment.create("SECTION 1: Apply Permanent Resistance V"));
        currentFunction.addLine(Comment.create("Effect: Resistance V (Amplifier 4) for 99999 seconds, particles hidden."));
        currentFunction.addLine(EffectCommand.create(EffectCommand.EffectAction.GIVE)
                .targets(Entity.ofSelector(TargetSelector.SENDER))
                .effect(EffectId.RESISTANCE)
                .seconds(99999)
                .amplifier(4)
                .hideParticles(true));

        currentFunction.addLine(Comment.create(""));

        // SECTION 2: Equip Custom Weapon
        currentFunction.addLine(Comment.create("SECTION 2: Equip Custom Weapon (The Impaler Trident)"));
        currentFunction.addLine(Comment.create("Replaces the item in the main hand with a custom, unbreakable Trident."));
        currentFunction.addLine(ItemCommand.create(
                        ItemCommand.ItemAction.REPLACE_WITH,
                        ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                .slot(ItemSlot.MAINHAND)
                .replaceWith(
                        ComponentItemStack.create(ItemId.TRIDENT)
                                .addComponent(CustomNameComponent.create(   // Item Component: Custom Name (Decorative Text Formatting)
                                        TextComponent.array(List.of(
                                                TextComponent.simple("aA").color(TextColor.WHITE).obfuscated(true),
                                                TextComponent.simple("The").color(HexColor.create("#8C3CC1")).bold(true),
                                                TextComponent.simple(" Impaler ").color(HexColor.create("#E280FF")).bold(true),
                                                TextComponent.simple("Aa").color(TextColor.WHITE).obfuscated(true)))))
                                .addComponent(LoreComponent.create("This holy weapon impales anything it touches")) // Item Component: Lore/Description
                                .addComponent(DamageComponent.create(0))    // Item Component: Damage (0 = Unbreakable/Max Durability)
                                .addComponent(EnchantmentsComponent.create(Map.of(  // Item Component: Max-Level Enchantments (255)
                                        EnchantmentId.FIRE_ASPECT, 255,
                                        EnchantmentId.SHARPNESS, 255,
                                        EnchantmentId.IMPALING, 255,
                                        EnchantmentId.LOYALTY, 255,
                                        EnchantmentId.EFFICIENCY, 255)))
                                .addComponent(AttributeModifiersComponent.create(List.of(   // Item Component: Attribute Modifiers (1000.0 added to Armor and Attack Damage)
                                        AttributeModifierEntry.create(
                                                AttributeId.ARMOR,
                                                AttributeId.ARMOR,
                                                1000.0,
                                                AttributeOperation.ADD_VALUE,
                                                AttributeSlot.ARMOR,
                                                AttributeDisplayTag.create(AttributeTooltipDisplayType.HIDDEN)),
                                        AttributeModifierEntry.create(
                                                AttributeId.ATTACK_DAMAGE,
                                                AttributeId.ATTACK_DAMAGE,
                                                1000.0,
                                                AttributeOperation.ADD_VALUE,
                                                AttributeSlot.MAINHAND,
                                                AttributeDisplayTag.create(AttributeTooltipDisplayType.HIDDEN)))))
                                .addComponent(UnbreakableComponent.create()))); // Item Component: Unbreakable Flag


        // --- 4. Register Component ---
        currentFunction.addLine(Comment.create(""));
        currentFunction.addLine(Comment.create("--- End of God Mode Function ---"));
        customNamespace.addComponent(currentFunction);
    }
}