package arguments;

public class BlockPos {

    private final String x;
    private final String y;
    private final String z;

    private BlockPos(String x, String y, String z) {
        if (!isValidCoordinate(x) || !isValidCoordinate(y) || !isValidCoordinate(z)) {
            throw new IllegalArgumentException("Invalid coordinate format");
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos absolute(int x, int y, int z)  {
        return new BlockPos("" + x, "" + y, "" + z);
    }

    public static BlockPos absolute(int[] pos)  {
        return new BlockPos("" + pos[0], "" + pos[1], "" + pos[2]);
    }

    public static BlockPos relative(int x, int y, int z)  {
        return new BlockPos("~" + x, "~" + y, "~" + z);
    }

    public static BlockPos local(int x, int y, int z)  {
        return new BlockPos("^" + x, "^" + y, "^" + z);
    }

    private boolean isValidCoordinate(String value) {
        return value.matches("~?\\^?-?\\d*"); // allows 0, -1, ~, ~1, ^2 etc.
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}
