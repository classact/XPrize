package classact.com.xprize.control;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.database.model.SimpleStoryWord;

/**
 * Created by Tseliso on 1/3/2017.
 */

public class SimpleStorySentence {
    private List<SimpleStoryWord> words;
    private String fullSound;
    private int sentenceSetNo;

    /* * * * *
     * Words *
     * * * * */
    public List<SimpleStoryWord> getWords() { return words; }

    public void setWords(List<SimpleStoryWord> words) { this.words = words; }

    /* * * * * * * *
     * Full Sound *
     * * * * * * */
    public String getFullSound() { return fullSound; }

    public void setFullSound(String fullSound) { this.fullSound = fullSound; }
}
