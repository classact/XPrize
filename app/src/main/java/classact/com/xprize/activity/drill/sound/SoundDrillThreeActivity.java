package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class SoundDrillThreeActivity extends DrillActivity {
    private MediaPlayer mp;
    private ImageView demoItem;
    private ImageView item1;
    private ImageView item2;
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

    private final Context THIS = this;

    private SoundDrill03ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_three);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill03ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        demoItem = (ImageView)findViewById(R.id.item_demo);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        // demoItem.setBackgroundColor(Color.argb(100, 0, 0, 255));

        ViewGroup.MarginLayoutParams demoItemLP = (ViewGroup.MarginLayoutParams) demoItem.getLayoutParams();
        demoItemLP.width = (int) density * 326;
        demoItemLP.height = (int) density * 380;
        demoItemLP.topMargin = (int) density * 60;
        demoItemLP.setMarginStart((int) density * 485);
        demoItem.setLayoutParams(demoItemLP);

        // item1.setBackgroundColor(Color.argb(100, 255, 0, 0));
        // item2.setBackgroundColor(Color.argb(100, 0, 255, 0));

        ViewGroup.MarginLayoutParams item1LP = (ViewGroup.MarginLayoutParams) item1.getLayoutParams();
        ViewGroup.MarginLayoutParams item2LP = (ViewGroup.MarginLayoutParams) item2.getLayoutParams();
        item1LP.width = (int) density * 326;
        item2LP.width = (int) density * 326;
        item1LP.height = (int) density * 380;
        item2LP.height = (int) density * 380;
        item1LP.leftMargin = (int) density * 170;
        item2LP.leftMargin = (int) density * 290;
        item1LP.bottomMargin = (int) density * 160;
        item2LP.bottomMargin = (int) density * 160;
        item1.setLayoutParams(item1LP);
        item2.setLayoutParams(item2LP);

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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void playSound(String sound, final Runnable action) {
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
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    public void showSet(){
        demoItem.setVisibility(View.VISIBLE);
        itemsLayout.setVisibility(View.INVISIBLE);
        try{
            JSONObject setData = sets.getJSONObject(currentSet);
            String image = setData.getString("image");
            demoItem.setImageResource(FetchResource.imageId(THIS, image));
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
                item1.setImageResource(FetchResource.imageId(THIS, item1JSONObject, "image"));
                item2.setImageResource(FetchResource.imageId(THIS, item2JSONObject, "image"));
            } else if (correctItem == 1) {
                // item1 is item2JSONObject
                item1.setImageResource(FetchResource.imageId(THIS, item2JSONObject, "image"));
                item2.setImageResource(FetchResource.imageId(THIS, item1JSONObject, "image"));
            }

            String sound = params.getString("this_is_the_letter");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    playSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void playSound(){
        try {
            playSound(currentSound, new Runnable() {
                @Override
                public void run() {
                    playItMakes();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playItMakes();
                }
            }, 800);
        }
    }

    private void playItMakes(){
        try {
            String sound = params.getString("it_makes_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    playPhonicSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void playPhonicSound(){
        try {
            playSound(currentPhonicSound, new Runnable() {
                @Override
                public void run() {
                    playNextSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playNextSound();
                }
            }, 800);
        }
    }

    public void playNextSound(){
        try {
            demoItem.setVisibility(View.INVISIBLE);
            itemsLayout.setVisibility(View.VISIBLE);
            String sound = params.getString("touch");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    playSoundAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void playSoundAgain(){
        try {
            playSound(currentPhonicSound, new Runnable() {
                @Override
                public void run() {
                    itemsEnabled = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    itemsEnabled = true;
                }
            }, 700);
        }
    }

    private void playSoundAndRunnableAfterCompletion(String sound) {
        try {
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
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

    public void clickedItem(int item){
        if (itemsEnabled) {
            if (item == correctItem) {
                // setItemsEnabled(false);

                ImageView iv = null;
                if (item == 0) {
                    iv = item1;
                } else if (item == 1) {
                    iv = item2;
                }
                Globals.playStarWorks(THIS, iv);

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
                playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                    @Override
                    public void run() {
                        if (mRunnable != null) {
                            mRunnable.run();
                        }
                    }
                });
            } else {
                playSound(FetchResource.negativeAffirmation(THIS), null);
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
