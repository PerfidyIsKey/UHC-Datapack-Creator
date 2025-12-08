package HelperClasses;

import shared.TextColor;

public class TextItem {

    protected TextColor color;
    protected boolean isBold;
    protected boolean isItalic;
    public String getText() {
        return "";
    }
    public String getText(Boolean technical) {return ""; }

    public TextItem(TextColor color, boolean isBold, boolean isItalic) {
        this.color = color;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }

    public TextItem(TextColor color) {
        this.color = color;
    }

    public TextItem() {}

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
