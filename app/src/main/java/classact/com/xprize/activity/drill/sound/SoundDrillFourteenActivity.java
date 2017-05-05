package classact.com.xprize.activity.drill.sound;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.view.WriteView;

public class SoundDrillFourteenActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;
    private ImageView item9;
    private ImageView receptable1;
    private ImageView receptable2;
    private ImageView receptable3;
    private ImageView receptable4;
    private ImageView receptable5;
    private ImageView receptable6;
    private ImageView receptable7;
    private ImageView receptable8;
    private ImageView receptable9;
    private LinearLayout writingContainer;
    private LinearLayout displayContainer;
    private DrillFourteenWriteView writingView;
    private JSONArray words;
    private int currentWord;
    private MediaPlayer mp;
    private int currentTries;
    private JSONObject allData;

    String textholder;
    Handler handler;

    private boolean mCanWrite;
    private boolean mIsWriting;
    private long mLastWrittenTime;
    private TextView mTimer;
    private int mTimerCounter;
    private boolean mTimerReset;
    private RelativeLayout mRootView;
    private LinearLayout mItemsParent;
    private LinearLayout mReceptaclesParent;

    private final int TIMER_MAX = 2;
    private final int DRAW_WAIT_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_fourteen);
        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        item5 = (ImageView) findViewById(R.id.item5);
        item6 = (ImageView) findViewById(R.id.item6);
        item7 = (ImageView) findViewById(R.id.item7);
        item8 = (ImageView) findViewById(R.id.item8);
        item9 = (ImageView) findViewById(R.id.item9);
        receptable1 = (ImageView) findViewById(R.id.loc1);
        receptable2 = (ImageView) findViewById(R.id.loc2);
        receptable3 = (ImageView) findViewById(R.id.loc3);
        receptable4 = (ImageView) findViewById(R.id.loc4);
        receptable5 = (ImageView) findViewById(R.id.loc5);
        receptable6 = (ImageView) findViewById(R.id.loc6);
        receptable7 = (ImageView) findViewById(R.id.loc7);
        receptable8 = (ImageView) findViewById(R.id.loc8);
        receptable9 = (ImageView) findViewById(R.id.loc9);
        writingContainer = (LinearLayout) findViewById(R.id.writing_canvas_container);
        displayContainer = (LinearLayout)findViewById(R.id.layout1);
        mRootView = (RelativeLayout) displayContainer.getParent();
        mItemsParent = (LinearLayout) item1.getParent();
        mReceptaclesParent = (LinearLayout) receptable1.getParent();

        // mItemsParent.setGravity(Gravity.CENTER);
        // mReceptaclesParent.setGravity(Gravity.CENTER);
        // displayContainer.setGravity(Gravity.CENTER);

        String drillData = getIntent().getExtras().getString("data");
        handler = new Handler();
        initialiseData(drillData);
    }

    private void initialiseData(String drillData) {
        try {
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            currentWord = 1;
            startDrill();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }


    private void startDrill(){
        try {
            mCanWrite = false;
            mTimerReset = true;
            mIsWriting = false;
            mLastWrittenTime = 0;

            loadWord();
            displayContainer.setVisibility(View.INVISIBLE);
            writingContainer.removeAllViews();
            mRootView.removeView(mTimer);
            writingView = new DrillFourteenWriteView(this, 0, true);
            writingView.setThisActivity(this);
            writingView.setAlpha(0.95f);
            writingContainer.addView(writingView);

            // Add draw timer
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
            timerLayoutParams.topMargin = 105;
            timerLayoutParams.leftMargin = 2170;
            mTimer.setLayoutParams(timerLayoutParams);
            mTimer.setVisibility(View.INVISIBLE);
            mRootView.addView(mTimer);

            // Get position on scree

            int sound = allData.getInt("write");
            if (mp == null)
                mp = MediaPlayer.create(this, sound);
            else{
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayWord();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayWord(){
        try {
            int sound =  words.getJSONObject(currentWord - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mCanWrite = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable showWordRunnable = new Runnable() {
        @Override
        public void run() {
            showWord();
        }
    };

    private void loadWord(){
        try {
            currentTries = 0;

            JSONArray word = words.getJSONObject(currentWord - 1).getJSONArray("letters");
            item1.setImageResource(word.getInt(0));
            item1.setVisibility(View.VISIBLE);
            receptable1.setVisibility(View.VISIBLE);

            if (word.length() > 1) {
                item2.setImageResource(word.getInt(1));
                item2.setVisibility(View.VISIBLE);
                receptable2.setVisibility(View.VISIBLE);
            }
            else{
                item2.setVisibility(View.INVISIBLE);
                receptable2.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 2) {
                item3.setImageResource(word.getInt(2));
                item3.setVisibility(View.VISIBLE);
                receptable3.setVisibility(View.VISIBLE);
            }
            else{
                item3.setVisibility(View.INVISIBLE);
                receptable3.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 3) {
                item4.setImageResource(word.getInt(3));
                item4.setVisibility(View.VISIBLE);
                receptable4.setVisibility(View.VISIBLE);
            }
            else{
                item4.setVisibility(View.INVISIBLE);
                receptable4.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 4) {
                item5.setImageResource(word.getInt(4));
                item5.setVisibility(View.VISIBLE);
                receptable5.setVisibility(View.VISIBLE);
            }
            else{
                item5.setVisibility(View.INVISIBLE);
                receptable5.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 5) {
                item6.setImageResource(word.getInt(5));
                item6.setVisibility(View.VISIBLE);
                receptable6.setVisibility(View.VISIBLE);
            }
            else{
                item6.setVisibility(View.INVISIBLE);
                receptable6.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 6) {
                item7.setImageResource(word.getInt(6));
                item7.setVisibility(View.VISIBLE);
                receptable7.setVisibility(View.VISIBLE);
            }
            else{
                item7.setVisibility(View.INVISIBLE);
                receptable7.setVisibility(View.INVISIBLE);
            }


            if (word.length() > 7) {
                item8.setImageResource(word.getInt(7));
                item8.setVisibility(View.VISIBLE);
                receptable8.setVisibility(View.VISIBLE);
            }
            else{
                item8.setVisibility(View.INVISIBLE);
                receptable8.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 8) {
                item9.setImageResource(word.getInt(8));
                item9.setVisibility(View.VISIBLE);
                receptable9.setVisibility(View.VISIBLE);
            }
            else{
                item9.setVisibility(View.INVISIBLE);
                receptable9.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void showWord(){
        try {
            displayContainer.setVisibility(View.VISIBLE);
            int sound = allData.getInt("this_is");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    repeatWord();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            repeatWord();
        }
    }

    public void repeatWord(){
        try {
            int sound =  words.getJSONObject(currentWord - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    handler.postDelayed(wereYouCorrectRunnable,100);
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            handler.postDelayed(wereYouCorrectRunnable,100);
        }
    }

    public Runnable wereYouCorrectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                int sound = allData.getInt("were_you_correct");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                mp.reset();
                mp.setDataSource(getApplicationContext(), myUri);
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
                        handler.postDelayed(continueRunnable, 2000);
                    }
                });
                mp.prepare();
            }
            catch (Exception ex){
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                mp = null;
                handler.postDelayed(continueRunnable, 2000);
            }
        }
    };

    public Runnable continueRunnable = new Runnable() {
        @Override
        public void run() {
            currentWord++;
            if (currentWord <= words.length()) {
                startDrill();
            } else {
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    };


    private void reward(){
        switch (currentWord) {
            case 1:
                //reward1.setImageResource(R.drawable.rewardball1colour);
                break;
            case 2:
                //reward2.setImageResource(R.drawable.rewardball1colour);
                break;
            case 3:
                //reward3.setImageResource(R.drawable.rewardball1colour);
                break;
            case 4:
                //reward4.setImageResource(R.drawable.rewardball1colour);
                break;
            case 5:
                //reward5.setImageResource(R.drawable.rewardball1colour);
                break;
        }
    }

    private void rewardAndGoNext(){
        reward();
        playSound(R.raw.good_job);
    }

    private void playSound(int sound){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    currentWord++;
                    if (currentWord <= 5)
                        startDrill();
                    else
                        finish();
                }
            });
            mp.prepare();
        }
         catch (Exception ex){
             ex.printStackTrace();
             if (mp != null) {
                 mp.release();
             }
             mp = null;
             currentWord++;
             if (currentWord <= 5)
                 startDrill();
             else
                 finish();
         }
    }

    private class DrillFourteenWriteView extends WriteView {

        private SoundDrillFourteenActivity mThisActivity;

        private DrillFourteenWriteView(Context context, int background, boolean transparent) {
            super(context, background, transparent);
        }

        private void setThisActivity(SoundDrillFourteenActivity thisActivity) {
            mThisActivity = thisActivity;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (mCanWrite) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mThisActivity.setIsWriting(true);
                        mThisActivity.setLastWrittenTime(0);
                        mThisActivity.showTimer(false);
                        System.out.println("Writing not complete!");
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mThisActivity.setIsWriting(false);
                        mThisActivity.setTimerReset(true);
                        mThisActivity.setLastWrittenTime(new Date().getTime());

                        handler.removeCallbacks(countDown);
                        handler.postDelayed(countDown, DRAW_WAIT_TIME);

                        System.out.println("Writing complete!");
                        break;
                }
            }
            return true;
        }
    }

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            try {

                System.out.println("Starting countdown!! " + mIsWriting + ", " + mLastWrittenTime);

                long currentTime = new Date().getTime();

                if (!(mIsWriting || mLastWrittenTime == 0l || (currentTime - mLastWrittenTime) < DRAW_WAIT_TIME )) {
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
                        mCanWrite = false;
                        mTimer.setTextColor(Color.parseColor("#33ccff"));
                        handler.postDelayed(showWordRunnable, 500);
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

    public void showTimer(boolean showTimer) {
        if (showTimer) {
            mTimer.setVisibility(View.VISIBLE);
        } else {
            mTimer.setVisibility(View.INVISIBLE);
        }
    }

    public void setIsWriting(boolean isWriting) {
        mIsWriting = isWriting;
    }

    public void setTimerReset(boolean timerReset) {
        mTimerReset = timerReset;
    }

    public void setLastWrittenTime(long lastWrittenTime) {
        mLastWrittenTime = lastWrittenTime;
    }

    public boolean getIsWriting() {
        return mIsWriting;
    }

    public boolean getTimerReset() {
        return mTimerReset;
    }

    public long getLastWrittenTime() {
        return mLastWrittenTime;
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
