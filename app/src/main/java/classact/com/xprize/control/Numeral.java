package classact.com.xprize.control;

/**
 * Created by Tseliso on 12/24/2016.
 */

public class Numeral {
    private String sound;
    private String blackImage;
    private String sparklingImage;

    public Numeral(String sound, String blackImage,String sparklingImage){
        this.sound = sound;
        this.blackImage = blackImage;
        this.sparklingImage = sparklingImage;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getBlackImage() {
        return blackImage;
    }

    public void setBlackImage(String blackImage) {
        this.blackImage = blackImage;
    }

    public String getSparklingImage() {
        return sparklingImage;
    }

    public void setSparklingImage(String sparklingImage) {
        this.sparklingImage = sparklingImage;
    }
}
