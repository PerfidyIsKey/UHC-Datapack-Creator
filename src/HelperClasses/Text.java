package HelperClasses;

import Enums.Color;

public class Text extends TextItem {
    private Color color;
    private String text;
    private boolean isBold;
    private boolean isItalic;

    public Text(Color color, String text, boolean isBold, boolean isItalic) {
        this.color = color;
        this.text = text;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    public String getText() {
        return "{\"text\":\"" + text + "\"," + bold() + italic() + " \"color\":\"" + color + "\"}";
    }

    private String bold() {
        if (!isBold) {
            return "";
        }
        return " \"bold\":true,";
    }

    private String italic() {
        if (!isItalic) {
            return "";
        }
        return " \"italic\":true,";
    }
}
