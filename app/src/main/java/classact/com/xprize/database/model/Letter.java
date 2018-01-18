package classact.com.xprize.database.model;

/**
 * Created by Tseliso on 5/5/2016.
 */
public class Letter {
    private int letterId;
    private int languageId;
    private String letterName;
    private String letterPictureLowerCaseBlackURI;
    private String letterPictureUpperCaseBlackURI;
    private String letterPictureLowerCaseBlueURI;
    private String letterPictureUpperCaseRedURI;
    private String letterPictureLowerCaseDotsURI;
    private String letterPictureUpperCaseDotsURI;
    private String letterSoundURI;
    private String phonicSoundURI;
    private String letterLowerPath;
    private String letterUpperPath;
    private int isLetter;

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLetterName() {
        return letterName;
    }

    public void setLetterName(String letterName) {
        this.letterName = letterName;
    }

    public String getLetterPictureLowerCaseBlackURI() {
        return letterPictureLowerCaseBlackURI;
    }

    public void setLetterPictureLowerCaseBlackURI(String letterPictureLowerCaseBlackURI) {
        this.letterPictureLowerCaseBlackURI = letterPictureLowerCaseBlackURI;
    }

    public String getLetterPictureUpperCaseBlackURI() {
        return letterPictureUpperCaseBlackURI;
    }

    public void setLetterPictureUpperCaseBlackURI(String letterPictureUpperCaseBlackURI) {
        this.letterPictureUpperCaseBlackURI = letterPictureUpperCaseBlackURI;
    }

    public String getLetterPictureLowerCaseBlueURI() {
        return letterPictureLowerCaseBlueURI;
    }

    public void setLetterPictureLowerCaseBlueURI(String letterPictureLowerCaseBlueURI) {
        this.letterPictureLowerCaseBlueURI = letterPictureLowerCaseBlueURI;
    }

    public String getLetterPictureUpperCaseRedURI() {
        return letterPictureUpperCaseRedURI;
    }

    public void setLetterPictureUpperCaseRedURI(String letterPictureUpperCaseRedURI) {
        this.letterPictureUpperCaseRedURI = letterPictureUpperCaseRedURI;
    }

    public String getLetterPictureLowerCaseDotsURI() {
        return letterPictureLowerCaseDotsURI;
    }

    public void setLetterPictureLowerCaseDotsURI(String letterPictureLowerCaseDotsURI) {
        this.letterPictureLowerCaseDotsURI = letterPictureLowerCaseDotsURI;
    }

    public String getLetterPictureUpperCaseDotsURI() {
        return letterPictureUpperCaseDotsURI;
    }

    public void setLetterPictureUpperCaseDotsURI(String letterPictureUpperCaseDotsURI) {
        this.letterPictureUpperCaseDotsURI = letterPictureUpperCaseDotsURI;
    }

    public String getLetterSoundURI() {
        return letterSoundURI;
    }

    public void setLetterSoundURI(String letterSoundURI) {
        this.letterSoundURI = letterSoundURI;
    }

    public String getPhonicSoundURI() {
        return phonicSoundURI;
    }

    public void setPhonicSoundURI(String phonicSoundURI) {
        this.phonicSoundURI = phonicSoundURI;
    }

    public String getLetterLowerPath() {
        return letterLowerPath;
    }

    public void setLetterLowerPath(String letterLowerPath) {
        this.letterLowerPath = letterLowerPath;
    }
    public String getLetterUpperPath() {
        return letterUpperPath;
    }

    public void setLetterUpperPath(String letterUpperPath) {
        this.letterUpperPath = letterUpperPath;
    }

    public int getIsLetter() {
        return isLetter;
    }

    public void setIsLetter(int isLetter) {
        this.isLetter = isLetter;
    }
}