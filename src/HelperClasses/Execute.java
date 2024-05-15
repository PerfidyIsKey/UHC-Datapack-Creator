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

    /*
        execute if
     */
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

    // if score
    public String If(ScoreboardPlayersComp comp) {
        return If(comp, true);
    }

    public String If(ScoreboardPlayersComp comp, Boolean run) {
        return Standard(IfNext(comp), run);
    }

    public String IfNext(ScoreboardPlayersComp comp) {
        return IfNext(comp, false);
    }

    public String IfNext(ScoreboardPlayersComp comp, Boolean run) {
        return Next("if score " + comp.getComparison() + " ", run);
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
    public String Store(ExecuteStore storeType, ScoreboardPlayers score) {
        return Store(storeType, score, true);
    }

    public String Store(ExecuteStore storeType, ScoreboardPlayers score, Boolean run) {
        return Standard(StoreNext(storeType, score), run);
    }

    public String StoreNext(ExecuteStore storeType, ScoreboardPlayers score) {
        return StoreNext(storeType, score, false);
    }


    public String StoreNext(ExecuteStore storeType, ScoreboardPlayers score, Boolean run) {
        return wrap("store " + storeType + " score " + score.getText() + " ", false, run);
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
    public String Unless(Entity entity) {
        return Unless(entity, true);
    }

    public String Unless(Entity entity, Boolean run) {
        return Standard(UnlessNext(entity), run);
    }

    public String UnlessNext(Entity entity) {
        return UnlessNext(entity, false);
    }

    public String UnlessNext(Entity entity, Boolean run) {
        return Next("unless entity " + entity.getEntity() + " ", run);
    }

}
