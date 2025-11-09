package ArgumentTypes;

public class BlockPredicate implements ArgumentType {
    private final String predicate;

    public BlockPredicate(String predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        return predicate;
    }

    public void Sync() {
    }
}
