package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.effect.EffectId;

import java.util.Objects;

/**
 * 🧪 **Effect Command Factory**
 * <p>
 * Provides a static interface for generating {@code /effect} commands.
 * This class uses method overloading to support the full spectrum of Minecraft
 * effect syntax without requiring external builder methods.
 * </p>
 */
public class EffectCommand implements MinecraftCommand {

    /** * The final generated command string. */
    private final String command;

    /**
     * Private constructor to store the pre-validated command string.
     * @param command The validated Minecraft command.
     */
    private EffectCommand(String command) {
        this.command = command;
    }

    // --- 🧹 CLEAR ACTIONS ---

    /**
     * Generates: {@code /effect clear}
     * Clears all effects from all entities.
     * @return A new {@link EffectCommand} instance.
     */
    public static EffectCommand clear() {
        return new EffectCommand("effect clear");
    }

    /**
     * Generates: {@code /effect clear <targets>}
     * @param targets The target entity; must not be null.
     * @return A new {@link EffectCommand} instance.
     */
    public static EffectCommand clear(Entity targets) {
        Objects.requireNonNull(targets, "Effect Clear Error: Targets cannot be null.");
        return new EffectCommand("effect clear " + targets);
    }

    /**
     * Generates: {@code /effect clear <targets> <effect>}
     * @param targets The target entity; must not be null.
     * @param effect  The specific effect ID to remove; must not be null.
     * @return A new {@link EffectCommand} instance.
     */
    public static EffectCommand clear(Entity targets, EffectId effect) {
        Objects.requireNonNull(targets, "Effect Clear Error: Targets cannot be null.");
        Objects.requireNonNull(effect, "Effect Clear Error: Effect ID cannot be null.");
        return new EffectCommand("effect clear " + targets + " " + effect);
    }

    // --- 💊 GIVE ACTIONS ---

    /**
     * Generates: {@code /effect give <targets> <effect>}
     * Defaults to 30 seconds, amplifier 0, particles visible.
     */
    public static EffectCommand give(Entity targets, EffectId effect) {
        return assembleGive(targets, effect, null, null, null);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> <seconds>}
     */
    public static EffectCommand give(Entity targets, EffectId effect, int seconds) {
        return assembleGive(targets, effect, String.valueOf(validateSeconds(seconds)), null, null);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> infinite}
     */
    public static EffectCommand giveInfinite(Entity targets, EffectId effect) {
        return assembleGive(targets, effect, "infinite", null, null);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> <seconds> <amplifier>}
     */
    public static EffectCommand give(Entity targets, EffectId effect, int seconds, int amplifier) {
        return assembleGive(targets, effect, String.valueOf(validateSeconds(seconds)), validateAmplifier(amplifier), null);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> infinite <amplifier>}
     */
    public static EffectCommand giveInfinite(Entity targets, EffectId effect, int amplifier) {
        return assembleGive(targets, effect, "infinite", validateAmplifier(amplifier), null);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> <seconds> <amplifier> <hideParticles>}
     */
    public static EffectCommand give(Entity targets, EffectId effect, int seconds, int amplifier, boolean hideParticles) {
        return assembleGive(targets, effect, String.valueOf(validateSeconds(seconds)), validateAmplifier(amplifier), hideParticles);
    }

    /**
     * Generates: {@code /effect give <targets> <effect> infinite <amplifier> <hideParticles>}
     */
    public static EffectCommand giveInfinite(Entity targets, EffectId effect, int amplifier, boolean hideParticles) {
        return assembleGive(targets, effect, "infinite", validateAmplifier(amplifier), hideParticles);
    }

    // --- 🛠️ Internal Logic & Validation ---

    /**
     * Internal assembly logic to ensure strict parameter ordering.
     */
    private static EffectCommand assembleGive(Entity targets, EffectId effect, String duration, Integer amp, Boolean hide) {
        Objects.requireNonNull(targets, "Effect Give Error: Targets cannot be null.");
        Objects.requireNonNull(effect, "Effect Give Error: Effect ID cannot be null.");

        StringBuilder sb = new StringBuilder("effect give ").append(targets).append(" ").append(effect);

        if (duration != null) {
            sb.append(" ").append(duration);
            if (amp != null) {
                sb.append(" ").append(amp);
                if (hide != null) {
                    sb.append(" ").append(hide);
                }
            }
        }
        return new EffectCommand(sb.toString());
    }

    private static int validateSeconds(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Effect Error: Seconds must be > 0. Use giveInfinite() for permanent effects.");
        }
        return seconds;
    }

    private static int validateAmplifier(int amplifier) {
        if (amplifier < 0 || amplifier > 255) {
            throw new IllegalArgumentException("Effect Error: Amplifier must be between 0 and 255.");
        }
        return amplifier;
    }

    // --- ⚙️ Command Implementation ---

    /**
     * Returns the pre-constructed command string.
     * @return A valid Minecraft command.
     */
    @Override
    public String generate() {
        return this.command;
    }

    /**
     * Returns the command string.
     * @return Result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}