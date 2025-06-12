public final class Singleton {
    private static Singleton INSTANCE;

    private int minToCPScore;

    public static Singleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Singleton();
        }

        return INSTANCE;
    }

    public int getMinToCPScore() {
        return minToCPScore;
    }

    public void setMinToCPScore(int minToCPScore) {
        this.minToCPScore = minToCPScore;
    }
}
