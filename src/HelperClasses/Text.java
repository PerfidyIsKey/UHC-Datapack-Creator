package HelperClasses;

import Enums.Color;

public class Text extends TextItem {

    private String text;


    public Text(Color color, boolean isBold, boolean isItalic, String text) {
        super(color, isBold, isItalic);
        this.text = text;

    }

    public String getText() {
        return "{\"text\":\"" + text + "\"," + bold() + italic() + " \"color\":\"" + color + "\"}";
    }


}
