package EntityClasses;

import Enums.AttributeType;

public class MovementSpeed implements Attributes{
    // Fields
    double base;

    // Constructor
    public MovementSpeed(double base) {
        this.base = base;
    }

    // Get attribute
    public String GetAttribute() {
        return "\"id\":\"" + AttributeType.MOVEMENT_SPEED + "\",\n" +
                "\"base\":" + base;
    }
}
