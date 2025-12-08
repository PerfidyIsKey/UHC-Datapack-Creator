package HelperClasses;

public class PaperPlugin {
    private String mainFileName;
    private Boolean active;
    private String[] extraFileNames;

    public PaperPlugin(String fileName, Boolean active) {
        this.mainFileName = fileName;
        this.active = active;
    }

    public PaperPlugin(String fileName, Boolean active, String extra) {
        this.mainFileName = fileName;
        this.active = active;
        this.extraFileNames = new String[] {extra};
    }

    public PaperPlugin(String fileName, Boolean active, String[] extra) {
        this.mainFileName = fileName;
        this.active = active;
        this.extraFileNames = extra;
    }

    public Boolean getActive() {
        return active;
    }

    public String getMainFileName() {
        return mainFileName;
    }

    public String[] getExtraFileNames() {
        return extraFileNames;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setMainFileName(String mainFileName) {
        this.mainFileName = mainFileName;
    }

    public void setExtraFileNames(String[] extraFileNames) {
        this.extraFileNames = extraFileNames;
    }
}
