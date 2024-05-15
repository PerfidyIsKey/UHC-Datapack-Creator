package HelperClasses;

import HelperClasses.Condition;

public class Score extends Condition {
    public Score(String entity, String param1, String operator, String param2) {
        super("score " + entity + " " + param1 + " " + operator + " " + param2);
    }
}