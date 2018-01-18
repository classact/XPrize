package classact.com.xprize.di.module;

import classact.com.xprize.MainActivity;
import classact.com.xprize.activity.drill.books.StoryActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFiveActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFiveAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFourActivity;
import classact.com.xprize.activity.drill.math.MathsDrillOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndFourActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndTwoActivity;
import classact.com.xprize.activity.drill.math.MathsDrillThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillTwoActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillEightActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillElevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFifteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFiveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillNineActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSixActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThirteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThreeActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwelveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwoActivity;
import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.activity.menu.DrillsMenu;
import classact.com.xprize.activity.menu.HelpMenu;
import classact.com.xprize.activity.menu.PhonicsSubMenu;
import classact.com.xprize.activity.menu.SectionsMenu;
import classact.com.xprize.activity.menu.StarsMenu;
import classact.com.xprize.activity.menu.VolumeMenu;
import classact.com.xprize.activity.movie.MovieActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hcdjeong on 2017/12/27.
 */

@Module
public abstract class ActivityModule {

    /* Main Activity */

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MainActivity contributeMainActivity();

    /* Menu Activities */

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract HelpMenu contributeHelpMenuActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract VolumeMenu contributeVolumeMenuActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract StarsMenu contributeStarsMenuActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SectionsMenu contributeSectionsMenuActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract PhonicsSubMenu contributePhonicsSubMenuActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract DrillsMenu contributeDrillsMenuActivity();

    /* Story Drill Activity */

    @ContributesAndroidInjector(modules = {
            ControlFragmentModule.class,
            DrillStoryFragmentModule.class
    })
    abstract StoryActivity contributeStoryActivity();

    /* Movie Drill Activity */

    @ContributesAndroidInjector(modules = {
            ControlFragmentModule.class,
            DrillMovieFragmentModule.class
    })
    abstract MovieActivity contributeMovieActivity();

    /* Tutorial Activity */

    @ContributesAndroidInjector(modules = {
            ControlFragmentModule.class
    })
    abstract Tutorial contributeTutorialActivity();

    /* Sound Drill Activities */

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillOneActivity contributeSoundDrill01Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillTwoActivity contributeSoundDrill02Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillThreeActivity contributeSoundDrill03Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillFourActivity contributeSoundDrill04Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillFiveActivity contributeSoundDrill05Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillSixActivity contributeSoundDrill06Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillSevenActivity contributeSoundDrill07Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillEightActivity contributeSoundDrill08Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillNineActivity contributeSoundDrill09Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillTenActivity contributeSoundDrill10Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillElevenActivity contributeSoundDrill11Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillTwelveActivity contributeSoundDrill12Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillThirteenActivity contributeSoundDrill13Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillFourteenActivity contributeSoundDrill14Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract SoundDrillFifteenActivity contributeSoundDrill15Activity();

    /* Math Drill Activities */

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillOneActivity contributeMathDrill01Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillTwoActivity contributeMathDrill02Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillThreeActivity contributeMathDrill03Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillFourActivity contributeMathDrill04Activity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillFiveActivity contributeMathDrill05AActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillFiveAndOneActivity contributeMathDrill05BActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSixActivity contributeMathDrill06AActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSixAndOneActivity contributeMathDrill06BActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSixAndTwoActivity contributeMathDrill06CActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSixAndThreeActivity contributeMathDrill06DActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSixAndFourActivity contributeMathDrill06EActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSevenActivity contributeMathDrill07AActivity();

    @ContributesAndroidInjector(modules = ControlFragmentModule.class)
    abstract MathsDrillSevenAndOneActivity contributeMathDrill07BActivity();
}