package classact.com.xprize.control;

import java.util.ArrayList;

/**
 * Created by Tseliso on 12/3/2016.
 */

public class ObjectAndSound<T> {
    private String _objectImage;
    private String _objectSound;
    private String _objectPhonicSound;
    private String _beginningLetterSound;
    private T _customData;
    private String _objectSlowSound;
    private String _spelling;

    public ObjectAndSound(String objectImage, String objectSound, String objectPhonicSound){
        _objectImage = objectImage;
        _objectSound = objectSound;
        _objectPhonicSound = objectPhonicSound;
    }

    public String getObjectImage() {
        return _objectImage;
    }

    public void setObjectImage(String _objectImage) {
        this._objectImage = _objectImage;
    }

    public String getObjectSound() {
        return _objectSound;
    }

    public void setObjectSound(String _objectSound) {
        this._objectSound = _objectSound;
    }

    public String getObjectPhonicSound() {
        return _objectPhonicSound;
    }

    public void setObjectPhonicSound(String _objectPhonicSound) {
        this._objectPhonicSound = _objectPhonicSound;
    }

    public T getCustomData() {
        return _customData;
    }

    public void setCustomData(T _customData) {
        this._customData = _customData;
    }

    public String getBeginningLetterSound() {
        return _beginningLetterSound;
    }

    public void setBeginningLetterSound(String _beginningLetterSound) {
        this._beginningLetterSound = _beginningLetterSound;
    }

    public String getObjectSlowSound() {
        return _objectSlowSound;
    }

    public void setObjectSlowSound(String _objectSlowSound) {
        this._objectSlowSound = _objectSlowSound;
    }

    public String getSpelling() {
        return _spelling;
    }

    public void setSpelling(String spelling){
        _spelling = spelling;
    }
}

