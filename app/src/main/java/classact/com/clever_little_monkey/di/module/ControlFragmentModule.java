package classact.com.clever_little_monkey.di.module;

import classact.com.clever_little_monkey.di.FragmentScoped;
import classact.com.clever_little_monkey.fragment.control.ControlFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hcdjeong on 2017/09/14.
 */

@Module
public abstract class ControlFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ControlFragment contributeControlFragment();
}
