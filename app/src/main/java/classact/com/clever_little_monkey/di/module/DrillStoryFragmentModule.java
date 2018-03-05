package classact.com.clever_little_monkey.di.module;

import classact.com.clever_little_monkey.di.FragmentScoped;
import classact.com.clever_little_monkey.fragment.drill.book.StoryListenAndRead;
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
