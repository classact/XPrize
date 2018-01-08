package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.WordLetterLayout;

public class SoundDrillSevenActivity extends DrillActivity {
    //private SegmetedWritingView segmentWritingView;

    @BindView(R.id.activity_sound_drill_seven) LinearLayout rootView;

    @BindView(R.id.writing_container) LinearLayout writingContainer;

    @BindView(R.id.letter_1) ImageView letter1;
    @BindView(R.id.letter_2) ImageView letter2;
    @BindView(R.id.letter_3) ImageView letter3;
    @BindView(R.id.letter_4) ImageView letter4;
    @BindView(R.id.letter_5) ImageView letter5;
    @BindView(R.id.letter_6) ImageView letter6;
    @BindView(R.id.letter_7) ImageView letter7;

    @BindView(R.id.objects_container) LinearLayout objectsContainer;

    @BindView(R.id.item1) ImageView item1;
    @BindView(R.id.item2) ImageView item2;
    @BindView(R.id.item3) ImageView item3;

    private String[] letterSounds;
    private String mWordSound;
    private String mWordString;
    private int correctItem;
    private ImageView[] items;
    private TextView mFullWordTextView;
    private int currentTripple;
    boolean itemsEnabled;
    boolean roundEnd;

    private JSONArray data;
    private JSONArray pictures;
    private JSONObject params;
    private ArrayList<JSONObject> currentWord;

    private LinkedHashMap<ImageView, JSONObject> imagePictures;

    private SoundDrill07ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_seven);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill07ViewModel.class)
                .register(getLifecycle())
                .prepare(context);
        
        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

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
        }
    }

    private void hideAllLetters() {
        for (int i = 0 ; i < writingContainer.getChildCount(); i++){
            writingContainer.getChildAt(i).setVisibility(View.INVISIBLE);
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

            if (mFullWordTextView != null) {
                writingContainer.removeView(mFullWordTextView);
            }

            hideAllLetters();

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

            pictures = data.getJSONObject(currentTripple).getJSONArray("pictures");
            imagePictures = new LinkedHashMap<>();

            int[] shuffledArrayIndexes = FisherYates.shuffle(pictures.length()); // Randomized indexes
            int length = Math.min(pictures.length(), items.length);
            boolean foundCorrectItem = false;
            for(int i = 0; i < length; i++) {
                int si = shuffledArrayIndexes[i];
                System.out.println("SoundDrillSevenActivity.showTripple > Debug: Shuffled index is (" + si + ")");
                JSONObject pictureObject = pictures.getJSONObject(si);
                ImageView iv = items[i];
                imagePictures.put(iv, pictureObject);

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
                iv.setAlpha(1f);
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
                ImageView iv = items[correctItem];
                iv.setImageResource(correctObject.getInt("picture"));
                imagePictures.put(iv, correctObject);
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
                    context,
                    letterViews,
                    letterResources,
                    letterWord,
                    displayMetrics,
                    letterWidth,
                    letterScale,
                    true
            );

            mWordString = letterWord;

            playListenToWordAndTouch();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playListenToWordAndTouch() {
        try {
            System.out.println("SoundDrillSevenActivity.playListenToWordAndTouch > Debug: correctItem is (" + correctItem + ")");
            String sound = params.getString("listen_to_word_and_touch");
            playSound(sound, this::playSayWordSlowly);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playSayWordSlowly() {
        String sound;
        try{
            sound = data.getJSONObject(currentTripple).getString("segmeted_word_slow_sound");
            playSound(sound, () -> {
                if (!roundEnd) {
                    itemsEnabled = true;
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickedItem(final int item) {
        if (itemsEnabled) {
            try {
                ImageView iv = items[item];
                JSONObject picture = imagePictures.get(iv);
                String pictureSound = picture.getString("word_sound");
                if (item == correctItem) {
                    roundEnd = true;
                    itemsEnabled = false;
                    if (iv != null) {
                        starWorks.play(this, iv);
                    }
                    playSound(pictureSound, new Runnable() {
                        @Override
                        public void run() {
                            playAffirmationSound(item);
                        }
                    });
                } else {
                    playSound(pictureSound, new Runnable() {
                        @Override
                        public void run() {
                            playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()), null);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fadeIncorrect(ImageView ivCorrect, long fadeDuration) {
        for (int i = 0; i < items.length; i++) {
            ImageView iv = items[i];
            if (iv != ivCorrect) {
                iv.animate()
                        .alpha(0f)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(fadeDuration)
                        .start();
            }
        }
    }

    private void playAffirmationSound(final int item){
        try {
            int soundId = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, myUri);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    ImageView iv = items[item];
                    int waitDuration = Math.min(mp.getDuration(), 200);
                    fadeIncorrect(iv, waitDuration);
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            showLettersAndPlayAndLetterSounds(0);
                        }
                    }, 400);
                }
            });
            mediaPlayer.prepare();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void showLettersAndPlayAndLetterSounds(final int i) {
        try{
            // base case
            if (i == currentWord.size()) {
                handler.delayed(new Runnable() {
                    @Override
                    public void run() {
                        playFullWord();
                    }
                }, 700);
            } else {
                JSONObject letterOfCurrentWord = currentWord.get(i);

                writingContainer.getChildAt(i).setVisibility(View.VISIBLE);
                ((ImageView) writingContainer.getChildAt(i)).setImageResource(letterOfCurrentWord.getInt("black"));

                String sound = letterSounds[i];
                playSound(sound, () -> {
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            showLettersAndPlayAndLetterSounds(i + 1);
                        }
                    }, 400);
                });
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playLetterSounds(final int i) {
        try{
            // base case
            if (i == currentWord.size()) {
                if (!roundEnd) {
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            itemsEnabled = true;
                        }
                    }, 250);
                }
            } else {
                String sound = letterSounds[i];
                playSound(sound, () -> playLetterSounds(i + 1));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playFullWord() {
        String sound = "";
        try{

            hideAllLetters();

            mFullWordTextView = new TextView(context);
            mFullWordTextView.setText(mWordString);
            mFullWordTextView.setTextSize(150f);
            mFullWordTextView.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
            mFullWordTextView.setTextColor(Color.BLACK);
            writingContainer.addView(mFullWordTextView, 1);
            MarginLayoutParams tViewLP = (MarginLayoutParams) mFullWordTextView.getLayoutParams();
            tViewLP.topMargin = 19;
            mFullWordTextView.setLayoutParams(tViewLP);

            // sound = letterSounds[0];
            sound = mWordSound;

            playSound(sound, () -> {
                currentTripple++;
                if (currentTripple < data.length()) {
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            showTripple();
                        }
                    }, 1300);
                } else {
                    handler.delayed(new Runnable() {
                        @Override
                        public void run() {
                            theEnd();
                        }
                    }, 1300);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
    }

    private void theEnd() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

