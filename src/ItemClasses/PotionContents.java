package ItemClasses;

import Enums.Duration;
import Enums.Effect;

public class PotionContents implements Components {
    // Fields
    private Effect id; // The ID of the effect.
    private int custom_color; // The overriding color of this potion texture, and/or the particles of the area effect cloud created.
    private int amplifier; // The amplifier of the effect, with level I having value 0.
    private int duration;   // The duration of the effect in ticks. Value -1 is treated as infinity. Values 0 or less than -2 are treated as 1.
    private Boolean ambient; // Whether or not this is an effect provided by a beacon and therefore should be less intrusive on the screen.
    private Boolean show_particles; // Whether or not this effect produces particles.
    private Boolean show_icon; // Whether or not an icon should be shown for this effect.

    // Constructors
    public PotionContents(Effect id, int amplifier, int duration) {
        this.id = id;
        this.amplifier = amplifier;
        this.duration = duration * 20;
    }
    public PotionContents(Effect id, int amplifier, int duration, Duration durationType) {
        this.id = id;
        this.amplifier = amplifier;
        int unitConversion = 1;
        if (durationType == Duration.seconds) {
            unitConversion = 20;
        }
        this.duration = duration * unitConversion;
    }

    public PotionContents(Effect id, int amplifier, int duration, String custom_color) {
        this.id = id;
        this.amplifier = amplifier;
        this.duration = duration * 20;
        this.custom_color = Integer.parseInt(custom_color, 16);
    }

    public PotionContents(Effect id, int amplifier, int duration, String custom_color, Boolean ambient, Boolean show_particles, Boolean show_icon) {
        this.id = id;
        this.amplifier = amplifier;
        this.duration = duration * 20;
        this.custom_color = Integer.parseInt(custom_color, 16);
        this.ambient = ambient;
        this.show_particles = show_particles;
        this.show_icon = show_icon;
    }

    // Make NBT tag
    public String GenerateNBT() {
        return "";
    }

    // Make component tag
    public String GenerateComponent() {
        // Optional fields
        // Color
        String colorContent = "";
        if (custom_color != 0) {
            colorContent = "\"custom_color\":" + custom_color + ",\n";
        }

        // Booleans
        String boolContent = "";
        if (ambient != null) {
            boolContent = ",\n" +
                    "\"ambient\":" + ambient + ",\n" +
                    "\"show_particles\":" + show_particles + ",\n" +
                    "\"show_icon\":" + show_icon + "\n";
        }

        return "\"potion_contents\":{\n" +
                colorContent +
                "\"custom_effects\":[\n" +
                "{\n" +
                "\"id\":\"" + id + "\",\n" +
                "\"amplifier\":" + amplifier + ",\n" +
                "\"duration\":" + duration +
                boolContent +
                "}\n" +
                "]\n" +
                "}\n";
    }
}
