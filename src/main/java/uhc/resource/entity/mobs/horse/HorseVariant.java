package uhc.resource.entity.mobs.horse;

import java.util.Objects;

/**
 * 🐎 **Horse Variant Enum**
 * <p>
 * Defines the base colors and marking patterns for horses.
 * Uses internal categorization to prevent mixing colors and markings in logic.
 * </p>
 */
public enum HorseVariant {

    // --- Base Colors (Values 0-6) ---
    WHITE(0, VariantType.COLOR),
    CREAMY(1, VariantType.COLOR),
    CHESTNUT(2, VariantType.COLOR),
    BROWN(3, VariantType.COLOR),
    BLACK(4, VariantType.COLOR),
    GRAY(5, VariantType.COLOR),
    DARK_BROWN(6, VariantType.COLOR),

    // --- Marking Patterns (Values 256-1024) ---
    NONE(0, VariantType.MARKING),
    WHITE_STOCKINGS(256, VariantType.MARKING),
    WHITE_FIELD(512, VariantType.MARKING),
    WHITE_DOTS(768, VariantType.MARKING),
    BLACK_DOTS(1024, VariantType.MARKING);

    private final int value;
    private final VariantType type;

    HorseVariant(int value, VariantType type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Calculates the combined variant ID for the NBT.
     * <p>
     * Formula: {@code color + marking}.
     * This method validates that the first argument is a COLOR and the second is a MARKING.
     * </p>
     * * @param color   The base coat color (must be a COLOR type).
     * @param marking The pattern overlay (must be a MARKING type).
     * @return The combined integer ID for the NBT "Variant" tag.
     * @throws IllegalArgumentException if the parameters are swapped or of the wrong category.
     */
    public static int calculate(HorseVariant color, HorseVariant marking) {
        Objects.requireNonNull(color, "Color cannot be null.");
        Objects.requireNonNull(marking, "Marking cannot be null.");

        if (color.type != VariantType.COLOR) {
            throw new IllegalArgumentException("The first argument must be a base color (e.g., WHITE, BLACK), but found: " + color.name());
        }

        if (marking.type != VariantType.MARKING) {
            throw new IllegalArgumentException("The second argument must be a marking pattern (e.g., WHITE_DOTS, NONE), but found: " + marking.name());
        }

        return color.value + marking.value;
    }

    /**
     * Internal categorization for safety checks.
     */
    private enum VariantType {
        COLOR, MARKING
    }
}