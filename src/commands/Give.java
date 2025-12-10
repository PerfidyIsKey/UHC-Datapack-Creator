package commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.itemstack.ItemStack;

public class Give {
    private final Entity targets;
    private final ItemStack item;
    private int count;

    private Give(Entity targets, ItemStack item) {
        this.targets = targets;
        this.item = item;
    }

    public static Give create(Entity targets, ItemStack item) {
        return new Give(targets, item);
    }

    public Give count(int count) {
        this.count = count;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("give ");

        // give <targets> <item>
        sb.append(targets).append(" ").append(item.build());

        // [<count>] - Only append if explicitly set to a value > 1 (since 1 is the default)
        // If the user calls count(1), it will be saved as 1, but we omit printing it.
        if (count > 1) {
            sb.append(" ").append(count);
        }

        return sb.toString();
    }

}
