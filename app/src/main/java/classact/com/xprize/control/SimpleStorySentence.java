package classact.com.xprize.control;

import java.lang.reflect.Array;
import java.util.ArrayList;

import classact.com.xprize.database.model.SimpleStoryWords;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStorySentence {
    private ArrayList<SimpleStoryWords> words;
    private String fullSound;

    public ArrayList<SimpleStoryWords> getWords() {
        return words;
    }

    public void setWords(ArrayList<SimpleStoryWords> words) {
        this.words = words;
    }

    public String getFullSound() {
        return fullSound;
    }

    public void setFullSound(String fullSound) {
        this.fullSound = fullSound;
    }
}
