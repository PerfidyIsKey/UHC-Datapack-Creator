package HelperClasses;

import Enums.Color;

public class TextItem {

    protected Color color;
    protected boolean isBold;
    protected boolean isItalic;
    public String getText() {
        return "";
    }

    public TextItem(Color color, boolean isBold, boolean isItalic) {
        this.color = color;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    public TextItem(Color color) {
        this.color = color;
    }

    public TextItem(boolean isBold, boolean isItalic) {
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    private boolean hasColor() {
        return color != null;
    }

    protected String bold() {
        if (!isBold) {
            return "";
        }
        return ", \"bold\":true";
    }

    protected String italic() {
        if (!isItalic) {
            return "";
        }
        return ", \"italic\":true";
    }

    protected String colorShow() {
        if (!hasColor()) {
            return "";
        }
        return ", \"color\":\"" + color + "\"";
    }
}
