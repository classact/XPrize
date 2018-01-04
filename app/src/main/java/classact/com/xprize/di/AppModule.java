package classact.com.xprize.di;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;

import javax.inject.Singleton;

import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.database.DbHelper;
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

    @Singleton @Provides
    DatabaseController databaseController(DbHelper dbHelper) {
        return new DatabaseController(dbHelper);
    }

    @Singleton @Provides
    DrillFetcher drillFetcher(DatabaseController databaseController) {
        return new DrillFetcher(databaseController);
    }

    @Singleton @Provides
    EZ ez(Context context) {
        return new EZ(context);
    }
}