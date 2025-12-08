package HelperClasses;

import shared.TextColor;

public class Select extends TextItem {
    private String selector;

    public Select(TextColor color, boolean isBold, boolean isItalic, String selector) {
        super(color, isBold, isItalic);
        this.selector = selector;
    }

    public Select(boolean isBold, boolean isItalic, String selector) {
        super(isBold, isItalic);
        this.selector = selector;
    }

    public Select(String selector) {
        super(TextColor.WHITE);
        this.selector = selector;
    }

    public String getText() {
        return "{\"selector\":\"" + selector + "\"" + bold() + italic() + colorShow() +"}";
    }
}
