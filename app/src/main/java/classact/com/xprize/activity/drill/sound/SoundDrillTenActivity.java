package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTenActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_ten) RelativeLayout rootView;

    @BindView(R.id.button_word1) ImageView buttonWord1;
    @BindView(R.id.button_word2) ImageView buttonWord2;
    @BindView(R.id.button_word3) ImageView buttonWord3;
    @BindView(R.id.button_word4) ImageView buttonWord4;
    @BindView(R.id.button_word5) ImageView buttonWord5;

    @BindView(R.id.flash_word) ImageView flashButton;

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

        buttonsEnabled = false;

        LayoutParams flashButtonLP = (LayoutParams) flashButton.getLayoutParams();
        flashButtonLP.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);
        flashButtonLP.removeRule(RelativeLayout.ALIGN_START);
        flashButtonLP.leftMargin = FLASH_LEFT_MARGIN;
        flashButtonLP.topMargin = FLASH_TOP_MARGIN;
        flashButton.setLayoutParams(flashButtonLP);

        LayoutParams button1LP = (LayoutParams) buttonWord1.getLayoutParams();
        LayoutParams button2LP = (LayoutParams) buttonWord2.getLayoutParams();
        LayoutParams button3LP = (LayoutParams) buttonWord3.getLayoutParams();
        LayoutParams button4LP = (LayoutParams) buttonWord4.getLayoutParams();
        LayoutParams button5LP = (LayoutParams) buttonWord5.getLayoutParams();

        button1LP.removeRule(RelativeLayout.BELOW);
        button2LP.removeRule(RelativeLayout.BELOW);
        button3LP.removeRule(RelativeLayout.BELOW);
        button4LP.removeRule(RelativeLayout.BELOW);
        button5LP.removeRule(RelativeLayout.BELOW);

        button2LP.addRule(RelativeLayout.RIGHT_OF, R.id.button_word1);
        button3LP.addRule(RelativeLayout.RIGHT_OF, R.id.button_word2);
        button5LP.addRule(RelativeLayout.RIGHT_OF, R.id.button_word4);

        button1LP.setMargins(620, TOP_LINE_START, 0, 0);
        button2LP.setMargins(WORD_GAP, TOP_LINE_START, 0, 0);
        button3LP.setMargins(WORD_GAP, TOP_LINE_START, 0, 0);
        button4LP.setMargins(850, BOTTOM_LINE_START, 0, 0);
        button5LP.setMargins(WORD_GAP, BOTTOM_LINE_START, 0, 0);

        button1LP.width = LayoutParams.WRAP_CONTENT;
        button2LP.width = LayoutParams.WRAP_CONTENT;
        button3LP.width = LayoutParams.WRAP_CONTENT;
        button4LP.width = LayoutParams.WRAP_CONTENT;
        button5LP.width = LayoutParams.WRAP_CONTENT;

        button1LP.height = LayoutParams.WRAP_CONTENT;
        button2LP.height = LayoutParams.WRAP_CONTENT;
        button3LP.height = LayoutParams.WRAP_CONTENT;
        button4LP.height = LayoutParams.WRAP_CONTENT;
        button5LP.height = LayoutParams.WRAP_CONTENT;

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
                            checkIfWordCorrect(0);
                    }
                }
        );
        buttonWord2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(1);
                    }
                }
        );
        buttonWord3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(2);
                    }
                }
        );
        buttonWord4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(3);
                    }
                }
        );
        buttonWord5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(4);
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

            flashButton.setImageResource(image);
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
                                buttonWord1.setImageResource(wordImage);
                                break;
                            case 1:
                                buttonWord2.setImageResource(wordImage);
                                break;
                            case 2:
                                buttonWord3.setImageResource(wordImage);
                                break;
                            case 3:
                                buttonWord4.setImageResource(wordImage);
                                break;
                            case 4:
                                buttonWord5.setImageResource(wordImage);
                                break;
                            default:
                                break;
                        }
                    }
                }

                BitmapDrawable bd1 = (BitmapDrawable) buttonWord1.getDrawable();
                BitmapDrawable bd2 = (BitmapDrawable) buttonWord2.getDrawable();
                BitmapDrawable bd3 = (BitmapDrawable) buttonWord3.getDrawable();
                BitmapDrawable bd4 = (BitmapDrawable) buttonWord4.getDrawable();
                BitmapDrawable bd5 = (BitmapDrawable) buttonWord5.getDrawable();

                Bitmap b1 = bd1.getBitmap();
                Bitmap b2 = bd2.getBitmap();
                Bitmap b3 = bd3.getBitmap();
                Bitmap b4 = bd4.getBitmap();
                Bitmap b5 = bd5.getBitmap();

                int b1Width = b1.getWidth();
                int b2Width = b2.getWidth();
                int b3Width = b3.getWidth();
                int b4Width = b4.getWidth();
                int b5Width = b5.getWidth();

                int topWidth = b1Width + WORD_GAP + b2Width + WORD_GAP + b3Width;
                int bottomWidth = b4Width + WORD_GAP + b5Width;

                LayoutParams button1LP = (LayoutParams) buttonWord1.getLayoutParams();
                LayoutParams button4LP = (LayoutParams) buttonWord4.getLayoutParams();

                button1LP.setMargins(CENTER_MARGIN_LEFT - (topWidth / 2), TOP_LINE_START, 0, 0);
                button4LP.setMargins(CENTER_MARGIN_LEFT - (bottomWidth / 2) + BOTTOM_LINE_LEFT_OFFSET, BOTTOM_LINE_START, 0, 0);

                buttonWord1.setLayoutParams(button1LP);
                buttonWord4.setLayoutParams(button4LP);

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
                buttonWord1.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 1:
                buttonWord2.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 2:
                buttonWord3.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 3:
                buttonWord4.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            case 4:
                buttonWord5.setVisibility(View.INVISIBLE);
                objects[currentWord] = null;
                break;
            default:
                System.err.println("SoundTrillTenActivity.reward > Error: Invalid currentWord (" + currentWord + ")");
                break;
        }
    }

    private void playThisSound(int soundId){
        
        try {
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

    public void checkIfWordCorrect(int word){
        
        if (buttonsEnabled) {
            if (word == currentWord) {
                buttonsEnabled = false;
                reward(word);
                playThisSound(ResourceSelector.getPositiveAffirmationSound(context));
                currentWord++;
            } else {
                int soundId = ResourceSelector.getNegativeAffirmationSound(context);
                playSound(soundId, null);
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
