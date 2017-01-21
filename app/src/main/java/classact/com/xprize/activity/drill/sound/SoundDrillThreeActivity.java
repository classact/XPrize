package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
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
    private int currentSound;
    private int currentPhonicSound;
    private LinearLayout itemsLayout;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_three);
        demoItem = (ImageView)findViewById(R.id.item_demo);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        itemsLayout = (LinearLayout)findViewById(R.id.items);
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
        currentSet = 1;
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
            finish();
        }
    }

    public void showSet(){
        demoItem.setVisibility(View.VISIBLE);
        itemsLayout.setVisibility(View.INVISIBLE);
        try{
            JSONObject setData = sets.getJSONObject(currentSet - 1);
            demoItem.setImageResource(setData.getInt("image"));
            currentSound = setData.getInt("sound");
            currentPhonicSound = setData.getInt("phonic_sound");
            JSONArray images = setData.getJSONArray("images");
            for(int i = 0; i < 2; i++){
                JSONObject item = images.getJSONObject(i);
                if (i == 0)
                    item1.setImageResource(item.getInt("image"));
                else if (i == 1)
                    item2.setImageResource(item.getInt("image"));
                if (item.getInt("correct") == 1)
                    correctItem = i+1;
            }
            int sound = params.getInt("this_is_the_letter");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null)
                mp = new MediaPlayer();
            mp.setDataSource(this, myUri);
            mp.prepare();
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
            mp = MediaPlayer.create(this, currentSound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playItMakes();

                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable playPhonicRunnable = new Runnable(){
        @Override
        public void run() {
            playItMakes();
        }
    };

    private void playItMakes(){
        try {
            int sound = params.getInt("it_makes_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playPhonicSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playPhonicSound(){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + currentPhonicSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playNextSound();
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
            demoItem.setVisibility(View.INVISIBLE);
            itemsLayout.setVisibility(View.VISIBLE);
            int sound = params.getInt("touch");
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
        if (item == correctItem){
            playSound(ResourceSelector.getPositiveAffirmationSound(this));
            /*if (currentSet == 1)
                reward1.setImageResource(R.drawable.rewardball1colour);
            else if (currentSet == 2)
                reward2.setImageResource(R.drawable.rewardball1colour);
            else if (currentSet == 3)
                reward3.setImageResource(R.drawable.rewardball1colour);
            else
                reward4.setImageResource(R.drawable.rewardball1colour);*/
            if (currentSet < 4){
                currentSet ++;
                handler.postDelayed(nextDrill,2000);
            }
            else{
                this.finish();
            }
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
