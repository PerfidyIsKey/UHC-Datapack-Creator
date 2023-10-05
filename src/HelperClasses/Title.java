package HelperClasses;

import Enums.TitleType;

import java.util.ArrayList;

public class Title {
    private String entity;
    private TitleType type;
    private String text;

    public Title(String entity, TitleType type, TextItem text) {
        this.entity = entity;
        this.type = type;
        this.text = text.getText();
    }

    public Title(String entity, TitleType type, ArrayList<TextItem> texts) {
        this.entity = entity;
        this.type = type;
        this.text = getTexts(texts);
    }

    public String displayTitle() {
        return "title " + entity + " " + type + " " + text;
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
