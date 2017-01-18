package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTwoActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageView item1;
    private ImageView item2;
    private int currentPair;
    private int correctItem;
    private String drillData;
    private int totalItems = 4;
    private JSONArray pairs;
    private int play_mode = 1;
    private Handler handler;
    private int drillSound;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_two);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(1);
                    }
                }
        );
        item2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(2);
                    }
                }
        );
        drillData = getIntent().getExtras().getString("data");
        currentPair = 1;
        handler = new Handler(Looper.getMainLooper());
        initialiseData(drillData);
    }

    private void initialiseData (String data){
        try {
            this.data = new JSONObject(data);
            totalItems = this.data.getInt("paircount");
            pairs = this.data.getJSONArray("pairs");
            drillSound = this.data.getInt("drillsound");
            totalItems = this.data.getInt("paircount");
            showPair();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void showPair(){
        try {
            play_mode = 1;
            item1.setVisibility(View.INVISIBLE);
            item2.setVisibility(View.INVISIBLE);
            int correctImage = pairs.getJSONObject(currentPair - 1).getInt("correctimage");
            int wrongImage = pairs.getJSONObject(currentPair - 1).getInt("wrongimage");
            Random rand = new Random();
            correctItem = rand.nextInt(2);
            if (correctItem < 1) {
                correctItem = 1;
                item1.setImageResource(correctImage);
                item2.setImageResource(wrongImage);
            } else {
                correctItem = 2;
                item2.setImageResource(correctImage);
                item1.setImageResource(wrongImage);
            }
            showFirstItem();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void showFirstItem(){
        try {
            item1.setVisibility(View.VISIBLE);
            int sound = data.getInt("this_is_a");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null)
                mp = new MediaPlayer();
            else
                mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playFirstSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable sayFirstItem = new Runnable(){
        @Override
        public void run() {
            playFirstSound();
        }
    };

    private void playFirstSound(){
        try {
            int sound = 0;
            if (correctItem == 1)
                sound = pairs.getJSONObject(currentPair - 1).getInt("correctsound");
            else
                sound = pairs.getJSONObject(currentPair - 1).getInt("wrongsound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    startSecondItem();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable showSecondItem = new Runnable(){
        @Override
        public void run() {
            startSecondItem();
        }
    };

    private void startSecondItem(){
        try {
            item2.setVisibility(View.VISIBLE);
            int sound = data.getInt("this_is_a");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playSecondSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable saySecondItem = new Runnable(){
        @Override
        public void run() {
            playSecondSound();
        }
    };

    private void playSecondSound(){
        try {
            int sound = 0;
            if (correctItem == 2)
                sound = pairs.getJSONObject(currentPair - 1).getInt("correctsound");
            else
                sound = pairs.getJSONObject(currentPair - 1).getInt("wrongsound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playIntro();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable playDrillIntro = new Runnable(){
        @Override
        public void run() {
            playIntro();
        }
    };

    public void playIntro(){
        try {
            play_mode = 2;
            int sound = data.getInt("touch_picture_starts_with");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playSound(drillSound);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable playDrillSound = new Runnable(){
        @Override
        public void run() {
            playSound(drillSound);
        }
    };


    private void playSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void clickedItem(int item){
        try {
            if (play_mode == 2) {
                if (item == correctItem) {
                    playSound(ResourceSelector.getPositiveAffirmationSound(this));
                    if (currentPair < pairs.length()) {
                        currentPair++;
                        Handler good_handler = new Handler();
                        good_handler.postDelayed(new Runnable() {
                            public void run() {
                                showPair();
                            }
                        }, 1000);
                    }
                    else{
                        mp.release();
                        finish();
                    }
                }
                else{
                    playSound(ResourceSelector.getNegativeAffirmationSound(this));
                }
            } else {
                /*int sound = 0;
                if (item == correctItem)
                    sound = pairs.getJSONObject(currentPair - 1).getInt("correctsound");
                else
                    sound =  pairs.getJSONObject(currentPair - 1).getInt("wrongsound");
                playSound(sound);*/
            }
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

//    @Override
//    public void onBackPressed() {
//    }

}
