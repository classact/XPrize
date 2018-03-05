package classact.com.clever_little_monkey.control;

/**
 * Created by Tseliso on 12/3/2016.
 */

public class SoundDrillThreeObject {
    private ObjectAndSound _object;
    private RightWrongPair _pair;

    public SoundDrillThreeObject(ObjectAndSound object, RightWrongPair pair){
        _object = object;
        _pair = pair;
    }

    public ObjectAndSound getObject() {
        return _object;
    }

    public void setObject(ObjectAndSound _object) {
        this._object = _object;
    }

    public RightWrongPair getPair() {
        return _pair;
    }

    public void setPair(RightWrongPair _pair) {
        this._pair = _pair;
    }
}
