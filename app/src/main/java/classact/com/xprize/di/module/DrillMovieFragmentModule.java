package classact.com.xprize.di.module;

import classact.com.xprize.di.FragmentScoped;
import classact.com.xprize.fragment.drill.book.StoryListenAndRead;
import classact.com.xprize.fragment.drill.movie.DrillMovieFragment;
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