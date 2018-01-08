package classact.com.xprize.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public abstract class MenuActivity extends DaggerAppCompatActivity {

    protected void loadImage(ImageView iv, int resId) {
        Glide.with(this).load(resId).into(iv);
    }

    protected int getMeasuredWidth(TextView textView, String text) {
        textView.setText(text);
        textView.measure(0, 0);
        return textView.getMeasuredWidth();
    }
}
