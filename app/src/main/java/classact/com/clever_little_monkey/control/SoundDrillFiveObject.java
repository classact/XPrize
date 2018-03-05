package classact.com.clever_little_monkey.control;

import java.util.ArrayList;

/**
 * Created by Tseliso on 12/4/2016.
 */

public class SoundDrillFiveObject {
    private ObjectAndSound _drillObject;
    public ArrayList<ObjectAndSound> _images;

    public SoundDrillFiveObject (ObjectAndSound drillObject,ArrayList<ObjectAndSound> images){
        _drillObject = drillObject;
        _images = images;
    }

    public ObjectAndSound getDrillObject() {
        return _drillObject;
    }

    public void setDrillObject(ObjectAndSound _drillObject) {
        this._drillObject = _drillObject;
    }

    public ArrayList<ObjectAndSound> getImages() {
        return _images;
    }

    public void setImages(ArrayList<ObjectAndSound> _images) {
        this._images = _images;
    }
}
