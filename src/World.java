import Enums.WorldShape;
import java.util.ArrayList;

public class World {
    private int size;
    private int height;
    private int bottom;
    private WorldShape shape;

    public World(int size, int height, int bottom, WorldShape shape) {
        this.size = size;
        this.height = height;
        this.bottom = bottom;
        this.shape = shape;
    }

    // World size
    public void setWorldSizeByPlayers(int players) {
        if (players <= 6) {
            this.size = 500;
        } else if (players <= 20) {
            this.size = 750;
        } else {
            this.size = 1000;
        }
    }

    // World pre-generation
    private String worldGenSetRadius() {
        return "chunky radius " + this.size;
    }

    private String worldGenSetShape() {
        return "chunky shape " + this.shape;
    }

    private String worldGenStart() {
        return "chunky start";
    }

    public ArrayList<String> worldGen() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(worldGenSetRadius());
        fileCommands.add(worldGenSetShape());
        fileCommands.add(worldGenStart());

        return fileCommands;
    }

    // Setters and getters
    public int getSize() {
        return size;
    }

    public int getFullSize() {
        return 2 * size;
    }
}
