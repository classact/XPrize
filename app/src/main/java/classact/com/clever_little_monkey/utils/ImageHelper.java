package classact.com.clever_little_monkey.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Tseliso on 1/11/2017.
 */

public class ImageHelper {
    public static int getLength(int imageResource, Context context){
        Drawable d = ContextCompat.getDrawable(context,imageResource);
        return  d.getIntrinsicWidth();
    }
}
