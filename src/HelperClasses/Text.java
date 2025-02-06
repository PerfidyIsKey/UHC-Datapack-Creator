package HelperClasses;

import Enums.Color;

public class Text extends TextItem {

    private String text;


    public Text(Color color, boolean isBold, boolean isItalic, String text) {
        super(color, isBold, isItalic);
        this.text = text;

    }

    public Text(Color color, String text) {
        super(color);
        this.text = text;

    }

    public Text(boolean isBold, boolean isItalic, String text) {
        super(isBold, isItalic);
        this.text = text;

    }

    public String getText(Boolean technical) {
        if (technical) {
            text = convertSpecial(text);
            text = convertSpecial(text);
        }

        String content = "{\"text\":\"" + text + "\"" + bold() + italic() + colorShow() + "}";

        if (technical) {
            content = convertSpecial(content);
        }

        return content;
    }

    public String getText() {
        return getText(false);
    }

    private String convertSpecial(String content) {
        // List of special characters
        String[] special = {"\""};
        String[] correction = {"\\\""};

        for (int i = 0; i < special.length; i++) {
            content = content.replace(special[i], correction[i]);
        }

        return content;
    }

}
