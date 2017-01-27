package classact.com.xprize.activity.drill.sound;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTenActivity extends AppCompatActivity {

    private final int FLASH_LEFT_MARGIN = 130;
    private final int FLASH_TOP_MARGIN = 675;

    private final int WORD_GAP = 200;
    private final int TOP_LINE_START = 300;
    private final int BOTTOM_LINE_START = 1070;

    private final int CENTER_MARGIN_LEFT = 1108;
    private final int BOTTOM_LINE_LEFT_OFFSET = 0;

    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private ImageButton buttonWord4;
    private ImageButton buttonWord5;
    private ImageButton flashButton;
    private JSONArray words;
    private int currentWord;
    private MediaPlayer mp;
    private Handler handler;
    private int mode;
    private JSONObject[] objects;
    private JSONObject allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_ten);
        buttonWord1 = (ImageButton)findViewById(R.id.button_word1);
        buttonWord2 = (ImageButton)findViewById(R.id.button_word2);
        buttonWord3 = (ImageButton)findViewById(R.id.button_word3);
        buttonWord4 = (ImageButton)findViewById(R.id.button_word4);
        buttonWord5 = (ImageButton)findViewById(R.id.button_word5);
        flashButton = (ImageButton)findViewById(R.id.flash_word);

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
        handler = new Handler();
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
            int sound = allData.getInt("instructions");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentWord = 0;
                    flashButton.setVisibility(View.VISIBLE);
                    showWord();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
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
            int word = words.getJSONObject(currentWord).getInt("word");
            System.out.println("SoundDrillTenActivity.showWord > Debug: word is " + word);
            flashButton.setImageResource(word);
            int sound = words.getJSONObject(currentWord).getInt("sound");
            playSoundAndShowNext(sound);

        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playSoundAndShowNext(int soundid){

        // Debug
        System.out.println("-- SoundTrillTenActivity.playSoundAndShowNext > Debug: METHOD CALLED");

        try {
            // Get uri for sound
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);

            // Set data source to uri
            mp.setDataSource(getApplicationContext(), myUri);

            // Set on prepared listener
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    // Start the sound
                    mp.start();
                }
            });

            // Set on completion listener
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    // Reset media player
                    mp.reset();

                    // After 0.5s, show the next word
                    handler.postDelayed(showNextWord, 500);
                }
            });

            // Prepare the media player
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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
                handler.postDelayed(beginTouchWord,100);
            }
        }
    };

    private void playWordSound() {

        // Debug
        System.out.println("-- SoundTrillTenActivity.playWordSound > Debug: METHOD CALLED");

        try{
            int sound = objects[currentWord].getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
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
            ex.printStackTrace();;
            finish();
        }
    }

    private void sayTouchWord() {

        // Debug
        System.out.println("-- SoundTrillTenActivity.sayTouchWord > Debug: METHOD CALLED");

        try {
            // Extra sound resource id from 'allData' array
            int sound = allData.getInt("touch");

            // Get uri of sound to play
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);

            // Set media player's data source to uri
            mp.setDataSource(getApplicationContext(), myUri);

            // Set on prepared listener
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    // Start the media player
                    mp.start();
                }
            });

            // Set on completion listener
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    // Reset the media player
                    mp.reset();

                    // Play word sound
                    playWordSound();
                }
            });

            // Prepare the media player
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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
                    int[] shuffledIndexes = fisherYatesShuffle(objects.length);

                    // Assign words to objects array
                    for (int i = 0; i < objects.length; i++) {

                        // Initialize shuffled index
                        int shuffledIndex = shuffledIndexes[i];

                        // Retrieve word from 'words' based on shuffledIndex
                        JSONObject word = words.getJSONObject(shuffledIndex);

                        // Assign word to 'objects[i]'
                        objects[i] = word;

                        // Extra word resource from objects[i]
                        int wordImage = word.getInt("word");

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
                finish();

            }
        }
    };

    public Runnable sayWord = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTenActivity.sayWord.run(Runnable) > Debug: METHOD CALLED");

            playWordSound();
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

    private void playThisSound(int soundid){

        // Debug
        System.out.println("-- SoundTrillTenActivity.playThisSound > Debug: METHOD CALLED");

        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();

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

                        // Debug
                        System.out.println("SoundTrillTenActivity.playThisSound > Debug: " +
                                        "Finishing with Current Word (" + currentWord + ")");
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

    public void checkIfWordCorrect(int word){

        // Debug
        System.out.println("-- SoundTrillTenActivity.checkIfWordCorrect > Debug: METHOD CALLED");

        if (word == currentWord){
            reward(word);
            playThisSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
            currentWord++;
        }
        else{
            try {
                int soundid = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
                mp.reset();
                mp.setDataSource(getApplicationContext(), myUri);
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
    }

    @Override
    public void onPause(){
        super.onPause();

        // Debug
        System.out.println("-- SoundTrillTenActivity.onPause > Debug: METHOD CALLED");

        if (mp != null){
            mp.release();
        }
    }

    public int[] fisherYatesShuffle(int N) {

        // Initialize base based on N
        int[] base = new int[N];

        // Initialized rnd variable used in the shuffle
        Random rnd = new Random();

        // Declare variables to be used;
        int j, a, b;

        // Populate base
        for (int i = 0; i < N; i++) {
            base[i] = i;
        }

        // Start the shuffle
        for (int i = 0; i < N - 1; i++) {

            // Get random value j, where i <= j < N
            j = i + rnd.nextInt(N - i);

            // Store current values
            a = base[i];
            b = base[j];

            // Swap them
            base[i] = b;
            base[j] = a;
        }

        return base;
    }

//    @Override
//    public void onBackPressed() {
//    }
}
