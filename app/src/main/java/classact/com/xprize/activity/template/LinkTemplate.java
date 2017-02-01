package classact.com.xprize.activity.template;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import java.io.IOException;

public abstract class LinkTemplate extends AppCompatActivity {

    protected int mRequestCode;

    protected final String REQ_CODE_KEY = "REQ_CODE";
    protected final String NARRATOR_KEY = "NARRATOR";
    protected final String STATE_KEY = "STATE";
    protected final String ACTIVITY_NAME_KEY = "ACTIVITY_NAME";
    protected final String NEXT_ACTIVITY_KEY = "NEXT_ACTIVITY";

    /* States */
    protected final String INIT = "INIT";
    protected final String PREPARED = "PREPARED";
    protected final String PLAY = "PLAY";
    protected final String PAUSE = "PAUSE";
    protected final String COMPLETE = "COMPLETE";

    protected RelativeLayout mBackground;
    protected Context mContext;
    protected MediaPlayer mPlayer;
    protected String mNarrator;
    protected String mState;
    protected String mActivityName;
    protected String mNextActivityClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra("REQ_CODE", 0);

        mState = INIT;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart"+mState);

        if (mPlayer == null && mState.equalsIgnoreCase(INIT)) {
            createPlayer(mPlayer);
        }
        restartPlayer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        System.out.println("savedInstanceState"+mState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop"+mState);
        stopPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause"+mState);
        stopPlayer();
    }

    @Override
    protected void onResume() {
       super.onResume();
        System.out.println("onResume"+mState);
        restartPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy"+mState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        stopPlayer();
        System.out.println("onSaveInstanceState"+mState);

        outState.putString(NARRATOR_KEY, mNarrator);
        outState.putString(STATE_KEY, mState);
        outState.putString(ACTIVITY_NAME_KEY, mActivityName);
        outState.putString(NEXT_ACTIVITY_KEY, mNextActivityClassName);
        outState.putInt(REQ_CODE_KEY, mRequestCode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState"+mState);

        mNarrator = savedInstanceState.getString(NARRATOR_KEY);
        mState = savedInstanceState.getString(STATE_KEY);
        mActivityName = savedInstanceState.getString(ACTIVITY_NAME_KEY);
        mNextActivityClassName = savedInstanceState.getString(NEXT_ACTIVITY_KEY);
        mRequestCode = savedInstanceState.getInt(REQ_CODE_KEY);
        restartPlayer();
    }

    public void createPlayer(MediaPlayer player) {
        System.out.println("createPlayer"+mState);

        try {
            mPlayer = player;

            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
            }

            // validate context
            if (mContext == null) {
                System.err.println(mActivityName + " > createPlayer: invalid context");
            }

            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mNarrator);

            mState = PREPARED;

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                    mState = PLAY;
                }
            });

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mState = COMPLETE;
                    mPlayer.release();
                    close(mNextActivityClassName);
                }
            });

            mPlayer.prepare();

        } catch (IOException ioex) {
            System.err.println(mActivityName + ".createPlayer > IOException: " + ioex.getMessage());

        } catch (Exception ex) {
            System.err.println(mActivityName + ".createPlayer > Exception: " + ex.getMessage());

        }
    }

    public void stopPlayer() {
        System.out.println("stopPlayer"+mState);
        if (mPlayer != null && mState.equalsIgnoreCase(PLAY)) {
            mPlayer.setOnPreparedListener(null);
            mPlayer.setOnCompletionListener(null);
            mPlayer.stop();
            mState = PAUSE;
        }
    }

    public void restartPlayer() {
        System.out.println("restartPlayer"+mState);
        if (mPlayer != null && mState.equalsIgnoreCase(PAUSE)) {
            createPlayer(mPlayer);
        }
    }

    /**
     * CLOSE
     * @param nextActivityClassName
     */
    protected void close(String nextActivityClassName) {
        System.out.println("close");

        try {
            if (mNextActivityClassName != null) {
                Intent intent = new Intent(this, Class.forName(nextActivityClassName));
                startActivity(intent);
            } else {
                finishIntent();
            }
            overridePendingTransition(0, android.R.anim.fade_out);
        } catch (ClassNotFoundException cnfex) {
            System.err.println(mActivityName + " > close: next activity's class could not be found");
        }
    }

    public abstract void finishIntent();
}