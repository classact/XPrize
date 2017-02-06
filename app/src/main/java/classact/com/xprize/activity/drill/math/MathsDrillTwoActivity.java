package classact.com.xprize.activity.drill.math;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillTwoActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout objectsContainer;
    private int[] positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_two);
        numberOne = (ImageView)findViewById(R.id.cakedemo_obect);
        numberOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(1);
            }
        });
        numberTwo = (ImageView)findViewById(R.id.numeral_2);
        numberTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(2);
            }
        });
        numberThree = (ImageView)findViewById(R.id.numeral_3);
        numberThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(3);
            }
        });
        objectsContainer = (RelativeLayout)findViewById(R.id.objects_container);
        initialiseData();
    }

    private void setupObjects(){
        try{
            int number = allData.getInt("number_of_objects");
            int resId = allData.getInt("object");
            for (int i = 1; i <= 20; i++){
                ImageView object = (ImageView)objectsContainer.getChildAt(i-1);
                if (i <= number){
                    object.setVisibility(View.VISIBLE);
                    object.setImageResource(resId);
                }
                else{
                    object.setVisibility(View.INVISIBLE);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void setupNumbers(){
        try {
            positions = new int[3];
            Arrays.fill(positions, -1);
            Random rand = new Random();
            for (int i = 0; i < 3; i++) {
                int pos = rand.nextInt(3);
                if (positions[pos] == -1) {
                    positions[pos] = i;
                } else {
                    boolean done = false;
                    for (int j = 2; j > -1; j--) {
                        if (positions[j] == -1 && !done) {
                            positions[j] = i;
                            done = true;
                            pos = j;
                        }
                    }
                }
                switch (pos) {
                    case 0:
                        numberOne.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                    case 1:
                        numberTwo.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                    case 2:
                        numberThree.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }


    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            int sound = allData.getInt("monkey_has");
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
                    sayNumberOfObjects();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void sayNumberOfObjects(){
        try{
            int sound = allData.getInt("number_of_objects_sound");
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
                    sayCanYouTouch();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }


    public void numberClicked(int position){
        try {
            int correct = numbers.getJSONObject(positions[position - 1]).getInt("right");
            Uri myUri;
            int sound;
            if (correct == 0) {
                sound = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
                myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
                    }
                });
                mp.prepare();
            } else {
                sound = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
                myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
                        finish();
                    }
                });
                mp.prepare();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void sayCanYouTouch(){
        try{
            int sound = allData.getInt("can_you_find_and_touch");
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
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void sayNumber(){
        try{
            int sound = allData.getInt("numeral_sound");
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

                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
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
