package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillThreeActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageView demoItem;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private String drillData;
    private JSONArray sets;
    private Handler handler;
    private int currentSet;
    private int correctItem;
    private String currentSound;
    private String currentPhonicSound;
    private LinearLayout itemsLayout;
    private JSONObject params;
    private Runnable mRunnable;
    private boolean itemsEnabled;
    private Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_three);
        demoItem = (ImageView)findViewById(R.id.item_demo);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        itemsLayout = (LinearLayout)findViewById(R.id.items);
        rnd = new Random();
        // setItemsEnabled(false);
        itemsEnabled = false;
        item1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(0);
                    }
                }
        );
        item2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(1);
                    }
                }
        );
        drillData = getIntent().getExtras().getString("data");
        currentSet = 0;
        handler = new Handler(Looper.getMainLooper());
        initialiseData();
        showSet();
    }

    private void initialiseData(){
        try {
            params = new JSONObject(drillData);
            sets = params.getJSONArray("sets");
            showSet();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void showSet(){
        demoItem.setVisibility(View.VISIBLE);
        itemsLayout.setVisibility(View.INVISIBLE);
        try{
            JSONObject setData = sets.getJSONObject(currentSet);
            demoItem.setImageResource(setData.getInt("image"));
            currentSound = setData.getString("sound");
            currentPhonicSound = setData.getString("phonic_sound");
            JSONArray images = setData.getJSONArray("images");

            JSONObject item1JSONObject = null;
            JSONObject item2JSONObject = null;

            for (int i = 0; i < images.length(); i++) {
                // Get item from images JSONArray
                JSONObject item = images.getJSONObject(i);

                // Check if it's the correct one
                if (item.getInt("correct") == 1) {
                    item1JSONObject = item;
                } else {
                    item2JSONObject = item;
                }
            }

            // Get a random value between 0 and 1
            correctItem = rnd.nextInt(2);

            // Determine where item{1|2}JSONObject should be assigned to
            if (correctItem == 0) {
                // item1 is item1JSONObject
                item1.setImageResource(item1JSONObject.getInt("image"));
                item2.setImageResource(item2JSONObject.getInt("image"));
            } else if (correctItem == 1) {
                // item1 is item2JSONObject
                item1.setImageResource(item2JSONObject.getInt("image"));
                item2.setImageResource(item1JSONObject.getInt("image"));
            }

            String sound = params.getString("this_is_the_letter");
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
                    playSound();
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

    public void playSound(){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), currentSound);
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
                    playItMakes();

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

    private void playItMakes(){
        try {
            String sound = params.getString("it_makes_sound");
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
                    playPhonicSound();
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

    private void playPhonicSound(){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), currentPhonicSound);
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
                    playNextSound();
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

    public void playNextSound(){
        try {
            demoItem.setVisibility(View.INVISIBLE);
            itemsLayout.setVisibility(View.VISIBLE);
            String sound = params.getString("touch");
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
                    playSoundAgain();
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

    public void playSoundAgain(){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), currentSound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // setItemsEnabled(true);
                            itemsEnabled = true;
                        }
                    }, mp.getDuration() - 100);
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
            finish();
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
            if (item == correctItem) {
                // setItemsEnabled(false);
                itemsEnabled = false;
                mRunnable = null;
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (currentSet < sets.length() - 1) {
                            currentSet++;
                            handler.postDelayed(nextDrill, 350);
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
        }
    }

    private Runnable nextDrill = new Runnable(){
        @Override
        public void run() {
            showSet();
        }
    };

    private void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
    }

    /*
    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }
    */

//    @Override
//    public void onBackPressed() {
//    }
}
