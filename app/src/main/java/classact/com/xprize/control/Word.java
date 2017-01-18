package classact.com.xprize.control;

import java.util.ArrayList;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class Word {
    private int numberInSentence;
    private String blackImage;
    private String redImage;
    private String sound;
    private ArrayList<String> sounds;

    public Word(int numberInSentence,String blackImage, String redImage){
        this.numberInSentence = numberInSentence;
        this.blackImage = blackImage;
        this.redImage = redImage;
    }

    public int getNumberInSentence() {
        return numberInSentence;
    }

    public void setNumberInSentence(int numberInSentence) {
        this.numberInSentence = numberInSentence;
    }

    public String getBlackImage() {
        return blackImage;
    }

    public void setBlackImage(String blackImage) {
        this.blackImage = blackImage;
    }

    public String getRedImage() {
        return redImage;
    }

    public void setRedImage(String redImage) {
        this.redImage = redImage;
    }

    public ArrayList<String> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<String> sounds) {
        this.sounds = sounds;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
