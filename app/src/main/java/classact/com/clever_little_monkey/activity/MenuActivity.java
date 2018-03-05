package classact.com.clever_little_monkey.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import classact.com.clever_little_monkey.utils.EZ;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public abstract class MenuActivity extends DaggerAppCompatActivity {

    protected @Inject Context context;
    protected @Inject ViewModelProvider.Factory vmFactory;
    protected @Inject EZ ez;

    protected void loadImage(ImageView iv, int resId) {
        Glide.with(this).load(resId).into(iv);
    }

    protected void loadAndLayoutImage(ImageView iv, int resId) {
        Drawable d = context.getResources().getDrawable(resId, null);
        ViewGroup.MarginLayoutParams ivLayoutParams = (ViewGroup.MarginLayoutParams) iv.getLayoutParams();
        ivLayoutParams.width = d.getIntrinsicWidth();
        ivLayoutParams.height = d.getIntrinsicHeight();
        iv.setLayoutParams(ivLayoutParams);
        Glide.with(this).load(resId).into(iv);
    }

    protected void loadAndLayoutImage(ImageView iv, int resId, float percentageScale) {
        Drawable d = context.getResources().getDrawable(resId, null);
        ViewGroup.MarginLayoutParams ivLayoutParams = (ViewGroup.MarginLayoutParams) iv.getLayoutParams();
        ivLayoutParams.width = (int) (percentageScale * d.getIntrinsicWidth());
        ivLayoutParams.height = (int) (percentageScale * d.getIntrinsicHeight());
        iv.setLayoutParams(ivLayoutParams);
        Glide.with(this).load(resId).into(iv);
    }

    protected void loadFadeImage(ImageView iv, int resId, int duration) {
        Glide.with(this).load(resId).transition(DrawableTransitionOptions.withCrossFade(duration)).into(iv);
    }

    protected void loadImage(ImageView iv, int placeholderResId, int newResId) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholderResId);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(newResId).into(iv);
    }

    protected int getMeasuredWidth(TextView textView, String text) {
        textView.setText(text);
        textView.measure(0, 0);
        return textView.getMeasuredWidth();
    }

    protected void setTouchListener(ImageView iv, int upResId, int downResId) {
        iv.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    loadImage(iv, upResId, downResId);
                    return true;
                case MotionEvent.ACTION_UP:
                    loadImage(iv, downResId, upResId);
                    iv.performClick();
                    return true;
                default:
                    return false;
            }
        });
    }

    protected void preloadImage(int... resIds) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(this).load(resIds[0]);
        if (resIds.length > 1) {
            for (int i = 1; i < resIds.length; i++) {
                requestBuilder = requestBuilder.load(resIds[i]);
            }
        }
        requestBuilder.preload();
    }
}