package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.SoundPrescence;

public class SoundDrillOneActivity extends DrillActivity implements SoundPrescence.SoundPresenceListener {
    private JSONObject drillData;
    private ImageView letter;
    private JSONArray objects;
    private int letterSound;
    private MediaPlayer mp;
    private Handler handler;
    SoundPrescence soundPrescence;
    private int currentObject;
    private int letterType;

    private int mStage;

    private final Context THIS = this;

    private SoundDrill01ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_one);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill01ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        letter = (ImageView)findViewById(R.id.item1);
        // letter.setBackgroundColor(Color.argb(100, 255, 0, 0));
        RelativeLayout.LayoutParams letterLP = (RelativeLayout.LayoutParams) letter.getLayoutParams();
        letterLP.removeRule(RelativeLayout.CENTER_HORIZONTAL);
        letterLP.leftMargin = 765;
        letter.setLayoutParams(letterLP);

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        soundPrescence = new SoundPrescence(this);
        handler = new Handler(Looper.getMainLooper());
        //this letter is a small
        playIntro();
    }

    public void stage1() {
        // This is the letter
        // "M"
        // It makes the sound "mmm"
        // Now you try
    }

    public void stage2() {
        // "M" mushroom

    }

    //
    // This method reads the JSON that is passed into the activity
    //
    private void initialiseData(String data){
        try {
            drillData = new JSONObject(data);
            int item = drillData.getInt("letter");
            letterType = drillData.getInt("letter_type");
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
        String sound = "";
        try {
            sound = drillData.getString("intro");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playNormalLetterSound();
                }
            }, 800);
        }
    }

    public void playNormalLetterSound(){
        String sound = "";
        try {
            if (letterType == 1) {
                sound = drillData.getString("letter_sound");
            } else {
                sound = drillData.getString("letter_phonic_sound");
            }
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playPhonicSoundIntro();
                }
            }, 800);
        }
    }

    public Runnable playPhonicSoundIntroRunnable = new Runnable(){
        @Override
        public void run() {
            playPhonicSoundIntro();
        }
    };

    public void playPhonicSoundIntro(){
        String sound = "";
        try {
            if (letterType == 1) {
                sound = drillData.getString("it_makes_sound");
            } else {
                sound = "m_phrase26";
            }
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playletterSound();
                }
            }, 800);
        }
    }

    public void playletterSound(){
        String sound = "";
        try {
            sound = drillData.getString("letter_phonic_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nowYouTry();
                }
            }, 800);
        }
    }

    public Runnable nowYouTryRunnable = new Runnable(){
        @Override
        public void run() {
            nowYouTry();
        }
    };

    private void nowYouTry(){
        String sound = "";
        try {
            sound = drillData.getString("now_you_try");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    intiatePrescence();
                }
            }, 800);
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
        String sound = "";
        try {
            int image = objects.getJSONObject(currentObject).getInt("object");
            letter.setImageResource(image);
            if (letterType == 1) {
                // sound = drillData.getString("letter_sound");
                sound = drillData.getString("letter_phonic_sound");
            } else {
                sound = drillData.getString("letter_phonic_sound");
            }
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playObject();
                }
            }, 800);
        }
    }


    public Runnable objectRunnable = new Runnable(){
        @Override
        public void run() {
            playObject();
        }
    };

    private void playObject(){
        String sound = "";
        try {
            sound = objects.getJSONObject(currentObject).getString("object_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
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
                    currentObject ++;
                    if (currentObject < 3){
                        handler.postDelayed(soundAndObjectRunnable,1000);
                    } else {
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
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            mp = null;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentObject ++;
                    if (currentObject < 3){
                        handler.postDelayed(soundAndObjectRunnable,1000);
                    } else {
                        finish();
                    }
                }
            }, 800);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}