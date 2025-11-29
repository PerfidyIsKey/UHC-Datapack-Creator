import Enums.*;
import HelperClasses.*;
import arguments.Entity;
import arguments.targetselector.SelectorArgumentsBuilder;
import arguments.targetselector.TargetSelector;
import commands.Attribute;
import commands.Summon;
import controlpoints.ControlPointTag;
import shared.*;
import shared.attributes.AttributeId;
import shared.item.ItemId;

import java.util.ArrayList;

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

    public static String modifyWaypointColor(String waypoint, TextColor color) {
        return "waypoint modify " + waypoint + " color " + color;
    }

    public static String modifyWaypointColor(String waypoint) {
        return modifyWaypointColor(waypoint, TextColor.WHITE);
    }
}