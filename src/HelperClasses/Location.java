package HelperClasses;

public class Location {
    private String location;

    public Location(String location) {
        this.location = location;
    }

    public Location(int x, int y, int z) {
        this.location = x + " " + y + " " + z;
    }

    public String getLocation() {
        return location;
    }
}
