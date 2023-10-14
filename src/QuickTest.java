import HelperClasses.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class QuickTest {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();

        players.add(new Player("Snodog627", 114));
        players.add(new Player("Infima", 8));
        players.add(new Player("Thatepicpotato", 71));
        players.add(new Player("Joker447xd9", 18));
        players.add(new Player("ICEturbo", 21));
        players.add(new Player("mrtkrl", 13));
        players.add(new Player("Maurcy", 38));
        players.add(new Player("Correawesome", 16));
        players.add(new Player("thekillertb", 0));
        players.add(new Player("Greyhalfbuster", 17));
        players.add(new Player("xPromachos", 10));
        players.add(new Player("BorisBeast", 5));

        try {
            File file = new File("Files\\KINJIN\\players.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Player player: players) {
                writer.write(player.getPlayerName() + ',' + player.getRank() + ',' + player.getIgnoreTraitor());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {

        }

    }
}
