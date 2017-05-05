package classact.com.xprize.activity.drill.sound;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.view.PathAnimationView;
import classact.com.xprize.view.PathCoordinate;
import classact.com.xprize.view.WriteView;

public class SoundDrillEightActivity extends AppCompatActivity implements PathAnimationView.AnimationDone{
    private RelativeLayout drawArea;
    private PathAnimationView animationView;
    private DrillEightWriteView writingView;
    private JSONObject drillData;
    private JSONArray paths;
    private ImageView letter;
    private MediaPlayer mp;
    private Handler handler;
    private int numberOfChecks = 0;
    private int repeat = 1;

    private boolean mCanDraw;
    private boolean mIsDrawing;
    private long mLastDrawnTime;
    private TextView mTimer;
    private int mTimerCounter;
    private boolean mTimerReset;

    private final int TIMER_MAX = 2;
    private final int DRAW_WAIT_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_eight);
        letter = (ImageView)findViewById(R.id.item1);
        drawArea = (RelativeLayout) findViewById(R.id.draw_area);
        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        handler = new Handler(Looper.getMainLooper());
        startDrill();
    }

    private void initialiseData(String data){
        try {
            drillData = new JSONObject(data);
            //getWindow().getDecorView().getRootView().setBackgroundResource(drillData.getInt("background"));
            //int item = drillData.getInt("letter");
            //letter.setImageResource(item);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void startDrill(){
        try {
            mCanDraw = false;

            drawArea.removeAllViews();
            drawArea.addView(letter);
            int item = drillData.getInt("big_letter");
            if (repeat == 2)
                item = drillData.getInt("small_letter");
            letter.setImageResource(item);
            getPathData();
            String sound = "";
            if (repeat == 1) {
                sound = drillData.getString("lets_learn_how_to_write_upper");
            } else {
                sound = drillData.getString("lets_learn_how_to_write_lower");
            }
            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playLetterSound();
                }
            });
            mp.prepare();
        }
        catch(Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void playLetterSound(){
        try {
            String sound = drillData.getString("letter_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    prepareToWrite();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public Runnable writeRunnable = new Runnable(){
        @Override
        public void run() {
            prepareToWrite();
        }
    };

    public void prepareToWrite(){
        try{
            drawArea.removeAllViews();
            drawArea.addView(letter);

            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());

            //write the small letter
            playWatch();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }


    public void playWatch() {
        try {
            //watch first
            String sound = drillData.getString("watch");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    animationView.setLayoutParams(drawArea.getLayoutParams());
                    drawArea.addView(animationView);
                    animationView.bringToFront();
                    //drawArea.bringToFront();
                    //animationView.bringToFront();
                    //drawArea.setZ(10);
                    //animationView.setZ(10);
                    animationView.animateThisPath();
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    @Override
    public void onAnimationDone(){
        prepareWritingCanvas();
    }

    public void prepareWritingCanvas(){
        mCanDraw = false;

        writingView = new DrillEightWriteView(this, R.drawable.backgroundtrace1, true);
        writingView.setThisActivity(this);
        writingView.setAlpha(0.675f);
        writingView.setLayoutParams(drawArea.getLayoutParams());
        drawArea.removeAllViews();
        drawArea.addView(letter);
        drawArea.addView(writingView);

        // Add draw timer
        drawArea.removeView(mTimer);
        mTimer = new TextView(getApplicationContext());
        mTimer.setBackgroundResource(android.R.color.transparent);
        mTimerCounter = TIMER_MAX;

        mTimer.setText(String.valueOf(mTimerCounter));
        mTimer.setTypeface(null, Typeface.BOLD);
        mTimer.setTextSize(115.0f);
        mTimer.setAlpha(0.4f);
        mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        LinearLayout.LayoutParams timerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        timerLayoutParams.topMargin = 225;
        timerLayoutParams.leftMargin = 1800;
        mTimer.setLayoutParams(timerLayoutParams);
        mTimer.setVisibility(View.INVISIBLE);
        drawArea.addView(mTimer);

        playYouTry();
    }

    public void playYouTry() {
        try {
            //now you try
            String sound = drillData.getString("now_you_write");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mCanDraw = true;
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public Runnable checkDone = new Runnable() {
        @Override
        public void run() {
            checkIsDone();
        }
    };

    public void checkIsDone(){
        try {
            if (writingView.didDraw()) {
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + ResourceSelector.getPositiveAffirmationSound(this));
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                mp.reset();
                mp.setDataSource(this, myUri);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        completed();
                    }
                });
                mp.prepare();
            } /* else {
                if (numberOfChecks % 2 == 0) {
                    playYouTry();
                } else {
                    prepareToWrite();
                }
                numberOfChecks++;

            } */
        } catch (IOException ioex) {
            System.err.println("SoundDrillEightActivity.checkIsDone() > IOException: " + ioex.getMessage());
            ioex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void completed(){
        if (repeat == 1) {
            repeat++;
            startDrill();
        } else {
            if (mp != null) {
                mp.release();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
        }
    }

    private void getPathData(){
        try {
            int letterPathFile = drillData.getInt("big_letter_path");
            if (repeat == 2)
                letterPathFile = drillData.getInt("small_letter_path");
            InputStream is = this.getResources().openRawResource(letterPathFile);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                try {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    paths = new JSONObject(result.toString()).getJSONArray("paths");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (mp != null) {
                        mp.release();
                    }
                    finish();
                } finally {
                    reader.close();
                }
            } finally {
                is.close();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private  ArrayList<ArrayList<PathCoordinate>> getPathArray(){
        ArrayList<ArrayList<PathCoordinate>> pathsArray = new  ArrayList<ArrayList<PathCoordinate>>();
        try {
            for (int i = 0; i < paths.length(); i++) {
                ArrayList<PathCoordinate> path = new ArrayList<>();
                JSONObject obj = paths.getJSONObject(i);
                JSONArray array = obj.getJSONArray("path");
                for(int k = 0; k < array.length(); k++) {
                    PathCoordinate coordinate = new PathCoordinate((float)array.getJSONObject(k).getDouble("x"),(float)array.getJSONObject(k).getDouble("y"));
                    path.add(coordinate);
                }
                pathsArray.add(path);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }

        return pathsArray;
    }

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            try {

                System.out.println("Starting countdown!! " + mIsDrawing + ", " + mLastDrawnTime);

                long currentTime = new Date().getTime();

                if (!(mIsDrawing || mLastDrawnTime == 0l || (currentTime - mLastDrawnTime) < DRAW_WAIT_TIME )) {
                    if (mTimerReset) {
                        mTimerReset = false;
                        mTimerCounter = TIMER_MAX;
                    }

                    mTimer.setText(String.valueOf(mTimerCounter));
                    mTimer.setVisibility(View.VISIBLE);

                    if (mTimerCounter > 0) {
                        mTimerCounter--;
                        handler.postDelayed(countDown, 1000);
                    } else {
                        mCanDraw = false;
                        mTimer.setTextColor(Color.parseColor("#33ccff"));
                        handler.postDelayed(checkDone, 500);
                    }
                }
            } catch (Exception ex) {
                System.err.println("==========================================");
                System.err.println("SDFourteenActivity.countDown > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------");
                ex.printStackTrace();
                System.err.println("==========================================");
            }
        }
    };

    private class DrillEightWriteView extends WriteView {

        private SoundDrillEightActivity mThisActivity;

        private DrillEightWriteView(Context context, int background, boolean transparent) {
            super(context, background, transparent);
        }

        private void setThisActivity(SoundDrillEightActivity thisActivity) {
            mThisActivity = thisActivity;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (mCanDraw) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mThisActivity.setIsDrawing(true);
                        mThisActivity.setLastDrawnTime(0);
                        mThisActivity.showTimer(false);
                        System.out.println("Drawing not complete!");
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mThisActivity.setIsDrawing(false);
                        mThisActivity.setTimerReset(true);
                        mThisActivity.setLastDrawnTime(new Date().getTime());

                        handler.removeCallbacks(countDown);
                        handler.postDelayed(countDown, DRAW_WAIT_TIME);

                        System.out.println("Drawing complete!");
                        break;
                }
            }
            return true;
        }
    }

    public void showTimer(boolean showTimer) {
        if (showTimer) {
            mTimer.setVisibility(View.VISIBLE);
        } else {
            mTimer.setVisibility(View.INVISIBLE);
        }
    }

    public void setIsDrawing(boolean isDrawing) {
        mIsDrawing = isDrawing;
    }

    public void setTimerReset(boolean timerReset) {
        mTimerReset = timerReset;
    }

    public void setLastDrawnTime(long lastDrawnTime) {
        mLastDrawnTime = lastDrawnTime;
    }

    public boolean getIsDrawing() {
        return mIsDrawing;
    }

    public boolean getTimerReset() {
        return mTimerReset;
    }

    public long getLastDrawnTime() {
        return mLastDrawnTime;
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
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}