package classact.com.clever_little_monkey.activity.drill.books;

import android.os.Bundle;
import android.view.KeyEvent;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.fragment.drill.book.StoryListenAndRead;
import dagger.android.support.DaggerAppCompatActivity;

public class StoryActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new StoryListenAndRead())
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
