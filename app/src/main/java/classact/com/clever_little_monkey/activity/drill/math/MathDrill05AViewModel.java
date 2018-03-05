package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import javax.inject.Inject;

import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public class MathDrill05AViewModel extends DrillViewModel {

    @Inject
    public MathDrill05AViewModel(Bus bus) {
        super(bus);
    }

    @Override
    public MathDrill05AViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public MathDrill05AViewModel prepare(Context context) {
        return this;
    }
}