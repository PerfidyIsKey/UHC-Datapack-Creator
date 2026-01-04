package uhc.functions.fun;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.command.commands.EffectCommand;
import uhc.command.commands.ItemCommand;
import uhc.command.commands.Comment;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.arguments.item.slot.ItemSlot;
import uhc.data.nbt.item.components.*;
import uhc.functions.DatapackFunction;
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

import java.util.Objects;

/**
 * 🛡️ **God Mode Module**
 * <p>
 * This class orchestrates the generation of a specialized Minecraft function designed
 * to grant a player near-invincibility and legendary equipment.
 * </p>
 * <p>
 * <b>Functionality:</b>
 * <ul>
 * <li>Grants Resistance V (99.9% damage reduction) permanently.</li>
 * <li>Equips "The Impaler", a Trident with 255-level enchantments and +1000 Armor.</li>
 * </ul>
 * </p>
 */
public class GodModeFunction implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for registering the God Mode functional component.
     * <p>
     * This method follows a strict multi-stage assembly process:
     * 1. <b>Validation:</b> Ensures the Datapack environment and Namespace are healthy.
     * 2. <b>Header:</b> Injects the standardized auto-generation disclaimer.
     * 3. <b>Status Effects:</b> Injects the Resistance command targeting the sender.
     * 4. <b>Equipment:</b> Constructs the complex Trident NBT and injects the replace command.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException if {@code datapack}, {@code namespace}, or the internal
     * {@link FunctionPath} is null.
     * @throws RuntimeException     if any error occurs during command building or NBT nesting.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Environment & Parameter Validation ---
        Objects.requireNonNull(datapack, "GodMode Error: Datapack environment is null.");
        Objects.requireNonNull(namespace, "GodMode Error: Target Namespace is null.");

        // Fetching path from registry; fail immediately if the constant is missing
        final FunctionPath path = Objects.requireNonNull(FunctionPath.GOD_MODE,
                "Registry Error: FunctionPath.GOD_MODE constant is not defined.");

        final Function currentFunction = new Function(path);

        // --- 2. Sequence Assembly ---
        try {
            // Inherited method from DatapackFunction interface
            this.appendStandardHeader(currentFunction, "Module: God Mode Activation");

            // SECTION 1: Status Effects
            currentFunction.addLine(Comment.create("SECTION 1: Permanent Resistance V"));
            currentFunction.addLine(EffectCommand.giveInfinite(Entity.ofSelector(TargetSelector.SENDER),
                    EffectId.RESISTANCE,
                    4,
                    true));

            currentFunction.addLine(Comment.create(" "));

            // SECTION 2: Equipment Setup
            currentFunction.addLine(Comment.create("SECTION 2: Equip 'The Impaler' Trident"));

            // Build and add the command object directly
            currentFunction.addLine(this.buildWeaponCommand());

            currentFunction.addLine(Comment.create(" "));
            currentFunction.addLine(Comment.create("--- God Mode successfully initialized ---"));

            // Commit the function to the namespace
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // Fail-fast with high-level context to aid in debugging assembly logic
            throw new RuntimeException("CRITICAL: Failed to assemble GodModeModule at "
                    + path.toString() + ". Details: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Constructs the complex {@link ItemCommand} for "The Impaler" weapon.
     * <p>
     * <b>Item Specs:</b>
     * <ul>
     * <li><b>Enchantments:</b> Level 255 Fire Aspect, Sharpness, Impaling, Loyalty, Efficiency.</li>
     * <li><b>Attributes:</b> +1000.0 Base Armor (Hidden).</li>
     * <li><b>Visuals:</b> Obfuscated name prefix/suffix with a Hex-colored center.</li>
     * </ul>
     * </p>
     *
     * @return A fully configured {@link ItemCommand} object.
     * @throws NullPointerException if any resource IDs (ItemId, EnchantmentId) are missing.
     * @throws RuntimeException     if NBT component nesting fails.
     */
    private ItemCommand buildWeaponCommand() {
        try {
            return ItemCommand.replaceEntityWith(Entity.ofSelector(TargetSelector.SENDER),
                    ItemSlot.MAINHAND,
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
                                                    .display(AttributeDisplayType.HIDDEN))));
        } catch (Exception e) {
            // Differentiates item-building errors from registration errors
            throw new RuntimeException("Weapon Construction Error: Failed to build 'The Impaler' Trident NBT. "
                    + e.getMessage(), e);
        }
    }
}