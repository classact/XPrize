package classact.com.clever_little_monkey.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class SentenceDB {
    private int sentenceID;
    private int languageID;
    private int unitID;
    private int drillID;
    private String sentence;
    private int wordCount;
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

    public String getSentence() { return sentence;}

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getSentenceSoundFile() { return sentenceSoundFile;}

    public void setSentenceSoundFile(String sentenceSoundFile) {
        this.sentenceSoundFile = sentenceSoundFile;
    }

}
