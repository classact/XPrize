package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class SoundDrill15ViewModel extends DrillViewModel {

    @Inject
    public SoundDrill15ViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public SoundDrill15ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill15ViewModel prepare(Context context) {
        return this;
    }
}