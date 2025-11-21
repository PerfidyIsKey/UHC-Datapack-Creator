package arguments.coordinate;

public class Vec3 {

    private final String x;
    private final String y;
    private final String z;

    private Vec3(String x, String y, String z) {
        if (!isValidCoordinate(x) || !isValidCoordinate(y) || !isValidCoordinate(z)) {
            throw new IllegalArgumentException("Invalid coordinate format");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3 create(String x, String y, String z) {
        return new Vec3(x, y, z);
    }

    public static Vec3 create(int x, int y, int z) {
        return new Vec3("" + x, "" + y, "" + z);
    }

    public static Vec3 create(double x, double y, double z) {
        return new Vec3("" + x, "" + y, "" + z);
    }

    private boolean isValidCoordinate(String value) {
        return value.matches("~?\\^?-?\\d*"); // allows 0, -1, ~, ~1, ^2 etc.
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

}
