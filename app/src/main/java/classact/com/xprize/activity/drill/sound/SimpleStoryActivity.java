package classact.com.xprize.activity.drill.sound;

import android.app.ActionBar;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.control.SimpleStorySentence;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ImageHelper;
import classact.com.xprize.utils.ResourceSelector;

public class SimpleStoryActivity extends AppCompatActivity {
    private String drillData;
    private JSONArray sentences;
    private MediaPlayer mp;
    private Handler handler;
    private int currentSentence;
    ArrayList<String> sounds;
    ArrayList<ImageView> sentenceViews;
    private int currentSound;
    private JSONObject allData;
    private LinearLayout container;
    private ImageView nextButton;
    private int currentQuestion;
    private JSONArray questions;
    private ImageView singleImage;
    private ImageView trippleImageOne;
    private ImageView trippleImageTwo;
    private ImageView tripleImageThree;

    private SimpleStoryActivity thisActivity;

    private LinearLayout col;

    // Image View grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private ArrayList<ArrayList<ArrayList<ImageView>>> imageViewGridSets;

    // Black word grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private ArrayList<ArrayList<ArrayList<Integer>>> blackWordGridSets;

    // Black word grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private ArrayList<ArrayList<ArrayList<Integer>>> redWordGridSets;

    // Sound path grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private ArrayList<ArrayList<ArrayList<String>>> soundPathGridSets;


    private final int STARTING_SET = 0;
    private final int BLACK_WORD = 0;
    private final int RED_WORD = 1;

