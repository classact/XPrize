package classact.com.clever_little_monkey.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoryWord {
    private int simpleStoryWordID;
    private int languageID;
    private int unitID;
    private int sentenceID;
    private int sentenceNo;
    private int wordNo;
    private String blackWord;
    private String redWord;
    private String sound;
    private int sentenceSetNo;

    /* * * * * * * * * * *
     * SimpleStoryWordID *
     * * * * * * * * * * */
    public int getSimpleStoryWordID() {
        return simpleStoryWordID;
    }

    public void setSimpleStoryWordID(int simpleStoryWordID) {
        this.simpleStoryWordID = simpleStoryWordID;
    }

    /* * * * * * * *
     * LanguageID *
     * * * * * * */
    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    /* * * * *
     * UnitID *
     * * * * * */
    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    /* * * * * * * *
     * SentenceID *
     * * * * * * */
    public int getSentenceID() {
        return sentenceID;
    }

    public void setSentenceID(int sentenceID) {
        this.sentenceID = sentenceID;
    }

    /* * * * * * *
     * SentenceNo *
     * * * * * * * */
    public int getSentenceNo() {
        return sentenceNo;
    }

    public void setSentenceNo(int sentenceNo) {
        this.sentenceNo = sentenceNo;
    }

    /* * * * * *
     * WordNo *
     * * * * */
    public int getWordNo() {
        return wordNo;
    }

    public void setWordNo(int wordNo) {
        this.wordNo = wordNo;
    }

    /* * * * * * *
     * BlackWord *
     * * * * * * */
    public String getBlackWord() { return blackWord;}

    public void setBlackWord(String blackWord) {
        this.blackWord = blackWord;
    }

    /* * * * * *
     * RedWord *
     * * * * * */
    public String getRedWord() { return redWord;}

    public void setRedWord(String redWord) {
        this.redWord = redWord;
    }

    /* * * * *
     * Sound *
     * * * * */
    public String getSound() { return sound;}

    public void setSound(String sound) {
        this.sound = sound;
    }

    /* * * * * * * * *
     * SentenceSetNo *
     * * * * * * * * */
    public int getSentenceSetNo() { return sentenceSetNo;}

    public void setSentenceSetNo(int sentenceSetNo) {
        this.sentenceSetNo = sentenceSetNo;
    }
}