package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class MathDrill02ViewModel extends DrillViewModel {

    @Inject
    public MathDrill02ViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public MathDrill02ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public MathDrill02ViewModel prepare(Context context) {
        return this;
    }
}
