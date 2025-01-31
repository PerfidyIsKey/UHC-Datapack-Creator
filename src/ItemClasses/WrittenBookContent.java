package ItemClasses;

import HelperClasses.TextItem;
import java.util.ArrayList;

public class WrittenBookContent implements Components {
    // Fields
    private String title;
    private String author;
    private ArrayList<ArrayList<TextItem>> pages;

    // Constructor
    public WrittenBookContent(String title, String author, ArrayList<ArrayList<TextItem>> pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    // Make NBT tag
    public String GenerateNBT() {
        return "written_book_content={title:\"" + title + "\"," +
                "author:\"" + author + "\"," +
                "pages:[" + GeneratePages(1) + "]}";
    }

    // Make Component tag
    public String GenerateComponent() {

        return "\"written_book_content\":{\n" +
                "\"title\":\"" + title + "\",\n" +
                "\"author\":\"" + author + "\",\n" +
                "\"pages\":[\n" + GeneratePages(2) + "\n" +
                "]\n" +
                "}";
    }

    private String GeneratePages(int mode) {
        // Pick mode
        String terminationCharacter = "\"";
        if (mode == 1) {    // NBT tag
            terminationCharacter = "'";
        }
        else if (mode == 2) {   // Component
            terminationCharacter = "\"";
        }

        // Create pages NBT tag
        String pageContent = "";
        for (int i = 0; i < pages.size(); i++) {
            // Select current page
            ArrayList<TextItem> currentPage = pages.get(i);

            // Open page
            pageContent += terminationCharacter;

            // Multiple fields per page
            int fieldsNumber = currentPage.size();
            if (fieldsNumber > 1) {

                for (int j = 0; j < fieldsNumber; j++) {    // Loop through all fields
                    if (j == 0) {   // Open page
                        pageContent += "[";
                    }

                    // Add content
                    if (mode == 1) {    // NBT tag
                        pageContent += currentPage.get(j).getText();
                    }
                    else if (mode == 2) {   // Component
                        pageContent += currentPage.get(j).getText(true);
                    }

                    if (j < (fieldsNumber - 1)) {   // Extend to next field
                        pageContent += ",";
                    }
                    else {  // Close page
                        pageContent += "]";
                    }
                }
            }
            else {
                // Add content
                if (mode == 1) {    // NBT tag
                    pageContent += pages.get(i).get(0).getText();
                }
                else if (mode == 2) {   // Component
                    pageContent += pages.get(i).get(0).getText(true);
                }
            }

            if (i < (pages.size() - 1)) {   // Extend to next page
                pageContent += terminationCharacter + ",";
            }
            else {  // Close pages NBT
                pageContent += terminationCharacter;
            }
        }

        return pageContent;
    }

}
