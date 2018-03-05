package classact.com.clever_little_monkey.di.module;

import classact.com.clever_little_monkey.di.FragmentScoped;
import classact.com.clever_little_monkey.fragment.drill.movie.DrillMovieFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hcdjeong on 2017/12/27.
 */

@Module
public abstract class DrillMovieFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DrillMovieFragment contributeDrillMovieFragment();
}