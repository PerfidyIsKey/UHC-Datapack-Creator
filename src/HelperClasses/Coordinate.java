package HelperClasses;

import Enums.Dimension;
import Enums.ReferenceFrame;

public class Coordinate {

    private int x;
    private int y;
    private int z;
    private ReferenceFrame ref;
    private Enums.Dimension dimension;

    // Constructors
    public Coordinate() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.ref = ReferenceFrame.absolute;
        this.dimension = Dimension.overworld;
    }

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ref = ReferenceFrame.absolute;
        this.dimension = Dimension.overworld;
    }

    public Coordinate(int x, int y, int z, Dimension dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.ref = ReferenceFrame.absolute;
    }

    public Coordinate(int x, int y, int z, ReferenceFrame ref) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = Dimension.overworld;
        this.ref = ref;
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

    public void setCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getCoordinateString() {
        String prefix = "";
        if (this.ref == ReferenceFrame.relative)
        {
            prefix = "~";
        } else if (this.ref == ReferenceFrame.relative_facing) {
            prefix = "^";
        }
        return prefix + this.x + " " + prefix + this.y + " " + prefix + this.z;
    }

    public String getDimensionName() {
        String result;
        switch (dimension) {
            case the_end:
                result = "End";
                break;
            case the_nether:
                result = "Nether";
                break;
            default:
                result = "Overworld";
        }

        return result;
    }
}
