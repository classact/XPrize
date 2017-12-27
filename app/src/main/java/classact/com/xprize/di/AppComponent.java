package classact.com.xprize.di;

import android.app.Application;

import javax.inject.Singleton;

import classact.com.xprize.CleverLittleMonkeyApp;
import classact.com.xprize.di.module.ActivityModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by hcdjeong on 2017/08/31.
 */

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityModule.class
})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(CleverLittleMonkeyApp cleverLittleMonkeyApp);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}