package classact.com.clever_little_monkey.activity.movie;

import android.os.Bundle;
import android.view.KeyEvent;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.fragment.drill.movie.DrillMovieFragment;
import dagger.android.support.DaggerAppCompatActivity;

public class MovieActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DrillMovieFragment())
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();

        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}