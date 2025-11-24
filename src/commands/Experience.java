package commands;

import shared.ExperienceType;
import arguments.Entity;
import commands.experience.ExperienceAction;

public class Experience {
    private final ExperienceAction action;
    private final Entity targets;
    private int amount;
    private ExperienceType type;

    private Experience(ExperienceAction action, Entity targets) {
        this.action = action;
        this.targets = targets;
    }

    public static Experience create(ExperienceAction action, Entity targets) {
        return new Experience(action, targets);
    }

    public Experience amount(int amount) {
        this.amount = amount;
        return this;
    }

    public Experience type(ExperienceType type) {
        this.type = type;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("xp ");

        sb.append(action).append(" ").append(targets).append(" ");
        if (action == ExperienceAction.ADD || action == ExperienceAction.SET) {
            sb.append(amount).append(" ").append(type);
        }
        else {
            sb.append(type);
        }

        return sb.toString();
    }
}
