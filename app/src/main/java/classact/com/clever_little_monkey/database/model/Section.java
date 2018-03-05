package classact.com.clever_little_monkey.database.model;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class Section {

    private int sectionId;
    private String name;
    private int languageId;

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
}
