package arguments.particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleArgumentBuilder {
    private final List<String> arguments = new ArrayList<>();

    private ParticleArgumentBuilder() {}

    public static ParticleArgumentBuilder create() {
        return new ParticleArgumentBuilder();
    }

    public ParticleArgumentBuilder color(float red, float green, float blue) {
        this.arguments.add("color:[" + red + "," + green + "," + blue + "]");
        return this;
    }

    public ParticleArgumentBuilder color(float[] rgb) {
        this.arguments.add("color:[" + rgb[0] + "," + rgb[1] + "," + rgb[2] + "]");
        return this;
    }

    public ParticleArgumentBuilder scale(float scale) {
        this.arguments.add("scale:" + scale);
        return this;
    }

    public String build() {
        if (arguments.isEmpty()) {
            return "";
        }
        // Format: {arg1=val1,arg2=val2}
        String content = String.join(",", arguments);
        return "{" + content + "}";
    }

}
