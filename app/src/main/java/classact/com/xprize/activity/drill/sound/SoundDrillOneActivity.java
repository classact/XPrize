package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.SoundPrescence;

public class SoundDrillOneActivity extends AppCompatActivity implements SoundPrescence.SoundPresenceListener {
    private JSONObject drillData;
    private ImageView letter;
    private JSONArray objects;
    private int letterSound;
    private MediaPlayer mp;
    private Handler handler;
    SoundPrescence soundPrescence;
    private int currentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_one);
        letter = (ImageView)findViewById(R.id.item1);
        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        soundPrescence = new SoundPrescence(this);
        handler = new Handler(Looper.getMainLooper());
        //this letter is a small
        playIntro();
    }

    //
    // This method reads the JSON that is passed into the activity
    //
    private void initialiseData(String data){
        try {
            drillData = new JSONObject(data);
            int item = drillData.getInt("letter");
            letter.setImageResource(item);
            objects = drillData.getJSONArray("objects");
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    //
    // Play the introduction sound.  This is the letter
    //
    private void playIntro(){
        try {
            String intro = drillData.getString("intro");
            String soundPath = FetchResource.sound(getApplicationContext(), intro);
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
                    //mp.release();
                    mp.reset();
                    playNormalLetterSound();
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

    public void playNormalLetterSound(){
        try {
            String letterSound = drillData.getString("letter_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), letterSound);
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
                    playPhonicSoundIntro();
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

    public Runnable playPhonicSoundIntroRunnable = new Runnable(){
        @Override
        public void run() {
            playPhonicSoundIntro();
        }
    };

    public void playPhonicSoundIntro(){
        try {
            String sound = drillData.getString("it_makes_sound");
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
                    playletterSound();
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

    public void playletterSound(){
        try {
            String letterSound = drillData.getString("letter_phonic_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), letterSound);
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
                    nowYouTry();
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

    public Runnable nowYouTryRunnable = new Runnable(){
        @Override
        public void run() {
            nowYouTry();
        }
    };

    private void nowYouTry(){
        try {
            String sound = drillData.getString("now_you_try");
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
                    intiatePrescence();
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

    public void intiatePrescence(){
        //soundPrescence = new SoundPrescence(this);
        //soundPrescence.listen();
        currentObject = 0;
        handler.postDelayed(soundAndObjectRunnable,1000);

    }

    @Override
    public void heard(boolean heard) {
        if (heard){
            //playObjets();
        }
        else{
            playIntro();
        }
    }


    public Runnable soundAndObjectRunnable = new Runnable(){
        @Override
        public void run() {
            soundAndObject();
        }
    };

    private void soundAndObject(){
        try {
            int image = objects.getJSONObject(currentObject).getInt("object");
            letter.setImageResource(image);
            String letterSound = drillData.getString("letter_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), letterSound);
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
                    playObject();
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


    public Runnable objectRunnable = new Runnable(){
        @Override
        public void run() {
            playObject();
        }
    };

    private void playObject(){
        try {
            String letterSound = objects.getJSONObject(currentObject).getString("object_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), letterSound);
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
                    currentObject ++;
                    if (currentObject < 3){
                        handler.postDelayed(soundAndObjectRunnable,1000);
                    }
                    else{
                        mp.release();
                        finish();
                    }
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
