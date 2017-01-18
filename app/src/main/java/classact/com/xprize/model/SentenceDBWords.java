package classact.com.xprize.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class SentenceDBWords {
    private int sentenceWordID;
    private int languageID;
    private int unitID;
    private int sentenceID;
    private int sentenceNo;
    private int wordNo;
    private String wordName;
    private String wordImage;
    private String wordSound;

    public int getSentenceWordID() {
        return sentenceWordID;
    }

    public void setSentenceWordID(int sentenceWordID) {
        this.sentenceWordID = sentenceWordID;
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

    public int getSentenceID() {
        return sentenceID;
    }

    public void setSentenceID(int sentenceID) {
        this.sentenceID = sentenceID;
    }

    public int getSentenceNo() {
        return sentenceNo;
    }

    public void setSentenceNo(int sentenceNo) {
        this.sentenceNo = sentenceNo;
    }

    public int getWordNo() {
        return wordNo;
    }

    public void setWordNo(int wordNo) {
        this.wordNo = wordNo;
    }

    public String getWordName() { return wordName;}

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordImage() { return wordImage;}

    public void setWordImage(String wordImage) {
        this.wordImage = wordImage;
    }
    public String getWordSound() { return wordSound;}

    public void setWordSound(String wordSound) {
        this.wordSound = wordSound;
    }

}
