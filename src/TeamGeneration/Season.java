package TeamGeneration;

import java.util.Date;

public class Season {

    private double ID;
    private int numberOfPlayers;
    private Date date;

    public Season(double ID, int numberOfPlayers) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
    }

    public Season(double ID, int numberOfPlayers, Date date) {
        this.ID = ID;
        this.numberOfPlayers = numberOfPlayers;
        this.date = date;
    }

    public double getID() {
        return ID;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Date getDate() {
        return date;
    }
}
