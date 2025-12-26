package uhc.functions;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.command.commands.EffectCommand;
import uhc.command.commands.ItemCommand;
import uhc.command.commands.Comment; // <-- NEW: Import the Comment command class
import uhc.command.commands.item.ItemTargetEntity;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.arguments.item.slot.ItemSlot;
import uhc.data.nbt.item.components.*;
import uhc.resource.effect.EffectId;
import uhc.resource.enchantment.EnchantmentId;
import uhc.resource.item.ItemId;
import uhc.resource.attribute.AttributeDisplayType;
import uhc.resource.attribute.AttributeModifierId;
import uhc.resource.attribute.AttributeId;
import uhc.resource.item.EquipmentSlot;
import uhc.text.color.HexColor;
import uhc.resource.color.TextColor;
import uhc.text.TextComponent;

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
        currentFunction.addLine(ItemCommand.create(ItemCommand.ItemAction.REPLACE_WITH,
                        ItemTargetEntity.create(Entity.ofSelector(TargetSelector.SENDER)))
                .slot(ItemSlot.MAINHAND)
                .replaceWith(
                        ItemStack.create(ItemId.TRIDENT)
                                .with(CustomNameComponent.create(
                                        TextComponent.text("aA").color(TextColor.WHITE).obfuscated(true)
                                                .append(TextComponent.text("The").color(HexColor.create("#8C3CC1")).bold(true))
                                                .append(TextComponent.text(" Impaler ").color(HexColor.create("#E280FF")).bold(true))
                                                .append(TextComponent.text("Aa").color(TextColor.WHITE).obfuscated(true))))
                                .with(LoreComponent.create(TextComponent.text("This holy weapon impales anything it touches")))
                                .with(DamageComponent.create(0))
                                .with(EnchantmentsComponent.create()
                                        .add(EnchantmentId.FIRE_ASPECT, 255)
                                        .add(EnchantmentId.SHARPNESS, 255)
                                        .add(EnchantmentId.IMPALING, 255)
                                        .add(EnchantmentId.LOYALTY, 255)
                                        .add(EnchantmentId.EFFICIENCY, 255))
                                .with(AttributeModifiersComponent.create()
                                        .add(AttributeModifiersComponent.Entry.create(
                                                        AttributeId.ARMOR,
                                                        AttributeModifierId.BASE_ARMOR,
                                                        1000.0,
                                                        uhc.resource.attribute.AttributeOperation.ADD_VALUE)
                                                .slot(EquipmentSlot.ARMOR)
                                                .display(AttributeDisplayType.HIDDEN))))); // Item Component: Unbreakable Flag


        // --- 4. Register Component ---
        currentFunction.addLine(Comment.create(""));
        currentFunction.addLine(Comment.create("--- End of God Mode Function ---"));
        customNamespace.addComponent(currentFunction);
    }
}