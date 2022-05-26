package FileGeneration;

import Enums.FileName;

import java.util.ArrayList;

public class FileData {

    private String name;
    private ArrayList<String> fileText;
    private String type = "function";

    public FileData(String name, ArrayList<String> fileText) {
        this.name = name;
        this.fileText = fileText;
    }

    public FileData(FileName name, ArrayList<String> fileText) {
        this.name = name + "";
        this.fileText = fileText;
    }

    public FileData(String name, ArrayList<String> fileText, String type) {
        this.name = name;
        this.fileText = fileText;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getNameWithoutExtension(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFileText() {
        return fileText;
    }

    public void setFileText(ArrayList<String> fileText) {
        this.fileText = fileText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
