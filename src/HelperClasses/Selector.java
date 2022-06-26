package HelperClasses;

import HelperClasses.Condition;

import java.util.ArrayList;

public class Selector extends Condition {


    public Selector(String selector) {
        super(selector);
    }

    public Selector(String selector, ArrayList<String> arguments) {
        String result = "entity " + selector + " [";
        for (int i = 0; i < arguments.size(); i++) {
            if (i != arguments.size()-1) {
                result += arguments.get(i) + ',';
            }
        }
        result += ']';
        this.setText(result);
    }
}
