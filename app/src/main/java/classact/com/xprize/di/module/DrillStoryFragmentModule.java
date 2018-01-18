package classact.com.xprize.di.module;

import classact.com.xprize.di.FragmentScoped;
import classact.com.xprize.fragment.drill.book.StoryListenAndRead;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hcdjeong on 2017/12/25.
 */

@Module
public abstract class DrillStoryFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract StoryListenAndRead contributeDrillStoryFragment();
}
