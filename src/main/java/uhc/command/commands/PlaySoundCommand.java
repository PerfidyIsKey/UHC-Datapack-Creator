package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.resource.sound.SoundSource;
import uhc.arguments.entity.Entity;
import uhc.arguments.coordinate.Vec3;
import uhc.resource.sound.SoundId;

import java.util.Objects;

/**
 * 🔊 **PlaySound Command Builder**
 * <p>
 * Provides a fluent API for the {@code /playsound} command.
 * Note that Minecraft's argument parser is positional: to specify a later
 * argument (like pitch), all preceding arguments must be provided.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /playsound <sound> <source> <targets> [<pos>] [<volume>] [<pitch>] [<minVolume>]}
 * </p>
 */
public class PlaySoundCommand implements MinecraftCommand {
    private final SoundId sound;
    private SoundSource source;
    private Entity targets;
    private Vec3 pos;
    private Float volume;
    private Float pitch;
    private Float minVolume;

    private PlaySoundCommand(SoundId sound) {
        this.sound = Objects.requireNonNull(sound, "SoundId cannot be null.");
    }

    /**
     * Initializes a new PlaySoundCommand builder.
     * @param sound The namespaced ID of the sound (e.g., minecraft:entity.lightning_bolt.thunder).
     */
    public static PlaySoundCommand create(SoundId sound) {
        return new PlaySoundCommand(sound);
    }

    /** Sets the category for the sound (e.g., master, music, voice, player). */
    public PlaySoundCommand source(SoundSource source) {
        this.source = source;
        return this;
    }

    /** Sets the player(s) who will hear the sound. */
    public PlaySoundCommand targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /** Sets the location from which the sound originates. */
    public PlaySoundCommand pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    /** * Sets the volume (distance the sound travels).
     * @param volume Must be >= 0.0. Values > 1.0 increase the distance, not the local loudness.
     */
    public PlaySoundCommand volume(float volume) {
        if (volume < 0.0f) {
            throw new IllegalArgumentException("Volume must be greater than or equal to 0.0.");
        }
        this.volume = volume;
        return this;
    }

    public PlaySoundCommand volume(int volume) {
        if (volume < 0) {
            throw new IllegalArgumentException("Volume must be greater than or equal to 0.0.");
        }
        this.volume = (float) volume;
        return this;
    }

    /** Sets the speed/pitch of the sound (0.0 to 2.0). Default is 1.0. */
    public PlaySoundCommand pitch(float pitch) {
        if (pitch < 0.0f || pitch > 2.0f) {
            throw new IllegalArgumentException("Pitch must be between 0.0 and 2.0 (inclusive).");
        }
        this.pitch = pitch;
        return this;
    }

    public PlaySoundCommand pitch(int pitch) {
        if (pitch < 0 || pitch > 2) {
            throw new IllegalArgumentException("Pitch must be between 0.0 and 2.0 (inclusive).");
        }
        this.pitch = (float) pitch;
        return this;
    }

    /** Sets the minimum volume for players outside the audible range (0.0 to 1.0). */
    public PlaySoundCommand minVolume(float minVolume) {
        if (minVolume < 0.0f || minVolume > 1.0f) {
            throw new IllegalArgumentException("Minimum volume must be between 0.0 and 1.0 (inclusive).");
        }
        this.minVolume = minVolume;
        return this;
    }

    public PlaySoundCommand minVolume(int minVolume) {
        if (minVolume < 0 || minVolume > 1) {
            throw new IllegalArgumentException("Minimum volume must be between 0.0 and 1.0 (inclusive).");
        }
        this.minVolume = (float) minVolume;
        return this;
    }

    /**
     * Generates the final command string.
     * @throws IllegalStateException if a positional argument is missing while a subsequent one is set.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("playsound ");
        sb.append(sound.getResourceLocation());

        // Positional Dependency Check
        // Minecraft requires source and targets before position, volume, etc.
        if (source == null && (targets != null || pos != null || volume != null)) {
            throw new IllegalStateException("SoundSource must be specified if targets, pos, or volume are set.");
        }
        if (targets == null && (pos != null || volume != null)) {
            throw new IllegalStateException("Targets must be specified if pos, volume, or pitch are set.");
        }

        if (source != null) {
            sb.append(" ").append(source.getResourceName());
            if (targets != null) {
                sb.append(" ").append(targets);

                // If we have targets, we check if we need to continue the chain
                if (pos != null || volume != null || pitch != null || minVolume != null) {
                    sb.append(" ").append(pos != null ? pos : "~ ~ ~");

                    if (volume != null || pitch != null || minVolume != null) {
                        sb.append(" ").append(volume != null ? volume : "1.0");

                        if (pitch != null || minVolume != null) {
                            sb.append(" ").append(pitch != null ? pitch : "1.0");

                            if (minVolume != null) {
                                sb.append(" ").append(minVolume);
                            }
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}