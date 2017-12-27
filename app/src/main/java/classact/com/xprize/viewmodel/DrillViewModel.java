package classact.com.xprize.viewmodel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import classact.com.xprize.event.PauseEvent;
import classact.com.xprize.event.ResumeEvent;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.utils.LiveHandler;
import classact.com.xprize.utils.LiveMediaPlayer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcdjeong on 2017/12/25.
 * Master view model template for drills
 */

public abstract class DrillViewModel extends ViewModel {

    protected Bus bus;
    protected CompositeDisposable compositeDisposable;
    protected LiveHandler handler;
    protected LiveMediaPlayer mediaPlayer;
    protected boolean paused;

    protected DrillViewModel(Bus bus) {
        this.bus = bus;
        compositeDisposable = new CompositeDisposable();
        handler = new LiveHandler();
        mediaPlayer = new LiveMediaPlayer();
        paused = false;

        compositeDisposable.add(bus
                .asFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBusEvent));
    }

    public abstract DrillViewModel prepare(Context context);

    /**
     *
     * @param o message received by bus
     */
    protected void onBusEvent(Object o) {
        if (o instanceof PauseEvent) {
            paused = true;
            handler.forcePause();
            mediaPlayer.forcePause();
        } else if (o instanceof ResumeEvent) {
            paused = false;
            mediaPlayer.forceResume();
            handler.forceResume();
        }
    }

    public DrillViewModel register(Lifecycle lifecycle) {
        lifecycle.addObserver(handler);
        lifecycle.addObserver(mediaPlayer);
        return this;
    }

    public boolean isPaused() {
        return paused;
    }

    public LiveHandler getHandler() {
        return handler;
    }

    public LiveMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}