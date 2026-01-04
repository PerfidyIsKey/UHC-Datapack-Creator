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
import uhc.resource.attribute.AttributeOperation;
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
 * <li>Equips "The Impaler", a Trident with level 255 enchantments and +1000 Armor.</li>
 * </ul>
 * </p>
 */
public class GodModeFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The header title used for file documentation. */
    private static final String MODULE_TITLE = "Module: God Mode Activation";

    /** * The target selector used for all commands within this module. */
    private static final Entity TARGET = Entity.ofSelector(TargetSelector.SENDER);

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Entry point for registering the God Mode functional component.
     * <p>
     * Orchestrates the assembly of status effects and legendary equipment.
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The target {@link Namespace} for storage (non-null).
     * @throws NullPointerException if {@code datapack} or {@code namespace} is null.
     * @throws RuntimeException     if assembly logic fails during the line-injection phase.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        // 1. Validate Infrastructure
        Objects.requireNonNull(datapack, "GodMode Error: Datapack environment is null.");
        Objects.requireNonNull(namespace, "GodMode Error: Target Namespace is null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.GOD_MODE,
                "Registry Error: FunctionPath.GOD_MODE is not defined in the system.");

        final Function currentFunction = new Function(path);

        try {
            // 2. Build Header
            this.appendStandardHeader(currentFunction, MODULE_TITLE);

            // 3. Add Status Effects
            this.addSafeLine(currentFunction, "SECTION 1: Permanent Resistance V");
            currentFunction.addLine(EffectCommand.giveInfinite(TARGET, EffectId.RESISTANCE, 4, true));
            this.addSafeLine(currentFunction, "");

            // 4. Add Legendary Equipment
            this.addSafeLine(currentFunction, "SECTION 2: Equip 'The Impaler' Trident");
            currentFunction.addLine(this.buildWeaponCommand());

            // 5. Finalize
            this.addSafeLine(currentFunction, "");
            this.addSafeLine(currentFunction, "--- God Mode successfully initialized ---");

            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            // "No-Fallback" Policy: Throw clear error to prevent broken datapack generation
            throw new RuntimeException("CRITICAL: Failed to assemble GodMode at [" + path + "]. " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Logic Builders ---

    /**
     * Constructs the complex {@link ItemCommand} for the "The Impaler" weapon.
     * <p>
     * This method separates the NBT construction from the command assembly for better error tracking.
     * </p>
     *
     * @return A fully configured {@link ItemCommand}.
     * @throws RuntimeException if the ItemStack or NBT assembly fails.
     */
    private ItemCommand buildWeaponCommand() {
        try {
            ItemStack impaler = ItemStack.create(ItemId.TRIDENT)
                    .with(this.createNameComponent())
                    .with(LoreComponent.create(TextComponent.text("This holy weapon impales anything it touches")))
                    .with(DamageComponent.create(0))
                    .with(this.createEnchantmentComponent())
                    .with(this.createAttributeComponent());

            return ItemCommand.replaceEntityWith(TARGET, ItemSlot.MAINHAND, impaler);

        } catch (Exception e) {
            throw new RuntimeException("Weapon Assembly Error: Failed to build 'The Impaler' Trident. " + e.getMessage(), e);
        }
    }

    /**
     * Generates the multi-layered visual name for the weapon.
     */
    private CustomNameComponent createNameComponent() {
        return CustomNameComponent.create(
                TextComponent.text("aA").color(TextColor.WHITE).obfuscated(true)
                        .append(TextComponent.text("The").color(HexColor.create("#8C3CC1")).bold(true))
                        .append(TextComponent.text(" Impaler ").color(HexColor.create("#E280FF")).bold(true))
                        .append(TextComponent.text("Aa").color(TextColor.WHITE).obfuscated(true))
        );
    }

    /**
     * Generates the level-255 enchantment stack.
     */
    private EnchantmentsComponent createEnchantmentComponent() {
        return EnchantmentsComponent.create()
                .add(EnchantmentId.FIRE_ASPECT, 255)
                .add(EnchantmentId.SHARPNESS, 255)
                .add(EnchantmentId.IMPALING, 255)
                .add(EnchantmentId.LOYALTY, 255)
                .add(EnchantmentId.EFFICIENCY, 255);
    }

    /**
     * Generates the hidden attribute modifiers for armor.
     */
    private AttributeModifiersComponent createAttributeComponent() {
        return AttributeModifiersComponent.create()
                .add(AttributeModifiersComponent.Entry.create(
                                AttributeId.ARMOR,
                                AttributeModifierId.BASE_ARMOR,
                                1000.0,
                                AttributeOperation.ADD_VALUE)
                        .slot(EquipmentSlot.ARMOR)
                        .display(AttributeDisplayType.HIDDEN));
    }
}