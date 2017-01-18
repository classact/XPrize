package classact.com.xprize.utils;

import android.media.MediaPlayer;

/**
 * Created by Tseliso on 12/4/2016.
 */

public class MediaPlayerHelper {
    public static void play(MediaPlayer mp){
        //new Thread(){
            mp.start();
        //}.start();
    }
}
