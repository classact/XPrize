package classact.com.xprize.control;

import java.util.ArrayList;

/**
 * Created by Tseliso on 1/14/2017.
 */

public class ComprehensionQuestion {
    private String questionSound;
    private String answerSound;
    private int isATouchQuestion;
    private ArrayList<DraggableImage<String>> images;

    public ComprehensionQuestion(String questionSound, String answerSound, int isATouchQuestion){
        this.questionSound = questionSound;
        this.answerSound = answerSound;
        this.isATouchQuestion = isATouchQuestion;
    }

    public String getQuestionSound() {
        return questionSound;
    }

    public void setQuestionSound(String questionSound) {
        this.questionSound = questionSound;
    }

    public String getAnswerSound() {
        return answerSound;
    }

    public void setAnswerSound(String answerSound) {
        this.answerSound = answerSound;
    }

    public int getIsATouchQuestion() {
        return isATouchQuestion;
    }

    public void setIsATouchQuestion(int isTouchAQuestion) {
        this.isATouchQuestion = isATouchQuestion;
    }

    public ArrayList<DraggableImage<String>> getImages() {
        return images;
    }

    public void setImages(ArrayList<DraggableImage<String>> images) {
        this.images = images;
    }
}
