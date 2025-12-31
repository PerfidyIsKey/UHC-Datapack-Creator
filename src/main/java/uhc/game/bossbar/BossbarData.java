package uhc.game.bossbar;

import uhc.game.control_points.ControlPointData;
import uhc.resource.bossbar.BossbarStyle;
import uhc.text.TextComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * 📊 **Bossbar Data**
 * <p>
 * This class serves as the configuration model for Minecraft boss bars. It utilizes
 * a Builder pattern to handle optional visual attributes (like segmentation styles
 * and maximum values) without requiring null parameters in the constructor.
 * </p>
 */
public final class BossbarData {

    // --- 📄 Fields ---

    /** * The unique internal identifier used by the Minecraft bossbar registry. */
    private final String id;

    /** * The display name of the bossbar, stored as a rich {@link TextComponent}. */
    private final TextComponent name;

    /** * The maximum progress value of the bossbar.
     * If present, this defines the 100% threshold for the bar's fill level.
     */
    private final Integer max;

    /** * The visual segmentation style of the bossbar.
     * Sourced from {@link BossbarStyle} (e.g., Progress, Notched 6, 10, 12, or 20).
     */
    private final BossbarStyle style;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor used exclusively by the static Builder and Factory methods.
     * Ensures all fields are assigned safely without exposing the constructor publicly.
     *
     * @param id    Unique identifier string.
     * @param name  Formatted name component.
     * @param max   Maximum value (nullable internally).
     * @param style Visual style (nullable internally).
     */
    private BossbarData(String id, TextComponent name, Integer max, BossbarStyle style) {
        this.id = id;
        this.name = name;
        this.max = max;
        this.style = style;
    }

    // --- 🚀 Static Entry Points ---

    /**
     * 🏗️ **Initialization Builder**
     * <p>Begins the construction of a new boss bar by requiring mandatory identity fields.</p>
     *
     * @param id   The unique ID string (e.g., "capture_point_1").
     * @param name The {@link TextComponent} to be displayed above the bar.
     * @return A {@link Builder} instance to configure optional progress and style settings.
     * @throws NullPointerException if id or name are null.
     */
    public static Builder builder(String id, TextComponent name) {
        return new Builder(id, name);
    }

    /**
     * 🔄 **From Control Point**
     * <p>Automatically transforms a {@link ControlPointData} objective into a visible boss bar.
     * It maps the control point's name, position, and dimension into a formatted title.</p>
     *
     * @param cp The source control point to be visualized.
     * @return A fully initialized and configured BossbarData instance.
     * @throws NullPointerException if the provided control point is null.
     */
    public static BossbarData fromControlPoint(ControlPointData cp) {
        try {
            Objects.requireNonNull(cp, "Source ControlPointData cannot be null.");

            // Constructing a detailed title using the CP's metadata
            String rawTitle = String.format("%s: %s (%s)",
                    cp.name(),
                    cp.getPos().title(),
                    cp.getDimension().title());

            return new Builder(cp.name(), TextComponent.text(rawTitle))
                    .max(cp.getMax())
                    .style(BossbarStyle.PROGRESS)
                    .build();
        } catch (Exception e) {
            // Error Catching: Provides a safe fallback to prevent UI-driven crashes
            return new Builder("error", TextComponent.text("Control Point Error"))
                    .max(100)
                    .style(BossbarStyle.PROGRESS)
                    .build();
        }
    }

    // --- 🔍 Accessors ---

    /** * @return The unique string identifier. */
    public String getId() {
        return id;
    }

    /** * @return The display name {@link TextComponent}. */
    public TextComponent getName() {
        return name;
    }

    /** * @return An {@link Optional} containing the max value if configured. */
    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }

    /** * @return An {@link Optional} containing the {@link BossbarStyle} if configured. */
    public Optional<BossbarStyle> getStyle() {
        return Optional.ofNullable(style);
    }

    // --- 🛠️ Builder Pattern ---

    /**
     * 🛠️ **Bossbar Builder**
     * <p>Facilitates the creation of BossbarData instances while ensuring
     * mandatory fields are present and optional fields are handled without nulls.</p>
     */
    public static class Builder {
        private final String id;
        private final TextComponent name;
        private Integer max;
        private BossbarStyle style;

        /**
         * Standard builder constructor for mandatory fields.
         * @param id   Unique ID.
         * @param name Display component.
         */
        public Builder(String id, TextComponent name) {
            this.id = Objects.requireNonNull(id, "Bossbar ID is required.");
            this.name = Objects.requireNonNull(name, "Bossbar name is required.");
        }

        /**
         * Configures the maximum progress value.
         * @param max The integer limit for the bar.
         * @return The current builder instance.
         */
        public Builder max(int max) {
            this.max = max;
            return this;
        }

        /**
         * Configures the visual style/segmentation.
         * @param style The desired {@link BossbarStyle}.
         * @return The current builder instance.
         * @throws NullPointerException if style is null.
         */
        public Builder style(BossbarStyle style) {
            this.style = Objects.requireNonNull(style, "Bossbar style cannot be null.");
            return this;
        }

        /**
         * Finalizes the construction process.
         * @return A new {@link BossbarData} instance.
         */
        public BossbarData build() {
            try {
                return new BossbarData(id, name, max, style);
            } catch (Exception e) {
                // Final safety check during assembly
                return new BossbarData(id, name, 100, BossbarStyle.PROGRESS);
            }
        }
    }
}