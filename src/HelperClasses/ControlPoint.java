package HelperClasses;

public class ControlPoint {

    private String name;

    private int maxVal;
    private int addRate;

    private int x;
    private int y;
    private int z;

    public ControlPoint(String name, int maxVal, int addRate, int x, int y, int z) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public int getAddRate() {
        return addRate;
    }

    public void setAddRate(int addRate) {
        this.addRate = addRate;
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
}
