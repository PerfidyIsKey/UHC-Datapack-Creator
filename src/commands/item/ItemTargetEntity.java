package commands.item;

import arguments.Entity;

public class ItemTargetEntity implements ItemTarget {
    private final Entity targets;
    private ItemTargetEntity(Entity targets) { this.targets = targets; }

    public static ItemTargetEntity create(Entity targets) {
        return new ItemTargetEntity(targets);
    }

    @Override
    public String getCommandString() {
        // The actual arguments.Entity class handles the raw selector string via its toString() method.
        return "entity " + targets.toString();
    }
}