package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class SoundDrill05ViewModel extends DrillViewModel {

    @Inject
    public SoundDrill05ViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public SoundDrill05ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill05ViewModel prepare(Context context) {
        return this;
    }
}