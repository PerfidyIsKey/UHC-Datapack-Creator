public final class Singleton {
    private static Singleton INSTANCE;

    private int minToCPScore;
    private String admin;

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

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
