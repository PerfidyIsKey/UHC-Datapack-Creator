package commands;

import arguments.Entity;
import arguments.itempredicate.ItemPredicate;

public class Clear {
    private Entity targets;
    private ItemPredicate item;
    private int maxCount;

    private Clear() {}

    public static Clear create() { return new Clear(); }

    public Clear targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    public Clear item(ItemPredicate item) {
        this.item = item;
        return this;
    }

    public Clear maxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("clear");

        if (targets != null) {
            sb.append(" ").append(targets);
            if (item != null) {
                sb.append(" ").append(item);
                if (maxCount > 0)
                    sb.append(" ").append(maxCount);
            }
        }

        return sb.toString();
    }
}
