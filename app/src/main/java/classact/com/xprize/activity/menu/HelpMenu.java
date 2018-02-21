package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.MenuActivity;
import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import dagger.android.support.DaggerAppCompatActivity;

public class HelpMenu extends MenuActivity {

    @BindView(R.id.activity_help_menu) ConstraintLayout mRootView;

    @BindView(R.id.tutorial_button) ImageView mTutorialButton;
    @BindView(R.id.volume_button) ImageView mVolumeButton;

    private Handler mHandler;
    private Intent mIntent;
    private boolean mFinishActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
        ButterKnife.bind(this);

        mHandler = new Handler();
        mIntent = getIntent();
        mFinishActivity = false;

        preloadImage(R.drawable.tutorial_button_down, R.drawable.volume_button_down);

        loadAndLayoutImage(mTutorialButton, R.drawable.tutorial_button_up);
        loadAndLayoutImage(mVolumeButton, R.drawable.volume_button_up);

        setTouchListener(mTutorialButton, R.drawable.tutorial_button_up, R.drawable.tutorial_button_down);
        setTouchListener(mVolumeButton, R.drawable.volume_button_up, R.drawable.volume_button_down);

        // Tutorial Button
        mTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishActivity = true;
                Globals.STOP_BACKGROUND_MUSIC(context);
                Intent intent = new Intent(context, Tutorial.class);
                intent.putExtra("requestCode", Code.TO_HELP);
                startActivityForResult(intent, Code.TO_HELP);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Volume Button
        mVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishActivity = true;
                Intent intent = new Intent(context, VolumeMenu.class);
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
            case Code.TO_HELP:
                Globals.RESUME_BACKGROUND_MUSIC(context);
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
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mFinishActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFinishActivity) {
            mHandler.removeCallbacksAndMessages(null);
            Globals.PAUSE_BACKGROUND_MUSIC(context);
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
