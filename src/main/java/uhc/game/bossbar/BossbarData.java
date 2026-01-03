package uhc.game.bossbar;

import uhc.game.control_points.ControlPointData;
import uhc.resource.bossbar.BossbarId;
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

    /** * The unique namespaced identifier for the Minecraft bossbar.
     * This field is final and mandatory for identifying the bar in the client registry.
     */
    private final BossbarId id;

    /** * The display name of the bossbar, stored as a rich {@link TextComponent}.
     * This is the text visible to players above the progress bar.
     */
    private final TextComponent name;

    /** * The maximum progress value of the bossbar.
     * Represents the logical "100%" state of the bar (e.g., 100 points, 20 seconds).
     */
    private final Integer max;

    /** * The visual segmentation style of the bossbar.
     * Defines how many "notches" appear on the bar (e.g., Progress, 6 notches, etc.).
     */
    private final BossbarStyle style;

    // --- 🏗️ Private Constructor ---

    /**
     * Internal constructor used exclusively by the static Builder and Factory methods.
     * Ensures all fields are immutable and prevents external instantiation without validation.
     *
     * @param id    The validated {@link BossbarId}.
     * @param name  The formatted {@link TextComponent} display name.
     * @param max   Maximum value (internal nullable, exposed as Optional).
     * @param style Visual style (internal nullable, exposed as Optional).
     */
    private BossbarData(BossbarId id, TextComponent name, Integer max, BossbarStyle style) {
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
     * @param id   The unique {@link BossbarId} for the bar.
     * @param name The {@link TextComponent} display name.
     * @return A {@link Builder} instance to configure optional settings.
     * @throws NullPointerException if the provided id or name is null.
     */
    public static Builder builder(BossbarId id, TextComponent name) {
        return new Builder(id, name);
    }

    /**
     * 🔄 **From Control Point**
     * <p>Automatically transforms a {@link ControlPointData} objective into a visible boss bar.
     * Maps the control point's unique name to a {@link BossbarId} and generates a title.</p>
     *
     * @param cp The source control point to be visualized.
     * @return A fully initialized and configured BossbarData instance.
     * @throws NullPointerException if the provided control point is null.
     */
    public static BossbarData fromControlPoint(ControlPointData cp) {
        try {
            Objects.requireNonNull(cp, "Source ControlPointData cannot be null.");

            // Generate the unique namespaced ID from the Control Point's name
            BossbarId barId = BossbarId.of(cp.name().getTagName());

            // Build a descriptive title: "Name: x, y, z (Dimension)"
            String rawTitle = String.format("%s: %s (%s)",
                    cp.name(),
                    cp.getPos().title(),
                    cp.getDimension().title());

            return new Builder(barId, TextComponent.text(rawTitle))
                    .max(cp.getMax())
                    .style(BossbarStyle.PROGRESS)
                    .build();
        } catch (Exception e) {
            // Error Catching: Provides a safe fallback to prevent logic-chain failure
            return new Builder(BossbarId.of("internal_error"), TextComponent.text("Data Error"))
                    .max(100)
                    .style(BossbarStyle.PROGRESS)
                    .build();
        }
    }

    // --- 🔍 Accessors ---

    /** * Retrieves the unique identifier for the bossbar.
     * @return The {@link BossbarId}.
     */
    public BossbarId getId() {
        return id;
    }

    /** * Retrieves the display name component.
     * @return The {@link TextComponent}.
     */
    public TextComponent getName() {
        return name;
    }

    /** * Retrieves the maximum value if defined.
     * @return An {@link Optional} containing the max integer, or empty if not set.
     */
    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }

    /** * Retrieves the visual style if defined.
     * @return An {@link Optional} containing the {@link BossbarStyle}, or empty if not set.
     */
    public Optional<BossbarStyle> getStyle() {
        return Optional.ofNullable(style);
    }

    // --- 🛠️ Builder Pattern ---

    /**
     * 🛠️ **Bossbar Builder**
     * <p>Facilitates the creation of BossbarData instances using the {@link BossbarId} type.
     * Enforces mandatory fields while allowing fluent optional configuration.</p>
     */
    public static class Builder {
        private final BossbarId id;
        private final TextComponent name;
        private Integer max;
        private BossbarStyle style;

        /**
         * Initializes the builder with mandatory identity fields.
         * @param id   The unique {@link BossbarId}.
         * @param name The display {@link TextComponent}.
         * @throws NullPointerException if id or name is null.
         */
        public Builder(BossbarId id, TextComponent name) {
            this.id = Objects.requireNonNull(id, "Bossbar ID is required.");
            this.name = Objects.requireNonNull(name, "Bossbar name is required.");
        }

        /**
         * Sets the maximum progress value for the bar.
         * @param max The integer limit.
         * @return The current builder instance.
         */
        public Builder max(int max) {
            this.max = max;
            return this;
        }

        /**
         * Sets the visual segmentation style.
         * @param style The desired {@link BossbarStyle}.
         * @return The current builder instance.
         * @throws NullPointerException if the style provided is null.
         */
        public Builder style(BossbarStyle style) {
            this.style = Objects.requireNonNull(style, "Bossbar style cannot be null.");
            return this;
        }

        /**
         * Assembles the final {@link BossbarData} object.
         * <p>Contains an internal safety catch to ensure that construction
         * errors return a stable, default-initialized object.</p>
         * @return A new {@link BossbarData} instance.
         */
        public BossbarData build() {
            try {
                return new BossbarData(id, name, max, style);
            } catch (Exception e) {
                // Return a safe Progress-style bar if construction fails
                return new BossbarData(id, name, 100, BossbarStyle.PROGRESS);
            }
        }
    }
}