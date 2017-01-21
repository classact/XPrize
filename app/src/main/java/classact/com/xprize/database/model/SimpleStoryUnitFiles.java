package classact.com.xprize.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoryUnitFiles {
    private int simpleStoryUnitID;
    private int languageID;
    private int unitID;
    private String simpleStoryUnitSoundFile;
    private String simpleStoryUnitImage;
    private String compInstr1;
    private String compInstr2;

    public int getSimpleStoryUnitID() {
        return simpleStoryUnitID;
    }

    public void setSimpleStoryUnitID(int simpleStoryUnitID) {
        this.simpleStoryUnitID = simpleStoryUnitID;
    }

    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getSimpleStoryUnitSoundFile() {
        return simpleStoryUnitSoundFile;
    }

    public void setSimpleStoryUnitSoundFile(String simpleStoryUnitSoundFile) {
        this.simpleStoryUnitSoundFile = simpleStoryUnitSoundFile;
    }

    public String getSimpleStoryUnitImage() {
        return simpleStoryUnitImage;
    }

    public void setSimpleStoryUnitImage(String simpleStoryUnitImage) {
        this.simpleStoryUnitImage = simpleStoryUnitImage;
    }

    public String getCompInstr1() { return compInstr1;}

    public void setCompInstr1(String compInstr1) {
        this.compInstr1 = compInstr1;
    }

    public String getCompInstr2() { return compInstr2;}

    public void setCompInstr2(String compInstr2) {
        this.compInstr2 = compInstr2;
    }
}
