package HelperClasses;

public class Select extends TextItem{
    private String selector;

    public Select(String selector){
        this.selector = selector;
    }

    public String getText() {
        return "{\"selector\":\"" + selector + "\"}";
    }
}
