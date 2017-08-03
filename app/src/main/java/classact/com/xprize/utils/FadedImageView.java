package classact.com.xprize.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by hcdjeong on 2017/08/03.
 * https://stackoverflow.com/questions/21888674/apply-fading-edges-to-imageview
 */

public class FadedImageView extends android.support.v7.widget.AppCompatImageView {

    protected Context mContext;

    public FadedImageView(Context context) {
        super(context);
        mContext = context;
    }


}
