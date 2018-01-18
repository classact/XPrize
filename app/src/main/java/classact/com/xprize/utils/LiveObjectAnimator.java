package classact.com.xprize.utils;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator.AnimatorPauseListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * Created by hcdjeong on 2017/09/20.
 * Allows life-cycle aware behaviour for {@link ObjectAnimator}
 */

public class LiveObjectAnimator implements LifecycleObserver {

    private final Lifecycle lifecycle;
    private final ObjectAnimator objectAnimator;
    private AnimatorOnDestroyListener animatorOnDestroyListener;

    public LiveObjectAnimator(Lifecycle lifecycle, ObjectAnimator objectAnimator) {
        this.lifecycle = lifecycle;
        this.objectAnimator = objectAnimator;
        this.lifecycle.addObserver(this);
    }

    public ObjectAnimator get() {
        return objectAnimator;
    }

    public LiveObjectAnimator setDuration(long duration) {
        objectAnimator.setDuration(duration);
        return this;
    }

    public LiveObjectAnimator setStartDelay(long startDelay) {
        objectAnimator.setStartDelay(startDelay);
        return this;
    }

    public LiveObjectAnimator setRepeatCount(int value) {
        objectAnimator.setRepeatCount(value);
        return this;
    }

    public LiveObjectAnimator setRepeatMode(int value) {
        objectAnimator.setRepeatMode(value);
        return this;
    }

    public LiveObjectAnimator setCurrentFraction(float fraction) {
        objectAnimator.setCurrentFraction(fraction);
        return this;
    }

    public LiveObjectAnimator setCurrentPlayTime(long currentPlayTime) {
        objectAnimator.setCurrentPlayTime(currentPlayTime);
        return this;
    }

    public LiveObjectAnimator setInterpolator(TimeInterpolator timeInterpolator) {
        objectAnimator.setInterpolator(timeInterpolator);
        return this;
    }

    public LiveObjectAnimator addListener(AnimatorListener animatorListener) {
        objectAnimator.addListener(animatorListener);
        return this;
    }

    public LiveObjectAnimator addPauseListener(AnimatorPauseListener animatorPauseListener) {
        objectAnimator.addPauseListener(animatorPauseListener);
        return this;
    }

    public LiveObjectAnimator addUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        objectAnimator.addUpdateListener(animatorUpdateListener);
        return this;
    }

    public LiveObjectAnimator setOnDestroyListener(AnimatorOnDestroyListener animatorOnDestroyListener) {
        this.animatorOnDestroyListener = animatorOnDestroyListener;
        return this;
    }

    public LiveObjectAnimator start() {
        objectAnimator.start();
        return this;
    }

    public void pause() {
        objectAnimator.pause();
    }

    public void resume() {
        objectAnimator.resume();
    }

    public boolean isStarted() {
        return objectAnimator.isStarted();
    }


    public boolean isRunning() {
        return objectAnimator.isRunning();
    }

    public boolean isPaused() {
        return objectAnimator.isPaused();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (objectAnimator.isRunning() && !objectAnimator.isPaused()) {
            objectAnimator.pause();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (objectAnimator.isRunning() && objectAnimator.isPaused()) {
            objectAnimator.resume();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (animatorOnDestroyListener != null) {
            animatorOnDestroyListener.onDestroy(objectAnimator);
        }
        lifecycle.removeObserver(this);
    }
}
