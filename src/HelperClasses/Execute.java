package HelperClasses;

import Enums.*;
import shared.StaticBlockId;

public class Execute {

    private static String wrap(String content, boolean execute, boolean run) {
        String result = content;
        if (execute) {
            result = "execute " + content;
        }
        if (run) {
            result += "run ";
        }
        return result;
    }

    private static String Standard(String content, boolean run) {
        return wrap(content, true, run);
    }

    private static String Next(String content, boolean run) {
        return wrap(content, false, run);
    }

    /*
        execute as
     */
    // as entity
    public static String As(String entity) {
        return As(entity, true);
    }

    public static String As(String entity, Boolean run) {
        return Standard(AsNext(entity), run);
    }

    public static String AsNext(String entity) {
        return AsNext(entity, false);
    }

    public static String AsNext(String entity, Boolean run) {
        return Next("as " + entity + " ", run);
    }

    /*
        execute at
     */
    // at entity
    public static String At(String entity) {
        return At(entity, true);
    }

    public static String At(String entity, Boolean run) {
        return Standard(AtNext(entity), run);
    }

    public static String AtNext(String entity) {
        return AtNext(entity, false);
    }

    public static String AtNext(String entity, Boolean run) {
        return Next("at " + entity + " ", run);
    }

    /*
        execute facing
     */
    // facing entity
    public static String Facing(String entity, EntityAnchor anchor) {
        return Facing(entity, anchor, true);
    }

    public static String Facing(String entity, EntityAnchor anchor, Boolean run) {
        return Standard(FacingNext(entity, anchor), run);
    }

    public static String FacingNext(String entity, EntityAnchor anchor) {
        return FacingNext(entity, anchor, false);
    }

    public static String FacingNext(String entity, EntityAnchor anchor, Boolean run) {
        return Next("facing entity " + entity + " " + anchor + " ", run);
    }

    /*
        execute if
     */
    // if data
    public static String If(DataClasses data, String command) { return If(data, command, true); }

    public static String If(DataClasses data, String command, Boolean run) { return Standard(IfNext(data, command), run); }

    public static String IfNext(DataClasses data, String command) { return IfNext(data, command, false); }

    public static String IfNext(DataClasses data, String command, Boolean run) { return Next("if data " + data + " " + command + " ", run); }

    // if entity
    public static String If(String entity) {
        return If(entity, true);
    }

    public static String If(String entity, Boolean run) {
        return Standard(IfNext(entity), run);
    }

    public static String IfNext(String entity) {
        return IfNext(entity, false);
    }

    public static String IfNext(String entity, Boolean run) {
        return Next("if entity " + entity + " ", run);
    }

    // if score
    public static String If(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2) {
        return If(entity1, objective1, comp, entity2, objective2, true);
    }

    public static String If(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2, Boolean run) {
        return Standard(IfNext(entity1, objective1, comp, entity2, objective2), run);
    }

    public static String IfNext(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2) {
        return IfNext(entity1, objective1, comp, entity2, objective2, false);
    }

    public static String IfNext(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2, Boolean run) {
        return Next("if score " + entity1 + " " + objective1.getName() + " " + comp + " " + entity2 + " " + objective2.getName() + " ", run);
    }

    public static String If(String entity1, Objective objective1, ComparatorType comp, String entity2, Objective objective2) {
        return If(entity1, objective1, comp, entity2, objective2, true);
    }

    public static String If(String entity1, Objective objective1, ComparatorType comp, String entity2, Objective objective2, Boolean run) {
        return Standard(IfNext(entity1, objective1, comp, entity2, objective2), run);
    }

    public static String IfNext(String entity1, Objective objective1, ComparatorType comp, String entity2, Objective objective2) {
        return IfNext(entity1, objective1, comp, entity2, objective2, false);
    }

    public static String IfNext(String entity1, Objective objective1, ComparatorType comp, String entity2, Objective objective2, Boolean run) {
        return Next("if score " + entity1 + " " + objective1 + " " + comp + " " + entity2 + " " + objective2 + " ", run);
    }

