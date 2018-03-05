package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class MathDrill06BViewModel extends DrillViewModel {

    @Inject
    public MathDrill06BViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public MathDrill06BViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public MathDrill06BViewModel prepare(Context context) {
        return this;
    }
}