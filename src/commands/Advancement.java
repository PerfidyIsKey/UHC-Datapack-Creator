package commands;

import uhc.arguments.entity.Entity;
import uhc.resource.advancement.AdvancementResourceLocation;
import uhc.arguments.targetselector.TargetSelector;
import commands.advancement.AdvancementAction;
import commands.advancement.AdvancementMode;

/**
 * Fluent builder for the Minecraft /advancement command, ensuring assignment safety.
 * This class supports all five sub-commands: everything, only, from, through, and until.
 */
public class Advancement {
    private final AdvancementAction action;
    private final Entity targets;

    // Command scope
    private AdvancementMode mode; // Stores the command keyword (e.g., "everything", "only")
    private boolean requiresAdvancementId;

    // Arguments required by specific modes.
    private AdvancementResourceLocation advancement; // Type-safe advancement resource location
    private String criterion;           // Optional for ONLY

    private Advancement(AdvancementAction action, Entity targets) {
        if (action == null) {
            throw new IllegalArgumentException("AdvancementAction cannot be null.");
        }
        if (targets == null) {
            throw new IllegalArgumentException("Targets entity argument cannot be null.");
        }
        this.action = action;
        this.targets = targets;
    }

    public static Advancement create(AdvancementAction action, Entity targets) {
        return new Advancement(action, targets);
    }

    // --- Type-Safe Examples ---

    /**
     * Example: To grant the "Cover Me With Diamonds" advancement to the player:
     * /advancement grant @s only minecraft:story/shiny_gear
     */
    public static String grantShinyGearToSelf() {
        Entity self = Entity.ofSelector(TargetSelector.SENDER);
        AdvancementResourceLocation shinyGear = AdvancementResourceLocation.story("shiny_gear");

        return Advancement.create(AdvancementAction.GRANT, self)
                .only(shinyGear)
                .build();
    }

    /**
     * Example: To grant all advancements to every player:
     * /advancement grant @a everything
     */
    public static String grantAllToAllPlayers() {
        Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

        return Advancement.create(AdvancementAction.GRANT, allPlayers)
                .everything()
                .build();
    }

    /**
     * Example: To revoke "Cover Me With Diamonds" advancement from the player:
     * /advancement revoke @s only minecraft:story/shiny_gear
     */
    public static String revokeShinyGearFromSelf() {
        Entity self = Entity.ofSelector(TargetSelector.SENDER);
        AdvancementResourceLocation shinyGear = AdvancementResourceLocation.story("shiny_gear");

        return Advancement.create(AdvancementAction.REVOKE, self)
                .only(shinyGear)
                .build();
    }

    /**
     * Example: To revoke all advancements from every player:
     * /advancement revoke @a everything
     */
    public static String revokeAllFromAllPlayers() {
        Entity allPlayers = Entity.ofSelector(TargetSelector.ALL_PLAYERS);

        return Advancement.create(AdvancementAction.REVOKE, allPlayers)
                .everything()
                .build();
    }

    // --- Mode Setters ---

    /**
     * Sets the mode to 'everything'. The command will grant/revoke all loaded advancements.
     */
    public Advancement everything() {
        setMode(AdvancementMode.EVERYTHING, false);
        return this;
    }

    /**
     * Sets the mode to 'only' and specifies the target advancement ID.
     * Optionally call .criterion(String) after this to target a specific criterion.
     */
    public Advancement only(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.ONLY, true);
        this.advancement = advancement;
        return this;
    }

    /**
     * Sets the mode to 'from' and specifies the starting advancement ID.
     */
    public Advancement from(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.FROM, true);
        this.advancement = advancement;
        return this;
    }

    /**
     * Sets the mode to 'through' and specifies the center advancement ID.
     */
    public Advancement through(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.THROUGH, true);
        this.advancement = advancement;
        return this;
    }

    /**
     * Sets the mode to 'until' and specifies the ending advancement ID.
     */
    public Advancement until(AdvancementResourceLocation advancement) {
        setMode(AdvancementMode.UNTIL, true);
        this.advancement = advancement;
        return this;
    }

    // --- Optional Arguments ---

    /**
     * Sets an optional criterion. This is only valid when the mode is 'only'.
     * @param criterion The specific criterion string.
     * @throws IllegalStateException if the mode is not 'only'.
     */
    public Advancement criterion(String criterion) {
        if (mode != AdvancementMode.ONLY) {
            throw new IllegalStateException("Criterion can only be specified when the mode is set to 'only'.");
        }
        if (criterion == null || criterion.trim().isEmpty()) {
            throw new IllegalArgumentException("Criterion string cannot be null or empty.");
        }
        this.criterion = criterion;
        return this;
    }

    // --- Safety Helpers ---

    private void setMode(AdvancementMode mode, boolean requiresAdvancementId) {
        if (this.mode != null) {
            throw new IllegalStateException("Advancement mode is already set to '" + this.mode + "'. Only one mode can be selected.");
        }
        this.mode = mode;
        this.requiresAdvancementId = requiresAdvancementId;
    }


    /**
     * Builds the final /advancement command string, enforcing parameter requirements.
     * @throws IllegalStateException if the mode is not set or if required arguments for the mode are missing.
     */
    public String build() {
        if (mode == null) {
            throw new IllegalStateException("An advancement mode (everything, only, from, through, or until) must be set.");
        }

        StringBuilder sb = new StringBuilder("advancement ");

        // 1. Base command and mandatory arguments (action and targets)
        sb.append(action).append(" ").append(targets).append(" ");

        // 2. Append the mode keyword
        sb.append(mode);

        // 3. Append required arguments based on the mode
        if (requiresAdvancementId) {
            if (advancement == null) {
                throw new IllegalStateException("Mode '" + mode + "' requires an advancement resource location.");
            }
            sb.append(" ").append(advancement.toString());

            // 4. Handle optional criterion for 'only' mode
            if (mode == AdvancementMode.ONLY && criterion != null) {
                // Criterion is a greedy phrase
                sb.append(" ").append(criterion);
            }
        }

        return sb.toString();
    }
}