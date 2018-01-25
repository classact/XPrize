package classact.com.xprize.database.model;

/**
 * Created by Tseliso on 4/24/2016.
 */
public class Word {
    private int wordID;
    private int languageID;
    private String wordName;
    private int wordType;
    private String wordPictureURI;
    private String imagePictureURI;
    private String wordSlowSoundURI;
    private String wordSoundURI;
    private String english;
    private int isPlural;
    private int isVowel;

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

    public int getWordType() {
        return wordType;
    }

    public void setWordType(int wordType) {
        this.wordType = wordType;
    }

    public String getWordPictureURI() {
        return wordPictureURI;
    }

    public void setWordPictureURI(String wordPictureURI) {
        this.wordPictureURI = wordPictureURI;
    }

    public String getImagePictureURI() {
        return imagePictureURI;
    }

    public void setImagePictureURI(String imagePictureURI) {
        this.imagePictureURI = imagePictureURI;
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

    public int getIsPlural() {
        return isPlural;
    }

    public void setIsPlural(int isPlural) {
        this.isPlural = isPlural;
    }

    public int getIsVowel() {
        return isVowel;
    }

    public void setIsVowel(int isVowel) {
        this.isVowel = isVowel;
    }
}
