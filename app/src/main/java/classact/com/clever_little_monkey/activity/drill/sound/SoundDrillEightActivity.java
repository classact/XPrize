package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.view.PathAnimationView;
import classact.com.clever_little_monkey.view.PathCoordinate;
import classact.com.clever_little_monkey.view.WriteView;

public class SoundDrillEightActivity extends DrillActivity implements PathAnimationView.AnimationDone{

    @BindView(R.id.draw_area) RelativeLayout drawArea;
    @BindView(R.id.item1) ImageView letterView;

    private PathAnimationView animationView;
    private DrillEightWriteView writingView;

    private JSONArray paths;

    private int repeat = 1;

    private boolean mCanDraw;
    private boolean mIsDrawing;
    private long mLastDrawnTime;
    private TextView mTimer;
    private int mTimerCounter;
    private boolean mTimerReset;

    private final int TIMER_MAX = 2;
    private final int DRAW_WAIT_TIME = 1000;

    private final int OFFSET_Y = 90;

    private RelativeLayout mDetectionView;

    private SoundDrill08ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_eight);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill08ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        initialize();
        startDrill();
    }

    private void initialize(){
        try {


            mDetectionView = new RelativeLayout(context);
            RelativeLayout.LayoutParams detectionLayout = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            mDetectionView.setLayoutParams(detectionLayout);

            RelativeLayout.LayoutParams letterViewLayoutParams = (RelativeLayout.LayoutParams) letterView.getLayoutParams();
            letterViewLayoutParams.topMargin += OFFSET_Y;
            letterView.setLayoutParams(letterViewLayoutParams);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startDrill(){
        try {
            mCanDraw = false;

            drawArea.removeAllViews();
            drawArea.addView(letterView);
            drawArea.addView(mDetectionView);
            int item = FetchResource.imageId(context, vm.getLetter().getLetterPictureUpperCaseDotsURI());
            if (repeat == 2)
                item = FetchResource.imageId(context, vm.getLetter().getLetterPictureLowerCaseDotsURI());
            loadImage(letterView, item);
            getPathData();
            String sound;
            if (repeat == 1) {
                sound = "drill8drillsound1";
            } else {
                sound = "drill8drillsound4";
            }
            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());

            /* ROCK N ROLL */
            playSound(sound, this::playLetterSound);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void playLetterSound(){
        String sound;
        try {
            sound = vm.getLetter().getLetterSoundURI();
            playSound(sound, this::prepareToWrite);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void prepareToWrite(){
        try{
            drawArea.removeAllViews();
            drawArea.addView(letterView);

            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());

            //write the small letter
            playWatch();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void playWatch() {
        try {
            //watch first
            String sound = "drill8drillsound2";
            playSound(sound, () -> {
                animationView.setLayoutParams(drawArea.getLayoutParams());
                drawArea.addView(animationView);
                animationView.bringToFront();
                animationView.animateThisPath();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
        drawArea.addView(letterView);
        drawArea.addView(writingView);

        // Add draw timer
        drawArea.removeView(mTimer);
        mTimer = new TextView(getApplicationContext());
        mTimer.setBackgroundColor(Color.TRANSPARENT);
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
            String sound = "drill8drillsound3";
            playSound(sound, () -> mCanDraw = true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Runnable checkDone = this::checkIsDone;

    public void checkIsDone(){
        try {
            if (writingView.didDraw()) {
                playSound(FetchResource.positiveAffirmation(context), () ->
                        handler.delayed(this::completed, 1000));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void completed(){
        if (repeat == 1) {
            repeat++;
            startDrill();
        } else {
            handler.delayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 500);
        }
    }

    private void getPathData(){
        try {
            int letterPathFile = fetch.rawId(vm.getLetter().getLetterUpperPath());
            if (repeat == 2)
                letterPathFile = fetch.rawId(vm.getLetter().getLetterLowerPath());
            InputStream is = this.getResources().openRawResource(letterPathFile);
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
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            reader.close();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private  ArrayList<ArrayList<PathCoordinate>> getPathArray(){
        ArrayList<ArrayList<PathCoordinate>> pathsArray = new  ArrayList<>();
        try {
            for (int i = 0; i < paths.length(); i++) {
                ArrayList<PathCoordinate> path = new ArrayList<>();
                JSONObject obj = paths.getJSONObject(i);
                JSONArray array = obj.getJSONArray("path");
                for(int k = 0; k < array.length(); k++) {
                    PathCoordinate coordinate = new PathCoordinate((float)array.getJSONObject(k).getDouble("x"),(float)array.getJSONObject(k).getDouble("y") + OFFSET_Y);
                    path.add(coordinate);
                }
                pathsArray.add(path);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return pathsArray;
    }

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            try {

                System.out.println("Starting countdown!! " + mIsDrawing + ", " + mLastDrawnTime);

                long currentTime = new Date().getTime();

                if (!(mIsDrawing || mLastDrawnTime == 0 || (currentTime - mLastDrawnTime) < DRAW_WAIT_TIME )) {
                    if (mTimerReset) {
                        mTimerReset = false;
                        mTimerCounter = TIMER_MAX;
                    }

                    mTimer.setText(String.valueOf(mTimerCounter));
                    mTimer.setVisibility(View.VISIBLE);

                    if (mTimerCounter > 0) {
                        mTimerCounter--;
                        handler.delayed(countDown, 1000);
                    } else {
                        mCanDraw = false;
                        mTimer.setTextColor(Color.parseColor("#33ccff"));
                        handler.delayed(checkDone, 500);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                        mIsDrawing = true;
                        mThisActivity.setLastDrawnTime(0);
                        mTimer.setVisibility(View.INVISIBLE);
                        System.out.println("Drawing not complete!");
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mIsDrawing = false;
                        mTimerReset = true;
                        mThisActivity.setLastDrawnTime(new Date().getTime());

                        handler.removeCallbacks(countDown);
                        handler.delayed(countDown, DRAW_WAIT_TIME);

                        System.out.println("Drawing complete!");
                        break;
                }
            }
            return true;
        }
    }

    public void setLastDrawnTime(long lastDrawnTime) {
        mLastDrawnTime = lastDrawnTime;
    }
}