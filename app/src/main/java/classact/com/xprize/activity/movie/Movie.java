package classact.com.xprize.activity.movie;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceDecoder;
import classact.com.xprize.utils.ResourceSelector;

public class Movie extends AppCompatActivity {

    /*protected int mRequestCode;
    protected final String REQ_CODE_KEY = "REQ_CODE";*/

    protected final String SWAHILI_PREFIX = "s";

    // Views
    protected RelativeLayout mSplashScreenContainer;
    protected RelativeLayout mSplashScreen;
    protected ImageView mSplashImage;
    protected MediaPlayer mVideoPlayer;
    protected VideoView mVideo;
    protected Uri mVideoURI;

    // Handlers and runnables
    protected static class MovieTemplateHandler extends Handler {}
    protected final MovieTemplateHandler mHandler = new MovieTemplateHandler();
    protected FadeOutSplashScreen mFadeOutSplashScreen = new FadeOutSplashScreen(this);

    // Non persistent variables
    protected Animation mSplashScreenFadeOutAnimation;
    protected int mSplashScreenFadeOutDelay;
    protected int mVideoStopPosition;
    protected String mNextActivityClassName;
    protected int mState;
    protected int mUnitId;
    protected String mActivityName;
    protected int mNextBgCode;

    // Keys to hold non-persistent data
    protected final String FADE_OUT_DELAY_KEY = "FADE_OUT_DELAY";
    protected final String VIDEO_POS_KEY = "VIDEO_POS";
    protected final String STATE_KEY = "STATE";
    protected final String UNIT_ID_KEY = "UNIT_ID";
    protected final String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";
    protected final String NEXT_ACTIVITY_KEY = "NEXT_ACTIVITY";
    protected final String NEXT_BG = "NEXT_BG_CODE";

    // States
    protected final int INIT = 0;
    protected final int PLAY = 1;
    protected final int PAUSE = 2;
    protected final int COMPLETE = 3;

