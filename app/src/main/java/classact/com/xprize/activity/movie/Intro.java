package classact.com.xprize.activity.movie;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;

public class Intro extends AppCompatActivity {

    RelativeLayout mVideCoverContainer;
    ImageView mVideoCover;
    VideoView mVideo;
    Handler delayHandler;
    Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        delayHandler = new Handler();
        mVideo = (VideoView) findViewById(R.id.intro_video);
        mVideoCover = (ImageView) findViewById(R.id.intro_video_cover);
        mVideCoverContainer = (RelativeLayout) findViewById(R.id.intro_video_cover_container);

        String introVideoPath = Globals.RESOURCE_PREFIX;

        if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
            mVideoCover.setBackgroundResource(R.drawable.tutorial_intro_bg_english);
            introVideoPath += R.raw.intro_movie;
        } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            mVideoCover.setBackgroundResource(R.drawable.tutorial_intro_bg_swahili);
            introVideoPath += R.raw.sintro_movie;
        } else {
            mVideoCover.setBackgroundResource(R.drawable.tutorial_intro_bg_english);
            introVideoPath += R.raw.intro_movie;
        }

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mVideCoverContainer.setVisibility(View.INVISIBLE);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        Uri introVideoURI = Uri.parse(introVideoPath);
        mVideo.setVideoURI(introVideoURI);

        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.start();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoCover.startAnimation(fadeOut);
                    }
                }, 1000);
            }
        });

        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startTutorial();
            }
        });
    }

    public void startTutorial() {
        /*
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_out);
        */
        Intent intent = new Intent();
        setResult(Code.TUTORIAL, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}