    public static String If(String entity1, Objective objective1, ComparatorType comp, String entity2, String objective2) {
        return If(entity1, objective1, comp, entity2, objective2, true);
    }

    public static String If(String entity1, Objective objective1, ComparatorType comp, String entity2, String objective2, Boolean run) {
        return Standard(IfNext(entity1, objective1, comp, entity2, objective2), run);
    }

    public static String IfNext(String entity1, Objective objective1, ComparatorType comp, String entity2, String objective2) {
        return IfNext(entity1, objective1, comp, entity2, objective2, false);
    }

    public static String IfNext(String entity1, Objective objective1, ComparatorType comp, String entity2, String objective2, Boolean run) {
        return Next("if score " + entity1 + " " + objective1 + " " + comp + " " + entity2 + " " + objective2 + " ", run);
    }

    public static String If(String target, Objective targetObjective, int value) {
        return If(target, targetObjective, value, true);
    }

    public static String If(String target, Objective targetObjective, int value, Boolean run) {
        return Standard(IfNext(target, targetObjective, value), run);
    }

    public static String IfNext(String target, Objective targetObjective, int value) {
        return IfNext(target, targetObjective, value, false);
    }

    public static String IfNext(String target, Objective targetObjective, int value, Boolean run) {
        return Next("if score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

    public static String If(String target, String targetObjective, int value) {
        return If(target, targetObjective, value, true);
    }

    public static String If(String target, String targetObjective, int value, Boolean run) {
        return Standard(IfNext(target, targetObjective, value), run);
    }

    public static String IfNext(String target, String targetObjective, int value) {
        return IfNext(target, targetObjective, value, false);
    }

    public static String IfNext(String target, String targetObjective, int value, Boolean run) {
        return Next("if score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

    public static String If(String target, Objective targetObjective, String value) {
        return If(target, targetObjective, value, true);
    }

    public static String If(String target, Objective targetObjective, String value, Boolean run) {
        return Standard(IfNext(target, targetObjective, value), run);
    }

    public static String IfNext(String target, Objective targetObjective, String value) {
        return IfNext(target, targetObjective, value, false);
    }

    public static String IfNext(String target, Objective targetObjective, String value, Boolean run) {
        return Next("if score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

    public static String If(String target, String targetObjective, String value) {
        return If(target, targetObjective, value, true);
    }

    public static String If(String target, String targetObjective, String value, Boolean run) {
        return Standard(IfNext(target, targetObjective, value), run);
    }

    public static String IfNext(String target, String targetObjective, String value) {
        return IfNext(target, targetObjective, value, false);
    }

    public static String IfNext(String target, String targetObjective, String value, Boolean run) {
        return Next("if score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

    /*
        execute in
     */
    // in dimension
    public static String In(Dimension dimension) {
        return In(dimension, true);
    }

    public static String In(Dimension dimension, boolean run) {
        return Standard(InNext(dimension), run);
    }

    public static String InNext(Dimension dimension) {
        return InNext(dimension, false);
    }

    public static String InNext(Dimension dimension, boolean run) {
        return Next("in minecraft:" + dimension + " ", run);
    }

    /*
        execute positioned
     */
    // positioned coordinate
    public static String Positioned(Coordinate coordinate) {
        return Positioned(coordinate, true);
    }

    public static String Positioned(Coordinate coordinate, Boolean run) {
        return Standard(PositionedNext(coordinate), run);
    }

    public static String PositionedNext(Coordinate coordinate) {
        return PositionedNext(coordinate, false);
    }

    public static String PositionedNext(Coordinate coordinate, Boolean run) {
        return Next("positioned " + coordinate.getCoordinateString() + " ", run);
    }

    /*
       execute store
    */
    // store bossbar
    public static String Store(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType) {
        return Store(storeType, bossbar, bossBarStoreType, true);
    }

    public static String Store(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType, Boolean run) {
        return Standard(StoreNext(storeType, bossbar, bossBarStoreType), run);
    }

    public static String StoreNext(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType) {
        return StoreNext(storeType, bossbar, bossBarStoreType, false);
    }

    public static String StoreNext(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType, Boolean run) {
        return wrap("store " + storeType + " bossbar " + bossbar.getName() + " " + bossBarStoreType + " ", false, run);
    }

    // store score
    public static String Store(ExecuteStore storeType, String entity, ScoreboardObjective objective) {
        return Store(storeType, entity, objective, true);
    }

    public static String Store(ExecuteStore storeType, String entity, ScoreboardObjective objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public static String StoreNext(ExecuteStore storeType, String entity, ScoreboardObjective objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public static String StoreNext(ExecuteStore storeType, String entity, ScoreboardObjective objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective.getName() + " ", false, run);
    }

    public static String Store(ExecuteStore storeType, String entity, Objective objective) {
        return Store(storeType, entity, objective, true);
    }

    public static String Store(ExecuteStore storeType, String entity, Objective objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public static String StoreNext(ExecuteStore storeType, String entity, Objective objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public static String StoreNext(ExecuteStore storeType, String entity, Objective objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective + " ", false, run);
    }

    public static String Store(ExecuteStore storeType, String entity, String objective) {
        return Store(storeType, entity, objective, true);
    }

    public static String Store(ExecuteStore storeType, String entity, String objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public static String StoreNext(ExecuteStore storeType, String entity, String objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public static String StoreNext(ExecuteStore storeType, String entity, String objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective + " ", false, run);
    }

    /*
        execute unless
     */
    // unless block
    public static String Unless(Coordinate coordinate, StaticBlockId blockType) {
        return Unless(coordinate, blockType, true);
    }

    public static String Unless(Coordinate coordinate, StaticBlockId blockType, Boolean run) {
        return Standard(UnlessNext(coordinate, blockType), run);
    }

    public static String UnlessNext(Coordinate coordinate, StaticBlockId blockType) {
        return UnlessNext(coordinate, blockType, false);
    }

    public static String UnlessNext(Coordinate coordinate, StaticBlockId blockType, Boolean run) {
        return Next("unless block " + coordinate.getCoordinateString() + " " + blockType + " ", run);
    }

    public static String Unless(int x, int y, int z, StaticBlockId blockType) {
        return Unless(x, y, z, blockType, true);
    }

    public static String Unless(int x, int y, int z, StaticBlockId blockType, Boolean run) {
        return Standard(UnlessNext(x, y, z, blockType), run);
    }

    public static String UnlessNext(int x, int y, int z, StaticBlockId blockType) {
        return UnlessNext(x, y, z, blockType, false);
    }

    public static String UnlessNext(int x, int y, int z, StaticBlockId blockType, Boolean run) {
        return Next("unless block " + x + " " + y + " " + z + " " + blockType + " ", run);
    }

    // unless entity
    public static String Unless(String entity) {
        return Unless(entity, true);
    }

    public static String Unless(String entity, Boolean run) {
        return Standard(UnlessNext(entity), run);
    }

    public static String UnlessNext(String entity) {
        return UnlessNext(entity, false);
    }

    public static String UnlessNext(String entity, Boolean run) {
        return Next("unless entity " + entity + " ", run);
    }

    // unless score
    public static String Unless(String target, Objective targetObjective, int value) {
        return Unless(target, targetObjective, value, true);
    }

    public static String Unless(String target, Objective targetObjective, int value, Boolean run) {
        return Standard(UnlessNext(target, targetObjective, value), run);
    }

    public static String UnlessNext(String target, Objective targetObjective, int value) {
        return UnlessNext(target, targetObjective, value, false);
    }

    public static String UnlessNext(String target, Objective targetObjective, int value, Boolean run) {
        return Next("unless score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

}
