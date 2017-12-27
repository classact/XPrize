package classact.com.xprize.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by hcdjeong on 2017/12/27.
 */

public abstract class DrillActivity extends DaggerAppCompatActivity {

    protected @Inject ViewModelProvider.Factory viewModelFactory;
    protected @Inject Context context;
}
