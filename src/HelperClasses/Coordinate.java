package HelperClasses;

import Enums.Dimension;

public class Coordinate {

    private int x;
    private int y;
    private int z;
    private Enums.Dimension dimension;

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = Dimension.overworld;
    }

    public Coordinate(int x, int y, int z, Dimension dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public String getCoordinate() {
        return this.x + " " + this.y + " " + this.z;
    }
}
