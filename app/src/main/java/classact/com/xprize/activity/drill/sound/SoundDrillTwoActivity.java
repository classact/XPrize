package classact.com.xprize.activity.drill.sound;

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
import classact.com.xprize.utils.FetchResource;
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
    private String drillSound;
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
            drillSound = this.data.getString("drillsound");
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
            String sound = data.getString("this_is_a");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
                    playFirstSound();
                }
            });
            mp.prepare();
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
            String sound = "";
            if (correctItem == 1)
                sound = pairs.getJSONObject(currentPair - 1).getString("correctsound");
            else
                sound = pairs.getJSONObject(currentPair - 1).getString("wrongsound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
                    startSecondItem();
                }
            });
            mp.prepare();
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
            String sound = data.getString("this_is_a");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
                    playSecondSound();
                }
            });
            mp.prepare();
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
            String sound = "";
            if (correctItem == 2) {
                sound = pairs.getJSONObject(currentPair - 1).getString("correctsound");
            } else {
                sound = pairs.getJSONObject(currentPair - 1).getString("wrongsound");
            }
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
                    playIntro();
                }
            });
            mp.prepare();
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
            String sound = data.getString("touch_picture_starts_with");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
                    playSound(drillSound);
                }
            });
            mp.prepare();
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

    private void playSound(String sound){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(soundPath);
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
            finish();
        }
    }

    private void playSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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
                }
            });
            mp.prepare();
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
