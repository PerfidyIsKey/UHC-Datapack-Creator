package commands.data;

/**
 * Represents the "set value <value>" modification.
 */
public class ModificationSetValue implements DataModification {
    private final DataValue value;

    /**
     * Private constructor. Use the static 'create' factory method.
     */
    private ModificationSetValue(DataValue value) {
        if (value == null) {
            throw new IllegalArgumentException("DataValue cannot be null for ModificationSetValue.");
        }
        this.value = value;
    }

    /**
     * Factory method to create a ModificationSetValue.
     */
    public static ModificationSetValue create(DataValue value) {
        return new ModificationSetValue(value);
    }

    @Override
    public String build() {
        // "set value " is the required command syntax
        return "set value " + value.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}