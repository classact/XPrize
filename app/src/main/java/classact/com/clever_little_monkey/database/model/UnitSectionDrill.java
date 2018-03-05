package classact.com.clever_little_monkey.database.model;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class UnitSectionDrill {

    private int unitSectionDrillId;
    private int unitSectionId;
    private int drillId;
    private int drillSubId;
    private int languageId;
    private int drillOrder;
    private int drillScore;
    private int drillScoreMax;
    private int unlocked;
    private String unlockedDate;
    private int inProgress;

    public UnitSectionDrill() {}

    public UnitSectionDrill(
            int unitSectionDrillId, int unitSectionId,
            int drillId, int drillSubId,
            int languageId,
            int drillOrder,
            int drillScore, int drillScoreMax,
            int unlocked, String unlockedDate,
            int inProgress) {
        this.unitSectionDrillId = unitSectionDrillId;
        this.unitSectionId = unitSectionId;
        this.drillId = drillId;
        this.drillSubId = drillSubId;
        this.languageId = languageId;
        this.drillOrder = drillOrder;
        this.drillScore = drillScore;
        this.drillScoreMax = drillScoreMax;
        this.unlocked = unlocked;
        this.unlockedDate = unlockedDate;
        this.inProgress = inProgress;
    }

    public int getUnitSectionDrillId() {
        return unitSectionDrillId;
    }

    public void setUnitSectionDrillId(int unitSectionDrillId) {
        this.unitSectionDrillId = unitSectionDrillId;
    }

    public int getUnitSectionId() {
        return unitSectionId;
    }

    public void setUnitSectionId(int unitSectionId) {
        this.unitSectionId = unitSectionId;
    }

    public int getDrillId() {
        return drillId;
    }

    public void setDrillId(int drillId) {
        this.drillId = drillId;
    }

    public int getDrillSubId() {
        return drillSubId;
    }

    public void setDrillSubId(int drillSubId) {
        this.drillSubId = drillSubId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getDrillOrder() {
        return drillOrder;
    }

    public void setDrillOrder(int drillOrder) {
        this.drillOrder = drillOrder;
    }

    public int getDrillScore() {
        return drillScore;
    }

    public void setDrillScore(int drillScore) {
        this.drillScore = drillScore;
    }

    public int getDrillScoreMax() {
        return drillScoreMax;
    }

    public void setDrillScoreMax(int drillScoreMax) {
        this.drillScoreMax = drillScoreMax;
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