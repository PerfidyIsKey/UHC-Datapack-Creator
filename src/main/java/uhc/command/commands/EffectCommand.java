package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.EffectId;

/**
 * Represents the Minecraft {@code /effect} command.
 * <p>
 * This class provides a fluent API for constructing the command, supporting
 * both {@code clear} and {@code give} actions with optional parameters like
 * duration, amplifier, and particle visibility.
 * </p>
 */
public class EffectCommand implements MinecraftCommand {
    private final EffectAction action;
    private Entity targets;
    private EffectId effect;
    private int seconds;
    private int amplifier;
    private boolean hideParticles;

    /**
     * Private constructor to enforce use of the static factory method.
     * @param action The required {@code EffectAction} (GIVE or CLEAR).
     */
    private EffectCommand(EffectAction action) {
        this.action = action;
    }

    /**
     * Factory method to begin constructing an {@code EffectCommand}.
     * @param action The initial action to perform (GIVE or CLEAR).
     * @return A new instance of {@code EffectCommand}.
     */
    public static EffectCommand create(EffectAction action) {
        return new EffectCommand(action);
    }

    /**
     * Sets the target entity/entities for the command.
     * Required for {@code GIVE} action. Optional for {@code CLEAR}.
     * @param targets The target entity (e.g., {@code @s}, {@code @a}, or a specific entity UUID).
     * @return The command builder instance for chaining.
     */
    public EffectCommand targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /**
     * Sets the specific effect ID to give or clear.
     * Required for {@code GIVE} action. Optional for {@code CLEAR} (to clear only one specific effect).
     * @param effect The effect ID (e.g., {@code resistance}, {@code regeneration}).
     * @return The command builder instance for chaining.
     */
    public EffectCommand effect(EffectId effect) {
        this.effect = effect;
        return this;
    }

    /**
     * Sets the duration of the effect in seconds.
     * Defaults to the value set here, or uses "infinite" if set to 0 (based on original logic).
     * @param seconds The duration.
     * @return The command builder instance for chaining.
     */
    public EffectCommand seconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    /**
     * Sets the effect strength amplifier (0 is level 1, 1 is level 2, etc.).
     * Optional for {@code GIVE} action. Defaults to 0 if omitted.
     * @param amplifier The amplifier level.
     * @return The command builder instance for chaining.
     */
    public EffectCommand amplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    /**
     * Sets whether the effect particles should be hidden.
     * Optional for {@code GIVE} action. Defaults to false if omitted.
     * @param hideParticles True to hide particles, false otherwise.
     * @return The command builder instance for chaining.
     */
    public EffectCommand hideParticles(boolean hideParticles) {
        this.hideParticles = hideParticles;
        return this;
    }

    /**
     * Generates the final Minecraft command string based on the set parameters.
     * @return A valid Minecraft command string.
     * @throws IllegalStateException if mandatory parameters (targets or effect) are missing for the {@code GIVE} action.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("effect ");

        // Append the action name (e.g., "clear" or "give")
        sb.append(action);

        switch (action) {
            case CLEAR:
                // Command syntax: /effect clear [targets] [effect]
                if (targets != null) {
                    sb.append(" ").append(targets);
                    if (effect != null) {
                        // Append specific effect ID to clear only that effect.
                        sb.append(" ").append(effect);
                    }
                }
                break;

            case GIVE:
                // --- MANDATORY PARAMETER CHECKING ---
                // Command syntax: /effect give <targets> <effect> [seconds] [amplifier] [hideParticles]
                if (targets == null) {
                    throw new IllegalStateException("Targets must be specified for the 'GIVE' effect action.");
                }
                if (effect == null) {
                    throw new IllegalStateException("Effect ID must be specified for the 'GIVE' effect action.");
                }

                // Append mandatory parameters
                sb.append(" ").append(targets).append(" ").append(effect);

                // --- OPTIONAL PARAMETER HANDLING (Bug in original logic retained) ---
                if (seconds != 0) {
                    // Append the specific duration if set by the user
                    sb.append(" ").append(seconds);
                }
                else {
                    // DANGER: ORIGINAL BUG RETAINED
                    // If seconds is 0, the original logic appends "infinite".
                    // This is NOT valid in Minecraft and will cause the command to fail.
                    // A fix would be to append a very large number or omit the parameter, but the original logic is preserved.
                    sb.append(" ").append("infinite");
                }

                // Amplifier and Hide Particles are conditional on previous parameters
                if (amplifier != 0) {
                    // Append amplifier if not default (0 means level 1, so 0 is omitted by original logic)
                    sb.append(" ").append(amplifier);

                    if (hideParticles) {
                        // Append particle hiding flag (true/false) only if amplifier was present
                        sb.append(" ").append(hideParticles);
                    }
                }
                break;
        }

        return sb.toString();
    }

    /**
     * Generates and returns the final Minecraft command string.
     * @return The final command string.
     */
    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines the available actions for the {@code /effect} command: CLEAR and GIVE.
     */
    public enum EffectAction {
        CLEAR("clear"),
        GIVE("give");

        private final String actionName;

        EffectAction(String actionName) {
            this.actionName = actionName;
        }

        /**
         * Returns the lowercase action name used in the command string (e.g., "clear", "give").
         */
        @Override
        public String toString() {
            return actionName;
        }
    }
}