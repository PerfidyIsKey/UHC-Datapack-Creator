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
 * This class acts as the central data holder for a specific UHC team. It encapsulates
 * identification, display logic, and cross-module color mapping.
 * </p>
 * <p>
 * Use the static factory methods {@link #of(int, TextColor, BossbarColor, DyeColor, int, float[])}
 * or {@link #of(int, TextComponent, TextColor, BossbarColor, DyeColor, int, float[])} to instantiate.
 * </p>
 */
public final class TeamData {

    // --- 🏷️ Identification & Display ---

    /** * The unique numerical index for this team.
     * Used for internal naming (e.g., "Team0").
     */
    private final int id;

    /** * The optional custom {@link TextComponent} display name.
     * If null, the class generates a fallback name based on the {@link TextColor}.
     */
    private final TextComponent displayName;

    // --- 🎨 Color Mapping ---

    /** The {@link TextColor} used for JSON text components, chat, and nametags. */
    private final TextColor textColor;

    /** The {@link BossbarColor} used for the team's visual bossbar display. */
    private final BossbarColor bossbarColor;

    /** The {@link DyeColor} used for physical resource blocks (wool, glass, etc.). */
    private final DyeColor dyeColor;

    /** * Normalized RGB color components [R, G, B].
     * Values are stored as floats between 0.0f and 1.0f.
     */
    private final float[] rgb;

    /** * The integer ID used for Minecraft wolf collar colors.
     * Maps to values 0-15.
     */
    private final int collarColor;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor for TeamData.
     * Use factory methods for instantiation.
     *
     * @param id           Unique team index.
     * @param displayName  Optional custom {@link TextComponent} (can be null).
     * @param textColor    Required text color.
     * @param bossbarColor Required bossbar color.
     * @param dyeColor     Required dye color.
     * @param collarColor  Integer ID for wolf collars.
     * @param rgb          Float array [R, G, B] between 0.0 and 1.0.
     * @throws NullPointerException if required color objects are null.
     * @throws IllegalArgumentException if the RGB array is malformed or null.
     */
    private TeamData(int id, TextComponent displayName, TextColor textColor,
                     BossbarColor bossbarColor, DyeColor dyeColor,
                     int collarColor, float[] rgb) {
        this.id = id;
        this.displayName = displayName; // Nullable
        this.textColor = Objects.requireNonNull(textColor, "TextColor cannot be null.");
        this.bossbarColor = Objects.requireNonNull(bossbarColor, "BossbarColor cannot be null.");
        this.dyeColor = Objects.requireNonNull(dyeColor, "DyeColor cannot be null.");
        this.collarColor = collarColor;

        if (rgb == null || rgb.length != 3) {
            throw new IllegalArgumentException("RGB must be a float array of exactly 3 elements [R, G, B].");
        }

        // Defensive copy of the array to maintain immutability
        this.rgb = Arrays.copyOf(rgb, 3);
    }

    // --- 🏭 Factory Methods ---

    /**
     * Creates a TeamData instance <b>without</b> a custom display name.
     * <p>The display name will default to the Title Case version of the color.</p>
     *
     * @return A new, immutable TeamData instance.
     */
    public static TeamData of(int id, TextColor textColor, BossbarColor bossbarColor,
                              DyeColor dyeColor, int collarColor, float[] rgb) {
        return new TeamData(id, null, textColor, bossbarColor, dyeColor, collarColor, rgb);
    }

    /**
     * Creates a TeamData instance <b>with</b> a specific custom display name.
     *
     * @return A new, immutable TeamData instance.
     * @throws NullPointerException if displayName is null.
     */
    public static TeamData of(int id, TextComponent displayName, TextColor textColor,
                              BossbarColor bossbarColor, DyeColor dyeColor, int collarColor, float[] rgb) {
        Objects.requireNonNull(displayName, "Custom displayName cannot be null in this factory method.");
        return new TeamData(id, displayName, textColor, bossbarColor, dyeColor, collarColor, rgb);
    }

    // --- 📝 Name & Case Formatting ---

    /**
     * Retrieves the primary display name.
     * <p>Returns the custom {@link TextComponent} if present; otherwise, creates a new
     * component using the color-based {@link #title()} name.</p>
     *
     * @return A non-null {@link TextComponent}.
     */
    public TextComponent getDisplayName() {
        if (displayName != null) {
            return displayName;
        }
        try {
            return TextComponent.text(title()).color(textColor);
        } catch (Exception e) {
            // Ultimate fallback in case of formatting failure
            return TextComponent.text("Team " + id).color(textColor);
        }
    }

    /**
     * Generates the internal Minecraft team identifier.
     * @return A string (e.g., {@code "Team0"}).
     */
    public String name() {
        return "Team" + id;
    }

    /**
     * Retrieves the color constant name in uppercase.
     * @return Uppercase color name (e.g., {@code "LIGHT_PURPLE"}).
     */
    public String upper() {
        try {
            return textColor.name().toUpperCase();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * Formats the color name for user-friendly UI.
     * @return Title Case name (e.g., {@code "Dark Red"}).
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
     * Formats the color name for internal code identifiers.
     * @return PascalCase name (e.g., {@code "DarkRed"}).
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

    /** @return The unique numerical ID. */
    public int getId() { return id; }

    /** @return The assigned {@link TextColor}. */
    public TextColor getTextColor() { return textColor; }

    /** @return The assigned {@link BossbarColor}. */
    public BossbarColor getBossbarColor() { return bossbarColor; }

    /** @return The assigned {@link DyeColor}. */
    public DyeColor getDyeColor() { return dyeColor; }

    /** @return The wolf collar color index (0-15). */
    public int getCollarColor() { return collarColor; }

    /** * Returns a copy of the normalized RGB components.
     * @return A float array [R, G, B].
     */
    public float[] getRgb() {
        return Arrays.copyOf(rgb, 3);
    }
}