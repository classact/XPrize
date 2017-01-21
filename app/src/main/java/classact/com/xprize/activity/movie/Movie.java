package classact.com.xprize.activity.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import classact.com.xprize.R;
import classact.com.xprize.activity.template.MovieTemplate;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.ResourceDecoder;

public class Movie extends MovieTemplate {

    public final String SWAHILI_PREFIX = "s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        super.mSplashScreenContainer = (RelativeLayout) findViewById(R.id.chapter_01_movie_splash_container);
        super.mSplashScreen = (RelativeLayout) findViewById(R.id.chapter_01_movie_splash);
        super.mVideo = (VideoView) findViewById(R.id.chapter_01_movie_video);
        super.mPlayButton = (Button) findViewById(R.id.chapter_01_movie_play_button);
        super.mPauseButton = (Button) findViewById(R.id.chapter_01_movie_pause_button);

        super.mSplashScreenContainer.setVisibility(View.VISIBLE);
        super.mPauseButton.setVisibility(View.INVISIBLE);

        super.mUnitId = 1;
        super.mActivityName = "Movie";
        super.mNextActivityClassName = null;

        // Requires data from invoker intent
        Intent intent = getIntent();

        // Show hide play/stop buttons
        super.mShowVideoControls = intent.getBooleanExtra(Code.SHOW_MV_BUTTONS, false);
        if (!super.mShowVideoControls) {
            super.mPlayButton.setVisibility(View.INVISIBLE);
        }

        // Set splash screen delay
        super.mSplashScreenFadeOutDelay = intent.getIntExtra(Code.MV_SPLASH_DELAY, 1000);

        // Append appropriate language identifier if required
        // (Default is English)
        String resourceName = intent.getStringExtra(Code.RES_NAME);
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            resourceName = SWAHILI_PREFIX + resourceName;
        }

        // Get resource id using resource name
        int resourceId = ResourceDecoder.getIdentifier(getApplicationContext(), resourceName, "raw");

        // Create video path
        String videoPath = Globals.RESOURCE_PREFIX + getPackageName() + "/" + resourceId;

        // Set video URI
        Uri videoURI = Uri.parse(videoPath);
        super.mVideo.setVideoURI(videoURI);
    }

    @Override
    protected Unit unitUpdateLogic(Unit u, DbHelper dbHelper) {
        // Basica validation
        if (!(u == null || dbHelper == null)) {

        }

        // Happiness :-)
        return u;
    }

    @Override
    public void finishIntent() {
        Intent intent = new Intent();
        setResult(Code.TUTORIAL, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}