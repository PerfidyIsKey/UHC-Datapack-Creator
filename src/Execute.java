import Enums.Dimension;

public class Execute {

    public Execute() {

    }

    private String wrap(String content, boolean execute, boolean run) {
        String result = content;
        if(execute) {
            result = "execute " + content;
        }
        if(run) {
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

    public String If(String condition, boolean run) {
        return Standard(IfNext(condition, false), run);
    }

    public String IfNext(String condition, boolean run) {
        return Next("if " + condition + " ", run);
    }

    public String At(String location, boolean run) {
        return Standard(AtNext(location, false), run);
    }

    public String AtNext(String location, boolean run) {
        return Next("at " + location + " ", run);
    }

    public String As(String entity, boolean run) {
        return Standard(AsNext(entity, false), run);
    }

    public String AsNext(String entity, boolean run) {
        return Next("as " + entity + " ", run);
    }

    public String Positioned(int x, int y, int z, boolean run) {
        return Standard(PositionedNext(x, y, z, false), run);
    }

    public String PositionedNext(int x, int y, int z, boolean run) {
        return Next("positioned " + x + " " + y + " " + z + " ", run);
    }

    public String PositionedRelative(int x, int y, int z, boolean run) {
        return Standard(PositionedRelativeNext(x, y, z, false), run);
    }

    public String PositionedRelativeNext(int x, int y, int z, boolean run) {
        return Next("positioned ~" + x + " ~" + y + " ~" + z + " ", run);
    }

    public String In(Dimension dimension, boolean run) {
        return Standard(InNext(dimension, false), run);
    }

    public String InNext(Dimension dimension, boolean run) {
        return Next("in minecraft:" + dimension + " ", run);
    }

    public String Unless(String condition, boolean run) {
        return Standard(UnlessNext(condition, false), run);
    }

    public String UnlessNext(String condition, boolean run) {
        return Next("unless " + condition + " ", run);
    }

    public String StoreResult(String execution) {
        return wrap(StoreResultNext(execution), true, false);
    }

    public String StoreResultNext(String execution) {
        return "store result " + execution;
    }
}
