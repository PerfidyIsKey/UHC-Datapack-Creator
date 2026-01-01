package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.entity.TargetSelector;
import uhc.command.MinecraftCommand;
import uhc.resource.advancement.AdvancementId;

import java.util.Objects;

/**
 * 🏆 **Advancement Command Builder**
 * <p>
 * This class provides static factory methods to generate {@code /advancement} commands.
 * It enforces Minecraft's specific syntax rules for granting or revoking advancements
 * through typed methods, ensuring mutually exclusive modes cannot be mixed.
 * </p>
 * <p>
 * Supported syntaxes include targeting everything, specific advancements, or
 * branches (from, through, until).
 * </p>
 */
public final class AdvancementCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The specific operation to perform: {@code grant} or {@code revoke}. */
    private final AdvancementAction action;

    /** * The entities targeted by this command (e.g., @a, @s, or specific UUIDs). */
    private final Entity targets;

    /** * The logical scope of the command, such as {@code everything} or {@code only}. */
    private final AdvancementMode mode;

    /** * The specific advancement resource identifier. Nullable if mode is {@code EVERYTHING}. */
    private final AdvancementId advancement;

    /** * A specific criterion key within the advancement. Exclusive to {@code ONLY} mode. */
    private final String criterion;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor used by static factory methods to create a validated command state.
     *
     * @param action      The operation type.
     * @param targets     The target entities.
     * @param mode        The scope mode.
     * @param advancement The target advancement ID (nullable).
     * @param criterion   The specific criterion (nullable).
     * @throws NullPointerException if mandatory arguments (action, targets, mode) are null.
     */
    private AdvancementCommand(AdvancementAction action, Entity targets, AdvancementMode mode,
                               AdvancementId advancement, String criterion) {
        this.action = Objects.requireNonNull(action, "Command action (grant/revoke) cannot be null.");
        this.targets = Objects.requireNonNull(targets, "Command targets cannot be null.");
        this.mode = Objects.requireNonNull(mode, "Advancement mode cannot be null.");
        this.advancement = advancement;
        this.criterion = criterion;
    }

    // --- 🚀 Static Factory Methods (Entry Points) ---

    /**
     * Creates a command to target all advancements.
     * <p>Syntax: {@code advancement <action> <targets> everything}</p>
     *
     * @param action  The operation (GRANT/REVOKE).
     * @param targets The target entities.
     * @return A generated {@link AdvancementCommand} instance.
     */
    public static AdvancementCommand everything(AdvancementAction action, Entity targets) {
        try {
            return new AdvancementCommand(action, targets, AdvancementMode.EVERYTHING, null, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Creates a command to target one specific advancement.
     * <p>Syntax: {@code advancement <action> <targets> only <advancement>}</p>
     *
     * @param action      The operation (GRANT/REVOKE).
     * @param targets     The target entities.
     * @param advancement The target advancement identifier.
     * @return A generated {@link AdvancementCommand} instance.
     */
    public static AdvancementCommand only(AdvancementAction action, Entity targets, AdvancementId advancement) {
        try {
            Objects.requireNonNull(advancement, "Advancement ID is required for 'only' mode.");
            return new AdvancementCommand(action, targets, AdvancementMode.ONLY, advancement, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Creates a command to target a specific criterion within an advancement.
     * <p>Syntax: {@code advancement <action> <targets> only <advancement> <criterion>}</p>
     *
     * @param action      The operation (GRANT/REVOKE).
     * @param targets     The target entities.
     * @param advancement The target advancement identifier.
     * @param criterion   The specific criterion key.
     * @return A generated {@link AdvancementCommand} instance.
     */
    public static AdvancementCommand only(AdvancementAction action, Entity targets,
                                          AdvancementId advancement, String criterion) {
        try {
            Objects.requireNonNull(advancement, "Advancement ID is required for 'only' mode.");
            Objects.requireNonNull(criterion, "Criterion name is required for this overload.");
            return new AdvancementCommand(action, targets, AdvancementMode.ONLY, advancement, criterion);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Creates a command to target an advancement and all of its children.
     * <p>Syntax: {@code advancement <action> <targets> from <advancement>}</p>
     */
    public static AdvancementCommand from(AdvancementAction action, Entity targets, AdvancementId advancement) {
        try {
            Objects.requireNonNull(advancement, "Advancement ID is required for 'from' mode.");
            return new AdvancementCommand(action, targets, AdvancementMode.FROM, advancement, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Creates a command to target an advancement, its parents, and its children.
     * <p>Syntax: {@code advancement <action> <targets> through <advancement>}</p>
     */
    public static AdvancementCommand through(AdvancementAction action, Entity targets, AdvancementId advancement) {
        try {
            Objects.requireNonNull(advancement, "Advancement ID is required for 'through' mode.");
            return new AdvancementCommand(action, targets, AdvancementMode.THROUGH, advancement, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * Creates a command to target an advancement and all of its parents.
     * <p>Syntax: {@code advancement <action> <targets> until <advancement>}</p>
     */
    public static AdvancementCommand until(AdvancementAction action, Entity targets, AdvancementId advancement) {
        try {
            Objects.requireNonNull(advancement, "Advancement ID is required for 'until' mode.");
            return new AdvancementCommand(action, targets, AdvancementMode.UNTIL, advancement, null);
        } catch (Exception e) {
            return fallback();
        }
    }

    // --- 🛠️ Logic & Generation ---

    /**
     * Assembles the final command string.
     * Handles optional arguments like {@code AdvancementId} and criteria based on the selected mode.
     *
     * @return The formatted Minecraft command string.
     */
    @Override
    public String generate() {
        try {
            StringBuilder sb = new StringBuilder("advancement ");
            sb.append(this.action).append(" ").append(this.targets).append(" ").append(this.mode);

            if (this.advancement != null) {
                sb.append(" ").append(this.advancement);

                // Criteria are only syntactically valid in 'only' mode.
                if (this.mode == AdvancementMode.ONLY && this.criterion != null) {
                    sb.append(" ").append(this.criterion);
                }
            }

            return sb.toString();
        } catch (Exception e) {
            // Error Catching: Ensure the engine receives a safe fallback rather than a null or exception.
            return "/say Error: Failed to generate advancement command logic.";
        }
    }

    /**
     * Internal safety fallback to provide a non-destructive default command if initialization fails.
     *
     * @return A default command that revokes everything from the sender.
     */
    private static AdvancementCommand fallback() {
        return new AdvancementCommand(
                AdvancementAction.REVOKE,
                Entity.ofSelector(TargetSelector.SENDER),
                AdvancementMode.EVERYTHING,
                null,
                null
        );
    }

    /**
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }

    // --- ⚙️ Enums ---

    /**
     * Defines the operation type: GRANT (add progress) or REVOKE (remove progress).
     */
    public enum AdvancementAction {
        GRANT, REVOKE;
        @Override
        public String toString() { return name().toLowerCase(); }
    }

    /**
     * Defines the hierarchical scope of the command.
     */
    public enum AdvancementMode {
        EVERYTHING, ONLY, FROM, THROUGH, UNTIL;
        @Override
        public String toString() { return name().toLowerCase(); }
    }
}