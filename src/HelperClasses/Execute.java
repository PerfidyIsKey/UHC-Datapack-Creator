package HelperClasses;

import Enums.Dimension;
import Enums.EntityAnchor;
import Enums.ExecuteStore;
import Enums.BossBarStore;
import HelperClasses.Condition;

import javax.swing.text.Position;

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
        return Next("facing " + entity.getEntity() + " " + anchor + " ", run);
    }

    /*
        execute if
     */
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
    // Bossbar
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

    // Score
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
