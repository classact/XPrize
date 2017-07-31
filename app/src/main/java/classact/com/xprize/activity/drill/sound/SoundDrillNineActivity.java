package classact.com.xprize.activity.drill.sound;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.view.WriteView;

public class SoundDrillNineActivity extends AppCompatActivity {
    CustomWriteView view;
    private MediaPlayer mp;
    private Handler handler;
    private RelativeLayout writingContainer;
    private JSONObject params;
    private String data;
    private boolean startedDrawing;
    private boolean drawingTimeUp;
    private TextView timer;
    private int timerCounter;
    private boolean timerReset;
    private long lastDrawnTime;
    private boolean canDraw;
    private boolean drillComplete;

    private final int TIMER_MAX = 5;
    private final int DRAW_WAIT_TIME = 1000;

    private final float TIMER_MID_X = 2065f;
    private final float TIMER_MID_Y = 425f;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_nine);
        startedDrawing = false;
        drawingTimeUp = false;
        canDraw = false;
        drillComplete = false;
        view = new CustomWriteView(this, R.drawable.drawapic1, true);
        // view.setAlpha(0.0f);
        writingContainer = (RelativeLayout)findViewById(R.id.activity_sound_drill_nine);
        handler = new Handler(Looper.getMainLooper());
        writingContainer.addView(view);

        ImageView timerClock = new ImageView(THIS);
        timerClock.setImageResource(R.drawable.timer_clock_001);
        timerClock.setScaleX(0.75f);
        timerClock.setScaleY(0.75f);
        timerClock.setX(1775f);
        timerClock.setY(125f);
        writingContainer.addView(timerClock);

        timer = new TextView(getApplicationContext());
        timer.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()), Typeface.BOLD);
        timer.setPadding(16, 16, 16, 16);
        timerCounter = TIMER_MAX;
        timerReset = true;
        lastDrawnTime = 0l;

        timer.setText(String.valueOf(timerCounter));
        timer.setTextSize(115.0f);
        timer.setAlpha(0.8f);
        timer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        // timer.setBackgroundColor(Color.argb(100, 255, 0, 0));
        timer.setX(TIMER_MID_X);
        timer.setY(TIMER_MID_Y);
        writingContainer.addView(timer);

        Point textSize = Globals.TEXT_MEASURED_SIZE(timer, String.valueOf(timerCounter));
        System.out.println(textSize.x + ", " + textSize.y);
        timer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
        timer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

        data = getIntent().getExtras().getString("data");

        playLetsDraw();
    }

    private void playSound(String sound, final Runnable action) {
        try {
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
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    private void playLetsDraw() {
        try {
            params = new JSONObject(data);
            //Todo: Sound
            String sound = params.getString("lets_draw");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(playDrawSomethingThatStartsWithRunnable, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Runnable playDrawSomethingThatStartsWithRunnable = new Runnable(){
        @Override
        public void run() {
            playDrawSomethingThatStartsWith();
        }
    };

    private void playDrawSomethingThatStartsWith(){
        try {
            String sound = params.getString("draw_something_that_starts_with");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    playLetterSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void playLetterSound(){
        String sound = "";
        try{
            sound = params.getString("sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(enableDrawing,500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableDrawing.run();
                }
            }, 1100);
        }
    }

    private Runnable enableDrawing = new Runnable() {
        @Override
        public void run() {
            canDraw = true;
            // view.setAlpha(0.6f);
        }
    };

    public Runnable playWhatDidYouDraw = new Runnable(){
        @Override
        public void run() {
            try{
                String sound = params.getString("what_did_you_draw");
                playSound(sound, new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(completeDrill, 4550);
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private Runnable completeDrill = new Runnable() {
        @Override
        public void run() {
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    };

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            try {
                long currentTime = new Date().getTime();

                if (!(startedDrawing || drawingTimeUp || lastDrawnTime == 0l || (currentTime - lastDrawnTime) < DRAW_WAIT_TIME )) {
                    if (timerReset) {
                        timerReset = false;
                        timerCounter = TIMER_MAX;
                        timer.setTextColor(Color.parseColor("#000000"));
                    }

                    Point textSize = Globals.TEXT_MEASURED_SIZE(timer, String.valueOf(timerCounter));
                    System.out.println(textSize.x + ", " + textSize.y);
                    timer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
                    timer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

                    if (timerCounter > 0) {
                        timerCounter--;
                        timer.setVisibility(View.VISIBLE);
                        handler.postDelayed(countDown, 1000);
                    } else {
                        canDraw = false;
                        drillComplete = true;
                        timer.setTextColor(Color.parseColor("#33ccff"));
                        drawingTimeUp = true;
                        handler.postDelayed(playWhatDidYouDraw, 500);
                    }
                }
            } catch (Exception ex) {
                System.err.println("SoundDrillNineActivity.countDown > Exception: " + ex.getMessage());
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
            }
        }
    };

    class CustomWriteView extends WriteView {

        public CustomWriteView(Context context, int background, boolean transparent) {
            super(context, background, transparent);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (canDraw) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!(startedDrawing || drawingTimeUp)) {
                            startedDrawing = true;
                            lastDrawnTime = 0l;
                            timer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
                            // timer.setVisibility(View.INVISIBLE);

                            // Debug
                            System.out.println("SoundDrillNineActivity.CustomWriteView.onTouchEvent > Debug: Started writing");
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        if (startedDrawing && !drawingTimeUp) {
                            startedDrawing = false;
                            timerReset = true;

                            Point textSize = Globals.TEXT_MEASURED_SIZE(timer, String.valueOf(TIMER_MAX));
                            System.out.println(textSize.x + ", " + textSize.y);
                            timer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
                            timer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

                            lastDrawnTime = new Date().getTime();

                            // Debug
                            System.out.println("SoundDrillNineActivity.CustomWriteView.onTouchEvent > Debug: Last Drawn Time (" +
                                    lastDrawnTime + ")");

                            handler.removeCallbacks(countDown);
                            handler.postDelayed(countDown, DRAW_WAIT_TIME);

                            // Debug
                            System.out.println("SoundDrillNineActivity.CustomWriteView.onTouchEvent > Debug: Stopped writing");
                        }
                        break;
                }
            }
            if (drillComplete) {
                return false;
            }
            return true;
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
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
