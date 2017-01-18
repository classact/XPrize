package classact.com.xprize.control;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStorySentence {
    private ArrayList<Word> words;
    private String fullSound;

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public String getFullSound() {
        return fullSound;
    }

    public void setFullSound(String fullSound) {
        this.fullSound = fullSound;
    }
}
