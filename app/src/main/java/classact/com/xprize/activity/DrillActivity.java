package classact.com.xprize.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import classact.com.xprize.common.Globals;
import classact.com.xprize.event.PauseEvent;
import classact.com.xprize.event.ResumeEvent;
import classact.com.xprize.event.StopEvent;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.utils.EZ;
import classact.com.xprize.utils.Fetch;
import classact.com.xprize.utils.LiveHandler;
import classact.com.xprize.utils.LiveMediaPlayer;
import classact.com.xprize.utils.StarWorks;
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

    protected void playSound(String sound, Runnable action) {
        playSound(sound, action, 0);
    }

    protected void playSound(int soundId, Runnable action) {
        playSound(soundId, action, 0);
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
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}