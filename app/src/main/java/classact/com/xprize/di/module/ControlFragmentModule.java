package classact.com.xprize.di.module;

import classact.com.xprize.di.FragmentScoped;
import classact.com.xprize.fragment.control.ControlFragment;
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
