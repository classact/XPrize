package classact.com.clever_little_monkey.control;

import java.util.List;

/**
 * Created by Tseliso on 12/12/2016.
 */

public class Sentence {
    private int _wordCount;
    private String _sentenceText;
    private String _readSentenceSound;
    private List<DraggableImage<String>> _words;
    private String punctuation;

    public Sentence(int count,String text){
        _sentenceText = text;
        _wordCount = count;
    }

    public int getWCount() {
        return _wordCount;
    }

    public void setWordCount(int _wordCount) {
        this._wordCount = _wordCount;
    }

    public String getSentenceText() {
        return _sentenceText;
    }

    public void setSentenceText(String _sentenceText) {
        this._sentenceText = _sentenceText;
    }

    public String getReadSentenceSound() {
        return _readSentenceSound;
    }

    public void setReadSentenceSound(String _readSentenceSound) {
        this._readSentenceSound = _readSentenceSound;
    }

    public List<DraggableImage<String>> getWords() {
        return _words;
    }

    public void setWords(List<DraggableImage<String>> _words) {
        this._words = _words;
    }

    public void setPunctuation(String punctuation) {
        this.punctuation = punctuation;
    }

    public String getPunctuation() {
        return punctuation;
    }
}
