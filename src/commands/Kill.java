package commands;

import arguments.Entity;

public class Kill {
    private Entity targets;

    private Kill() {}

    public static Kill create() { return new Kill(); }

    public Kill targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("kill");

        if (targets != null) {
            sb.append(" ").append(targets);
        }

        return sb.toString();
    }
}
