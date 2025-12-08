package shared;

public enum FillMode {
    REPLACE("replace"),
    OUTLINE("outline"),
    HOLLOW("hollow"),
    DESTROY("destroy"),
    KEEP("keep"),
    STRICT("strict");

    private final String keyword;

    FillMode(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
