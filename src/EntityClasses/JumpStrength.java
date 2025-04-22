package EntityClasses;

public class JumpStrength implements Attributes{
    // Fields
    double base;

    // Constructor
    public JumpStrength(double base) {
        this.base = base;
    }

    // Get attribute
    public String GetAttribute() {
        return "\"id\":\"jump_strength\",\n" +
                "\"base\":" + base;
    }
}
