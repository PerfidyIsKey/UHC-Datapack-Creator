package EntityClasses;

public class MovementSpeed implements Attributes{
    // Fields
    double base;

    // Constructor
    public MovementSpeed(double base) {
        this.base = base;
    }

    // Get attribute
    public String GetAttribute() {
        return "\"id\":\"movement_speed\",\n" +
                "\"base\":" + base;
    }
}
