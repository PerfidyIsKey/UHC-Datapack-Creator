public class CarePackage {
    private String name;
    private String displayName;
    private String nbtTag;
    private int x;
    private int y;
    private int z;

    public CarePackage(String name, String displayName, String nbtTag, int x, int y, int z) {
        this.name = name;
        this.displayName = displayName;
        this.nbtTag = nbtTag;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNbtTag() {
        return nbtTag;
    }

    public void setNbtTag(String nbtTag) {
        this.nbtTag = nbtTag;
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
