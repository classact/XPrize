package classact.com.xprize.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import classact.com.xprize.MainActivityViewModel;
import classact.com.xprize.activity.drill.books.StoryActivityViewModel;
import classact.com.xprize.activity.drill.math.MathDrill01ViewModel;
import classact.com.xprize.activity.drill.math.MathDrill02ViewModel;
import classact.com.xprize.activity.drill.math.MathDrill03ViewModel;
import classact.com.xprize.activity.drill.math.MathDrill04ViewModel;
import classact.com.xprize.activity.drill.math.MathDrill05AViewModel;
import classact.com.xprize.activity.drill.math.MathDrill05BViewModel;
import classact.com.xprize.activity.drill.math.MathDrill06AViewModel;
import classact.com.xprize.activity.drill.math.MathDrill06BViewModel;
import classact.com.xprize.activity.drill.math.MathDrill06CViewModel;
import classact.com.xprize.activity.drill.math.MathDrill06DViewModel;
import classact.com.xprize.activity.drill.math.MathDrill06EViewModel;
import classact.com.xprize.activity.drill.math.MathDrill07AViewModel;
import classact.com.xprize.activity.drill.math.MathDrill07BViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill01ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill02ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill03ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill04ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill05ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill06ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill07ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill08ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill09ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill10ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill11ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill12ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill13ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill14ViewModel;
import classact.com.xprize.activity.drill.sound.SoundDrill15ViewModel;
import classact.com.xprize.activity.drill.tutorial.TutorialViewModel;
import classact.com.xprize.activity.movie.MovieActivityViewModel;
import classact.com.xprize.di.ViewModelKey;
import classact.com.xprize.fragment.control.ControlViewModel;
import classact.com.xprize.fragment.drill.book.StoryListenAndReadViewModel;
import classact.com.xprize.fragment.drill.movie.DrillMovieViewModel;
import classact.com.xprize.viewmodel.CleverLittleMonkeyViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by hcdjeong on 2017/08/31.
 */

@Module
public abstract class ViewModelModule {

    /* ACTIVITIES */

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoryActivityViewModel.class)
    abstract ViewModel bindStoryActivityViewModel(StoryActivityViewModel storyActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieActivityViewModel.class)
    abstract ViewModel bindMovieActivityViewModel(MovieActivityViewModel movieActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TutorialViewModel.class)
    abstract ViewModel bindTutorialActivityViewModel(TutorialViewModel tutorialViewModel);

    /* Sound Drill Activities */

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill01ViewModel.class)
    abstract ViewModel bindSoundDrill01ViewModel(SoundDrill01ViewModel soundDrill01ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill02ViewModel.class)
    abstract ViewModel bindSoundDrill02ViewModel(SoundDrill02ViewModel soundDrill02ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill03ViewModel.class)
    abstract ViewModel bindSoundDrill03ViewModel(SoundDrill03ViewModel soundDrill03ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill04ViewModel.class)
    abstract ViewModel bindSoundDrill04ViewModel(SoundDrill04ViewModel soundDrill04ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill05ViewModel.class)
    abstract ViewModel bindSoundDrill05ViewModel(SoundDrill05ViewModel soundDrill05ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill06ViewModel.class)
    abstract ViewModel bindSoundDrill06ViewModel(SoundDrill06ViewModel soundDrill06ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill07ViewModel.class)
    abstract ViewModel bindSoundDrill07ViewModel(SoundDrill07ViewModel soundDrill07ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill08ViewModel.class)
    abstract ViewModel bindSoundDrill08ViewModel(SoundDrill08ViewModel soundDrill08ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill09ViewModel.class)
    abstract ViewModel bindSoundDrill09ViewModel(SoundDrill09ViewModel soundDrill09ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill10ViewModel.class)
    abstract ViewModel bindSoundDrill10ViewModel(SoundDrill10ViewModel soundDrill10ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill11ViewModel.class)
    abstract ViewModel bindSoundDrill11ViewModel(SoundDrill11ViewModel soundDrill11ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill12ViewModel.class)
    abstract ViewModel bindSoundDrill12ViewModel(SoundDrill12ViewModel soundDrill12ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill13ViewModel.class)
    abstract ViewModel bindSoundDrill13ViewModell(SoundDrill13ViewModel soundDrill13ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill14ViewModel.class)
    abstract ViewModel bindSoundDrill14ViewModel(SoundDrill14ViewModel soundDrill14ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SoundDrill15ViewModel.class)
    abstract ViewModel bindSoundDrill15ViewModel(SoundDrill15ViewModel soundDrill15ViewModel);

    /* Math Drill Activities */

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill01ViewModel.class)
    abstract ViewModel bindMathDrill01ViewModel(MathDrill01ViewModel mathDrill01ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill02ViewModel.class)
    abstract ViewModel bindMathDrill02ViewModel(MathDrill02ViewModel mathDrill02ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill03ViewModel.class)
    abstract ViewModel bindMathDrill03ViewModel(MathDrill03ViewModel mathDrill03ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill04ViewModel.class)
    abstract ViewModel bindMathDrill04ViewModel(MathDrill04ViewModel mathDrill04ViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill05AViewModel.class)
    abstract ViewModel bindMathDrill05AViewModel(MathDrill05AViewModel mathDrill05AViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill05BViewModel.class)
    abstract ViewModel bindMathDrill05BViewModel(MathDrill05BViewModel mathDrill05BViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill06AViewModel.class)
    abstract ViewModel bindMathDrill06AViewModel(MathDrill06AViewModel mathDrill06AViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill06BViewModel.class)
    abstract ViewModel bindMathDrill06BViewModel(MathDrill06BViewModel mathDrill06BViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill06CViewModel.class)
    abstract ViewModel bindMathDrill06CViewModel(MathDrill06CViewModel mathDrill06CViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill06DViewModel.class)
    abstract ViewModel bindMathDrill06DViewModel(MathDrill06DViewModel mathDrill06DViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill06EViewModel.class)
    abstract ViewModel bindMathDrill06EViewModel(MathDrill06EViewModel mathDrill06EViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill07AViewModel.class)
    abstract ViewModel bindMathDrill07AViewModel(MathDrill07AViewModel mathDrill07AViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MathDrill07BViewModel.class)
    abstract ViewModel bindMathDrill07BViewModel(MathDrill07BViewModel mathDrill07BViewModel);

    /* FRAGMENTS */

    @Binds
    @IntoMap
    @ViewModelKey(ControlViewModel.class)
    abstract ViewModel bindControlViewModel(ControlViewModel controlViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoryListenAndReadViewModel.class)
    abstract ViewModel bindStoryListenAndReadViewModel(StoryListenAndReadViewModel storyListenAndReadViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DrillMovieViewModel.class)
    abstract ViewModel bindDrillMovieViewModel(DrillMovieViewModel drillMovieViewModel);

    /* FACTORY */

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CleverLittleMonkeyViewModelFactory factory);
}