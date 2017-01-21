package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFiveActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageView demoItem;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private String drillData;
    private JSONArray sets;
    JSONArray images;
    private Handler handler;
    private int currentSet;
    private int currentItem;
    private int correctItem;
    private int currentItemName;
    private int currentSound;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_five);
        demoItem = (ImageView)findViewById(R.id.item_demo);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item4 = (ImageView)findViewById(R.id.item4);
        toggleItemsVisibility(false);
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
        item3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(3);
                    }
                }
        );
        item4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(4);
                    }
                }
        );
        drillData = getIntent().getExtras().getString("data");
        currentSet = 1;
        handler = new Handler(Looper.getMainLooper());
        initialiseData();
        showSet();
    }

    private void initialiseData(){
        try {
            params = new JSONObject(drillData);
            sets = params.getJSONArray("sets");
            getWindow().getDecorView().getRootView().setBackgroundResource(params.getInt("background"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void toggleItemsVisibility(boolean show){
        if (show){
            item1.setVisibility(View.VISIBLE);
            item2.setVisibility(View.VISIBLE);
            item3.setVisibility(View.VISIBLE);
            item4.setVisibility(View.VISIBLE);
        }
        else{
            item1.setVisibility(View.INVISIBLE);
            item2.setVisibility(View.INVISIBLE);
            item3.setVisibility(View.INVISIBLE);
            item4.setVisibility(View.INVISIBLE);
        }
    }

    public void showSet(){
        demoItem.setVisibility(View.VISIBLE);
        toggleItemsVisibility(false);
        try{
            JSONObject setData = sets.getJSONObject(currentSet - 1);
            demoItem.setImageResource(setData.getInt("demoimage"));
            currentItemName = setData.getInt("demosound");
            currentSound = setData.getInt("sound");
            images = setData.getJSONArray("images");
            for(int i = 0; i < 4; i++){
                JSONObject item = images.getJSONObject(i);
                if (i == 0)
                    item1.setImageResource(item.getInt("image"));
                else if (i == 1)
                    item2.setImageResource(item.getInt("image"));
                else if (i == 2)
                    item3.setImageResource(item.getInt("image"));
                else
                    item4.setImageResource(item.getInt("image"));
                if (item.getInt("correct") == 1)
                    correctItem = i+1;
            }
            //Todo: Change the sound
            if (mp == null)
                mp = MediaPlayer.create(this, params.getInt("bahati_has_a"));
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playSound(){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + currentItemName);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(startDrill, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private Runnable startDrill = new Runnable(){
        @Override
        public void run() {
            playNextSound();
        }
    };

    public void playNextSound(){
        try {
            toggleItemsVisibility(true);
            int sound = params.getInt("she_needs_something_else");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playSoundAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playSoundAgain(){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + currentSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            toggleItemsVisibility(true);
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

    private void playSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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

    private void playPositiveSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (currentSet < 3) {
                        currentSet++;
                        handler.postDelayed(nextDrill, 1000);
                    } else {
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

    public void clickedItem(int item) {
        try {
            currentItem = item;
            int sound = images.getJSONObject(item - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    checkProgress();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }
    private void checkProgress(){
        if (currentItem == correctItem){
            playPositiveSound(ResourceSelector.getPositiveAffirmationSound(this));
        }
        else{
            playSound(ResourceSelector.getNegativeAffirmationSound(this));
        }
    }

    private Runnable nextDrill = new Runnable(){
        @Override
        public void run() {
            showSet();
        }
    };

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

