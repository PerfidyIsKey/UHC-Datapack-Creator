import Enums.Duration;
import Enums.FileName;

public class Schedule {
    // Call function through other function
    public static String callFunction(String functionName) {
        return "function uhc:" + functionName;
    }

    public static String callFunction(FileName functionName) {
        return callFunction("" + functionName);
    }

    public static String callFunction(String functionName, int delayInSeconds) {
        return callFunction(functionName, delayInSeconds, Duration.SECONDS);
    }

    public static String callFunction(FileName functionName, int delayInSeconds) {
        return callFunction("" + functionName, delayInSeconds, Duration.SECONDS);
    }

    public static String callFunction(String functionName, int delay, Duration unit) {
        return "schedule " + callFunction(functionName) + " " + delay + unit;
    }

    public static String callFunction(FileName functionName, int delay, Duration unit) {
        return callFunction("" + functionName, delay, unit);
    }

    // Clear schedule
    public static String clearFunction(String functionName) {
        return "schedule clear uhc:" + functionName;
    }

    public static String clearFunction(FileName functionName) {
        return clearFunction("" + functionName);
    }
}
