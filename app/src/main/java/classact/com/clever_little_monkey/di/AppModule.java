package classact.com.clever_little_monkey.di;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;

import javax.inject.Singleton;

import classact.com.clever_little_monkey.activity.menu.controller.DatabaseController;
import classact.com.clever_little_monkey.controller.DrillFetcher;
import classact.com.clever_little_monkey.controller.catalogue.MathDrills;
import classact.com.clever_little_monkey.controller.catalogue.StoryDrills;
import classact.com.clever_little_monkey.controller.catalogue.WordDrills;
import classact.com.clever_little_monkey.database.DbHelper;
import classact.com.clever_little_monkey.database.helper.DrillHelper;
import classact.com.clever_little_monkey.database.helper.DrillTypeHelper;
import classact.com.clever_little_monkey.database.helper.LetterHelper;
import classact.com.clever_little_monkey.database.helper.SectionHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionDrillHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionHelper;
import classact.com.clever_little_monkey.di.module.ViewModelModule;
import classact.com.clever_little_monkey.utils.EZ;
import dagger.Module;
import dagger.Provides;

/**
 * Created by hcdjeong on 2017/08/31.
 * App Module for Dependency Injection via Dagger 2
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
            WordDrills wordDrills,
            StoryDrills storyDrills, MathDrills mathDrills,
            LetterHelper letterHelper) {
        return new DrillFetcher(
                context,
                databaseController,
                dbHelper,
                wordDrills,
                storyDrills, mathDrills,
                letterHelper);
    }

    @Singleton @Provides
    EZ ez(Context context) {
        return new EZ(context);
    }
}