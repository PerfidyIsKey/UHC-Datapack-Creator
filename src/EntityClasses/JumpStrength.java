package EntityClasses;

import Enums.AttributeType;

public class JumpStrength implements Attributes{
    // Fields
    double base;

    // Constructor
    public JumpStrength(double base) {
        this.base = base;
    }

    // Get attribute
    public String GetAttribute() {
        return "\"id\":\"" + AttributeType.JUMP_STRENGTH +"\",\n" +
                "\"base\":" + base;
    }
}
