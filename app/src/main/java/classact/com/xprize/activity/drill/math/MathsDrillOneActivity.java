package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.activity.drill.sound.SoundDrill15ViewModel;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillOneActivity extends DrillActivity {

    @BindView(R.id.activity_maths_unit_one) RelativeLayout rootView;

    private JSONObject allData;
    private JSONArray numbers;
    private int currentNumber;
    private int[] positions;
    private int currentPosition;
    private Runnable returnRunnable;
    private RelativeLayout rootLayout;
    private RelativeLayout rln; // numbers layout

    private MathDrill01ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill01ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        rootLayout = (RelativeLayout) findViewById(R.id.activity_maths_unit_one);

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

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            numbers = allData.getJSONArray("numerals");
            positionAndShowNumbers();
            String sound = allData.getString("its_time_to_count");
            playSound(sound, () -> {
                currentNumber = 1;
                returnRunnable = showNumbersRunnable;
                showNumbers();
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
        loadImage(number, resId);
    }

    private void sparkle(){
        try{
            currentPosition = 0;
            for(int i = 0; i < 21 ; i++)
                if ((currentNumber-1) == positions[i])
                    currentPosition = i;
            showNumber(FetchResource.imageId(context, numbers, positions[currentPosition], "numeral_sparkling"), currentPosition);
            handler.delayed(resetNumberRunnable,200);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Runnable resetNumberRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                showNumber(FetchResource.imageId(context, numbers, positions[currentPosition], "numeral"), currentPosition);
                currentNumber++;
                handler.delayed(returnRunnable,500);
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
            playSound(sound, this::sparkle);
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
            playSound(sound, this::sayNumbers);
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
                handler.delayed(() -> {
                    handler.removeCallbacks(returnRunnable);
                    handler.removeCallbacks(showNumbersRunnable);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                },200);
        }
    };

    private void sayNumbers(){
        try {
            String sound = numbers.getJSONObject(currentNumber - 1).getString("sound");
            playSound(sound, this::sparkle);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}