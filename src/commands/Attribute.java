package commands;

import arguments.Entity;
import commands.attribute.ModifierOperation;
import shared.attributes.AttributeId;
import shared.attributes.ModifierId;

public class Attribute {
    private final Entity target;
    private final AttributeId attribute;

    // Command parts used by the build method to determine the final command.
    private String actionPath;
    private Double scale;
    private Double value;
    private ModifierId id;
    private ModifierOperation operation;

    // Internal state to handle the 'get' commands
    private boolean isBaseAction = false;
    private boolean isModifierValueGet = false;


    private Attribute(Entity target, AttributeId attribute) {
        this.target = target;
        this.attribute = attribute;
    }

    public static Attribute create(Entity target, AttributeId attribute) {
        return new Attribute(target, attribute);
    }

    // --- Fluent Parameter Setters ---

    // Required for get commands that take a scale argument.
    public Attribute scale(double scale) {
        this.scale = scale;
        return this;
    }

    public Attribute value(double value) {
        this.value = value;
        return this;
    }

    // --- Action-Defining Methods ---

    /**
     * Builds: attribute <target> <attribute> get [<scale>]
     */
    public String get() {
        this.actionPath = "get";
        this.isBaseAction = false;
        this.isModifierValueGet = false;
        // The scale parameter is optional, if set, it will be included by build().
        return build();
    }

    // --- BASE Actions ---

    /**
     * Builds: attribute <target> <attribute> base get [<scale>]
     */
    public String getBase() {
        this.actionPath = "base get";
        this.isBaseAction = true;
        this.isModifierValueGet = false;
        return build();
    }

    /**
     * Builds: attribute <target> <attribute> base set <value>
     */
    public String setBase(double value) {
        this.actionPath = "base set";
        this.isBaseAction = true;
        this.value = value;
        this.scale = null; // Clear scale
        this.isModifierValueGet = false;
        return build();
    }

    /**
     * Builds: attribute <target> <attribute> base reset
     */
    public String resetBase() {
        this.actionPath = "base reset";
        this.isBaseAction = true;
        this.value = null; // Clear value/scale
        this.scale = null;
        this.isModifierValueGet = false;
        return build();
    }

    // --- MODIFIER Actions ---

    /**
     * Builds: attribute <target> <attribute> modifier add <id> <value> <operation>
     */
    public String addModifier(ModifierId id, double value, ModifierOperation operation) {
        this.actionPath = "modifier add";
        this.isBaseAction = false;
        this.isModifierValueGet = false;

        this.id = id;
        this.value = value;
        this.operation = operation;
        this.scale = null; // Clear scale

        return build();
    }

    /**
     * Builds: attribute <target> <attribute> modifier remove <id>
     */
    public String removeModifier(ModifierId id) {
        this.actionPath = "modifier remove";
        this.isBaseAction = false;
        this.isModifierValueGet = false;

        this.id = id;
        this.value = null; // Clear value/scale/operation
        this.operation = null;
        this.scale = null;

        return build();
    }

    /**
     * Prepares for: attribute <target> <attribute> modifier value get <id> [<scale>]
     * Must be followed by .id(ModifierId) and optionally .scale(double) before calling build().
     * This is slightly less fluent to allow for the optional scale.
     */
    public Attribute getModifierValue() {
        this.actionPath = "modifier value get";
        this.isBaseAction = false;
        this.isModifierValueGet = true;
        this.value = null;
        this.operation = null;
        return this;
    }

    // Needed for the two-step getModifierValue() command.
    public Attribute id(ModifierId id) {
        this.id = id;
        return this;
    }

    // --- Final Build Method ---

    public String build() {
        if (actionPath == null) {
            throw new IllegalStateException("An action method (e.g., get(), setBase(), etc.) must be called before build().");
        }

        StringBuilder sb = new StringBuilder("attribute ");

        sb.append(target).append(" ").append(attribute).append(" ").append(actionPath);

        // Append values based on the defined actionPath
        if (actionPath.equals("base set")) {
            // base set <value>
            if (value == null) throw new IllegalStateException("Value must be set for 'base set'.");
            sb.append(" ").append(value);
        } else if (actionPath.equals("modifier add")) {
            // modifier add <id> <value> <operation>
            if (id == null || value == null || operation == null) {
                throw new IllegalStateException("ID, Value, and Operation must be set for 'modifier add'.");
            }
            sb.append(" ").append(id).append(" ").append(value).append(" ").append(operation);
        } else if (actionPath.equals("modifier remove")) {
            // modifier remove <id>
            if (id == null) throw new IllegalStateException("ID must be set for 'modifier remove'.");
            sb.append(" ").append(id);
        } else if (actionPath.equals("modifier value get")) {
            // modifier value get <id> [<scale>]
            if (id == null) throw new IllegalStateException("ID must be set for 'modifier value get'.");
            sb.append(" ").append(id);
            if (scale != null) {
                sb.append(" ").append(scale);
            }
        }

        // Handle optional scale for top-level 'get' and 'base get'
        if (actionPath.equals("get") || actionPath.equals("base get")) {
            if (scale != null) {
                sb.append(" ").append(scale);
            }
        }

        return sb.toString();
    }
}