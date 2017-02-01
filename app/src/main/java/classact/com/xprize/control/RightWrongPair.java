package classact.com.xprize.control;

/**
 * Created by Tseliso on 12/3/2016.
 */

public class RightWrongPair {
    private ObjectAndSound _rightObject;
    private ObjectAndSound _wrongObject;

    public RightWrongPair (ObjectAndSound rightObject, ObjectAndSound wrongObject) {
        _rightObject = rightObject;
        _wrongObject = wrongObject;
    }

    public ObjectAndSound getRightObject() {
        return _rightObject;
    }

    public void setRightObject(ObjectAndSound _rightObject) {
        this._rightObject = _rightObject;
    }

    public ObjectAndSound getWrongObject() {
        return _wrongObject;
    }

    public void set_wrongObjectrongObject(ObjectAndSound _wrongObject) {
        this._wrongObject = _wrongObject;
    }
}
