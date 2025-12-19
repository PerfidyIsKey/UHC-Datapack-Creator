package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.attribute.AttributeId;
import uhc.attribute.ModifierId;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🧬 **Attribute Command Builder**
 * <p>
 * Provides a fluent API for the {@code /attribute} command. This builder handles
 * the branching logic between querying the final value, modifying the base value,
 * or managing specific attribute modifiers.
 * </p>
 */
public class AttributeCommand implements MinecraftCommand {
    private final Entity target;
    private final AttributeId attribute;

    // Command parts used by the generate method to determine the final command string.
    private String actionPath;
    private Double scale;
    private Double value;
    private ModifierId id;
    private ModifierOperation operation;

    // Internal state to handle the logic flow of sub-commands.
    private boolean isBaseAction = false;
    private boolean isModifierValueGet = false;


    private AttributeCommand(Entity target, AttributeId attribute) {
        this.target = Objects.requireNonNull(target, "Target entity cannot be null.");
        this.attribute = Objects.requireNonNull(attribute, "Attribute ID cannot be null.");
    }

    /**
     * Initializes a new AttributeCommand builder.
     * @param target The entity to query or modify.
     * @param attribute The namespaced attribute (e.g., minecraft:generic.max_health).
     */
    public static AttributeCommand create(Entity target, AttributeId attribute) {
        return new AttributeCommand(target, attribute);
    }

    // --- Fluent Parameter Setters ---

    /**
     * Sets an optional multiplier for 'get' sub-commands.
     * @param scale The multiplier (e.g., use 2.0 to double the value in the return).
     */
    public AttributeCommand scale(double scale) {
        this.scale = scale;
        return this;
    }

    /** Sets the numeric value for base-setting or modifier-adding actions. */
    public AttributeCommand value(double value) {
        this.value = value;
        return this;
    }

    // --- Action-Defining Methods ---

    /**
     * Queries the final, fully calculated value of the attribute.
     * <p>Syntax: {@code attribute <target> <attribute> get [<scale>]}</p>
     */
    public String get() {
        this.actionPath = "get";
        this.isBaseAction = false;
        this.isModifierValueGet = false;
        return generate();
    }

    // --- BASE Actions ---

    /**
     * Queries the base value of the attribute before modifiers.
     * <p>Syntax: {@code attribute <target> <attribute> base get [<scale>]}</p>
     */
    public String getBase() {
        this.actionPath = "base get";
        this.isBaseAction = true;
        this.isModifierValueGet = false;
        return generate();
    }

    /**
     * Sets a new base value for the attribute.
     * <p>Syntax: {@code attribute <target> <attribute> base set <value>}</p>
     */
    public String setBase(double value) {
        this.actionPath = "base set";
        this.isBaseAction = true;
        this.value = value;
        this.scale = null; // Scale is not valid for 'set' actions
        this.isModifierValueGet = false;
        return generate();
    }

    /**
     * Resets the base value to its original default.
     * <p>Syntax: {@code attribute <target> <attribute> base reset}</p>
     */
    public String resetBase() {
        this.actionPath = "base reset";
        this.isBaseAction = true;
        this.value = null;
        this.scale = null;
        this.isModifierValueGet = false;
        return generate();
    }

    // --- MODIFIER Actions ---

    /**
     * Adds a persistent modifier. Fails if the ID already exists on the entity.
     * <p>Syntax: {@code attribute <target> <attribute> modifier add <id> <value> <operation>}</p>
     */
    public String addModifier(ModifierId id, double value, ModifierOperation operation) {
        this.actionPath = "modifier add";
        this.isBaseAction = false;
        this.isModifierValueGet = false;

        this.id = Objects.requireNonNull(id, "Modifier ID is required.");
        this.value = value;
        this.operation = Objects.requireNonNull(operation, "Operation is required.");
        this.scale = null;

        return generate();
    }

    /**
     * Removes the specified modifier from the entity.
     * <p>Syntax: {@code attribute <target> <attribute> modifier remove <id>}</p>
     */
    public String removeModifier(ModifierId id) {
        this.actionPath = "modifier remove";
        this.isBaseAction = false;
        this.isModifierValueGet = false;

        this.id = Objects.requireNonNull(id, "Modifier ID is required.");
        this.value = null;
        this.operation = null;
        this.scale = null;

        return generate();
    }

    /**
     * Prepares a query for the value of a specific modifier.
     * <p>Must call {@link #id(ModifierId)} before building.</p>
     */
    public AttributeCommand getModifierValue() {
        this.actionPath = "modifier value get";
        this.isBaseAction = false;
        this.isModifierValueGet = true;
        this.value = null;
        this.operation = null;
        return this;
    }

    /** Sets the Modifier ID for actions requiring it. */
    public AttributeCommand id(ModifierId id) {
        this.id = id;
        return this;
    }

    // --- Final Build Method ---

    /**
     * Validates the internal state and generates the command string.
     * @throws IllegalStateException if required parameters are missing for the selected action.
     */
    @Override
    public String generate() {
        if (actionPath == null) {
            throw new IllegalStateException("An action path (get, base, or modifier) must be chosen before generating.");
        }

        StringBuilder sb = new StringBuilder("attribute ");
        sb.append(target).append(" ").append(attribute).append(" ").append(actionPath);

        // Branching logic to append the correct arguments based on actionPath
        switch (actionPath) {
            case "base set":
                if (value == null) throw new IllegalStateException("'base set' requires a value.");
                sb.append(" ").append(value);
                break;

            case "modifier add":
                if (id == null || value == null || operation == null) {
                    throw new IllegalStateException("'modifier add' requires an ID, a value, and an operation.");
                }
                sb.append(" ").append(id).append(" ").append(value).append(" ").append(operation);
                break;

            case "modifier remove":
                if (id == null) throw new IllegalStateException("'modifier remove' requires an ID.");
                sb.append(" ").append(id);
                break;

            case "modifier value get":
                if (id == null) throw new IllegalStateException("'modifier value get' requires an ID via .id().");
                sb.append(" ").append(id);
                if (scale != null) sb.append(" ").append(scale);
                break;

            case "get":
            case "base get":
                if (scale != null) sb.append(" ").append(scale);
                break;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /** Defines how the modifier's value is applied to the attribute. */
    public enum ModifierOperation {
        /** Adds the value directly. */
        ADD_VALUE,
        /** Multiplies the base by (1 + value). */
        ADD_MULTIPLIED_BASE,
        /** Multiplies the total by (1 + value). */
        ADD_MULTIPLIED_TOTAL;

        @Override
        public String toString() { return name().toLowerCase(); }
    }
}