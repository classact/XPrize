package classact.com.clever_little_monkey.database.model;

/**
 * Created by JHB on 2016/12/16.
 */

public class Comprehension {
    private int comprehensionID;
    private int languageID;
    private int unitID;
    private String qA_No;
    private int questionHasSoundAnswer;
    private String questionSound;
    private String answerSound;
    private int numberOfPictures;
    private String correctPicture;
    private String picture1;
    private String picture2;
    private String picture3;
    private String picture4;

    public int getComprehensionID() {
        return comprehensionID;
    }

    public void setComprehensionID(int comprehensionID) {
        this.comprehensionID = comprehensionID;
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

    public String getqA_No() {
        return qA_No;
    }

    public void setqA_No(String qA_No) {
        this.qA_No = qA_No;
    }

    public int getQuestionHasSoundAnswer() {
        return questionHasSoundAnswer;
    }

    public void setQuestionHasSoundAnswer(int questionHasSoundAnswer) {
        this.questionHasSoundAnswer = questionHasSoundAnswer;
    }

    public String getQuestionSound() { return questionSound;}

    public void setQuestionSound(String questionSound) {
        this.questionSound = questionSound;
    }

    public String getAnswerSound() { return answerSound;}

    public void setAnswerSound(String answerSound) {
        this.answerSound = answerSound;
    }

    public int getNumberOfPictures() {
        return numberOfPictures;
    }

    public void setNumberOfPictures(int numberOfPictures) {
        this.numberOfPictures = numberOfPictures;
    }

    public String getCorrectPicture() { return correctPicture;}

    public void setCorrectPicture(String correctPicture) {
        this.correctPicture = correctPicture;
    }

    public String getPicture1() { return picture1; }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() { return picture2; }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() { return picture3; }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getPicture4() { return picture4; }

    public void setPicture4(String picture4) {
        this.picture4 = picture4;
    }
}
