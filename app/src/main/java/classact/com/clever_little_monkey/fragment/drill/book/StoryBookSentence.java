package classact.com.clever_little_monkey.fragment.drill.book;

import android.widget.LinearLayout;

import java.util.List;

import classact.com.clever_little_monkey.database.model.StorySentence;
import classact.com.clever_little_monkey.database.model.StoryWord;

/**
 * Created by hcdjeong on 2017/12/06.
 */

public class StoryBookSentence {

    private StorySentence sentence;
    private List<StoryWord> words;
    private List<LinearLayout> views;
    private List<Integer> lineIndexes;
    private int numberOfWords;

    public void setSentence(StorySentence sentence) {
        this.sentence = sentence;
    }

    public void setWords(List<StoryWord> words) {
        this.words = words;
    }

    public void setViews(List<LinearLayout> views) {
        this.views = views;
    }

    public void setLineIndexes(List<Integer> lineIndexes) {
        this.lineIndexes = lineIndexes;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public StorySentence getSentence() {
        return sentence;
    }

    public List<StoryWord> getWords() {
        return words;
    }

    public List<LinearLayout> getViews() {
        return views;
    }

    public List<Integer> getLineIndexes() {
        return lineIndexes;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }
}