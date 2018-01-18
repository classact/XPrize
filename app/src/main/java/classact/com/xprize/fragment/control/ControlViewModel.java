package classact.com.xprize.fragment.control;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

/**
 * Created by hcdjeong on 2017/09/14.
 * View Model for control
 */


public class ControlViewModel extends ViewModel {

    private boolean paused;

    @Inject
    public ControlViewModel() {
        this.paused = false;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }
}