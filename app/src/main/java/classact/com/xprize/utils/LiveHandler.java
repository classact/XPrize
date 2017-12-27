package classact.com.xprize.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;

/**
 * Created by hcdjeong on 2017/12/15.
 * Life-cycle aware Handler
 */

public class LiveHandler extends Handler implements LifecycleObserver {

    private LongSparseArray<Runnable> runnables;
    private LongSparseArray<Long> delays;
    private long pausedUptimeMillis;
    private boolean forcePaused;

    public LiveHandler() {
        this.runnables = new LongSparseArray<>();
        this.delays = new LongSparseArray<>();
        this.pausedUptimeMillis = 0;
        forcePaused = false;
    }

    public LiveHandler register(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        return this;
    }

    public void delayed(Runnable r, long delayMillis) {
        long uptimeMillis = SystemClock.uptimeMillis() + delayMillis;
        runnables.put(uptimeMillis, r);
        delays.put(uptimeMillis, delayMillis);
        Runnable runnable = () -> {
            r.run();
            remove(uptimeMillis);
        };
        this.sendMessageAtTime(getPostMessage(this, runnable), uptimeMillis);
    }

    private Message getPostMessage(Handler target, Runnable r) {
        return Message.obtain(target, r);
    }

    private void remove(long key) {
        runnables.remove(key);
        delays.remove(key);
    }

    public void forcePause() {
        forcePaused = true;
        pauseHandler();
    }

    public void forceResume() {
        forcePaused = false;
        resumeHandler();
    }

    private void pauseHandler() {
        pausedUptimeMillis = SystemClock.uptimeMillis();
        this.removeCallbacksAndMessages(null);
    }

    private void resumeHandler() {
        if (pausedUptimeMillis > 0) {

            // Get current uptime
            long currentUptimeMillis = SystemClock.uptimeMillis();

            // Instantiate new sparse arrays that will replace original
            LongSparseArray<Runnable> newRunnables = new LongSparseArray<>();
            LongSparseArray<Long> newDelays = new LongSparseArray<>();

            // Loop through original sparse arrays
            for (int i = 0; i < runnables.size(); i++) {

                // Get the key (Original uptime millis + delay millis)
                long key = runnables.keyAt(i);

                // Get the runnable and delay millis
                Runnable r = runnables.get(key);
                long delayMillis = delays.get(key);

                // Get the original uptime
                long originalUptimeMillis = key - delayMillis;

                // Get the uptime
                long uptimeDifference = pausedUptimeMillis - originalUptimeMillis;

                // Check if event has already passed
                if (uptimeDifference < 0) {
                    Log.e("LIVE HANDLER ERROR", "Uptime Difference is negative");
                    continue;
                }

                // Get delay difference
                long uptimeDelay = uptimeDifference - delayMillis;

                // Log.d("HANDLER", "d: " + delayMillis + ", u: " + uptimeDifference + ", ud: " + uptimeDelay);

                // Setup new uptime millis - add uptime delay to current uptime millis
                long newUptimeMillis = currentUptimeMillis + Math.abs(uptimeDelay);

                // Create new runnable
                Runnable runnable = () -> {
                    r.run();
                    remove(newUptimeMillis);
                };
                this.sendMessageAtTime(getPostMessage(this, runnable), newUptimeMillis);

                // Add new values to sparse arrays
                newRunnables.put(newUptimeMillis, r);
                newDelays.put(newUptimeMillis, delayMillis);
            }

            // Replace original sparse arrays
            runnables = newRunnables;
            delays = newDelays;
            pausedUptimeMillis = 0;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (!forcePaused) {
            resumeHandler();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (!forcePaused) {
            pauseHandler();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (!forcePaused) {
            pauseHandler();
        }
    }
}