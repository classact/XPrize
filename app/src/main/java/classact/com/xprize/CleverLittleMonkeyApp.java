package classact.com.xprize;

import classact.com.xprize.di.AppComponent;
import classact.com.xprize.di.DaggerAppComponent;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by hcdjeong on 2017/08/31.
 * The App for Dagger
 */

public class CleverLittleMonkeyApp extends DaggerApplication {

    /**
     * Implementations should return an {@link AndroidInjector} for the concrete {@link
     * DaggerApplication}. Typically, that injector is a {@link Component}.
     */
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);

        return appComponent;
    }
}