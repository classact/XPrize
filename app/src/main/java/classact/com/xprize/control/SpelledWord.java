package classact.com.xprize.control;

import java.util.ArrayList;

/**
 * Created by Tseliso on 12/10/2016.
 */

public class SpelledWord {
    private ObjectAndSound<String> _word;
    private ArrayList<String> _lettersSound;
    private ArrayList<DraggableImage<String>> _lettersImages;

    public ObjectAndSound<String> getWord() {
        return _word;
    }

    public void setWord(ObjectAndSound<String> _word) {
        this._word = _word;
    }

    public ArrayList<String> getLettersSound() {
        return _lettersSound;
    }

    public void setettersSound(ArrayList<String> _lettersSound) {
        this._lettersSound = _lettersSound;
    }

    public ArrayList<DraggableImage<String>> getLettersImages() {
        return _lettersImages;
    }

    public void setLettersImages(ArrayList<DraggableImage<String>> _lettersImages) {
        this._lettersImages = _lettersImages;
    }
}
