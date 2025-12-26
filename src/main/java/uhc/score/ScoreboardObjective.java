package uhc.score;

import uhc.text.TextComponent;
import java.util.Objects;

/**
 * 🏆 **Scoreboard Objective Data Model**
 * <p>
 * Holds the complete configuration for a Minecraft scoreboard objective.
 * Includes required identifiers and optional display settings such as sidebar visibility.
 * </p>
 */
public class ScoreboardObjective {

    /** The unique internal identifier for the objective. */
    private final ScoreboardObjectiveId id;

    /** The tracking logic (e.g., dummy, health). */
    private final ScoreboardCriteria criteria;

    /** The optional JSON display name. */
    private TextComponent displayName;

    /** Determines if the score is shown as hearts or integers. */
    private ListRenderType renderType = ListRenderType.INTEGER;

    /** Flag for modern Minecraft automatic display updates. */
    private boolean displayAutoUpdate = false;

    /** * Flag indicating if this objective should be displayed in the sidebar slot.
     * This is an optional internal property used for automated registration.
     */
    private boolean displaySidebar = false;

    // --- 🏗️ Constructor ---

    /**
     * Creates a new objective configuration.
     * @param id The non-null objective ID.
     * @param criteria The non-null criteria.
     * @throws NullPointerException if id or criteria is null.
     */
    public ScoreboardObjective(ScoreboardObjectiveId id, ScoreboardCriteria criteria) {
        this.id = Objects.requireNonNull(id, "Scoreboard objective ID cannot be null.");
        this.criteria = Objects.requireNonNull(criteria, "Scoreboard criteria cannot be null.");
    }

    // --- 🛰️ Fluent Optional Setters ---

    /**
     * Sets the display name for the objective.
     * @param name The rich text component.
     * @return This instance.
     */
    public ScoreboardObjective displayName(TextComponent name) {
        this.displayName = name;
        return this;
    }

    /**
     * Sets the render style in the player list.
     * @param type The render type.
     * @return This instance.
     * @throws NullPointerException if type is null.
     */
    public ScoreboardObjective renderType(ListRenderType type) {
        this.renderType = Objects.requireNonNull(type, "Render type cannot be null.");
        return this;
    }

    /**
     * Configures automatic display updates.
     * @param autoUpdate True for automatic.
     * @return This instance.
     */
    public ScoreboardObjective autoUpdate(boolean autoUpdate) {
        this.displayAutoUpdate = autoUpdate;
        return this;
    }

    /**
     * Marks whether this objective is intended for the sidebar display slot.
     * @param displaySidebar True to flag for sidebar display.
     * @return This instance.
     */
    public ScoreboardObjective displaySidebar(boolean displaySidebar) {
        this.displaySidebar = displaySidebar;
        return this;
    }

    // --- 🔍 Getters ---

    public ScoreboardObjectiveId getId() { return id; }
    public ScoreboardCriteria getCriteria() { return criteria; }
    public TextComponent getDisplayName() { return displayName; }
    public ListRenderType getRenderType() { return renderType; }
    public boolean isDisplayAutoUpdate() { return displayAutoUpdate; }

    /** @return True if this objective is configured to appear in the sidebar. */
    public boolean isDisplaySidebar() { return displaySidebar; }

    // --- 📦 Inner Types ---

    public enum ListRenderType {
        INTEGER, HEARTS;
        @Override public String toString() { return name().toLowerCase(); }
    }
}