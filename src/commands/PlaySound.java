package commands;

import shared.SoundSource;
import arguments.Entity;
import arguments.Vec3;
import shared.SoundId;

public class PlaySound {
    private final SoundId sound;
    private SoundSource source;
    private Entity targets;
    private Vec3 pos;
    private Float volume;
    private Float pitch;
    private Float minVolume;

    private PlaySound(SoundId sound) {
        this.sound = sound;
    }

    public static PlaySound create(SoundId sound) {
        return new PlaySound(sound);
    }

    public PlaySound source(SoundSource source) {
        this.source = source;
        return this;
    }

    public PlaySound targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    public PlaySound pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public PlaySound volume(float volume) {
        // Validation for Java Edition
        if (volume < 0.0f) {
            throw new IllegalArgumentException("Volume must be greater than or equal to 0.0.");
        }
        this.volume = volume;
        return this;
    }

    public PlaySound volume(int volume) {
        // Validation for Java Edition
        if (volume < 0) {
            throw new IllegalArgumentException("Volume must be greater than or equal to 0.0.");
        }
        this.volume = (float)volume;
        return this;
    }

    public PlaySound pitch(float pitch) {
        // Validation for Java Edition (0.0 to 2.0)
        if (pitch < 0.0f || pitch > 2.0f) {
            throw new IllegalArgumentException("Pitch must be between 0.0 and 2.0 (inclusive).");
        }
        this.pitch = pitch;
        return this;
    }

    public PlaySound pitch(int pitch) {
        // Validation for Java Edition (0.0 to 2.0)
        if (pitch < 0 || pitch > 2) {
            throw new IllegalArgumentException("Pitch must be between 0.0 and 2.0 (inclusive).");
        }
        this.pitch = (float)pitch;
        return this;
    }

    public PlaySound minVolume(float minVolume) {
        // Validation for Java Edition (0.0 to 1.0)
        if (minVolume < 0.0f || minVolume > 1.0f) {
            throw new IllegalArgumentException("Minimum volume must be between 0.0 and 1.0 (inclusive).");
        }
        this.minVolume = minVolume;
        return this;
    }

    public PlaySound minVolume(int minVolume) {
        // Validation for Java Edition (0.0 to 1.0)
        if (minVolume < 0 || minVolume > 1) {
            throw new IllegalArgumentException("Minimum volume must be between 0.0 and 1.0 (inclusive).");
        }
        this.minVolume = (float)minVolume;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("playsound ");
        sb.append(sound.getResourceLocation());

        if (source != null) {
            sb.append(" ").append(source.getResourceName());
            if (targets != null) {
                sb.append(" ").append(targets);
                if (pos != null) {
                    sb.append(" ").append(pos);
                    if (volume != null) {
                        sb.append(" ").append(volume);
                        if (pitch != null) {
                            sb.append(" ").append(pitch);
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
}
