package uhc.team;

import uhc.resource.color.BossbarColor;
import uhc.resource.color.TextColor;
import uhc.text.TextComponent;
import uhc.resource.color.DyeColor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🚩 **Team Data Model**
 * <p>
 * This class acts as the central data holder for a specific UHC team.
 * It synchronizes various color formats (JSON, Bossbars, Dye, Particles)
 * and provides utility methods for name formatting and identity retrieval.
 * </p>
 * <p>This class is <b>immutable</b>; once constructed, the team's properties
 * cannot be modified.</p>
 */
public final class TeamData {

    // --- 🏷️ Identification & Display ---

    /** * The unique numerical index for this team.
     * Typically used for internal identifiers and list ordering.
     */
    private final int id;

    /** * The optional custom {@link TextComponent} display name for the team.
     * <p>If this field is {@code null}, the system will fall back to a
     * Title Case representation of the team's primary color.</p>
     */
    private final TextComponent displayName;

    // --- 🎨 Color Mapping ---

    /** * The primary {@link TextColor} used for JSON chat messages,
     * name tags, and sidebar objectives.
     */
    private final TextColor textColor;

    /** * The {@link BossbarColor} used for the team-specific
     * information bars displayed at the top of the screen.
     */
    private final BossbarColor bossbarColor;

    /** * The {@link DyeColor} used for physical resource blocks
     * such as team-specific wool, glass, or leather armor.
     */
    private final DyeColor dyeColor;

    /** * An integer array representing the RGB color components [Red, Green, Blue].
     * Values are expected to be within the range [0, 255].
     */
    private final int[] rgb;

    /** * The integer ID used specifically for Minecraft wolf collar colors.
     * Maps to the internal color registry for entity data.
     */
    private final int collarColor;

    // --- 🏗️ Constructor ---

    /**
     * Constructs a complete, immutable TeamData profile.
     * * @param id           The unique index of the team.
     * @param displayName  Optional custom {@link TextComponent} (can be null).
     * @param textColor    Primary color for text and UI.
     * @param bossbarColor Color for the team bossbar.
     * @param dyeColor     Color for blocks and items.
     * @param collarColor  Integer ID for wolf collars (0-15).
     * @param rgb          An array of 3 integers representing [R, G, B].
     * * @throws NullPointerException if textColor, bossbarColor, or dyeColor is null.
     * @throws IllegalArgumentException if the RGB array is null, has a length other than 3,
     * or contains values outside the [0, 255] range.
     */
    public TeamData(int id, TextComponent displayName, TextColor textColor,
                    BossbarColor bossbarColor, DyeColor dyeColor,
                    int collarColor, int[] rgb) {
        // Validation for identification
        this.id = id;
        this.displayName = displayName; // Optional, null handled by getter logic.

        // Null safety for required color objects
        this.textColor = Objects.requireNonNull(textColor, "TextColor cannot be null.");
        this.bossbarColor = Objects.requireNonNull(bossbarColor, "BossbarColor cannot be null.");
        this.dyeColor = Objects.requireNonNull(dyeColor, "DyeColor cannot be null.");
        this.collarColor = collarColor;

        // Comprehensive validation for RGB array
        if (rgb == null) {
            throw new IllegalArgumentException("RGB array cannot be null for Team: " + id);
        }
        if (rgb.length != 3) {
            throw new IllegalArgumentException("RGB array must contain exactly 3 components [R, G, B]. Provided length: " + rgb.length);
        }
        for (int i = 0; i < 3; i++) {
            if (rgb[i] < 0 || rgb[i] > 255) {
                throw new IllegalArgumentException("RGB component at index " + i + " must be between 0 and 255. Provided: " + rgb[i]);
            }
        }

        // Defensive copy to preserve immutability
        this.rgb = Arrays.copyOf(rgb, 3);
    }

    // --- 📝 Name & Case Formatting ---

    /**
     * Retrieves the display name as a {@link TextComponent}.
     * <p>If a custom display name was not provided during construction, this method
     * generates a default component using the {@link #title()} logic and the
     * team's {@link TextColor}.</p>
     * * @return A non-null {@link TextComponent} representing the team name.
     */
    public TextComponent getDisplayName() {
        if (displayName != null) {
            return displayName;
        }
        // Fallback: Create a text component using the Title Case color name
        return TextComponent.text(title()).color(textColor);
    }

    /**
     * Generates the standard internal team identifier used by Minecraft commands.
     * * @return A string identifier (e.g., {@code "Team0"}).
     */
    public String name() {
        return "Team" + id;
    }

    /**
     * Formats the primary TextColor as a screaming snake case constant.
     * * @return The uppercase name (e.g., {@code "LIGHT_PURPLE"}).
     */
    public String upper() {
        try {
            return textColor.name().toUpperCase();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * Formats the TextColor name for user-friendly display strings.
     * * @return The name in Title Case (e.g., {@code "Light Purple"}).
     */
    public String title() {
        try {
            String raw = textColor.name().replace("_", " ").toLowerCase();
            return Arrays.stream(raw.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            return "Team Color";
        }
    }

    /**
     * Formats the TextColor name into a PascalCase identifier.
     * * @return The name in PascalCase (e.g., {@code "LightPurple"}).
     */
    public String pascal() {
        try {
            String raw = textColor.name().replace("_", " ").toLowerCase();
            return Arrays.stream(raw.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .collect(Collectors.joining());
        } catch (Exception e) {
            return "TeamColor";
        }
    }

    // --- 🔍 Getters ---

    /** @return The unique numerical ID of the team. */
    public int getId() {
        return id;
    }

    /** @return The primary {@link TextColor} assigned to this team. */
    public TextColor getTextColor() {
        return textColor;
    }

    /** @return The {@link BossbarColor} assigned to this team. */
    public BossbarColor getBossbarColor() {
        return bossbarColor;
    }

    /** @return The {@link DyeColor} assigned to this team. */
    public DyeColor getDyeColor() {
        return dyeColor;
    }

    /** * Returns a copy of the RGB components.
     * * @return A new {@code int[]} array containing [R, G, B].
     */
    public int[] getRgb() {
        return Arrays.copyOf(rgb, 3);
    }

    /** @return The integer ID for wolf collar data. */
    public int getCollarColor() {
        return collarColor;
    }
}