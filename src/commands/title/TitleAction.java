package commands.title;

public enum TitleAction {
    CLEAR,
    RESET;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
