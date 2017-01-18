package classact.com.xprize.activity.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.model.Unit;
import classact.com.xprize.template.MovieTemplate;

public class Chapter_01_Movie extends MovieTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_01_movie);

        super.mSplashScreenContainer = (RelativeLayout) findViewById(R.id.chapter_01_movie_splash_container);
        super.mSplashScreen = (RelativeLayout) findViewById(R.id.chapter_01_movie_splash);
        super.mVideo = (VideoView) findViewById(R.id.chapter_01_movie_video);
        super.mPlayButton = (Button) findViewById(R.id.chapter_01_movie_play_button);
        super.mPauseButton = (Button) findViewById(R.id.chapter_01_movie_pause_button);

        super.mSplashScreenContainer.setVisibility(View.VISIBLE);

        super.mPauseButton.setVisibility(View.INVISIBLE);

        super.mUnitId = 1;
        super.mActivityName = "Chapter_01_Movie";
        super.mNextActivityClassName = null;

        String videoPath = Globals.RESOURCE_PREFIX;
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            videoPath += R.raw.schapter1;
        } else {
            videoPath += R.raw.chapter1;
        }
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
        setResult(Code.MV01, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}