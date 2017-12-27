package classact.com.xprize.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by hcdjeong on 2017/09/05.
 * Life-cycle aware media player
 * Pauses when screen is paused
 */

public class LiveMediaPlayer extends MediaPlayer implements LifecycleObserver {

    private boolean play;
    private boolean paused;
    private boolean forcePaused;
    private OnResetListener onResetListener;

    public LiveMediaPlayer() {
        this.play = false;
        forcePaused = false;
    }

    public LiveMediaPlayer register(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }

    @Override
    public void start() {
        play = true;
        if (!paused) {
            super.start();
        }
    }

    @Override
    public void stop() {
        play = false;
        super.stop();
    }

    @Override
    public void reset() {
        play = false;
        OnResetListener onResetListener = this.onResetListener;
        if (onResetListener != null) {
            this.onResetListener.onReset(this);
        }
        super.reset();
        this.onResetListener = null;
    }

    @Override
    public void release() {
        onResetListener = null;
        super.release();
    }

    public void forcePause() {
        forcePaused = true;
        pausePlayer();
    }

    public void forceResume() {
        forcePaused = false;
        resumePlayer();
    }

    private void pausePlayer() {
        if (!paused && play) {
            pause();
        }
        paused = true;
    }

    private void resumePlayer() {
        paused = false;
        if (play) {
            start();
        }
    }

    public void setOnResetListener(OnResetListener listener) {
        this.onResetListener = listener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (!forcePaused) {
           resumePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (!forcePaused) {
            pausePlayer();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (!forcePaused) {
            pausePlayer();
        }
    }

    public interface OnResetListener {
        void onReset(MediaPlayer mp);
    }
}