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

import java.util.ArrayList;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
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
    private String currentItemName;
    private String currentSound;
    private JSONObject params;
    private boolean itemsEnabled;
    private JSONObject[] orderedImages;
    private Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_five);
        demoItem = (ImageView)findViewById(R.id.item_demo);
        itemsEnabled = false;
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item4 = (ImageView)findViewById(R.id.item4);
        toggleItemsVisibility(false);
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
        item3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(2);
                    }
                }
        );
        item4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(3);
                    }
                }
        );
        rnd = new Random();
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
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void showSet(){
        toggleItemsVisibility(false);
        try{
            JSONObject setData = sets.getJSONObject(currentSet);

            demoItem.setImageResource(setData.getInt("demoimage"));
            demoItem.setVisibility(View.VISIBLE);

            currentItemName = setData.getString("demosound");
            currentSound = setData.getString("sound");
            images = setData.getJSONArray("images");

            ImageView[] items = {item1, item2, item3, item4};
            int numberOfImages = images.length();
            int numberOfItems = items.length;
            orderedImages = new JSONObject[numberOfImages];

            System.out.println("Number of images: " + numberOfImages);
            System.out.println("Number of items: " + numberOfItems);

            // Exception case
            if (numberOfImages > numberOfItems) {
                throw new Exception("SoundDrillFiveActivity.showSet > Exception: " +
                        "number of images do not match number of items");
            }

            // Get shuffled array of indexes
            int[] shuffledItemIndexes = FisherYates.shuffle(numberOfItems);

            // Get right word
            for(int i = 0; i < numberOfImages; i++){
                int randomizedIndex = shuffledItemIndexes[i];
                JSONObject item = images.getJSONObject(i);
                int imageResourceId = item.getInt("image");

                System.out.println("::: Selected image #" + randomizedIndex + ": " + imageResourceId);

                items[randomizedIndex].setImageResource(imageResourceId);
                orderedImages[randomizedIndex] = item;

                if (item.getInt("correct") == 1) {
                    correctItem = randomizedIndex;
                    System.out.println(":::: It's correct btw ... ");
                }
            }
            playBahatiHasA();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playBahatiHasA(){
        try {
            String sound = params.getString("bahati_has_a");
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
                    playCurrentItemSound();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playCurrentItemSound(){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), currentItemName);
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playSheNeedsSomethingElse();
                        }
                    }, 500);
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playSheNeedsSomethingElse(){
        try {
            toggleItemsVisibility(true);
            String sound = params.getString("she_needs_something_else");
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
                    playCurrentSound();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playCurrentSound(){
        try {
            toggleItemsVisibility(true);
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
            if (mp != null){
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
            mp.setDataSource(getApplicationContext(), myUri);
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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void playPositiveSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    currentSet++;
                    if (currentSet < sets.length()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showSet();
                            }
                        }, 1000);
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
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void clickedItem(int item) {
        if (itemsEnabled) {
            try {
                currentItem = item;

                // Disable items here
                // Otherwise can continuously carry on tapping
                if (currentItem == correctItem) {
                    itemsEnabled = false;
                }

                String sound = orderedImages[currentItem].getString("sound");
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
                        checkProgress();
                    }
                });
                mp.prepare();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mp != null){
                    mp.release();
                }
                mp = null;
                checkProgress();
                // finish();
            }
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

    private void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
        item4.setEnabled(enable);
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

//    @Override
//    public void onBackPressed() {
//    }
}

