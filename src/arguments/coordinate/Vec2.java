package arguments.coordinate;

public class Vec2 {
    private final String x;
    private final String z;

    private Vec2(String x, String z) {
        if (!isValidCoordinate(x) || !isValidCoordinate(z)) {
            throw new IllegalArgumentException("Invalid coordinate format");
        }
        this.x = x;
        this.z = z;
    }

    public static Vec2 create(String x, String z) {
        return new Vec2(x, z);
    }

    public static Vec2 create(int x, int z) {
        return new Vec2("" + x, "" + z);
    }

    public static Vec2 create(double x, double z) {
        return new Vec2("" + x, "" + z);
    }

    private boolean isValidCoordinate(String value) {
        return value.matches("~?\\^?-?\\d*"); // allows 0, -1, ~, ~1, ^2 etc.
    }

    @Override
    public String toString() {
        return x + " " + z;
    }

}
