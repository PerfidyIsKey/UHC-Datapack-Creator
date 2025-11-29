import Enums.*;

public class CommandBuilder {

    // Change title display time
    public static String changeTitleDisplayTime(String targets, int fadeIn, int stay, int fadeOut) {
        return changeTitleDisplayTime(targets, fadeIn, stay, fadeOut, Duration.SECONDS);
    }

    public static String changeTitleDisplayTime(String targets, int fadeIn, int stay, int fadeOut, Duration durationType) {
        return "title " + targets + " times " + fadeIn + durationType + " " + stay + durationType + " " + fadeOut + durationType;
    }

    public static String titleDefaultTiming(String targets) {
        return changeTitleDisplayTime(targets, 10, 70, 20, Duration.TICKS);
    }

}