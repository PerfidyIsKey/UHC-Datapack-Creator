package HelperClasses;

import Enums.Sound;
import Enums.SoundSource;

public class PlaySound {
    private Sound sound;
    private SoundSource source;
    private String entity;
    private String x;
    private String y;
    private String z;
    private String x1;
    private String y1;
    private String z1;

    public PlaySound(Sound sound, SoundSource source, String entity, String x, String y, String z, String x1, String y1, String z1) {
        this.sound = sound;
        this.source = source;
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }

    public String getSound() {
        return "playsound " + sound.getValue() + " " + source + " " + entity + " " + x + " " + y + " " + z + " " + x1 + " " + y1 + " " + z1;
    }

}
