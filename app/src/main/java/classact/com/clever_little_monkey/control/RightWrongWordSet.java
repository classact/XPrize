package classact.com.clever_little_monkey.control;

import java.util.ArrayList;

import classact.com.clever_little_monkey.database.model.Word;

public class RightWrongWordSet {

    private Word mRightWord;
    private ArrayList<DraggableImage<Word>> mRightAndWrongWords;

    public RightWrongWordSet(Word rightWord, ArrayList<DraggableImage<Word>> rightAndWrongWords) {
        mRightWord = rightWord;
        mRightAndWrongWords = rightAndWrongWords;
    }

    /* SETTERS */
    public void setRightWord(Word rightWord) {
        mRightWord = rightWord;
    }

    public void setRightAndWrongWords(ArrayList<DraggableImage<Word>> rightAndWrongWords) {
        mRightAndWrongWords = rightAndWrongWords;
    }

    /* GETTERS */
    public Word getRightWord() {
        return mRightWord;
    }

    public ArrayList<DraggableImage<Word>> getRightAndWrongWords() {
        return mRightAndWrongWords;
    }
}
