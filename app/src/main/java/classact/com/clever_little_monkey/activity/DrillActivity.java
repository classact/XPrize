package classact.com.clever_little_monkey.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import classact.com.clever_little_monkey.common.Code;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.event.PauseEvent;
import classact.com.clever_little_monkey.event.ResumeEvent;
import classact.com.clever_little_monkey.event.StopEvent;
import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.utils.EZ;
import classact.com.clever_little_monkey.utils.Fetch;
import classact.com.clever_little_monkey.utils.LiveHandler;
import classact.com.clever_little_monkey.utils.LiveMediaPlayer;
import classact.com.clever_little_monkey.utils.StarWorks;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public abstract class DrillActivity extends DaggerAppCompatActivity {

    protected @Inject ViewModelProvider.Factory viewModelFactory;
    protected @Inject Bus bus;
    protected @Inject Context context;
    protected @Inject EZ ez;
    protected @Inject Fetch fetch;
    protected @Inject StarWorks starWorks;

    protected LiveMediaPlayer mediaPlayer;
    protected LiveHandler handler;
    protected CompositeDisposable compositeDisposable;

    protected View container;
    protected View pauseScreen;
    protected int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(bus.asFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((o) -> {
                    if (o instanceof PauseEvent) {
                        onPauseEvent();
                    } else if (o instanceof ResumeEvent) {
                        onResumeEvent();
                    } else if (o instanceof StopEvent) {
                        onStopEvent();
                    }
                }));
    }

    protected void disable(View... views) {
        for (View view : views) {
            view.setEnabled(false);
            view.setFocusable(false);
            view.setClickable(false);
            view.setAlpha(0.2f);
        }
    }

    protected void enable(View... views) {
        for (View view : views) {
            view.setEnabled(true);
            view.setFocusable(true);
            view.setClickable(true);
            view.setAlpha(1f);
        }
    }

    protected void unHighlight(View view) {
        if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(null);
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.BLACK);
        }
    }

    protected void highlightWrong(View view) {
        if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(Color.RED);
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.RED);
        }
    }

    protected void highlightCorrect(View view) {
        if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(Color.parseColor("#33ccff"));
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.parseColor("#33ccff"));
        }
    }

    protected void playSound(String sound, Runnable action) {
        playSound(sound, action, 0);
    }

    protected void playSound(int soundId, Runnable action) {
        playSound(soundId, action, 0);
    }

    protected void playSound(String sound, Runnable startAction, Runnable completeAction) {
        playSound(sound, startAction, 0, completeAction, 0);
    }

    protected void playSound(int soundId, Runnable startAction, Runnable completeAction) {
        playSound(soundId, startAction, 0, completeAction, 0);
    }

    protected void playSound(String sound, Runnable action, long delayMillis) {
        try {
            String soundPath = fetch.raw(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (action != null) {
                    handler.delayed(action, delayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for ‘" + sound + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Activity", "playSound(sound, action)");
            ex.printStackTrace();
            if (action != null) {
                handler.delayed(action, 800);
            }
        }
    }

    protected void playSound(int soundId, Runnable action, long delayMillis) {
        try {
            String soundPath = "android.resource://" + getApplicationContext().getPackageName() + "/" + soundId;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (action != null) {
                    handler.delayed(action, delayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for sound id ‘" + soundId + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Activity", "playSound(soundId, action)");
            ex.printStackTrace();
            if (action != null) {
                handler.delayed(action, 800);
            }
        }
    }

    protected void playSound(int soundId, Runnable preparedAction, long preparedDelayMillis, Runnable completeAction, long completeDelayMillis) {
        try {
            String soundPath = "android.resource://" + getApplicationContext().getPackageName() + "/" + soundId;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
                if (preparedAction != null) {
                    handler.delayed(preparedAction, preparedDelayMillis);
                }
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (completeAction != null) {
                    handler.delayed(completeAction, completeDelayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for sound id ‘" + soundId + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Activity", "playSound(soundId, preparedAction, preparedDelayMillis, completeAction, completeDelayMillis)");
            ex.printStackTrace();
            if (preparedAction != null) {
                preparedAction.run();
                if (completeAction != null) {
                    handler.delayed(completeAction, 800);
                }
            }
        }
    }


    protected void playSound(String sound, Runnable preparedAction, long preparedDelayMillis, Runnable completeAction, long completeDelayMillis) {
        try {
            String soundPath = fetch.raw(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
                if (preparedAction != null) {
                    handler.delayed(preparedAction, preparedDelayMillis);
                }
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (completeAction != null) {
                    handler.delayed(completeAction, completeDelayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for ‘" + sound + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Activity", "playSound(sound, preparedAction, preparedDelayMillis, completeAction, completeDelayMillis)");
            ex.printStackTrace();
            if (preparedAction != null) {
                preparedAction.run();
                if (completeAction != null) {
                    handler.delayed(completeAction, 200);
                }
            }
        }
    }

    protected void onPauseEvent() {
        showGrayScreen();
    }

    protected void onResumeEvent() {
        hideGrayScreen();
    }

    protected void onStopEvent() {
        moveTaskToBack(true);
    }

    private void showGrayScreen() {
        if (!(container == null || pauseScreen == null)) {
            ez.gray(container);
            ez.show(pauseScreen);
            handler.delayed(() -> {
                Bitmap snapShot = ez.image.takeSnapShot(container);
                BitmapDrawable pauseScreenBackground = new BitmapDrawable(getResources(), snapShot);
                pauseScreen.setBackground(pauseScreenBackground);
            }, 100);
        }
    }

    private void hideGrayScreen() {
        if (!(container == null || pauseScreen == null)) {
            ez.hide(pauseScreen);
            pauseScreen.setBackgroundColor(Color.TRANSPARENT);
            ez.ungray(container);
        }
    }

    protected void loadImage(ImageView iv, int resId) {
        Glide.with(this).load(resId).into(iv);
    }

    protected void loadImage(ImageView iv, int resId, RequestListener requestListener) {
        Glide.with(this).load(resId).listener(requestListener).into(iv);
    }

    protected void loadAndLayoutImage(ImageView iv, int resId) {
        Drawable d = context.getResources().getDrawable(resId, null);
        ViewGroup.MarginLayoutParams ivLayoutParams = (ViewGroup.MarginLayoutParams) iv.getLayoutParams();
        ivLayoutParams.width = d.getIntrinsicWidth();
        ivLayoutParams.height = d.getIntrinsicHeight();
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

    protected void loadImage(ImageView iv, int placeholderResId, int newResId, RequestListener requestListener) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholderResId);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(newResId).listener(requestListener).into(iv);
    }

    protected void loadImage(ImageView iv, int placeholderResId, int newResId, int width, int height, RequestListener requestListener) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholderResId).override(width, height).dontAnimate();
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(newResId).listener(requestListener).into(iv);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();

        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (requestCode == Code.TO_HELP) {
            setResult(Code.TO_HELP);
        } else {
            setResult(Globals.TO_MAIN);
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            mediaPlayer.release();
//        }
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}