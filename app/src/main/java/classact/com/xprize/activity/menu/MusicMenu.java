package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import classact.com.xprize.R;
import classact.com.xprize.common.Globals;

public class MusicMenu extends AppCompatActivity {

    private TextView mVolumeText;
    private SeekBar mVolumeSeekbar;
    private TextView mVolumePercentage;
    private AudioManager mAudioManager;
    private Intent mIntent;
    private int mSelectedLanguage;
    private boolean mFinishActivity;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_menu);
        mIntent = getIntent();
        mSelectedLanguage = mIntent.getIntExtra("selected_language", 1);
        mFinishActivity = false;

        mVolumeText = (TextView) findViewById(R.id.volume_text);
        mVolumeSeekbar = (SeekBar) findViewById(R.id.volume_seekbar);
        mVolumePercentage = (TextView) findViewById(R.id.volume_percentage);

        mVolumeText.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mVolumePercentage.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

        mVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int volume = (int) (((float) progress) / 15f * 100f);
                mVolumePercentage.setText("" + volume + " %");
                Globals.SET_VOLUME(THIS, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mVolumeSeekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
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
            Globals.PAUSE_BACKGROUND_MUSIC(THIS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                mVolumeSeekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                mVolumeSeekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                return true;
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        mFinishActivity = true;
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
