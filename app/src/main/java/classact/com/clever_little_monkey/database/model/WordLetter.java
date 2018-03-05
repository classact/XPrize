package classact.com.clever_little_monkey.database.model;

/**
 * Created by Tseliso on 5/6/2016.
 */
public class WordLetter {
    private int wordLetterId;
    private int wordId;
    private int letterId;
    private int position;

    public int getWordLetterId() {
        return wordLetterId;
    }

    public void setWordLetterId(int wordLetterId) {
        this.wordLetterId = wordLetterId;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
