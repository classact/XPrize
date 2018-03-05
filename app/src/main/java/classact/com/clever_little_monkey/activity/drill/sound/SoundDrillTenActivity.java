package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.FisherYates;
import classact.com.clever_little_monkey.utils.ResourceSelector;

public class SoundDrillTenActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_ten) ConstraintLayout rootView;

    @BindView(R.id.word_01) ImageView buttonWord1;
    @BindView(R.id.word_02) ImageView buttonWord2;
    @BindView(R.id.word_03) ImageView buttonWord3;
    @BindView(R.id.word_04) ImageView buttonWord4;
    @BindView(R.id.word_05) ImageView buttonWord5;

    @BindView(R.id.word_01_g_h) Guideline word01GH;
    @BindView(R.id.word_02_g_h) Guideline word02GH;
    @BindView(R.id.word_03_g_h) Guideline word03GH;
    @BindView(R.id.word_04_g_h) Guideline word04GH;
    @BindView(R.id.word_05_g_h) Guideline word05GH;

    @BindView(R.id.word_01_g_v) Guideline word01GV;
    @BindView(R.id.word_02_g_v) Guideline word02GV;
    @BindView(R.id.word_03_g_v) Guideline word03GV;
    @BindView(R.id.word_04_g_v) Guideline word04GV;
    @BindView(R.id.word_05_g_v) Guideline word05GV;

    @BindView(R.id.flash_word) ImageView flashButton;

    @BindView(R.id.flash_word_g_h) Guideline flashWordGH;
    @BindView(R.id.flash_word_g_v) Guideline flashWordGV;

    private ImageView lastWrongWord;

    private final int FLASH_LEFT_MARGIN = 130;
    private final int FLASH_TOP_MARGIN = 675;

    private final int WORD_GAP = 200;
    private final int TOP_LINE_START = 300;
    private final int BOTTOM_LINE_START = 1070;

    private final int CENTER_MARGIN_LEFT = 1108;
    private final int BOTTOM_LINE_LEFT_OFFSET = 0;

    private JSONArray words;
    private int currentWord;
    private int mode;
    private JSONObject[] objects;
    private JSONObject allData;
    private boolean buttonsEnabled;

    private SoundDrill10ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_ten);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill10ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        ez.guide.setPercentage(word01GH, 0.25f);
        ez.guide.setPercentage(word01GV, 0.25f);

        ez.guide.setPercentage(word02GH, 0.25f);
        ez.guide.setPercentage(word02GV, 0.45f);

        ez.guide.setPercentage(word03GH, 0.25f);
        ez.guide.setPercentage(word03GV, 0.65f);

        ez.guide.setPercentage(word04GH, 0.75f);
        ez.guide.setPercentage(word04GV, 0.375f);

        ez.guide.setPercentage(word05GH, 0.75f);
        ez.guide.setPercentage(word05GV, 0.575f);

        ez.guide.setPercentage(flashWordGH, 0.5f);
        ez.guide.setPercentage(flashWordGV, 0.455f);

        buttonsEnabled = false;

        ConstraintLayout.LayoutParams button1LP = (ConstraintLayout.LayoutParams) buttonWord1.getLayoutParams();
        ConstraintLayout.LayoutParams button2LP = (ConstraintLayout.LayoutParams) buttonWord2.getLayoutParams();
        ConstraintLayout.LayoutParams button3LP = (ConstraintLayout.LayoutParams) buttonWord3.getLayoutParams();
        ConstraintLayout.LayoutParams button4LP = (ConstraintLayout.LayoutParams) buttonWord4.getLayoutParams();
        ConstraintLayout.LayoutParams button5LP = (ConstraintLayout.LayoutParams) buttonWord5.getLayoutParams();

        button1LP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button2LP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button3LP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button4LP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button5LP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;

        button1LP.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button2LP.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button3LP.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button4LP.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        button5LP.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

        buttonWord1.setLayoutParams(button1LP);
        buttonWord2.setLayoutParams(button2LP);
        buttonWord3.setLayoutParams(button3LP);
        buttonWord4.setLayoutParams(button4LP);
        buttonWord5.setLayoutParams(button5LP);

        // Debug
        System.out.println("-- SoundTrillTenActivity.onCreate > Debug: METHOD CALLED");

        initialiseButtons();
        String drillData = getIntent().getExtras().getString("data");
        mode = 1;
        initialiseData(drillData);
    }

    private void initialiseData(String drillData) {

        // Debug
        System.out.println("-- SoundTrillTenActivity.initialiseData > Debug: METHOD CALLED");

        try {
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            flashButton.setVisibility(View.INVISIBLE);
            String sound = allData.getString("instructions");
            playSound(sound, () -> {
                currentWord = 0;
                flashButton.setVisibility(View.VISIBLE);
                showWord();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseButtons() {

        // Debug
        System.out.println("-- SoundTrillTenActivity.initialiseButtons > Debug: METHOD CALLED");

        buttonWord1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(v, 0);
                    }
                }
        );
        buttonWord2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(v, 1);
                    }
                }
        );
        buttonWord3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(v, 2);
                    }
                }
        );
        buttonWord4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(v, 3);
                    }
                }
        );
        buttonWord5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(v, 4);
                    }
                }
        );
    }

    private void showWord(){

        // Debug
        System.out.println("-- SoundTrillTenActivity.showWord > Debug: METHOD CALLED");

        try{
            int image = words.getJSONObject(currentWord).getInt("image");
            String name = words.getJSONObject(currentWord).getString("name");

            // Debug
            System.out.println("SoundDrillTenActivity.showWord > Debug: word is " + name);

            loadAndLayoutImage(flashButton, image);
            String sound = words.getJSONObject(currentWord).getString("sound");
            playSoundAndShowNext(sound);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundAndShowNext(String sound){

        // Debug
        System.out.println("-- SoundTrillTenActivity.playSoundAndShowNext > Debug: METHOD CALLED");

        try {
            playSound(sound, () ->  handler.delayed(showNextWord, 500));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Runnable showNextWord = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTenActivity.showNextWord.run(Runnable) > Debug: METHOD CALLED");

            // Remove all existing callbacks for this object
            handler.removeCallbacks(this);

            // Set current word to the next word
            currentWord++;

            // Validate current word
            if (currentWord < 5) {
                showWord();
            }
            else{
                // Remove all existing call backs for this object
                handler.removeCallbacks(this);

                // Switch to mode 2
                mode = 2;

                // Reset current word to first word
                currentWord = 0;

                // After 0.1s, begin touch words (get data ready for mode 2)
                handler.delayed(beginTouchWord,100);
            }
        }
    };

    private void playWordSound() {

        // Debug
        System.out.println("-- SoundTrillTenActivity.playWordSound > Debug: METHOD CALLED");

        String sound = "";
        try{
            sound = objects[currentWord].getString("sound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp) -> {
                mp.start();
                handler.delayed(() -> buttonsEnabled = true, mp.getDuration() - 250);
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mediaPlayer.stop();
            });
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();;
        }
    }

    private void sayTouchWord() {

        // Debug
        System.out.println("-- SoundTrillTenActivity.sayTouchWord > Debug: METHOD CALLED");

        String sound;
        try {
            // Extra sound resourcefrom 'allData' array
            sound = allData.getString("touch");
            playSound(sound, this::playWordSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Runnable beginTouchWord = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTenActivity.beginTouchWord > Debug: METHOD CALLED");

            try {
                // Hide flash button
                flashButton.setVisibility(View.INVISIBLE);

                // Initialize objects if it's null
                if (objects == null) {
                    objects = new JSONObject[]{null,null,null,null,null};

                    // Get a shuffled array of indexes for 'objects' array
                    int[] shuffledIndexes = FisherYates.shuffle(objects.length);

                    // Assign words to objects array
                    for (int i = 0; i < objects.length; i++) {

                        // Initialize shuffled index
                        int shuffledIndex = shuffledIndexes[i];

                        // Retrieve word from 'words' based on shuffledIndex
                        JSONObject word = words.getJSONObject(shuffledIndex);

                        // Assign word to 'objects[i]'
                        objects[i] = word;

                        // Extra word resource from objects[i]
                        int wordImage = word.getInt("image");

                        // Debug
                        System.out.println("SoundTrillTenActivity.beginTouchWord > Debug: Processed (" +
                                i + ", " + shuffledIndex + ", " +  word.getString("name") + ")");

                        // Update button backgrounds accordingly
                        switch (i) {
                            case 0:
                                loadAndLayoutImage(buttonWord1, wordImage);
                                break;
                            case 1:
                                loadAndLayoutImage(buttonWord2, wordImage);
                                break;
                            case 2:
                                loadAndLayoutImage(buttonWord3, wordImage);
                                break;
                            case 3:
                                loadAndLayoutImage(buttonWord4, wordImage);
                                break;
                            case 4:
                                loadAndLayoutImage(buttonWord5, wordImage);
                                break;
                            default:
                                break;
                        }
                    }
                }

                // Show all button words
                buttonWord1.setVisibility(View.VISIBLE);
                buttonWord2.setVisibility(View.VISIBLE);
                buttonWord3.setVisibility(View.VISIBLE);
                buttonWord4.setVisibility(View.VISIBLE);
                buttonWord5.setVisibility(View.VISIBLE);

                // Randomly set next 'currentWord'
                Random rnd = new Random();
                currentWord = rnd.nextInt(5);

                // Say the next touch word
                sayTouchWord();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    private void reward(int currentWord){

        // Debug
        System.out.println("-- SoundTrillTenActivity.reward > Debug: METHOD CALLED");

        switch (currentWord){
            case 0:
//                buttonWord1.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 1:
//                buttonWord2.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 2:
//                buttonWord3.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 3:
//                buttonWord4.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 4:
//                buttonWord5.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            default:
                System.err.println("SoundTrillTenActivity.reward > Error: Invalid currentWord (" + currentWord + ")");
                break;
        }
    }

    private void playThisSound(View view, int soundId){
        
        try {
            ((ImageView) view).setColorFilter(Color.parseColor("#33ccff"));
            playSound(soundId, () -> {
                Random rand = new Random();
                currentWord = rand.nextInt(5);

                // Check if word exists
                if (objects[currentWord] == null) {
                    // It doesn't so find the next one that exists
                    currentWord = -1;
                    int k = 0;
                    boolean done = false;
                    while (!done) {
                        if (objects[k] != null) {
                            currentWord = k;
                            done = true;
                        } else {
                            k++;
                            if (k == 5) {
                                done = true;
                            }
                        }
                    }
                }
                ((ImageView) view).setColorFilter(Color.parseColor("#777777"));
                if (currentWord > -1) {
                    // Say the word
                    sayTouchWord();
                } else {
                    // No words exit
                    // End it
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void checkIfWordCorrect(View view, int word){
        
        if (buttonsEnabled) {
            if (lastWrongWord != null) {
                lastWrongWord.setColorFilter(null);
            }
            if (word == currentWord) {
                view.setEnabled(false);
                lastWrongWord = null;
                buttonsEnabled = false;
                reward(word);
                int soundId = ResourceSelector.getPositiveAffirmationSound(context);
                playThisSound(view, soundId);
                starWorks.play(this, view);
                currentWord++;
            } else {
                lastWrongWord = (ImageView) view;
                int soundId = ResourceSelector.getNegativeAffirmationSound(context);
                playSound(soundId,
                        () -> ((ImageView) view).setColorFilter(Color.RED),
                        () -> ((ImageView) view).setColorFilter(null));
            }
        }
    }

    private void setButtonsEnabled(boolean enable) {
        buttonWord1.setEnabled(enable);
        buttonWord2.setEnabled(enable);
        buttonWord3.setEnabled(enable);
        buttonWord4.setEnabled(enable);
        buttonWord5.setEnabled(enable);
    }
}
