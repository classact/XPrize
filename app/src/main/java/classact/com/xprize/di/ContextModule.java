package classact.com.xprize.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by hcdjeong on 2017/09/18.
 */

@Module
abstract class ContextModule {

    @Binds
    abstract Context bindContext(Application application);
}
