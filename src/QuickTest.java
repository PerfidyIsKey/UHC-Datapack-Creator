import HelperClasses.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class QuickTest {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();


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
