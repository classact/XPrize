package classact.com.xprize.activity.drill.math;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import classact.com.xprize.R;

public class MathsDrillOneActivity extends AppCompatActivity {
    private JSONObject allData;
    private JSONArray numbers;
    private MediaPlayer mp;
    private Handler handler;
    private int currentNumber;
    private int[] positions;
    private int currentPosition;
    private Runnable returnRunnable;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_one);
        container = (RelativeLayout)findViewById(R.id.activity_maths_unit_one);
        handler = new Handler();
        initialiseData();
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            numbers = allData.getJSONArray("numerals");
            positionAndShowNumbers();
            int sound = allData.getInt("its_time_to_count");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentNumber = 1;
                    returnRunnable = showNumbersRunnable;
                    showNumbers();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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
                    showNumber(numbers.getJSONObject(positions[pos]).getInt("numeral"), pos);
                }
            }
            else {
                for (int i = 0; i < numbers.length(); i++) {
                    int pos = i;
                    positions[pos] = i;
                    showNumber(numbers.getJSONObject(positions[pos]).getInt("numeral"), pos);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void showNumber (int resId,int position){
        ImageView number = (ImageView)container.getChildAt(position);
        number.setVisibility(View.VISIBLE);
        number.setImageResource(resId);
    }

    private void sparkle(){
        try{
            currentPosition = 0;
            for(int i = 0; i < 21 ; i++)
                if ((currentNumber-1) == positions[i])
                    currentPosition = i;
            showNumber(numbers.getJSONObject(positions[currentPosition]).getInt("numeral_sparkling"),currentPosition);
            handler.postDelayed(resetNumberRunnable,200);
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable resetNumberRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                showNumber(numbers.getJSONObject(positions[currentPosition]).getInt("numeral"), currentPosition);
                currentNumber++;
                handler.postDelayed(returnRunnable,500);
            }
            catch (Exception ex){
                ex.printStackTrace();
                finish();
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
            int sound = numbers.getJSONObject(currentNumber - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sparkle();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTheNumbersWithMe(){
        try{
            currentNumber = 1;
            returnRunnable = sayNumbersRunnable;
            int sound = allData.getInt("say_numbers_with_me");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayNumbers();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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
                        finish();
                    }
                },200);
        }
    };

    private void sayNumbers(){
        try {
            int sound = numbers.getJSONObject(currentNumber - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sparkle();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }
}
