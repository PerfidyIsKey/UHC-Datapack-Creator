package HelperClasses;

import Enums.Dimension;
import Enums.EntityAnchor;
import HelperClasses.Condition;

public class Execute {

    public Execute() {

    }

    private String wrap(String content, boolean execute, boolean run) {
        String result = content;
        if (execute) {
            result = "execute " + content;
        }
        if (run) {
            result += "run ";
        }
        return result;
    }

    private String Standard(String content, boolean run) {
        return wrap(content, true, run);
    }

    private String Next(String content, boolean run) {
        return wrap(content, false, run);
    }

    public String If(Condition condition, boolean run) {
        return Standard(IfNext(condition), run);
    }

    public String If(Condition condition) {
        return If(condition, true);
    }

    public String IfNext(Condition condition, boolean run) {
        return Next("if " + condition.getText() + " ", run);
    }

    public String IfNext(Condition condition) {
        return IfNext(condition, false);
    }

    public String At(Location location, boolean run) {
        return Standard(AtNext(location), run);
    }

    public String At(String targets, boolean run) {
        return Standard(AtNext(targets), run);
    }

    public String At(Location location) {
        return At(location, true);
    }

    public String At(String targets) {
        return At(targets, true);
    }

    public String AtNext(Location location, boolean run) {
        return Next("at " + location.getLocation() + " ", run);
    }

    public String AtNext(String targets, boolean run) {
        return Next("at " + targets + " ", run);
    }

    public String AtNext(Location location) {
        return AtNext(location, false);
    }

    public String AtNext(String targets) {
        return AtNext(targets, false);
    }

    public String As(String entity, boolean run) {
        return Standard(AsNext(entity), run);
    }

    public String As(String entity) {
        return Standard(AsNext(entity), true);
    }

    public String AsNext(String entity, boolean run) {
        return Next("as " + entity + " ", run);
    }

    public String AsNext(String entity) {
        return Next("as " + entity + " ", false);
    }

    public String Positioned(int x, int y, int z, boolean run) {
        return Standard(PositionedNext(x, y, z), run);
    }

    public String Positioned(int x, int y, int z) {
        return Standard(PositionedNext(x, y, z), true);
    }

    public String PositionedNext(int x, int y, int z, boolean run) {
        return Next("positioned " + x + " " + y + " " + z + " ", run);
    }

    public String PositionedNext(int x, int y, int z) {
        return Next("positioned " + x + " " + y + " " + z + " ", false);
    }

    public String PositionedRelative(int x, int y, int z, boolean run) {
        return Standard(PositionedRelativeNext(x, y, z), run);
    }

    public String PositionedRelative(int x, int y, int z) {
        return Standard(PositionedRelativeNext(x, y, z), true);
    }

    public String PositionedRelativeNext(int x, int y, int z, boolean run) {
        return Next("positioned ~" + x + " ~" + y + " ~" + z + " ", run);
    }

    public String PositionedRelativeNext(int x, int y, int z) {
        return Next("positioned ~" + x + " ~" + y + " ~" + z + " ", false);
    }

    public String PositionedRelativeFacing(int x, int y, int z, boolean run) {
        return Standard(PositionedRelativeFacingNext(x, y, z), run);
    }

    public String PositionedRelativeFacing(int x, int y, int z) {
        return Standard(PositionedRelativeFacingNext(x, y, z), true);
    }

    public String PositionedRelativeFacingNext(int x, int y, int z, boolean run) {
        return Next("positioned ^" + x + " ^" + y + " ^" + z + " ", run);
    }

    public String PositionedRelativeFacingNext(int x, int y, int z) {
        return Next("positioned ^" + x + " ^" + y + " ^" + z + " ", false);
    }

    public String In(Dimension dimension, boolean run) {
        return Standard(InNext(dimension), run);
    }

    public String In(Dimension dimension) {
        return Standard(InNext(dimension), true);
    }

    public String InNext(Dimension dimension, boolean run) {
        return Next("in minecraft:" + dimension + " ", run);
    }

    public String InNext(Dimension dimension) {
        return Next("in minecraft:" + dimension + " ", false);
    }

    public String Unless(Condition condition, boolean run) {
        return Standard(UnlessNext(condition), run);
    }

    public String Unless(Condition condition) {
        return Unless(condition, true);
    }

    public String UnlessNext(Condition condition, boolean run) {
        return Next("unless " + condition.getText() + " ", run);
    }

    public String UnlessNext(Condition condition) {
        return UnlessNext(condition, false);
    }

    public String StoreResult(String execution) {
        return wrap(StoreResultNext(execution), true, false);
    }

    public String StoreResultNext(String execution) {
        return "store result " + execution;
    }

    public String Facing(Location location, boolean run) {
        return Standard(FacingNext(location), run);
    }

    public String Facing(Location location) {
        return Standard(FacingNext(location), true);
    }

    public String Facing(Condition target, EntityAnchor anchor, boolean run) {
        return Standard(FacingNext(target, anchor), run);
    }

    public String Facing(Condition target, EntityAnchor anchor) {
        return Standard(FacingNext(target, anchor), true);
    }

    public String FacingNext(Location location, boolean run) {
        return Next("facing " + location.getLocation() + " ", run);
    }

    public String FacingNext(Location location) {
        return FacingNext(location, false);
    }

    public String FacingNext(Condition target, EntityAnchor anchor, boolean run) {
        return Next("facing " + target.getText() + " " + anchor + " ", run);
    }

    public String FacingNext(Condition target, EntityAnchor anchor) {
        return FacingNext(target, anchor, false);
    }
}
