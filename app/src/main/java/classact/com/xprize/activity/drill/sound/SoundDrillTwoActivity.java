package classact.com.xprize.activity.drill.sound;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
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
    private Runnable mRunnable;
    private String drillSound;
    private JSONObject data;
    private boolean itemsEnabled;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_two);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);

        // item1.setBackgroundColor(Color.argb(100, 255, 0, 0));
        // item2.setBackgroundColor(Color.argb(100, 0, 255, 0));

        itemsEnabled = false;
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
        // setItemsEnabled(false);
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
            if (mp != null) {
                mp.release();
            }
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
                item1.setImageResource(wrongImage);
                item2.setImageResource(correctImage);
            }
            showFirstItem();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
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
                    playFirstSound();
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

    private void playFirstSound(){
        String sound = "";
        try {
            if (correctItem == 1)
                sound = pairs.getJSONObject(currentPair - 1).getString("correctsound");
            else
                sound = pairs.getJSONObject(currentPair - 1).getString("wrongsound");
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
                    startSecondItem();
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
                    startSecondItem();
                }
            }, 800);
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
                    playSecondSound();
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

    private Runnable saySecondItem = new Runnable(){
        @Override
        public void run() {
            playSecondSound();
        }
    };

    private void playSecondSound(){
        String sound = "";
        try {
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
                    playIntro();
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
                    playIntro();
                }
            }, 800);
        }
    }

    public void playIntro(){
        try {
            play_mode = 2;
            String sound = data.getString("touch_picture_starts_with");
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
                    mRunnable = null;
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // setItemsEnabled(true);
                            itemsEnabled = true;
                        }
                    };
                    playSoundAndRunnableAfterCompletion(drillSound);
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

    private void playSoundAndRunnableAfterCompletion(String sound) {
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
                    if (mRunnable != null) {
                        mRunnable.run();
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }
            }, 800);
        }
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
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
                   if (mRunnable != null) {
                       mRunnable.run();
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

    private void playSound(String sound){
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

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void clickedItem(int item){
        if (itemsEnabled) {
            try {
                if (play_mode == 2) {
                    if (item == correctItem) {
                        // setItemsEnabled(false);

                        ImageView iv = null;
                        if (item == 1) {
                            iv = item1;
                        } else if (item == 2) {
                            iv = item2;
                        }
                        Globals.playStarWorks(THIS, iv, 15, 12, 9);

                        itemsEnabled = false;
                        mRunnable = null; // Reset?
                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (currentPair < pairs.length()) {
                                    currentPair++;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showPair();
                                        }
                                    }, 350);
                                } else {
                                    if (mp != null) {
                                        mp.release();
                                    }
                                    finish();
                                }
                            }
                        };
                        playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
                    } else {
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
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    private void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
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