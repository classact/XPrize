package classact.com.xprize.activity.drill.tutorial;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/28.
 */

public class TutorialViewModel extends DrillViewModel {

    @Inject
    public TutorialViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public TutorialViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public TutorialViewModel prepare(Context context) {
        return this;
    }
}