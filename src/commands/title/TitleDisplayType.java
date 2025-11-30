package commands.title;

public enum TitleDisplayType {
    TITLE,
    SUBTITLE,
    ACTIONBAR;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
