public class Sign {
    private String name;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String command;


    public Sign(String name,String text1, String text2, String text3, String text4, String command) {
        this.name = name;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
