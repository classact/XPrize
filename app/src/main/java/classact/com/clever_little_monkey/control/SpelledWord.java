package classact.com.clever_little_monkey.control;

import java.util.List;

/**
 * Created by Tseliso on 12/10/2016.
 */

public class SpelledWord {
    private ObjectAndSound<String> _word;
    private List<String> _lettersSound;
    private List<DraggableImage<String>> _lettersImages;

    public ObjectAndSound<String> getWord() {
        return _word;
    }

    public void setWord(ObjectAndSound<String> _word) {
        this._word = _word;
    }

    public List<String> getLettersSound() {
        return _lettersSound;
    }

    public void setettersSound(List<String> _lettersSound) {
        this._lettersSound = _lettersSound;
    }

    public List<DraggableImage<String>> getLettersImages() {
        return _lettersImages;
    }

    public void setLettersImages(List<DraggableImage<String>> _lettersImages) {
        this._lettersImages = _lettersImages;
    }
}
