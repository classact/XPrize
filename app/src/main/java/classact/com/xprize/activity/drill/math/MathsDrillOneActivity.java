package classact.com.xprize.activity.drill.math;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillOneActivity extends AppCompatActivity {
    private JSONObject allData;
    private JSONArray numbers;
    private MediaPlayer mp;
    private Handler handler;
    private int currentNumber;
    private int[] positions;
    private int currentPosition;
    private Runnable returnRunnable;
    private RelativeLayout rootLayout;
    private RelativeLayout rln; // numbers layout
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_one);
        rootLayout = (RelativeLayout) findViewById(R.id.activity_maths_unit_one);
        handler = new Handler();

        rln = new RelativeLayout(getApplicationContext());
        rootLayout.addView(rln);
        // rln.setBackgroundColor(Color.argb(150, 255, 0, 0));
        RelativeLayout.LayoutParams rlnParams = (RelativeLayout.LayoutParams) rln.getLayoutParams();
        rlnParams.topMargin = 0; // 310 min, 370 max
        rlnParams.leftMargin = 700; // 1440 min, 1500 max
        rlnParams.width = 1860; // 795 max, 675 min
        rlnParams.height = 1470; // 995 max, 875 min
        rln.setLayoutParams(rlnParams);

        initialiseData();
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

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            numbers = allData.getJSONArray("numerals");
            System.out.println("::: NUMBER OF NUMBERS = " + numbers.length());
            positionAndShowNumbers();
            String sound = allData.getString("its_time_to_count");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    mp.reset();
                    currentNumber = 1;
                    returnRunnable = showNumbersRunnable;
                    showNumbers();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void positionAndShowNumbers(){
        try {
            positions = new int[21];
            Arrays.fill(positions, -1);
            if (numbers.length() < 21){
                for (int i = 0; i < numbers.length(); i++) {
                    int pos = i + 1;
                    positions[pos] = i;
                    showNumber(FetchResource.imageId(this, numbers, positions[pos], "numeral"), pos);
                }
            }
            else {
                for (int i = 0; i < numbers.length(); i++) {
                    int pos = i;
                    positions[pos] = i;
                    showNumber(FetchResource.imageId(this, numbers, positions[pos], "numeral"), pos);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showNumber(int resId, int position){
        ImageView number = (ImageView) rootLayout.getChildAt(position);
        number.setVisibility(View.VISIBLE);
        number.setImageResource(resId);
    }

    private void sparkle(){
        try{
            currentPosition = 0;
            for(int i = 0; i < 21 ; i++)
                if ((currentNumber-1) == positions[i])
                    currentPosition = i;
            showNumber(FetchResource.imageId(THIS, numbers, positions[currentPosition], "numeral_sparkling"), currentPosition);
            handler.postDelayed(resetNumberRunnable,200);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Runnable resetNumberRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                showNumber(FetchResource.imageId(THIS, numbers, positions[currentPosition], "numeral"), currentPosition);
                currentNumber++;
                handler.postDelayed(returnRunnable,500);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    private Runnable showNumbersRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentNumber <= numbers.length())
                showNumbers();
            else {
                currentNumber = 1;
                sayTheNumbersWithMe();
            }
        }
    };

    private void showNumbers(){
        try {
            String sound = numbers.getJSONObject(currentNumber - 1).getString("sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sparkle();
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayTheNumbersWithMe(){
        try{
            currentNumber = 1;
            returnRunnable = sayNumbersRunnable;
            String sound = allData.getString("say_numbers_with_me");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumbers();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Runnable sayNumbersRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentNumber <= numbers.length())
                sayNumbers();
            else
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        handler.removeCallbacks(returnRunnable);
                        handler.removeCallbacks(showNumbersRunnable);
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
                    }
                },200);
        }
    };

    private void sayNumbers(){
        try {
            String sound = numbers.getJSONObject(currentNumber - 1).getString("sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sparkle();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
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
        handler.removeCallbacks(returnRunnable);
        handler.removeCallbacks(showNumbersRunnable);
        if (mp != null) {
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}