    private int mCurrentSetIndex;
    private int mCurrentRowIndex;
    private int mCurrentWordIndex;
    private boolean mWordFlipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_story);

        // Set background to background-simple-story (until I change it in the layout file)
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);

        container = (LinearLayout)findViewById(R.id.container_simple_story);

        RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
        containerParams.topMargin = 0;
        containerParams.leftMargin = 0;
        container.setLayoutParams(containerParams);

        singleImage = (ImageView)findViewById(R.id.single_image);
        trippleImageOne = (ImageView)findViewById (R.id.tripple_image_1);
        trippleImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(0);
            }
        });
        trippleImageTwo = (ImageView)findViewById(R.id.tripple_image_2);
        trippleImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(1);
            }
        });
        tripleImageThree = (ImageView)findViewById(R.id.tripple_image_3);
        tripleImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(2);
            }
        });
        nextButton = (ImageView)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });
        handler = new Handler();
        /*initialiseData();
        //  playReadEachSentenceAfterMother();
        populateAndShowSentence();*/

        // Set this activity to ... well ... this activity!
        thisActivity = this;

        // Create columns
        col = new LinearLayout(getApplicationContext());
        // col.setBackgroundColor(Color.GRAY);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams colParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        col.setLayoutParams(colParams);

        // Retrieves the sentences from drill data
        // Do this before creating rows, in case there is an override for number of
        // sentences per set
        // * Note: NO override exists at the moment *
        initialiseData();

        // In the DB, Set numbers start from 1 instead of 0
        int startingSetIndex = STARTING_SET + 1;

        // Initialize the current set
        int currentSet = -1;

        // Loop through sentences to create Linear Layout rows
        // and populate with Image View rows
        try {
            if (sentences != null) {

                // Initialize Image View grid sets
                imageViewGridSets = new ArrayList<>();

                // Initialize black word grid sets
                blackWordGridSets = new ArrayList<>();

                // Initialize red word grid sets
                redWordGridSets = new ArrayList<>();

                // Initialize sound path grid sets
                soundPathGridSets = new ArrayList<>();

                // Create a grid to store list of Image Views
                ArrayList<ArrayList<ImageView>> imageViewGrid = null;

                // Create a grid to store list of black word resource ids
                ArrayList<ArrayList<Integer>> blackWordGrid = null;

                // Create a grid to store list of red word resource ids
                ArrayList<ArrayList<Integer>> redWordGrid = null;

                // Create a grid to store list of sound paths
                ArrayList<ArrayList<String>> soundPathGrid = null;

                // Loopy loop ...
                for (int i = 0; i < sentences.length(); i++) {

                    // Instantiate sentence object
                    JSONArray sentence = sentences.getJSONArray(i);

                    // Validate sentence object
                    if (sentence == null) {
                        throw new Exception("Sentence (" + i + ") is null");
                    }

                    // Create list of Image Views that will hold sentence words
                    ArrayList<ImageView> imageViews = new ArrayList<>();

                    // Create list of black word resource ids per sentence word
                    ArrayList<Integer> blackWords = new ArrayList<>();

                    // Create list of red word resource ids per sentence word
                    ArrayList<Integer> redWords = new ArrayList<>();

                    // Create list of sound paths that will hold sound paths per sentence word
                    ArrayList<String> soundPaths = new ArrayList<>();

                    // Grab each sentence word
                    // To do so, loop through each sentence
                    for (int j = 0; j < sentence.length(); j++) {
                        // Get the sentence word
                        JSONObject sentenceWord = sentence.getJSONObject(j);

                        // Check if new grids should be created
                        // Note that grids exists per set
                        // Check the current set number using JSON "set_no"
                        final int sentenceSet = sentenceWord.getInt("set_no");
                        if (sentenceSet != currentSet) {

                            // Assign new Image View grid for the new set
                            imageViewGrid = new ArrayList<>();

                            // Assign new black word grid for the new set
                            blackWordGrid = new ArrayList<>();

                            // Assign new red word grid for the new set
                            redWordGrid = new ArrayList<>();

                            // Assign new sound path grid for the new set
                            soundPathGrid = new ArrayList<>();

                            // Update current set
                            currentSet = sentenceSet;
                        }

                        // Get the black word (resource id) of the sentence word
                        int blackWord = sentenceWord.getInt("black_word");

                        // Get the red word (resource id) of the sentence word
                        int redWord = sentenceWord.getInt("red_word");

                        // Add black word to black words list
                        blackWords.add(blackWord);

                        // Add red word to red words list
                        redWords.add(redWord);

                        // Get the image resource id of the sentence word
                        // This is coincidentally the 'black word'
                        int imageResourceId = blackWord;

                        // Create Image View to hold the sentence word
                        ImageView imageView = new ImageView(getApplicationContext());

                        // Set sentence word's image resource to that of the resourceId
                        imageView.setImageResource(imageResourceId);

                        // Set layout params of Image View
                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        imageViewParams.leftMargin = 28; // Some 'padding' between sentence words
                        imageViewParams.rightMargin = 28;
                        imageView.setLayoutParams(imageViewParams);

                        // Add listener to ImageView
                        imageView.setOnClickListener(new TouchWordListener(thisActivity, soundPathGridSets.size(), soundPathGrid.size(), j));

                        // Add Image View to list of Image Views
                        imageViews.add(imageView);

                        // Get the sound of the sentence word
                        String sound = sentenceWord.getString("sound");

                        // Get the sound path
                        String soundPath = FetchResource.sound(getApplicationContext(), sound);

                        // Add sound path to list of sounds paths
                        soundPaths.add(soundPath);

                    }
                    // Add Image Views to Image View Grid
                    if (imageViewGrid != null) {
                        imageViewGrid.add(imageViews);
                    }

                    // Add black words to black word Grid
                    if (blackWordGrid != null) {
                        blackWordGrid.add(blackWords);
                    }

                    // Add red words to red word Grid
                    if (redWordGrid != null) {
                        redWordGrid.add(redWords);
                    }

                    // Add sound paths to sound path grid
                    if (soundPathGrid != null) {
                        soundPathGrid.add(soundPaths);
                    }

                    // Add Image View grid to Image View grid sets
                    if (imageViewGrid != null && imageViewGrid.size() > 0) {
                        imageViewGridSets.add(imageViewGrid);
                    }

                    // Add black word grid to black word grid sets
                    if (blackWordGrid != null && blackWordGrid.size() > 0) {
                        blackWordGridSets.add(blackWordGrid);
                    }

                    // Add red word grid to red word grid sets
                    if (redWordGrid != null && redWordGrid.size() > 0) {
                        redWordGridSets.add(redWordGrid);
                    }

                    // Add sound path grid to sound path grid sets
                    if (soundPathGrid != null && soundPathGrid.size() > 0) {
                        soundPathGridSets.add(soundPathGrid);
                    }

                    // See if the current set's Image Views must be displayed
                    // Do this by checking if the current set is the 'starting set'
                    if (currentSet == startingSetIndex) {

                        // Create new linear layout horizontal row
                        LinearLayout row = new LinearLayout(getApplicationContext());

                        // Custom colour the background (testing purposes)
                        // Even number colouring variations
                        /*if (i % 2 == 0) {
                            row.setBackgroundColor(Color.CYAN);
                        } else {
                            row.setBackgroundColor(Color.MAGENTA);
                        }*/
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        row.setBaselineAligned(true);

                        // Create row layout params
                        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        row.setLayoutParams(rowParams);

                        // Add Image Views to row
                        for (int j = 0; j < imageViews.size(); j++) {
                            row.addView(imageViews.get(j));
                        }

                        // Add row to col
                        col.addView(row);
                    }
                }
            } else {
                // Throw exception. Cannot work with null sentences
                throw new Exception("Sentences are null");
            }
        } catch (Exception ex) {
            // Finish the activity - it's bugged
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.onCreate > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }

        container.addView(col);
    }

    class TouchWordListener implements ImageView.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mSetIndex;
        private int mRowIndex;
        private int mWordIndex;

        public TouchWordListener(SimpleStoryActivity thisActivity, int setIndex, int rowIndex, int wordIndex) {
            mSetIndex = setIndex;
            mRowIndex = rowIndex;
            mWordIndex = wordIndex;
            mThisActivity = thisActivity;

            // Update current Set, Row and Word Indexes for the activity
            mThisActivity.setCurrentSetIndex(setIndex);
            mThisActivity.setCurrentRowIndex(rowIndex);
            mThisActivity.setCurrentWordIndex(wordIndex);
        }

        @Override
        public void onClick(View v) {
            mThisActivity.playSentenceWordSound(mSetIndex, mRowIndex, mWordIndex);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            flipWord(RED_WORD, mSetIndex, mRowIndex, mWordIndex);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            flipWord(BLACK_WORD, mSetIndex, mRowIndex, mWordIndex);

        }
    }

    private void playSentenceWordSound(int setIndex, int rowIndex, int wordIndex) {
        try {
            // Get sound path from sound path grid sets
            String soundPath = soundPathGridSets.get(setIndex).get(rowIndex).get(wordIndex);

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Check if media player is playing
            if (mp.isPlaying() && mWordFlipped) {
                mp.stop();
                flipWord(BLACK_WORD, mCurrentSetIndex, mCurrentRowIndex, mCurrentWordIndex);
            }

            // Media player jazz ♫♪
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new TouchWordListener(thisActivity, setIndex, rowIndex, wordIndex));
            mp.setOnCompletionListener(new TouchWordListener(thisActivity, setIndex, rowIndex, wordIndex));
            mp.prepare();

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.playWord(" + setIndex + ", " + rowIndex + ", " +
                    wordIndex + ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void flipWord(int side, int setIndex, int rowIndex, int wordIndex) {
        try {
            // Initialize image (resource id)
            int image = 0;

            // Get the resource id, based on the side
            // 0: black
            // 1: red
            if (side == BLACK_WORD) {
                image = blackWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mWordFlipped = false;
            } else if (side == RED_WORD) {
                image = redWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mWordFlipped = true;
            }

            // Validate image
            // Simply 'return' if image resource id remains as 0
            if (image == 0) {
                mWordFlipped = false;
                return;
            }

            // Update the corresponding Image View's image resource
            imageViewGridSets.get(setIndex).get(rowIndex).get(wordIndex).setImageResource(image);

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.flipWord(" + side + ", " + setIndex + ", " +
                    rowIndex + ", " + wordIndex + ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void setCurrentSetIndex(int currentSetIndex) {
        mCurrentSetIndex = currentSetIndex;
    }

    public void setCurrentRowIndex(int currentRowIndex) {
        mCurrentRowIndex = currentRowIndex;
    }

    public void setCurrentWordIndex(int currentWordIndex) {
        mCurrentWordIndex = currentWordIndex;
    }

    public void setWordFlipped(boolean wordFlipped) {
        mWordFlipped = wordFlipped;
    }

    public int getCurrentSetIndex() {
        return mCurrentSetIndex;
    }

    public int getCurrentRowIndex() {
        return mCurrentRowIndex;
    }

    public int getCurrentWordIndex() {
        return mCurrentWordIndex;
    }

    public boolean getWordFlipped() {
        return mWordFlipped;
    }

    private void initialiseData(){

        // Debug
        System.out.println(":: SimpleStoryActivity.initialiseData > Debug: METHOD CALLED");

        try {
            drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            sentences = allData.getJSONArray("sentences");
            currentSound = 0;
            currentSentence = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void playReadEachSentenceAfterMother() {

        // Debug
        System.out.println(":: SimpleStoryActivity.playReadEachSentenceAfterMother > Debug: METHOD CALLED");


        try{
            String sound = allData.getString("read_each_sentence_after_mother_sound");
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
                    saySentenceWord();
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

    private void populateAndShowSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateAndShowSentence > Debug: METHOD CALLED");

        try{
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
            populateSentence();
            currentSound = 0;
            sayListenFirst();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void populateSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateSentence > Debug: METHOD CALLED");

        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence);
            container.removeAllViews();
            container.setVisibility(View.VISIBLE);
            LinearLayout line = getLine();
            ImageView item;
            sounds = new ArrayList<>();
            sentenceViews = new ArrayList<>();
            int width = 0;
            for (int i = 0; i < sentence.length();i++) {
                JSONObject word = sentence.getJSONObject(i);
                item = new ImageView(this);
                item.setTag(word.getString("sound"));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playSound(view);
                    }
                });
                if (i > 0)
                    item.setPadding(50,10,0,0);
                item.setMaxHeight(143);
                item.setImageResource(word.getInt("black_word"));
                width += ImageHelper.getLength(word.getInt("black_word"),this);
                if (width > 1150){
                    container.addView(line);
                    line = getLine();
                    width = 0;
                }
                line.addView(item);
                sentenceViews.add(item);
                sounds.add(new String(word.getString("sound")));
            }
            container.addView(line);
            currentSound = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayListenFirst() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayListenFirst > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("listen_first_sound");
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
                    saySentenceWord();
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

    private void saySentenceWord() {

        // Debug
        System.out.println(":: SimpleStoryActivity.saySentenceWord > Debug: METHOD CALLED");

        try{
            if (currentSound < sounds.size()) {
                turnWord("red_word");
                String sound = sounds.get(currentSound);
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
                        turnWord("black_word");
                        currentSound++;
                        saySentenceWord();
                    }
                });
                mp.prepare();
            }
            else{
                sayItsYourTurn();
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

    private void sayItsYourTurn() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayItsYourTurn > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("now_read_sound");
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
                    currentSound = -1;
                    //currentSentence = 1;
                    handler.postDelayed(showSentenceWithoutSound,100);
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

    private void showSentenceNoSound() {

        // Debug
        System.out.println(":: SimpleStoryActivity.showSentenceNoSound > Debug: METHOD CALLED");

        try{
            if (currentSound < sounds.size()) {
                currentSound++;
                if (currentSound < sounds.size())
                    turnWord("red_word");
                handler.postDelayed(showSentenceWithoutSound,1500);
            }
            else{
                handler.postDelayed(finishListening,100);
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

    Runnable showSentenceWithoutSound = new Runnable()
    {
        @Override
        public void run() {

            // Debug
            System.out.println(":: SimpleStoryActivity.showSentenceWithoutSound(Runnable) > Debug: METHOD CALLED");

            try {
                if (currentSound > -1 && currentSound < sounds.size())
                    turnWord("black_word");
                showSentenceNoSound();
            }
            catch(Exception ex){
                ex.printStackTrace();
                if (mp != null){
                    mp.release();
                }
                finish();
            }
        }
    };

    Runnable finishListening = new Runnable()
    {
        @Override
        public void run() {

            // Debug
            System.out.println(":: SimpleStoryActivity.finishListening(Runnable) > Debug: METHOD CALLED");

            currentSentence++;
            if (currentSentence < sentences.length() ){
                populateAndShowSentence();
            }
            else{
                listenToTheWholeStory();
            }
        }
    };

    public void listenToTheWholeStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.listenToTheWholeStory > Debug: METHOD CALLED");

        try {
            String sound = allData.getString("listen_to_the_whole_story");
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
                    readWholeStory();
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

    public void readWholeStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.readWholeStory > Debug: METHOD CALLED");

        try{
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            container.setVisibility(View.INVISIBLE);
            String sound = allData.getString("full_story_sound");
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
                    readWholeStoryInstructions();
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

    public void readWholeStoryInstructions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.readWholeStoryInstructions > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            String sound = allData.getString("now_read_whole_story_sound");
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
                    touchArrowInstructions();
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

    public void touchArrowInstructions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.touchArrowInstructions > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.VISIBLE);
            container.removeAllViews();
            String sound = allData.getString("touch_the_arrow");
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
                    startStory();
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

    public void startStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.startStory > Debug: METHOD CALLED");

        currentSentence = 0;
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);
        nextButton.setVisibility(View.VISIBLE);
        nextSentence();
    }

    public void nextSentence(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nextSentence > Debug: METHOD CALLED");

        currentSentence++;
        if (currentSentence <  sentences.length())
            populateSentence();
        else
            wellDone();
    }

    public void wellDone(){

        // Debug
        System.out.println(":: SimpleStoryActivity.wellDone > Debug: METHOD CALLED");

        try {
            container.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
            int image = allData.getInt("story_image");
            getWindow().getDecorView().getRootView().setBackgroundResource(image);
            String sound = allData.getString("well_done_you_can_read_sound");
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
                    nowAnswerQuestions();
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

    public void nowAnswerQuestions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nowAnswerQuestions > Debug: METHOD CALLED");

        try {
            String sound = allData.getString("now_answer_sound");
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
                    doComprehension();
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

    private void doComprehension(){

        // Debug
        System.out.println(":: SimpleStoryActivity.doComprehension > Debug: METHOD CALLED");

        try {
            getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.backgound_comprehension);
            currentQuestion = 1;
            questions = allData.getJSONArray("questions");
            nextQuestion();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void nextQuestion(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nextQuestion > Debug: METHOD CALLED");

        try{

            if (currentQuestion < questions.length()){
                JSONObject question = questions.getJSONObject(currentQuestion);
                if (question.getInt("is_touch") == 1){ //Three objects
                    singleImage.setVisibility(View.INVISIBLE);
                    tripleImageThree.setVisibility(View.VISIBLE);
                    trippleImageOne.setVisibility(View.VISIBLE);
                    trippleImageTwo.setVisibility(View.VISIBLE);
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(0).getInt("image"));
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(1).getInt("image"));
                    trippleImageOne.setImageResource(question.getJSONArray("images").getJSONObject(2).getInt("image"));
                }
                else{
                    singleImage.setVisibility(View.VISIBLE);
                    tripleImageThree.setVisibility(View.INVISIBLE);
                    trippleImageOne.setVisibility(View.INVISIBLE);
                    trippleImageTwo.setVisibility(View.INVISIBLE);
                    singleImage.setImageResource(question.getJSONArray("images").getJSONObject(0).getInt("image"));

                }
                String sound = question.getString("question_sound");
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
                        try {
                            mp.reset();
                            JSONObject question = questions.getJSONObject(currentQuestion);
                            if (question.getInt("is_touch") == 0) { //Three objects
                                handler.postDelayed(plaSingleImageAnswerRunnable, 3000);
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                            finish();
                        }
                    }
                });
                mp.prepare();
            }
            else{
                if (mp != null){
                    mp.release();
                }
                finish();
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

    public Runnable plaSingleImageAnswerRunnable = new Runnable(){
        public void run(){

            // Debug
            System.out.println(":: SimpleStoryActivity.plaSingleImageAnswerRunnable(Runnable).run > Debug: METHOD CALLED");

            playSingleImageAnswer();
        }
    };

    private void playSingleImageAnswer() {

        // Debug
        System.out.println(":: SimpleStoryActivity.playSingleImageAnswer > Debug: METHOD CALLED");

        try{
            JSONObject question = questions.getJSONObject(currentQuestion);
            singleImage.setImageResource(question.getJSONArray("images").getJSONObject(0).getInt("image"));
            String sound = question.getString("answer_sound");
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
                    currentQuestion++;
                    nextQuestion();
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

    private LinearLayout getLine(){

        // Debug
        System.out.println(":: SimpleStoryActivity.getLine > Debug: METHOD CALLED");

        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        return line;
    }

    public void playSound(View v){

        // Debug
        System.out.println(":: SimpleStoryActivity.playSound > Debug: METHOD CALLED");

        try{
            String sound = (String) v.getTag();
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

    private void turnWord(String turnString){

        // Debug
        System.out.println(":: SimpleStoryActivity.turnWord > Debug: METHOD CALLED");

        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence);
            JSONObject word = sentence.getJSONObject(currentSound);
            ImageView image = sentenceViews.get(currentSound);
            int picture = word.getInt(turnString);
            if (picture > 0)
                image.setImageResource(picture);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    //
    public void populateFullStory(){

        // Debug
        System.out.println(":: SimpleStoryActivity.populateFullStory > Debug: METHOD CALLED");

        try {
            int lines = 0;
            boolean done = false;
            container.removeAllViews();
            container.setVisibility(View.VISIBLE);
            while (!done) {
                JSONArray sentence = sentences.getJSONArray(currentSentence);
                LinearLayout line = getLine();
                ImageView item;
                int width = 0;
                for (int i = 0; i < sentence.length(); i++) {
                    JSONObject word = sentence.getJSONObject(i);
                    item = new ImageView(this);
                    item.setTag(word.getString("sound"));
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            playSound(view);
                        }
                    });
                    if (i > 0)
                        item.setPadding(50, 10, 0, 0);
                    item.setMaxHeight(143);
                    item.setImageResource(word.getInt("black_word"));
                    width += ImageHelper.getLength(word.getInt("black_word"), this);
                    if (width > 1150) {
                        lines ++;
                        container.addView(line);
                        line = getLine();
                        width = 0;
                    }
                    line.addView(item);
                }
                lines++;
                currentSentence++;
                if ((lines > 5) || (currentSentence > sentences.length()))
                    done = true;
                container.addView(line);
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

    private  void imageClicked(int image){

        // Debug
        System.out.println(":: SimpleStoryActivity.imageClicked > Debug: METHOD CALLED");

        try {
            JSONObject question = questions.getJSONObject(currentQuestion);
            if (question.getJSONArray("images").getJSONObject(image).getInt("is_right") == 1){
                playSound(ResourceSelector.getPositiveAffirmationSound(this));
                currentQuestion++;
                nextQuestion();
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(this));
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

    public void playSound(int sound){

        // Debug
        System.out.println(":: SimpleStoryActivity.playSound > Debug: METHOD CALLED");

        try{
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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

    @Override
    public void onPause(){
        super.onPause();

        // Debug
        System.out.println(":: SimpleStoryActivity.onPause > Debug: METHOD CALLED");

        /*if (mp != null){
            mp.release();
        }*/
    }
}
