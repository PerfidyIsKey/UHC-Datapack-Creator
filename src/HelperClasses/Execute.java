package HelperClasses;

import Enums.*;

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

    /*
        execute as
     */
    // as entity
    public String As(Entity entity) {
        return As(entity, true);
    }

    public String As(Entity entity, Boolean run) {
        return Standard(AsNext(entity), run);
    }

    public String AsNext(Entity entity) {
        return AsNext(entity, false);
    }

    public String AsNext(Entity entity, Boolean run) {
        return Next("as " + entity.getEntity() + " ", run);
    }

    public String As(String entity) {
        return As(entity, true);
    }

    public String As(String entity, Boolean run) {
        return Standard(AsNext(entity), run);
    }

    public String AsNext(String entity) {
        return AsNext(entity, false);
    }

    public String AsNext(String entity, Boolean run) {
        return Next("as " + entity + " ", run);
    }

    /*
        execute at
     */
    // at entity
    public String At(Entity entity) {
        return At(entity, true);
    }

    public String At(Entity entity, Boolean run) {
        return Standard(AtNext(entity), run);
    }

    public String AtNext(Entity entity) {
        return AtNext(entity, false);
    }

    public String AtNext(Entity entity, Boolean run) {
        return Next("at " + entity.getEntity() + " ", run);
    }

    public String At(String entity) {
        return At(entity, true);
    }

    public String At(String entity, Boolean run) {
        return Standard(AtNext(entity), run);
    }

    public String AtNext(String entity) {
        return AtNext(entity, false);
    }

    public String AtNext(String entity, Boolean run) {
        return Next("at " + entity + " ", run);
    }

    /*
        execute facing
     */
    // facing entity
    public String Facing(Entity entity, EntityAnchor anchor) {
        return Facing(entity, anchor, true);
    }

    public String Facing(Entity entity, EntityAnchor anchor, Boolean run) {
        return Standard(FacingNext(entity, anchor), run);
    }

    public String FacingNext(Entity entity, EntityAnchor anchor) {
        return FacingNext(entity, anchor, false);
    }

    public String FacingNext(Entity entity, EntityAnchor anchor, Boolean run) {
        return Next("facing entity " + entity.getEntity() + " " + anchor + " ", run);
    }

    public String Facing(String entity, EntityAnchor anchor) {
        return Facing(entity, anchor, true);
    }

    public String Facing(String entity, EntityAnchor anchor, Boolean run) {
        return Standard(FacingNext(entity, anchor), run);
    }

    public String FacingNext(String entity, EntityAnchor anchor) {
        return FacingNext(entity, anchor, false);
    }

    public String FacingNext(String entity, EntityAnchor anchor, Boolean run) {
        return Next("facing entity " + entity + " " + anchor + " ", run);
    }

    /*
        execute if
     */
    // if data
    public String If(DataClasses data, String command) { return If(data, command, true); }

    public String If(DataClasses data, String command, Boolean run) { return Standard(IfNext(data, command), run); }

    public String IfNext(DataClasses data, String command) { return IfNext(data, command, false); }

    public String IfNext(DataClasses data, String command, Boolean run) { return Next("if data " + data + " " + command + " ", run); }

    // if entity
    public String If(Entity entity) {
        return If(entity, true);
    }

    public String If(Entity entity, Boolean run) {
        return Standard(IfNext(entity), run);
    }

    public String IfNext(Entity entity) {
        return IfNext(entity, false);
    }

    public String IfNext(Entity entity, Boolean run) {
        return Next("if entity " + entity.getEntity() + " ", run);
    }

    public String If(String entity) {
        return If(entity, true);
    }

    public String If(String entity, Boolean run) {
        return Standard(IfNext(entity), run);
    }

    public String IfNext(String entity) {
        return IfNext(entity, false);
    }

    public String IfNext(String entity, Boolean run) {
        return Next("if entity " + entity + " ", run);
    }

    // if score
    public String If(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2) {
        return If(entity1, objective1, comp, entity2, objective2, true);
    }

    public String If(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2, Boolean run) {
        return Standard(IfNext(entity1, objective1, comp, entity2, objective2), run);
    }

    public String IfNext(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2) {
        return IfNext(entity1, objective1, comp, entity2, objective2, false);
    }

    public String IfNext(String entity1, ScoreboardObjective objective1, ComparatorType comp, String entity2, ScoreboardObjective objective2, Boolean run) {
        return Next("if score " + entity1 + " " + objective1.getName() + " " + comp + " " + entity2 + " " + objective2.getName() + " ", run);
    }

    public String If(String target, Objective targetObjective, int value) {
        return If(target, targetObjective, value, true);
    }

    public String If(String target, Objective targetObjective, int value, Boolean run) {
        return Standard(IfNext(target, targetObjective, value), run);
    }

    public String IfNext(String target, Objective targetObjective, int value) {
        return IfNext(target, targetObjective, value, false);
    }

    public String IfNext(String target, Objective targetObjective, int value, Boolean run) {
        return Next("if score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

    /*
        execute in
     */
    // in dimension
    public String In(Dimension dimension) {
        return In(dimension, true);
    }

    public String In(Dimension dimension, boolean run) {
        return Standard(InNext(dimension), run);
    }

    public String InNext(Dimension dimension) {
        return InNext(dimension, false);
    }

    public String InNext(Dimension dimension, boolean run) {
        return Next("in minecraft:" + dimension + " ", run);
    }

    /*
        execute positioned
     */
    // positioned coordinate
    public String Positioned(Coordinate coordinate) {
        return Positioned(coordinate, true);
    }

    public String Positioned(Coordinate coordinate, Boolean run) {
        return Standard(PositionedNext(coordinate), run);
    }

    public String PositionedNext(Coordinate coordinate) {
        return PositionedNext(coordinate, false);
    }

    public String PositionedNext(Coordinate coordinate, Boolean run) {
        return Next("positioned " + coordinate.getCoordinateString() + " ", run);
    }

    /*
       execute store
    */
    // store bossbar
    public String Store(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType) {
        return Store(storeType, bossbar, bossBarStoreType, true);
    }

    public String Store(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType, Boolean run) {
        return Standard(StoreNext(storeType, bossbar, bossBarStoreType), run);
    }

    public String StoreNext(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType) {
        return StoreNext(storeType, bossbar, bossBarStoreType, false);
    }

    public String StoreNext(ExecuteStore storeType, BossBar bossbar, BossBarStore bossBarStoreType, Boolean run) {
        return wrap("store " + storeType + " bossbar " + bossbar.getName() + " " + bossBarStoreType + " ", false, run);
    }

    // store score
    public String Store(ExecuteStore storeType, String entity, ScoreboardObjective objective) {
        return Store(storeType, entity, objective, true);
    }

    public String Store(ExecuteStore storeType, String entity, ScoreboardObjective objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public String StoreNext(ExecuteStore storeType, String entity, ScoreboardObjective objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public String StoreNext(ExecuteStore storeType, String entity, ScoreboardObjective objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective.getName() + " ", false, run);
    }

    public String Store(ExecuteStore storeType, String entity, Objective objective) {
        return Store(storeType, entity, objective, true);
    }

    public String Store(ExecuteStore storeType, String entity, Objective objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public String StoreNext(ExecuteStore storeType, String entity, Objective objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public String StoreNext(ExecuteStore storeType, String entity, Objective objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective + " ", false, run);
    }

    public String Store(ExecuteStore storeType, String entity, String objective) {
        return Store(storeType, entity, objective, true);
    }

    public String Store(ExecuteStore storeType, String entity, String objective, Boolean run) {
        return Standard(StoreNext(storeType, entity, objective), run);
    }

    public String StoreNext(ExecuteStore storeType, String entity, String objective) {
        return StoreNext(storeType, entity, objective, false);
    }


    public String StoreNext(ExecuteStore storeType, String entity, String objective, Boolean run) {
        return wrap("store " + storeType + " score " + entity + " " + objective + " ", false, run);
    }

    /*
        execute unless
     */
    // unless block
    public String Unless(Coordinate coordinate, BlockType blockType) {
        return Unless(coordinate, blockType, true);
    }

    public String Unless(Coordinate coordinate, BlockType blockType, Boolean run) {
        return Standard(UnlessNext(coordinate, blockType), run);
    }

    public String UnlessNext(Coordinate coordinate, BlockType blockType) {
        return UnlessNext(coordinate, blockType, false);
    }

    public String UnlessNext(Coordinate coordinate, BlockType blockType, Boolean run) {
        return Next("unless block " + coordinate.getCoordinateString() + " minecraft:" + blockType + " ", run);
    }

    // unless entity
    public String Unless(String entity) {
        return Unless(entity, true);
    }

    public String Unless(String entity, Boolean run) {
        return Standard(UnlessNext(entity), run);
    }

    public String UnlessNext(String entity) {
        return UnlessNext(entity, false);
    }

    public String UnlessNext(String entity, Boolean run) {
        return Next("unless entity " + entity + " ", run);
    }

    // unless score
    public String Unless(String target, Objective targetObjective, int value) {
        return Unless(target, targetObjective, value, true);
    }

    public String Unless(String target, Objective targetObjective, int value, Boolean run) {
        return Standard(UnlessNext(target, targetObjective, value), run);
    }

    public String UnlessNext(String target, Objective targetObjective, int value) {
        return UnlessNext(target, targetObjective, value, false);
    }

    public String UnlessNext(String target, Objective targetObjective, int value, Boolean run) {
        return Next("unless score " + target + " " + targetObjective + " matches " + value + " ", run);
    }

}
