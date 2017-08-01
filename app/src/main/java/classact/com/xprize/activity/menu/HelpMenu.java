package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import classact.com.xprize.R;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Globals;

public class HelpMenu extends AppCompatActivity {

    private ConstraintLayout mRootView;

    private ImageButton mTutorialButton;
    private ImageButton mVolumeButton;

    private DatabaseController mDb;
    private Handler mHandler;
    private Intent mIntent;
    private boolean mFinishActivity;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
        mRootView = (ConstraintLayout) findViewById(R.id.activity_help_menu);

        mHandler = new Handler();
        mIntent = getIntent();
        mFinishActivity = false;

        // Tutorial Button
        mTutorialButton = (ImageButton) findViewById(R.id.tutorial_button);
        mTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(THIS, DemoStory.class);
                mFinishActivity = true;
                Globals.STOP_BACKGROUND_MUSIC(THIS);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                */
            }
        });

        // Volume Button
        mVolumeButton = (ImageButton) findViewById(R.id.volume_button);
        mVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishActivity = true;
                Intent intent = new Intent(THIS, VolumeMenu.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFinishActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(THIS);
        } else {
            mFinishActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFinishActivity) {
            mHandler.removeCallbacksAndMessages(null);
            Globals.PAUSE_BACKGROUND_MUSIC(THIS);
        }
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
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacksAndMessages(null);
        mFinishActivity = true;
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
