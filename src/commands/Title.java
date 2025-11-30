package commands;

import arguments.Entity;
import arguments.time.VariableGameTime;
import commands.title.TitleAction;
import commands.title.TitleDisplayType;

public class Title {
    private final Entity targets;
    private TitleAction action;
    private TitleDisplayType type;
    private String title;
    private VariableGameTime fadeIn;
    private VariableGameTime stay;
    private VariableGameTime fadeOut;

    private Title(Entity targets) {
        this.targets = targets;
    }

    public static Title create(Entity targets) {
        return new Title(targets);
    }

    public Title action(TitleAction action) {
        this.action = action;
        return this;
    }

    public Title type(TitleDisplayType type) {
        this.type = type;
        return this;
    }

    public Title title(String title) {
        this.title = title;
        return this;
    }

    public Title displayTime(VariableGameTime fadeIn, VariableGameTime stay, VariableGameTime fadeOut) {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    public Title defaultDisplayTime() {
        this.fadeIn = VariableGameTime.tick(10);
        this.stay = VariableGameTime.tick(70);
        this.fadeOut = VariableGameTime.tick(20);
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("title ");

        sb.append(targets).append(" ");
        if (action != null) {
            sb.append(action);
        } else if (type != null) {
            sb.append(type).append(" ").append(title);
        } else if (fadeIn != null) {
            sb.append("times").append(" ").append(fadeIn).append(" ").append(stay).append(" ").append(fadeOut);
        }

        return sb.toString();
    }
}
