package classact.com.clever_little_monkey.database.model;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class UnitSection {

    private int unitSectionId;
    private int unitId;
    private int sectionId;
    private int languageId;
    private int sectionOrder;
    private int sectionSubId;
    private String sectionSubject;
    private int unlocked;
    private String unlockedDate;
    private int inProgress;

    public int getUnitSectionId() {
        return unitSectionId;
    }

    public void setUnitSectionId(int unitSectionId) {
        this.unitSectionId = unitSectionId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getSectionOrder() {
        return sectionOrder;
    }

    public void setSectionOrder(int sectionOrder) {
        this.sectionOrder = sectionOrder;
    }

    public int getSectionSubId() {
        return sectionSubId;
    }

    public void setSectionSubId(int sectionSubId) {
        this.sectionSubId = sectionSubId;
    }

    public String getSectionSubject() {
        return sectionSubject;
    }

    public void setSectionSubject(String sectionSubject) {
        this.sectionSubject = sectionSubject;
    }

    public int getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(int unlocked) {
        this.unlocked = unlocked;
    }

    public String getUnlockedDate() {
        return unlockedDate;
    }

    public void setUnlockedDate(String unlockedDate) {
        this.unlockedDate = unlockedDate;
    }

    public int getInProgress() {
        return inProgress;
    }

    public void setInProgress(int inProgress) {
        this.inProgress = inProgress;
    }
}
