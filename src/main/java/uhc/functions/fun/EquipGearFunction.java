package uhc.functions.fun;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.arguments.item.ItemStack;
import uhc.arguments.item.slot.ItemSlot;
import uhc.arguments.item.slot.SpecificItemSlot;
import uhc.command.commands.EffectCommand;
import uhc.command.commands.ItemCommand;
import uhc.components.functions.Function;
import uhc.components.functions.FunctionPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.functions.DatapackFunction;
import uhc.resource.effect.EffectId;
import uhc.resource.item.*;

import java.util.Objects;

/**
 * 🛡️ **Equip Gear Function Module**
 * <p>
 * This class implements the {@link DatapackFunction} contract to generate the
 * {@code equip_gear.mcfunction} file. It provides players with a full kit of
 * Iron equipment and resets their health.
 * </p>
 * <p>
 * <b>Architectural Style:</b> Fail-Fast. Every method validates its inputs
 * immediately and throws descriptive exceptions to prevent malformed datapack output.
 * </p>
 */
public class EquipGearFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The title string used by {@link #appendStandardHeader(Function, String)}
     * to label the generated Minecraft function file.
     */
    private static final String FUNCTION_TITLE = "Fun: Equip Starter Gear";

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Orchestrates the registration and command assembly for the Equip Gear function.
     * <p>
     * This method follows a strict sequence:
     * 1. Infrastructure Validation
     * 2. Component Initialization
     * 3. Logic Injection (Header, Armor, Tools, Effects)
     * 4. Namespace Persistence
     * </p>
     * * @param datapack  The master {@link Datapack} container (non-null).
     * @param namespace The {@link Namespace} where the file will be registered (non-null).
     * @throws NullPointerException if mandatory infrastructure is missing.
     * @throws RuntimeException     if logic assembly or registration fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {
        Objects.requireNonNull(datapack, "EquipGear Registration Error: Datapack cannot be null.");
        Objects.requireNonNull(namespace, "EquipGear Registration Error: Namespace cannot be null.");

        final FunctionPath path = Objects.requireNonNull(FunctionPath.EQUIP_GEAR,
                "EquipGear Registry Error: FunctionPath.EQUIP_GEAR is not defined.");

        final Function currentFunction = new Function(path);

        try {
            // A. File Documentation Header
            this.appendStandardHeader(currentFunction, FUNCTION_TITLE);

            // B. Target Localization
            final Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

            // C. Sequential Logic Assembly
            this.addArmorLogic(currentFunction, allPlayers);
            this.addUtilityLogic(currentFunction, allPlayers);
            this.addStatusLogic(currentFunction, allPlayers);

            // D. Visual Spacing
            this.addSafeLine(currentFunction, "");

            // E. Persistence
            namespace.addComponent(currentFunction);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to assemble " + path + ". Error: " + e.getMessage(), e);
        }
    }

    // =================================================================================
    // INTERNAL LOGIC MODULES
    // =================================================================================

    /**
     * Injects the commands required to equip a full Iron armor set.
     * * @param function The target {@link Function} component (non-null).
     * @param target   The {@link Entity} receiving the armor (non-null).
     * @throws NullPointerException if any parameter is null.
     */
    private void addArmorLogic(Function function, Entity target) {
        Objects.requireNonNull(function, "Armor Logic Error: Function component is null.");
        Objects.requireNonNull(target, "Armor Logic Error: Target entity is null.");

        this.addSafeLine(function, "=== SECTION: ARMOR SET ===");

        this.equip(function, target, ItemSlot.HEAD, DynamicItem.armor(ArmorMaterial.IRON, ArmorPiece.HELMET));
        this.equip(function, target, ItemSlot.CHEST, DynamicItem.armor(ArmorMaterial.IRON, ArmorPiece.CHESTPLATE));
        this.equip(function, target, ItemSlot.LEGS, DynamicItem.armor(ArmorMaterial.IRON, ArmorPiece.LEGGINGS));
        this.equip(function, target, ItemSlot.FEET, DynamicItem.armor(ArmorMaterial.IRON, ArmorPiece.BOOTS));

        this.addSafeLine(function, "");
    }

    /**
     * Injects commands for the offhand shield and the primary hotbar tools.
     * * @param function The target {@link Function} component (non-null).
     * @param target   The {@link Entity} receiving the items (non-null).
     * @throws NullPointerException if any parameter is null.
     */
    private void addUtilityLogic(Function function, Entity target) {
        Objects.requireNonNull(function, "Utility Logic Error: Function component is null.");
        Objects.requireNonNull(target, "Utility Logic Error: Target entity is null.");

        this.addSafeLine(function, "=== SECTION: TOOLS & UTILITY ===");

        this.equip(function, target, ItemSlot.OFFHAND, ItemId.SHIELD);
        this.equip(function, target, ItemSlot.MAINHAND, DynamicItem.tool(ToolMaterial.IRON, ToolPiece.AXE));
        this.equip(function, target, ItemSlot.INVENTORY.withSlotNumber(0), DynamicItem.tool(ToolMaterial.IRON, ToolPiece.SWORD));

        this.addSafeLine(function, "");
    }

    /**
     * Injects commands to apply instantaneous health regeneration.
     * * @param function The target {@link Function} component (non-null).
     * @param target   The {@link Entity} receiving the status (non-null).
     * @throws NullPointerException if any parameter is null.
     */
    private void addStatusLogic(Function function, Entity target) {
        Objects.requireNonNull(function, "Status Logic Error: Function component is null.");
        Objects.requireNonNull(target, "Status Logic Error: Target entity is null.");

        this.addSafeLine(function, "=== SECTION: STATUS RESET ===");
        this.addSafeLine(function, "Instant health reset via high-potency regeneration.");

        this.addSafeLine(function, EffectCommand.give(target, EffectId.REGENERATION, 1, 255, true));
    }

    // =================================================================================
    // PRIVATE UTILITIES
    // =================================================================================

    /**
     * Maps an item provider (ItemId or DynamicItem) to an executable replace command.
     * <p>
     * <b>Validation:</b> Rejects unsupported types with a clear error message to
     * avoid silent failures or empty item stacks.
     * </p>
     * * @param function The function to inject the command into (non-null).
     * @param target   The target entity (non-null).
     * @param slot     The destination inventory slot (non-null).
     * @param provider The item source (supports {@link ItemId} and {@link DynamicItem}).
     * @throws IllegalArgumentException if the provider type is null or unsupported.
     */
    private void equip(Function function, Entity target, SpecificItemSlot slot, Object provider) {
        ItemStack item;

        if (provider instanceof ItemId id) {
            item = ItemStack.create(id);
        } else if (provider instanceof DynamicItem dynamic) {
            item = ItemStack.create(dynamic);
        } else {
            String typeName = (provider == null) ? "null" : provider.getClass().getSimpleName();
            throw new IllegalArgumentException("Equip Command Error: Type [" + typeName + "] is not a valid item provider.");
        }

        this.addSafeLine(function, ItemCommand.replaceEntityWith(target, slot, item));
    }
}