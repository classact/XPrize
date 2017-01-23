package classact.com.xprize.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStories {
    private int sentenceID;
    private int languageID;
    private int unitID;
    private int drillID;
    private int sentenceNo;
    private String sentenceSoundFile;

    public int getSentenceID() {
        return sentenceID;
    }

    public void setSentenceID(int sentenceID) {
        this.sentenceID = sentenceID;
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

    public int getDrillID() {
        return drillID;
    }

    public void setDrillID(int drillID) {
        this.drillID = drillID;
    }

    public int getSentenceNo() { return sentenceNo;}

    public void setSentenceNo(int sentenceNo) {
        this.sentenceNo = sentenceNo;
    }

    public String getSentenceSoundFile() { return sentenceSoundFile;}

    public void setSentenceSoundFile(String sentenceSoundFile) {
        this.sentenceSoundFile = sentenceSoundFile;
    }
}
