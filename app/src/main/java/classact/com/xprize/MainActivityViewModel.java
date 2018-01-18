package classact.com.xprize;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by hcdjeong on 2017/12/25.
 */

public class MainActivityViewModel extends ViewModel {

    @Inject
    public MainActivityViewModel() {
        Log.d("HELLO", "CLEVER LITTLE MONKEY");
    }
}
