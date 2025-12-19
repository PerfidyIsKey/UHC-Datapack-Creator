package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.advancement.AdvancementResourceLocation;
import uhc.arguments.entity.TargetSelector;

import java.util.Objects;

/**
 * 🏆 **Advancement Command Builder**
 * <p>
 * A fluent API for constructing {@code /advancement} commands. This builder enforces
 * Minecraft's command syntax rules, ensuring that mutually exclusive modes (like 'everything'
 * vs 'only') cannot be combined and that required resource locations are present.
 * </p>
 */
public class AdvancementCommand implements MinecraftCommand {
    private final AdvancementAction action;
    private final Entity targets;

    // Command scope
    private AdvancementMode mode;
    private boolean requiresAdvancementId;

    // Arguments required by specific modes.
    private AdvancementResourceLocation advancement;
    private String criterion;

    private AdvancementCommand(AdvancementAction action, Entity targets) {
        // Enforce non-nullability at the root of the builder
        this.action = Objects.requireNonNull(action, "AdvancementAction cannot be null.");
        this.targets = Objects.requireNonNull(targets, "Targets entity argument cannot be null.");
    }

    /**
     * Initializes a new AdvancementCommand.
     * @param action The operation to perform (GRANT or REVOKE).
     * @param targets The entities to target.
     * @return A new builder instance.
     */
    public static AdvancementCommand create(AdvancementAction action, Entity targets) {
        return new AdvancementCommand(action, targets);
    }

    // --- Type-Safe Examples ---

    /**
     * Example: Grant "Cover Me With Diamonds" to self.
     * {@code /advancement grant @s only minecraft:story/shiny_gear}
     */
    public static String grantShinyGearToSelf() {
        Entity self = Entity.ofSelector(TargetSelector.SENDER);
        AdvancementResourceLocation shinyGear = AdvancementResourceLocation.story("shiny_gear");

        return AdvancementCommand.create(AdvancementAction.GRANT, self)
                .only(shinyGear)
                .generate();
    }

    /**
     * Example: Grant all advancements to every player.
     * {@code /advancement grant @a everything}
     */
    public static String grantAllToAllPlayers() {
        Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

        return AdvancementCommand.create(AdvancementAction.GRANT, allPlayers)
                .everything()
                .generate();
    }

    /**
     * Example: Revoke "Cover Me With Diamonds" from self.
     * {@code /advancement revoke @s only minecraft:story/shiny_gear}
     */
    public static String revokeShinyGearFromSelf() {
        Entity self = Entity.ofSelector(TargetSelector.SENDER);
        AdvancementResourceLocation shinyGear = AdvancementResourceLocation.story("shiny_gear");

        return AdvancementCommand.create(AdvancementAction.REVOKE, self)
                .only(shinyGear)
                .generate();
    }

    /**
     * Example: Revoke all advancements from every player.
     * {@code /advancement revoke @a everything}
     */
    public static String revokeAllFromAllPlayers() {
        Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

        return AdvancementCommand.create(AdvancementAction.REVOKE, allPlayers)
                .everything()
                .generate();
    }

    // --- Mode Setters ---

    /**
     * Targets all advancements.
     * <p>Syntax: {@code ... everything}</p>
     */
    public AdvancementCommand everything() {
        setMode(AdvancementMode.EVERYTHING, false);
        return this;
    }

    /**
     * Targets a single advancement.
     * <p>Syntax: {@code ... only <advancement> [<criterion>]}</p>
     */
    public AdvancementCommand only(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.ONLY, true);
        this.advancement = Objects.requireNonNull(advancement, "Advancement ID cannot be null for 'only' mode.");
        return this;
    }

    /**
     * Targets an advancement and all its children.
     * <p>Syntax: {@code ... from <advancement>}</p>
     */
    public AdvancementCommand from(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.FROM, true);
        this.advancement = Objects.requireNonNull(advancement, "Advancement ID cannot be null for 'from' mode.");
        return this;
    }

    /**
     * Targets an advancement, its children, and its parents (the whole branch).
     * <p>Syntax: {@code ... through <advancement>}</p>
     */
    public AdvancementCommand through(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.THROUGH, true);
        this.advancement = Objects.requireNonNull(advancement, "Advancement ID cannot be null for 'through' mode.");
        return this;
    }

    /**
     * Targets an advancement and all its parents.
     * <p>Syntax: {@code ... until <advancement>}</p>
     */
    public AdvancementCommand until(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.UNTIL, true);
        this.advancement = Objects.requireNonNull(advancement, "Advancement ID cannot be null for 'until' mode.");
        return this;
    }

    // --- Optional Arguments ---

    /**
     * Specifies a specific criterion within an advancement.
     * <b>Note:</b> Only applicable when using {@code only()}.
     * @param criterion The criterion name.
     * @throws IllegalStateException if mode is not {@code ONLY}.
     */
    public AdvancementCommand criterion(String criterion) {
        if (mode != AdvancementMode.ONLY) {
            throw new IllegalStateException("Criteria can only be applied to the 'only' mode.");
        }
        if (criterion == null || criterion.trim().isEmpty()) {
            throw new IllegalArgumentException("Criterion cannot be null or empty.");
        }
        this.criterion = criterion.trim();
        return this;
    }

    // --- Safety Helpers ---

    private void setMode(AdvancementMode mode, boolean requiresAdvancementId) {
        if (this.mode != null) {
            throw new IllegalStateException("Cannot change advancement mode once set. Current: " + this.mode);
        }
        this.mode = mode;
        this.requiresAdvancementId = requiresAdvancementId;
    }

    /**
     * Validates and generates the final command string.
     * @return The formatted Minecraft command.
     * @throws IllegalStateException if no mode was selected.
     */
    @Override
    public String generate() {
        if (mode == null) {
            throw new IllegalStateException("Advancement mode must be set (everything, only, from, through, or until).");
        }

        StringBuilder sb = new StringBuilder("advancement ");
        sb.append(action).append(" ").append(targets).append(" ").append(mode);

        if (requiresAdvancementId) {
            // This should ideally not be null if setters use Objects.requireNonNull, but kept for double-safety
            if (advancement == null) {
                throw new IllegalStateException("Mode '" + mode + "' requires an advancement ID.");
            }
            sb.append(" ").append(advancement);

            if (mode == AdvancementMode.ONLY && criterion != null) {
                sb.append(" ").append(criterion);
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /** Defines the action to take: GRANT or REVOKE. */
    public enum AdvancementAction {
        GRANT, REVOKE;
        @Override
        public String toString() { return name().toLowerCase(); }
    }

    /** Defines the scope of the advancement command. */
    public enum AdvancementMode {
        EVERYTHING("everything"),
        ONLY("only"),
        FROM("from"),
        THROUGH("through"),
        UNTIL("until");

        private final String modeName;
        AdvancementMode(String modeName) { this.modeName = modeName; }
        @Override
        public String toString() { return modeName; }
    }
}