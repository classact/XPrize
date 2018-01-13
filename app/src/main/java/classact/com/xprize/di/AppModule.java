package classact.com.xprize.di;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.util.Log;

import javax.inject.Singleton;

import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.controller.catalogue.MathDrills;
import classact.com.xprize.controller.catalogue.PhonicsDrills;
import classact.com.xprize.controller.catalogue.StoryDrills;
import classact.com.xprize.controller.catalogue.WordDrills;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillHelper;
import classact.com.xprize.database.helper.DrillTypeHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.SectionHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.di.module.ViewModelModule;
import classact.com.xprize.utils.EZ;
import dagger.Module;
import dagger.Provides;

/**
 * Created by hcdjeong on 2017/08/31.
 */

@Module(includes = {
        ContextModule.class,
        ViewModelModule.class
})
class AppModule {

    @Singleton @Provides
    AssetManager assetManager(Context context) {
        return context.getAssets();
    }

    @Singleton @Provides
    AudioManager audioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Singleton @Provides
    DbHelper dbHelper(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        try {
            dbHelper.createDatabase(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dbHelper;
    }

    @Provides
    PhonicsDrills phonicsDrills(LetterHelper letterHelper) {
        return new PhonicsDrills(letterHelper);
    }

    @Provides
    WordDrills wordDrills(LetterHelper letterHelper) {
        return new WordDrills(letterHelper);
    }

    @Provides
    StoryDrills storyDrills() {
        return new StoryDrills();
    }

    @Provides
    MathDrills mathDrills() {
        return new MathDrills();
    }

    @Provides
    DrillHelper drillHelper() {
        return new DrillHelper();
    }

    @Provides
    DrillTypeHelper drillTypeHelper() {
        return new DrillTypeHelper();
    }

    @Provides
    LetterHelper letterHelper() {
        return new LetterHelper();
    }

    @Provides
    SectionHelper sectionHelper() {
        return new SectionHelper();
    }

    @Provides
    UnitSectionHelper unitSectionHelper() {
        return new UnitSectionHelper();
    }

    @Provides
    UnitSectionDrillHelper unitSectionDrillHelper() {
        return new UnitSectionDrillHelper();
    }

    @Singleton @Provides
    DatabaseController databaseController(DbHelper dbHelper,
                                          DrillHelper drillHelper, DrillTypeHelper drillTypeHelper,
                                          SectionHelper sectionHelper,
                                          UnitSectionHelper unitSectionHelper, UnitSectionDrillHelper unitSectionDrillHelper) {
        return new DatabaseController(
                dbHelper,
                drillHelper, drillTypeHelper,
                sectionHelper,
                unitSectionHelper, unitSectionDrillHelper);
    }

    @Singleton @Provides
    DrillFetcher drillFetcher(
            Context context,
            DatabaseController databaseController,
            DbHelper dbHelper,
            PhonicsDrills phonicsDrills, WordDrills wordDrills,
            StoryDrills storyDrills, MathDrills mathDrills,
            LetterHelper letterHelper) {
        return new DrillFetcher(
                context,
                databaseController,
                dbHelper,
                phonicsDrills, wordDrills,
                storyDrills, mathDrills,
                letterHelper);
    }

    @Singleton @Provides
    EZ ez(Context context) {
        return new EZ(context);
    }
}