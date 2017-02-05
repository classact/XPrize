package classact.com.xprize.control;

import java.util.ArrayList;

import classact.com.xprize.database.model.SimpleStoryWord;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStorySentence {
    private ArrayList<SimpleStoryWord> words;
    private String fullSound;

    public ArrayList<SimpleStoryWord> getWords() {
        return words;
    }

    public void setWords(ArrayList<SimpleStoryWord> words) {
        this.words = words;
    }

    public String getFullSound() {
        return fullSound;
    }

    public void setFullSound(String fullSound) {
        this.fullSound = fullSound;
    }
}
