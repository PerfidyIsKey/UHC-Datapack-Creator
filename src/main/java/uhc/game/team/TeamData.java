package uhc.game.team;

import uhc.arguments.entity.Entity;
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
 * This class acts as the central data holder for a specific UHC team. It encapsulates
 * identification, display logic, and cross-module color mapping.
 * </p>
 * <p>
 * All fields are immutable. Validation is performed during construction to ensure
 * color mapping consistency across Minecraft's various systems (Chat, Bossbars,
 * Entities, and Blocks).
 * </p>
 */
public final class TeamData {

    // --- 🏷️ Identification Fields ---

    /** * The unique numerical index for this team.
     * Used for internal naming conventions (e.g., "Team0").
     */
    private final int id;

    /** * The optional custom {@link TextComponent} display name.
     * If provided via the specific factory method, this overrides the default color name.
     */
    private final TextComponent displayName;

    // --- 🎨 Color Mapping Fields ---

    /** * The {@link TextColor} used for JSON text components, chat, and nametags.
     * This serves as the primary reference for naming logic.
     */
    private final TextColor textColor;

    /** * The {@link BossbarColor} used for the team's visual bossbar display.
     */
    private final BossbarColor bossbarColor;

    /** * The {@link DyeColor} used for physical resource blocks (wool, glass, etc.).
     */
    private final DyeColor dyeColor;

    /** * Normalized RGB color components [R, G, B].
     * Values must be floats between 0.0f and 1.0f.
     */
    private final float[] rgb;

    /** * The integer ID used for Minecraft wolf collar colors.
     * Valid range is typically 0-15.
     */
    private final int collarColor;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor for TeamData.
     *
     * @param id           Unique team index.
     * @param displayName  Optional custom {@link TextComponent} (can be null).
     * @param textColor    The required {@link TextColor} reference.
     * @param bossbarColor The required {@link BossbarColor} reference.
     * @param dyeColor     The required {@link DyeColor} reference.
     * @param collarColor  Integer ID for wolf collars.
     * @param rgb          Float array [R, G, B].
     * @throws NullPointerException if any required color object is null.
     * @throws IllegalArgumentException if the RGB array does not contain exactly 3 elements.
     */
    private TeamData(int id, TextComponent displayName, TextColor textColor,
                     BossbarColor bossbarColor, DyeColor dyeColor,
                     int collarColor, float[] rgb) {
        this.id = id;
        this.displayName = displayName; // Optional field
        this.textColor = Objects.requireNonNull(textColor, "Team Construction Error: TextColor is mandatory.");
        this.bossbarColor = Objects.requireNonNull(bossbarColor, "Team Construction Error: BossbarColor is mandatory.");
        this.dyeColor = Objects.requireNonNull(dyeColor, "Team Construction Error: DyeColor is mandatory.");
        this.collarColor = collarColor;

        if (rgb == null || rgb.length != 3) {
            throw new IllegalArgumentException("Team Construction Error: RGB array must contain exactly 3 elements [R, G, B].");
        }

        // Defensive copy to ensure internal immutability
        this.rgb = Arrays.copyOf(rgb, 3);
    }

    // --- 🏭 Static Factory Methods ---

    /**
     * Creates a TeamData instance using default color-based naming.
     *
     * @return A new immutable TeamData instance.
     */
    public static TeamData of(int id, TextColor textColor, BossbarColor bossbarColor,
                              DyeColor dyeColor, int collarColor, float[] rgb) {
        return new TeamData(id, null, textColor, bossbarColor, dyeColor, collarColor, rgb);
    }

    /**
     * Creates a TeamData instance with a specific custom display name.
     *
     * @param displayName The {@link TextComponent} to use as the team's name.
     * @return A new immutable TeamData instance.
     * @throws NullPointerException if displayName is null.
     */
    public static TeamData of(int id, TextComponent displayName, TextColor textColor,
                              BossbarColor bossbarColor, DyeColor dyeColor, int collarColor, float[] rgb) {
        Objects.requireNonNull(displayName, "Factory Error: Custom displayName must be non-null. Use of() without the parameter for defaults.");
        return new TeamData(id, displayName, textColor, bossbarColor, dyeColor, collarColor, rgb);
    }

    // --- 📝 Name & Formatting Methods ---

    /**
     * Retrieves the display name as a formatted {@link TextComponent}.
     * <p>
     * If a custom name was provided, it is returned. Otherwise, it constructs
     * a component using the Title Case version of the color (e.g., "Dark Red").
     * </p>
     *
     * @return A non-null {@link TextComponent}.
     * @throws RuntimeException if default name generation fails.
     */
    public TextComponent getDisplayName() {
        if (displayName != null) {
            return displayName;
        }

        try {
            return TextComponent.text(title()).color(textColor);
        } catch (Exception e) {
            throw new RuntimeException("Display Error: Failed to generate default display name for Team " + id + ". " + e.getMessage(), e);
        }
    }

    /**
     * Generates the unique namespaced team identifier for Minecraft's internal registry.
     * @return A string identifier (e.g., "Team5").
     */
    public String name() {
        return "Team" + id;
    }

    /**
     * Returns the {@link Entity} representation of this team, used for target selectors.
     * @return The team entity reference.
     */
    public Entity entity() {
        return Entity.ofName(textColor.pascal());
    }

    /**
     * Retrieves the raw color constant name.
     * @return Uppercase color name (e.g., "RED").
     * @throws IllegalStateException if the TextColor name is null or inaccessible.
     */
    public String upper() {
        String name = textColor.name();
        if (name == null) {
            throw new IllegalStateException("Data Error: TextColor name for Team " + id + " is null.");
        }
        return name.toUpperCase();
    }

    /**
     * Formats the color name for player-facing UI elements.
     * @return Title Case name (e.g., "Light Purple").
     * @throws IllegalStateException if the color name cannot be parsed.
     */
    public String title() {
        try {
            String raw = textColor.name().replace("_", " ").toLowerCase();
            return Arrays.stream(raw.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            throw new IllegalStateException("Formatting Error: Unable to convert color '" + textColor.name() + "' to Title Case.", e);
        }
    }

    /**
     * Formats the color name for code-compliant identifiers.
     * @return PascalCase name (e.g., "LightPurple").
     * @throws IllegalStateException if formatting fails.
     */
    public String pascal() {
        try {
            String raw = textColor.name().replace("_", " ").toLowerCase();
            return Arrays.stream(raw.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                    .collect(Collectors.joining());
        } catch (Exception e) {
            throw new IllegalStateException("Formatting Error: Unable to convert color '" + textColor.name() + "' to PascalCase.", e);
        }
    }

    // --- 🔍 Getter Methods ---

    /** @return The numerical ID of the team. */
    public int getId() { return id; }

    /** @return The assigned {@link TextColor}. */
    public TextColor getTextColor() { return textColor; }

    /** @return The assigned {@link BossbarColor}. */
    public BossbarColor getBossbarColor() { return bossbarColor; }

    /** @return The assigned {@link DyeColor}. */
    public DyeColor getDyeColor() { return dyeColor; }

    /** @return The integer index for wolf collars. */
    public int getCollarColor() { return collarColor; }

    /** * Returns a copy of the RGB components to maintain immutability.
     * @return A float array [R, G, B].
     */
    public float[] getRgb() {
        return Arrays.copyOf(rgb, 3);
    }
}