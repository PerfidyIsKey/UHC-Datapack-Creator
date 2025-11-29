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
    // Potions
    public static String giveSplashPotion(String targets, int slotNumber, EffectId effect, String colorHex, String displayName, String lore) {
        // Convert hex to decimal
        int potionColor = Integer.parseInt(colorHex, 16);

        return "item replace entity " + targets + " " + ItemSlot.HOTBAR.withSlotNumber(slotNumber).getCommandString() + " with " + ItemId.SPLASH_POTION + "[potion_contents={custom_color:" + potionColor + ",custom_effects:[{id:\"" + effect + "\",amplifier:0,duration:200,show_particles:0b,show_icon:0b,ambient:0b}]},lore=[\"" + lore + "\"],custom_name=\"" + displayName + "\"]";
    }

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