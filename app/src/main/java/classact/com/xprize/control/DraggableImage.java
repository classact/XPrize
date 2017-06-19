package classact.com.xprize.control;

/**
 * Created by Tseliso on 12/4/2016.
 */

public class DraggableImage<T> {
    private int _position;
    private int _isRight;
    private T  _content;
    private String extraData;
    private String sound;
    private String letter;
    private String word;

    public DraggableImage(int position, int isRight, T content){
        _position = position;
        _isRight= isRight;
        _content = content;
    }

    public int getPosition() {
        return _position;
    }

    public void setPosition(int _position) {
        this._position = _position;
    }

    public int isRight() {
        return _isRight;
    }

    public void setIsRight(int _isRight) {
        this._isRight = _isRight;
    }

    public T getcontent() {
        return _content;
    }

    public void setContent(T _content) {
        this._content = _content;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}