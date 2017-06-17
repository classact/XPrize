package classact.com.xprize.activity.drill.sound;

import android.content.Context;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.WordLetterLayout;

public class SoundDrillSevenActivity extends AppCompatActivity {
    //private SegmetedWritingView segmentWritingView;
    private JSONArray data;
    private LinearLayout writingContainer;
    private MediaPlayer mp;
    private Handler handler;
    private String[] letterSounds;
    private String mWordSound;
    private int correctItem;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView[] items;
    private int currentTripple;
    private JSONObject params;
    private ArrayList<JSONObject> currentWord;
    boolean itemsEnabled;
    boolean roundEnd;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_seven);
        itemsEnabled = false;
        roundEnd = false;
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);

        items = new ImageView[3];
        items[0] = item1;
        items[1] = item2;
        items[2] = item3;

        for (int i = 0; i < items.length; i++) {
            items[i].setImageResource(0);
        }

        currentTripple = 0;

        writingContainer = (LinearLayout)findViewById(R.id.writing_container);
        String drillData = getIntent().getExtras().getString("data");
        handler = new Handler(Looper.getMainLooper());
        initialiseData(drillData);
        showTripple();
    }

    private void initialiseData(String drillData){
        try{
            params = new JSONObject(drillData);
            data = params.getJSONArray("words");
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void showTripple(){
        try {
            // Setup word
            // Display metrics
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;

            roundEnd = false;
            //segmentWritingView = new SegmetedWritingView(this,R.drawable.backgroundwhite);
            //writingContainer.removeAllViews();
            //
            for (int i = 0 ; i < writingContainer.getChildCount(); i++){
                writingContainer.getChildAt(i).setVisibility(View.INVISIBLE);
            }

            mWordSound = data.getJSONObject(currentTripple).getString("word_sound");

            currentWord = new ArrayList();
            JSONArray ourWord = data.getJSONObject(currentTripple).getJSONArray("segmeted_word_spelling");
            for (int i = 0; i < ourWord.length(); i++) {
                currentWord.add(ourWord.getJSONObject(i));
            }

            JSONArray array = data.getJSONObject(currentTripple).getJSONArray("segmeted_word_sounds");
            letterSounds = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                letterSounds[i] = array.getJSONObject(i).getString("sound");
            }

            // Pictures
            int width = (int) (300 * density);
            int marginLeft = (int) (50 * density);
            int totalWidth = 0;
            int screenWidth = displayMetrics.widthPixels;

            JSONArray pictures = data.getJSONObject(currentTripple).getJSONArray("pictures");
            int[] shuffledArrayIndexes = FisherYates.shuffle(pictures.length()); // Randomized indexes
            int length = Math.min(pictures.length(), items.length);
            boolean foundCorrectItem = false;
            for(int i = 0; i < length; i++) {
                int si = shuffledArrayIndexes[i];
                System.out.println("SoundDrillSevenActivity.showTripple > Debug: Shuffled index is (" + si + ")");
                JSONObject pictureObject = pictures.getJSONObject(si);
                ImageView iv = items[i];

                totalWidth += width;
                if (i > 0) {
                    totalWidth += marginLeft;
                }

                iv.setImageResource(pictureObject.getInt("picture"));
                final int index = i;
                iv.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                clickedItem(index);
                            }
                        }
                );
                if (pictureObject.getInt("correct") == 1) {
                    foundCorrectItem = true;
                    correctItem = i;
                    System.out.println("SoundDrillSevenActivity.showTripple > Debug: correctItem is (" + correctItem + ")");
                }
            }

            // Image view
            ImageView iv1 = items[0];
            MarginLayoutParams iv1Layout = (MarginLayoutParams) iv1.getLayoutParams();
            iv1Layout.leftMargin = (screenWidth - totalWidth)/2;
            iv1.setLayoutParams(iv1Layout);

            // Double check that an image with correct picture exists
            if (!foundCorrectItem) {

                // Debug
                System.out.println("SoundDrillSevenActivity.showTripple > Debug: Hacking correct item, as it hasn't been found yet");

                JSONObject correctObject = null;

                for (int i = 0; i < pictures.length(); i++) {
                    JSONObject pictureObject = pictures.getJSONObject(i);
                    if (pictureObject.getInt("correct") == 1) {
                        correctObject = pictureObject;
                        System.out.println("SoundDrillSevenActivity.showTripple > Debug: correctItem is (" + correctItem + ")");
                        break;
                    }
                }

                // Validate that a correct object has been found
                if (correctObject == null) {
                    throw new Exception("Correct item could not be found at all");
                }

                // Otherwise, assign correct item to last item
                shuffledArrayIndexes = FisherYates.shuffle(items.length);
                correctItem = shuffledArrayIndexes[0];
                items[correctItem].setImageResource(correctObject.getInt("picture"));
            }

            // Clear word
            int writingContainerChildCount = writingContainer.getChildCount();
            for (int i = 0; i < writingContainerChildCount; i++) {
                ImageView iv = (ImageView) writingContainer.getChildAt(i);
                iv.setImageResource(0);
                iv.setVisibility(View.INVISIBLE);
                MarginLayoutParams ivLayout = (MarginLayoutParams) iv.getLayoutParams();
                ivLayout.topMargin = (int) (0 * density);
                iv.setLayoutParams(ivLayout);
            }

            // Populate word
            List<ImageView> letterViews = new ArrayList<>();
            List<Integer> letterResources = new ArrayList<>();
            String letterWord = "";
            int wordLength = Math.min(writingContainerChildCount, currentWord.size());
            for (int i = 0; i < wordLength; i++) {
                String letter = currentWord.get(i).getString("letter");
                int imageId = currentWord.get(i).getInt("black");
                ImageView iv = (ImageView) writingContainer.getChildAt(i);
                iv.setImageResource(imageId);
                letterViews.add(iv);
                letterResources.add(imageId);
                letterWord += letter;
            }

            // Order letters
            int letterCount = 0;
            float letterWidth = density * 150;
            float letterScale = 1.f;

            letterViews = WordLetterLayout.level(
                    THIS,
                    letterViews,
                    letterResources,
                    letterWord,
                    displayMetrics,
                    letterWidth,
                    letterScale
            );
            playListenToWordAndTouch();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playListenToWordAndTouch() {
        try {
            System.out.println("SoundDrillSevenActivity.playListenToWordAndTouch > Debug: correctItem is (" + correctItem + ")");
            String sound = params.getString("listen_to_word_and_touch");
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
                    playSayWordSlowly();
                }
            });
            mp.prepare();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void playSayWordSlowly() {
        String sound = "";
        try{
            sound = data.getJSONObject(currentTripple).getString("segmeted_word_slow_sound");
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!roundEnd) {
                                itemsEnabled = true;
                            }
                        }
                    }, mp.getDuration());
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
        catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            playLetterSounds(0);
        }
    }

    public void clickedItem(int item) {
        if (itemsEnabled) {
            try {
                if (item == correctItem) {
                    roundEnd = true;
                    itemsEnabled = false;
                    playAffirmationSound();
                } else {
                    playThisSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
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

    private void playAffirmationSound(){
        try {
            int soundId = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
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
                    showLettersAndPlayAndLetterSounds(0);
                }
            });
            mp.prepare();
        }
        catch(Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void showLettersAndPlayAndLetterSounds(final int i) {
        try{
            // base case
            if (i == currentWord.size()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playFullWord();
                    }
                }, 200);
            } else {
                JSONObject letterOfCurrentWord = currentWord.get(i);

                writingContainer.getChildAt(i).setVisibility(View.VISIBLE);
                ((ImageView) writingContainer.getChildAt(i)).setImageResource(letterOfCurrentWord.getInt("black"));

                String sound = letterSounds[i];
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
                        showLettersAndPlayAndLetterSounds(i + 1);
                    }
                });
                mp.prepare();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void playLetterSounds(final int i) {
        try{
            // base case
            if (i == currentWord.size()) {
                if (!roundEnd) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemsEnabled = true;
                        }
                    }, 250);
                }
            } else {
                String sound = letterSounds[i];
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
                        playLetterSounds(i + 1);
                    }
                });
                mp.prepare();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void playFullWord() {
        String sound = "";
        try{
            // sound = letterSounds[0];
            sound = mWordSound;
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
                    currentTripple++;
                    if (currentTripple < data.length()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showTripple();
                            }
                        }, 1000);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                theEnd();
                            }
                        }, 1000);
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
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            currentTripple++;
            if (currentTripple < data.length()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTripple();
                    }
                }, 1000);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        theEnd();
                    }
                }, 1000);
            }
        }
    }

    private void playThisSound(int soundId){
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
        catch (Exception  ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    public void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
    }

    private void theEnd() {

        // Debug
        System.out.println("SoundDrillSevenActivity.theEnd > Debug: THE END!!!!");

        if (mp != null) {
            mp.release();
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}