    /**
     * ON CREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        System.out.println("onCreate");

        int bg;

        mSplashScreenContainer = (RelativeLayout) findViewById(R.id.movie_splash_container);
        mSplashScreen = (RelativeLayout) findViewById(R.id.movie_splash);
        mSplashImage = (ImageView) findViewById(R.id.movie_splash_image);
        mVideo = (VideoView) findViewById(R.id.movie_video);

        // Requires data from invoker intent
        Intent intent = getIntent();
        mNextBgCode = intent.getIntExtra(Code.NEXT_BG_CODE, -1);

        if (mNextBgCode == Code.INTRO) {
            if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                mSplashImage.setBackgroundResource(R.drawable.sw_intro_bg);
            } else {
                mSplashImage.setBackgroundResource(R.drawable.en_intro_bg);
            }
        } else if (mNextBgCode == Code.FINALE) {
            mSplashImage.setBackgroundResource(R.drawable.star_level_20);
        }

        mUnitId = 1;
        mActivityName = "MoviePausable";
        mNextActivityClassName = null;

        // Set splash screen delay
        mSplashScreenFadeOutDelay = intent.getIntExtra(Code.MV_SPLASH_DELAY, 3000);
        mSplashScreenFadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        mSplashScreenFadeOutAnimation.setFillAfter(true);

        // Append appropriate language identifier if required
        // (Default is English)
        String resourceName = intent.getStringExtra(Code.RES_NAME);

        // Check if Swahili prefix should be attached
        if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            resourceName = SWAHILI_PREFIX + resourceName;
        }

        // Create video path
        //String videoPath = Environment.getExternalStorageDirectory() + "/Android/media/classact.com.xprize/videos/" + resourceName + ".mp4";
        String videoPath = FetchResource.video(getApplicationContext(), resourceName);

        // Set video URI
        Uri videoURI = Uri.parse(videoPath);
        mVideo.setVideoURI(videoURI);

        addListenersToViews();
    }

    /**
     * ANDROID ACTIVITY LIFECYCLE METHODS
     */

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        System.out.println("onPostCreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("onSaveInstanceState");
        outState.putInt(FADE_OUT_DELAY_KEY, mSplashScreenFadeOutDelay);
        outState.putInt(VIDEO_POS_KEY, mVideoStopPosition);
        outState.putString(NEXT_ACTIVITY_KEY, mNextActivityClassName);
        outState.putInt(STATE_KEY, mState);
        outState.putInt(UNIT_ID_KEY, mUnitId);
        outState.putString(ACTIVITY_NAME_KEY, mActivityName);
        outState.putInt(NEXT_BG, mNextBgCode);
        if (!(mState == INIT || mState == COMPLETE)) {
            pauseVideo();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState");
        mSplashScreenFadeOutDelay = savedInstanceState.getInt(FADE_OUT_DELAY_KEY);
        mVideoStopPosition = savedInstanceState.getInt(VIDEO_POS_KEY);
        mNextActivityClassName = savedInstanceState.getString(NEXT_ACTIVITY_KEY);
        mState = savedInstanceState.getInt(STATE_KEY);
        mUnitId = savedInstanceState.getInt(UNIT_ID_KEY);
        mActivityName = savedInstanceState.getString(ACTIVITY_NAME_KEY);
        mNextBgCode = savedInstanceState.getInt(NEXT_BG);
        if (!(mState == INIT || mState == COMPLETE)) {
            resumeVideo();
        } else if (mState == COMPLETE) {
            close(mNextActivityClassName);
        }
    }

    /**
     * ADD LISTENERS TO VIEWS
     */
    protected void addListenersToViews() {
        System.out.println("addListenersToViews");

        // If splash screen exists, ensure that splash screen container becomes invisible after splash screen fades out
        if (!(mSplashScreenContainer == null || mSplashScreenFadeOutAnimation == null)) {
            setAnimationListenerForSplashScreenFadeOutAnimation(mSplashScreenContainer, mSplashScreenFadeOutAnimation);
        } else {
            System.err.println(mActivityName + " > onStart: cannot set AnimationListenerForSplashScreenFadeOutAnimation");
        }

        // If video and splash screen exists, fade out splash screen after video has been prepared
        if (!(mVideo == null || mSplashScreen == null) && mSplashScreenFadeOutDelay >= 0) {
            setOnVideoPreparedListener(mVideo, mSplashScreen, mSplashScreenFadeOutDelay);
        } else {
            System.err.println(mActivityName + " > onStart: cannot set OnVideoPreparedListener");
        }

        if (!(mVideo == null)) {
            setOnVideoCompletionListener(mVideo, mNextActivityClassName);
        } else {
            System.err.println(mActivityName + " > onStart: cannot set OnVideoCompletionListener");
        }
    }

    /**
     * PAUSE VIDEO
     */
    protected void pauseVideo() {
        System.out.println("pauseVideo");
        if (mVideoPlayer != null) {
            // Update state
            mState = PAUSE;

            // Check if current position is valid
            if (mVideoStopPosition < 0) {
                System.err.println(mActivityName + " > pauseVideo: for some odd reason ... video stop position is < 0");
            }

            // Get latest video position
            mVideoStopPosition = mVideoPlayer.getCurrentPosition();
            mVideoPlayer.pause();

            System.out.println("Current video position: " + mVideoStopPosition);

        } else {
            System.err.println(mActivityName + " > pauseVideo: cannot pause - video is null");
        }
    }

    /**
     * RESUME VIDEO
     */
    protected void resumeVideo() {
        System.out.println("resumeVideo (" + mVideoStopPosition + ")");
        if (mVideoPlayer != null) {

            // Update state
            mState = PLAY;

            if (!mVideoPlayer.isPlaying()) {
                System.out.println("Is not playing");
                mVideoPlayer.start();
            } else {
                System.out.println("Is playing");
            }

        } else {
            System.err.println(mActivityName + " > resumeVideo: cannot resume - video is null");
        }
    }

    /**
     * SET ANIMATION LISTENER FOR SPLASH SCREEN
     * @param splashScreenContainer
     * @param splashScreenFadeOutAnimation
     */
    protected void setAnimationListenerForSplashScreenFadeOutAnimation(RelativeLayout splashScreenContainer, Animation splashScreenFadeOutAnimation) {
        System.out.println("setAnimationListenerForSplashScreenFadeOutAnimation");
        if (!(splashScreenContainer == null || splashScreenFadeOutAnimation == null)) {
            mSplashScreenContainer = splashScreenContainer;
            mSplashScreenFadeOutAnimation = splashScreenFadeOutAnimation;
            mSplashScreenFadeOutAnimation.setAnimationListener(null);

            mSplashScreenFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mSplashScreenContainer != null) {
                        mSplashScreenContainer.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            System.err.println(mActivityName + " > setAnimationListenerForSplashScreenFadeOutAnimation: invalid parameters");
        }
    }

    /**
     * SET ON VIDEO PREPARED LISTENER
     * @param video
     * @param splashScreen
     * @param splashScreenFadeOutDelay
     */
    protected void setOnVideoPreparedListener(VideoView video, RelativeLayout splashScreen, int splashScreenFadeOutDelay) {
        System.out.println("setOnVideoPreparedListener");
        if (!(mVideo == null || mSplashScreen == null) && mSplashScreenFadeOutDelay >= 0) {
            mVideo = video;
            mSplashScreen = splashScreen;
            mSplashScreenFadeOutDelay = splashScreenFadeOutDelay;
            mVideo.setOnPreparedListener(null);

            mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoPlayer = mp;

                    // Start video
                    mVideoPlayer.start();

                    // Start from previous position if exists
                    mVideoPlayer.seekTo(mVideoStopPosition);

                    // Update state
                    mState = PLAY;

                    // Set post delayed to hide splash screen
                    mHandler.postDelayed(mFadeOutSplashScreen, mSplashScreenFadeOutDelay);
                }
            });
        } else {
            System.err.println(mActivityName + " > setOnVideoPreparedListener: invalid parameters");
        }
    }

    /**
     * SET ON VIDEO COMPLETION LISTENER
     * @param video
     * @param nextActivityClassName
     */
    protected void setOnVideoCompletionListener(VideoView video, String nextActivityClassName) {
        System.out.println("setOnVideoCompletionListener");
        if (!(video == null)) {
            mVideo = video;
            mNextActivityClassName = nextActivityClassName;
            mVideo.setOnCompletionListener(null);

            mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mVideoPlayer.stop();
                    mState = COMPLETE;
                    close(mNextActivityClassName);
                }
            });
        } else {
            System.err.println(mActivityName + " > setOnVideoCompletionListener: invalid parameters");
        }
    }

    /**
     * CLOSE
     * @param nextActivityClassName
     */
    protected void close(String nextActivityClassName) {
        System.out.println("close");

        Intent intent = new Intent();
        setResult(Code.MOVIE, intent);
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    /**
     * FADE OUT SPLASH SCREEN RUNNABLE
     */
    protected static class FadeOutSplashScreen implements Runnable {
        private final WeakReference<Movie> mMovie;

        FadeOutSplashScreen(Movie movie) {
            mMovie = new WeakReference<>(movie);
        }

        @Override
        public void run() {
            System.out.println("FadeOutSplashScreen.run");
            Movie movie = mMovie.get();
            if (movie != null) {
                RelativeLayout splashScreen = movie.getSplashScreen();
                Animation splashScreenFadeOutAnimation = movie.getSplashScreenFadeOutAnimation();

                if (!(splashScreen == null || splashScreenFadeOutAnimation == null)) {
                    splashScreen.startAnimation(splashScreenFadeOutAnimation);
                }
            }
        }
    }

    /**
     * SPLASH SCREEN CONTAINER SETTER
     * @param splashScreenContainer
     */
    public void setSplashScreenContainer(RelativeLayout splashScreenContainer) {
        mSplashScreenContainer = splashScreenContainer;
    }

    /**
     * SPLASH SCREEN SETTER
     * @param splashScreen
     */
    public void setSplashScreen(RelativeLayout splashScreen) {
        mSplashScreen = splashScreen;
    }

    /**
     * SPLASH SCREEN FADE OUT ANIMATION SETTER
     * @param splashScreenFadeOutAnimation
     */
    public void setSplashScreenFadeOutAnimation(Animation splashScreenFadeOutAnimation) {
        mSplashScreenFadeOutAnimation = splashScreenFadeOutAnimation;
    }

    /**
     * SPLASH SCREEN FADE OUT DELAY SETTER
     * @param splashScreenFadeOutDelay
     */
    public void setSplashScreenFadeOutDelay(int splashScreenFadeOutDelay) {
        if (splashScreenFadeOutDelay >= 0) {
            mSplashScreenFadeOutDelay = splashScreenFadeOutDelay;
        } else {
            System.err.println(mActivityName + " > setSplashScreenFadeOutDelay: invalid screen fade-out delay time");
        }
    }

    /**
     * VIDEO SETTER
     * @param video
     */
    public void setVideo(VideoView video) {
        mVideo = video;
    }

    /**
     * UNIT ID SETTER
     * @param unitId
     */
    public void setUnitId(int unitId) {
        if (unitId >= 0) {
            mUnitId = unitId;
        } else {
            System.err.println(mActivityName + " > setUnitId: invalid unit id");
        }
    }

    /**
     * SPLASH SCREEN CONTAINER GETTER
     * @return
     */
    public RelativeLayout getSplashScreenContainer() {
        return mSplashScreenContainer;
    }

    /**
     * SPLASH SCREEN GETTER
     * @return
     */
    public RelativeLayout getSplashScreen() {
        return mSplashScreen;
    }

    /**
     * SPLASH SCREEN FADEOUT ANIMATION GETTER
     * @return
     */
    public Animation getSplashScreenFadeOutAnimation() {
        return mSplashScreenFadeOutAnimation;
    }

    /**
     * SPLASH SCREEN FADEOUT DELAY GETTER
     * @return
     */
    public int getSplashScreenFadeOutDelay() {
        return mSplashScreenFadeOutDelay;
    }

    /**
     * VIDEO GETTER
     * @return
     */
    public VideoView getVideo() {
        return mVideo;
    }

    /**
     * UNIT ID GETTER
     * @return
     */
    public int getUnitId() {
        return mUnitId;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayer != null) {
            mVideoPlayer.stop();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}