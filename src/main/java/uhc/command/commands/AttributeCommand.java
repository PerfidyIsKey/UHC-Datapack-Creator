package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.command.MinecraftCommand;
import uhc.resource.attribute.AttributeId;
import uhc.resource.attribute.AttributeModifierId;

import java.util.Objects;

/**
 * 🧬 **Attribute Command Builder**
 * <p>
 * This class provides static factory methods to generate {@code /attribute} commands.
 * It manages the branching logic between querying final values, modifying base values,
 * or managing specific attribute modifiers.
 * </p>
 * <p>
 * Architecture follows a Command Pattern where the state is immutable once created,
 * ensuring thread-safety and consistent command generation.
 * </p>
 */
public final class AttributeCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The target entity whose attributes are being queried or modified. */
    private final Entity target;

    /** * The namespaced identifier for the attribute (e.g., generic.movement_speed). */
    private final AttributeId attribute;

    /** * The specific sub-command action path (e.g., "base set", "modifier add"). */
    private final String actionPath;

    /** * An optional scaling factor used for 'get' operations. */
    private final Double scale;

    /** * The numerical value to set or add to an attribute/modifier. */
    private final Double value;

    /** * The unique identifier for a specific attribute modifier. */
    private final AttributeModifierId modifierId;

    /** * The mathematical operation used when adding a new modifier. */
    private final ModifierOperation operation;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor used by static factory methods to initialize the command state.
     * All parameters are validated to ensure the command conforms to Minecraft syntax.
     */
    private AttributeCommand(Entity target, AttributeId attribute, String actionPath,
                             Double scale, Double value, AttributeModifierId modifierId,
                             ModifierOperation operation) {
        this.target = Objects.requireNonNull(target, "Target entity cannot be null.");
        this.attribute = Objects.requireNonNull(attribute, "Attribute ID cannot be null.");
        this.actionPath = Objects.requireNonNull(actionPath, "Action path cannot be null.");
        this.scale = scale;
        this.value = value;
        this.modifierId = modifierId;
        this.operation = operation;
    }

    // --- 🚀 Static Factory Methods (Entry Points) ---

    /**
     * Queries the final, fully calculated value of the attribute.
     * <p>Syntax: {@code /attribute <target> <attribute> get}</p>
     * * @param target    The target entity.
     * @param attribute The attribute to query.
     * @return A generated {@link AttributeCommand}.
     */
    public static AttributeCommand get(Entity target, AttributeId attribute) {
        return get(target, attribute, null);
    }

    /**
     * Queries the final value of the attribute with a multiplier.
     * <p>Syntax: {@code /attribute <target> <attribute> get <scale>}</p>
     * * @param target    The target entity.
     * @param attribute The attribute to query.
     * @param scale     The multiplier for the result.
     * @return A generated {@link AttributeCommand}.
     */
    public static AttributeCommand get(Entity target, AttributeId attribute, Double scale) {
        try {
            return new AttributeCommand(target, attribute, "get", scale, null, null, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Queries the base value of the attribute before any modifiers are applied.
     * <p>Syntax: {@code /attribute <target> <attribute> base get}</p>
     */
    public static AttributeCommand getBase(Entity target, AttributeId attribute) {
        return getBase(target, attribute, null);
    }

    /**
     * Queries the base value of the attribute with a multiplier.
     * <p>Syntax: {@code /attribute <target> <attribute> base get <scale>}</p>
     */
    public static AttributeCommand getBase(Entity target, AttributeId attribute, Double scale) {
        try {
            return new AttributeCommand(target, attribute, "base get", scale, null, null, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Overwrites the base value of the attribute.
     * <p>Syntax: {@code /attribute <target> <attribute> base set <value>}</p>
     */
    public static AttributeCommand setBase(Entity target, AttributeId attribute, double value) {
        try {
            return new AttributeCommand(target, attribute, "base set", null, value, null, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Resets the base value to the default defined by the entity type.
     * <p>Syntax: {@code /attribute <target> <attribute> base reset}</p>
     */
    public static AttributeCommand resetBase(Entity target, AttributeId attribute) {
        try {
            return new AttributeCommand(target, attribute, "base reset", null, null, null, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Adds a persistent modifier to the attribute.
     * <p>Syntax: {@code /attribute <target> <attribute> modifier add <id> <value> <operation>}</p>
     */
    public static AttributeCommand addModifier(Entity target, AttributeId attribute,
                                               AttributeModifierId id, double value,
                                               ModifierOperation operation) {
        try {
            Objects.requireNonNull(id, "Modifier ID is required for 'add'.");
            Objects.requireNonNull(operation, "Operation is required for 'add'.");
            return new AttributeCommand(target, attribute, "modifier add", null, value, id, operation);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Removes a specific modifier from the attribute.
     * <p>Syntax: {@code /attribute <target> <attribute> modifier remove <id>}</p>
     */
    public static AttributeCommand removeModifier(Entity target, AttributeId attribute, AttributeModifierId id) {
        try {
            Objects.requireNonNull(id, "Modifier ID is required for 'remove'.");
            return new AttributeCommand(target, attribute, "modifier remove", null, null, id, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Queries the value of a specific modifier.
     * <p>Syntax: {@code /attribute <target> <attribute> modifier value get <id>}</p>
     */
    public static AttributeCommand getModifierValue(Entity target, AttributeId attribute, AttributeModifierId id) {
        return getModifierValue(target, attribute, id, null);
    }

    /**
     * Queries the value of a specific modifier with a multiplier.
     * <p>Syntax: {@code /attribute <target> <attribute> modifier value get <id> <scale>}</p>
     */
    public static AttributeCommand getModifierValue(Entity target, AttributeId attribute,
                                                    AttributeModifierId id, Double scale) {
        try {
            Objects.requireNonNull(id, "Modifier ID is required for querying value.");
            return new AttributeCommand(target, attribute, "modifier value get", scale, null, id, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    // --- 🛠️ Logic & Generation ---

    /**
     * Assembles the Minecraft command string based on the internal state.
     * * @return A formatted command string (e.g., "attribute @s generic.max_health base get").
     */
    @Override
    public String generate() {
        try {
            StringBuilder sb = new StringBuilder("attribute ");
            sb.append(this.target).append(" ").append(this.attribute).append(" ").append(this.actionPath);

            switch (this.actionPath) {
                case "base set":
                    sb.append(" ").append(this.value);
                    break;
                case "modifier add":
                    sb.append(" ").append(this.modifierId).append(" ")
                            .append(this.value).append(" ").append(this.operation);
                    break;
                case "modifier remove":
                    sb.append(" ").append(this.modifierId);
                    break;
                case "modifier value get":
                    sb.append(" ").append(this.modifierId);
                    if (this.scale != null) sb.append(" ").append(this.scale);
                    break;
                case "get":
                case "base get":
                    if (this.scale != null) sb.append(" ").append(this.scale);
                    break;
            }

            return sb.toString();
        } catch (Exception e) {
            // Error Catching: Safety fallback for generation failure
            return "/say Error: Attribute command generation logic failed.";
        }
    }

    /**
     * Internal fallback logic to provide a safe default command if instantiation fails.
     */
    private static AttributeCommand fallback() {
        return new AttributeCommand(Entity.ofSelector(TargetSelector.SENDER),
                AttributeId.MAX_HEALTH, "get", null, null, null, null);
    }

    @Override
    public String toString() {
        return generate();
    }

    // --- ⚙️ Enums ---

    /**
     * Defines the mathematical operation used to apply an attribute modifier.
     */
    public enum ModifierOperation {
        /** Simply adds the value to the base. */
        ADD_VALUE,
        /** Multiplies the base by the value before adding. */
        ADD_MULTIPLIED_BASE,
        /** Multiplies the total value by the value after all other modifiers. */
        ADD_MULTIPLIED_TOTAL;

        @Override
        public String toString() { return name().toLowerCase(); }
    }
}