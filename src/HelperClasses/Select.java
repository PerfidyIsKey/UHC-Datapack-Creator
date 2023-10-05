package HelperClasses;

import Enums.Color;

public class Select extends TextItem {
    private String selector;

    public Select(Color color, boolean isBold, boolean isItalic, String selector) {
        super(color, isBold, isItalic);
        this.selector = selector;
    }

    public String getText() {
        return "{\"selector\":\"" + selector + "\"," + bold() + italic() + " \"color\":\"" + color + "\"}";
    }
}
