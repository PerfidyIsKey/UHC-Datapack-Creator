package HelperClasses;

import java.util.ArrayList;

public class TellRaw {

    private String entity;
    private String text;

    public TellRaw(String entity, TextItem text) {
        this.entity = entity;
        this.text = text.getText();
    }

    public TellRaw(String entity, ArrayList<TextItem> texts) {
        this.entity = entity;
        this.text = getTexts(texts);
    }

    public String sendRaw() {
        return "tellraw " + entity + " " + text;
    }

    private String getTexts(ArrayList<TextItem> texts) {
        StringBuilder result = new StringBuilder("[");
        for (TextItem t : texts) {
            result.append(t.getText());
            result.append(",");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }
}
