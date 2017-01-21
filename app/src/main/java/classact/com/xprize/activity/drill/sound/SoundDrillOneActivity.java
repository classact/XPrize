package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
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
            finish();
        }
    }

    //
    // Play the introduction sound.  This is the letter
    //
    private void playIntro(){
        try {
            int intro = drillData.getInt("intro");
            mp = MediaPlayer.create(this, intro);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mp.release();
                    mp.reset();
                    playNormalLetterSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playNormalLetterSound(){
        try {
            int letterSound = drillData.getInt("letter_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + letterSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playPhonicSoundIntro();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            int sound = drillData.getInt("it_makes_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playletterSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playletterSound(){
        try {
            int letterSound = drillData.getInt("letter_phonic_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +  letterSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    nowYouTry();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            int sound = drillData.getInt("now_you_try");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +  sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    intiatePrescence();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            int letterSound = drillData.getInt("letter_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + letterSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playObject();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            int letterSound = objects.getJSONObject(currentObject).getInt("object_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +  letterSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
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
