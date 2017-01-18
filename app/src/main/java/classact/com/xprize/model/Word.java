package classact.com.xprize.model;

/**
 * Created by Tseliso on 4/24/2016.
 */
public class Word {
    private int wordID;
    private int languageID;
    private String wordName;
    private String wordPictureURI;
    private String wordSlowSoundURI;
    private String wordSoundURI;
    private String english;

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordPictureURI() {
        return wordPictureURI;
    }

    public void setWordPictureURI(String wordPictureURI) {
        this.wordPictureURI = wordPictureURI;
    }

    public String getWordSlowSoundURI() {
        return wordSlowSoundURI;
    }

    public void setWordSlowSoundURI(String wordSlowSoundURI) {
        this.wordSlowSoundURI = wordSlowSoundURI;
    }

    public String getWordSoundURI() {
        return wordSoundURI;
    }

    public void setWordSoundURI(String wordSoundURI) {
        this.wordSoundURI = wordSoundURI;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

}
