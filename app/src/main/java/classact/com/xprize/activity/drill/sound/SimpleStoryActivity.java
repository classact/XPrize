package classact.com.xprize.activity.drill.sound;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
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

import java.lang.reflect.Array;
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

    private RelativeLayout rootView;

    private LinearLayout col;

    private ImageView subBackgroundView;

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

    // Comprehension stuff
    private ArrayList<ArrayList<ImageView>> mComprehensionQuestionImageViewSets;
    private ArrayList<ArrayList<Boolean>> mComprehensionQuestionAnswerSets;
    private ArrayList<String> mComprehensionQuestionSoundPaths;
    private ArrayList<String> mComprehensionAnswerSoundPaths;
    private ArrayList<Boolean> mComprehensionAnswerTypes;

    private final int STARTING_SET = 0;
    private final int BLACK_WORD = 0;
    private final int RED_WORD = 1;

    private boolean touchWordsEnabled;
    private int mSelectedSetIndex;
    private int mSelectedRowIndex;
    private int mSelectedWordIndex;
    private boolean mSelectedWordFlipped;

    private int mActiveSetIndex;
    private int mActiveRowIndex;
    private int mActiveWordIndex;
    private boolean mActiveWordFlipped;
    private int mActiveWordDuration;

    private ImageView mNextArrow;
    private ImageView mPreviousArrow;

    private int mComprehensionQuestionIndex;
    private boolean mComprehensionTouchEnabled;

    private final int STATE_0 = 0;
    private final int STATE_1 = 1;
    private final int STATE_2 = 2;
    private final int STATE_3 = 3;
    private int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_story);

        // Set background to background-simple-story (until I change it in the layout file)
        getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.background_simple_story);

        rootView = (RelativeLayout) findViewById(R.id.activity_simple_story);

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
        /*initializeData();
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
        initData();

        // Initialize story and corresponding objects and views
        // Populates 'col;
        initStory();

        // Initialize comprehension and corresponding objects and views
        initComprehension();

        container.addView(col);

        currentState = STATE_0;

        //playPrompt("read_each_sentence_after_mother_sound");
        playPrompt("now_answer_sound");
    }

    class PromptListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private String mPrompt;

        public PromptListener(SimpleStoryActivity thisActivity, String prompt) {
            mPrompt = prompt;
            mThisActivity = thisActivity;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            // Reset volume to max (in case it was muted before)
            mp.setVolume(1, 1);

            try {
            switch (mPrompt) {
                case "read_each_sentence_after_mother_sound": {
                    break;
                }
                case "listen_first_sound": {
                    break;
                }
                case "now_read_sound": {
                    break;
                }
                case "listen_to_the_whole_story": {
                    break;
                }
                case "full_story_sound": {
                    break;
                }
                case "now_read_whole_story_sound": {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            // Reset set, row and active indexes
                            mThisActivity.setActiveSetIndex(0);
                            mThisActivity.setActiveRowIndex(0);
                            mThisActivity.setActiveWordIndex(0);

                            // Clear all story set views
                            mThisActivity.clearAllStorySetViews();

                            // Get Image Views for first row of new set
                            mThisActivity.showFullStorySet(0);

                        }
                    }, mp.getDuration() / 2);

                    break;
                }
                case "touch_the_arrow": {
                    break;
                }
                case "well_done_you_can_read_sound": {
                    break;
                }
                case "now_answer_sound": {

                    Fade fadeIn = new Fade(Fade.IN);
                    Fade fadeOut = new Fade(Fade.OUT);

                    subBackgroundView = new ImageView(getApplicationContext());
                    subBackgroundView.setBackgroundResource(R.drawable.backgound_comprehension);
                    LinearLayout.LayoutParams storyBackgroundViewParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    subBackgroundView.setLayoutParams(storyBackgroundViewParams);

                    TransitionManager.beginDelayedTransition(col, fadeOut);
                    TransitionManager.beginDelayedTransition(rootView, fadeIn);

                    rootView.addView(subBackgroundView, 0);
                    clearAllStorySetViews();

                    break;
                }
                case "comprehension_question_sound": {
                    break;
                }
                case "comprehension_instructions_sound": {
                    break;
                }
                default: {
                    break;
                }
            }

            // Play da beatz ♫♪
            mp.start();
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.PromptListener(class).onPrepared()." + mPrompt +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            try {
                switch (mPrompt) {
                    case "read_each_sentence_after_mother_sound": {
                        // Disable narration on touch
                        touchWordsEnabled = false;

                        // Reset the active word
                        mActiveSetIndex = 0;
                        mActiveRowIndex = 0;
                        mActiveWordIndex = 0;
                        mActiveWordFlipped = false;

                        // Play prompt
                        playPrompt("listen_first_sound");
                        break;
                    }
                    case "listen_first_sound": {
                        // Play the current active word (unmuted)
                        playActiveWord(false, mActiveSetIndex, mActiveRowIndex, mActiveWordIndex);
                        break;
                    }
                    case "now_read_sound": {
                        // Play the current active word (muted)
                        playActiveWord(true, mActiveSetIndex, mActiveRowIndex, mActiveWordIndex);
                        break;
                    }
                    case "listen_to_the_whole_story": {
                        Fade fadeIn = new Fade(Fade.IN);
                        Fade fadeOut = new Fade(Fade.OUT);

                        subBackgroundView = new ImageView(getApplicationContext());
                        subBackgroundView.setBackgroundResource(allData.getInt("story_image"));
                        LinearLayout.LayoutParams storyBackgroundViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        subBackgroundView.setLayoutParams(storyBackgroundViewParams);

                        TransitionManager.beginDelayedTransition(col, fadeOut);
                        TransitionManager.beginDelayedTransition(rootView, fadeIn);

                        rootView.addView(subBackgroundView, rootView.getChildCount());
                        clearAllStorySetViews();

                        // Play prompt
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playPrompt("full_story_sound");
                            }
                        }, 1200);

                        break;
                    }
                    case "full_story_sound": {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Fade fadeOut = new Fade(Fade.OUT);

                                // Reset the active word
                                mThisActivity.setActiveSetIndex(0);
                                mThisActivity.setActiveRowIndex(0);
                                mThisActivity.setActiveWordIndex(0);
                                mThisActivity.setActiveWordFlipped(false);

                                // Clear all story set views
                                mThisActivity.clearAllStorySetViews();

                                // Get Image Views for first row of new set
                                mThisActivity.showFullStorySet(0);

                                if (subBackgroundView != null) {
                                    TransitionManager.beginDelayedTransition(rootView, fadeOut);
                                    rootView.removeView(subBackgroundView);
                                    subBackgroundView = null;
                                }

                                // Play prompt
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        playPrompt("now_read_whole_story_sound");
                                    }
                                }, 850);
                            }
                        }, 500);

                        break;
                    }
                    case "now_read_whole_story_sound": {

                        // After a short delay, introduce arrows
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mNextArrow.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        playPrompt("touch_the_arrow");
                                    }
                                }, 100);
                            }
                        }, 300);
                        break;
                    }
                    case "touch_the_arrow": {
                        // Enable narration on touch
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                touchWordsEnabled = true;
                            }
                        }, 250);

                        break;
                    }
                    case "well_done_you_can_read_sound": {

                        //Play prompt
                        playPrompt("now_answer_sound");

                        break;
                    }
                    case "now_answer_sound": {

                        // Reset comprehension question set index
                        mThisActivity.setComprehensionQuestionIndex(0);

                        // Prepare views and ask next comprehension question
                        prepareViewsAndAskNextComprehensionQuestion();

                        break;
                    }
                    case "comprehension_question_sound": {
                        break;
                    }
                    case "comprehension_instructions_sound": {
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.PromptListener(class).onCompletion()." + mPrompt +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    private void playPrompt(String prompt) {

        // Debug
        System.out.println(":: SimpleStoryActivity.playPrompt(\"" + prompt + "\") > Debug: METHOD CALLED");

        try{
            String sound = allData.getString(prompt);
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new PromptListener(thisActivity, prompt));
            mp.setOnCompletionListener(new PromptListener(thisActivity, prompt));
            mp.prepare();
        }
        catch (Exception ex){
            System.err.println("============================================================");
            System.out.println(":: SimpleStoryActivity.playPrompt(\"" + prompt + "\") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    class ActiveWordListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mSetIndex;
        private int mRowIndex;
        private int mWordIndex;
        private boolean mMute;

        private int mMaxSets;
        private int mMaxRows;
        private int mMaxWords;

        private final int MIN_DURATION = 1100;

        public ActiveWordListener(SimpleStoryActivity thisActivity, boolean mute, int setIndex, int rowIndex, int wordIndex) {
            mThisActivity = thisActivity;
            mMute = mute;
            mSetIndex = setIndex;
            mRowIndex = rowIndex;
            mWordIndex = wordIndex;

            // Update selected Set, Row and Word Indexes for the activity
            mThisActivity.setActiveSetIndex(setIndex);
            mThisActivity.setActiveRowIndex(rowIndex);
            mThisActivity.setActiveWordIndex(wordIndex);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            // Mute media player if required
            if (mMute) {
                mp.setVolume(0, 0);
            } else {
                mp.setVolume(1, 1);
            }

            // Store duration
            int duration = mp.getDuration();
            mThisActivity.setActiveWordDuration(duration);

            System.out.println(">> Duration is: " + duration);

            // Start playing and flip word
            mp.start();
            flipActiveWord(RED_WORD, mSetIndex, mRowIndex, mWordIndex);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                mp.reset();
                flipActiveWord(BLACK_WORD, mSetIndex, mRowIndex, mWordIndex);

                // Get duration of active word
                int duration = mThisActivity.getActiveWordDuration();

                // See if word sound is 'too short for comfort'
                if (duration > 0 && duration < MIN_DURATION) {

                    // Word sound is too short

                    System.out.println(">> Slow it down!!");

                    // Wait the remainder until minimum duration is reached
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handleOnCompletion();
                        }
                    }, MIN_DURATION - duration);

                } else {
                    handleOnCompletion();
                }

            } catch (Exception ex){
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.onCompletion() > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        public void handleOnCompletion() {
            try {

                mMaxSets = mThisActivity.getSoundPathGridSets().size();
                mMaxRows = mThisActivity.getSoundPathGridSets().get(mSetIndex).size();
                mMaxWords = mThisActivity.getSoundPathGridSets().get(mSetIndex).get(mRowIndex).size();

                // Check the position of the active word
                if (mSetIndex == mMaxSets - 1 && mRowIndex == mMaxRows -1 && mWordIndex == mMaxWords - 1) {

                    // Is the very last word of the story

                    // Check if mute
                    if (mMute) {

                        // Muted. Child has finished reading the word
                        // Child has also finished reading the entire story with the narrator
                        // The child must now read the story by him or herself

                        // Play prompt
                        playPrompt("listen_to_the_whole_story");

                    } else {

                        // Un-muted. Narrator has finished reading the word
                        // Child must now read the row again

                        // Reset active word index
                        mActiveWordIndex = 0;

                        // Play prompt
                        playPrompt("now_read_sound");

                    }

                } else if (mRowIndex == mMaxRows - 1 && mWordIndex == mMaxWords - 1) {

                    // Is the very last word of the set

                    // Check if mute
                    if (mMute) {

                        // Muted. Child has finished reading the word
                        // Narrator must now read from first word of next set

                        // Clear all existing rows of child views
                        for (int i = 0; i < col.getChildCount(); i++) {

                            // Get row
                            LinearLayout row = (LinearLayout) col.getChildAt(i);

                            // Remove all views from row
                            row.removeAllViews();
                        }

                        // Clear column of all child views
                        col.removeAllViews();

                        // Reset active set index, active row index, and active word index
                        mActiveSetIndex = mSetIndex + 1;
                        mActiveRowIndex = 0;
                        mActiveWordIndex = 0;

                        // Add new row (sentence) to story set
                        mThisActivity.addSentenceToStorySet(mActiveSetIndex, mActiveRowIndex);

                        // Play prompt
                        playPrompt("listen_first_sound");

                    } else {

                        // Un-muted. Narrator has finished reading the word
                        // Child must now read the row again

                        // Reset active word index
                        mActiveWordIndex = 0;

                        // Play prompt
                        playPrompt("now_read_sound");

                    }

                } else if (mWordIndex == mMaxWords - 1) {

                    // Is the very last word of the row

                    // Check if mute
                    if (mMute) {

                        // Muted. Child has finished reading the word
                        // Narrator must now read from first word of next row

                        // Reset active row index and active word index
                        mActiveRowIndex = mRowIndex + 1;
                        mActiveWordIndex = 0;

                        // Add new row (sentence) to story set
                        mThisActivity.addSentenceToStorySet(mActiveSetIndex, mActiveRowIndex);

                        // Play prompt
                        playPrompt("listen_first_sound");

                    } else {

                        // Un-muted. Narrator has finished reading the word
                        // Child must now read the row again

                        // Reset active word index
                        mActiveWordIndex = 0;

                        // Play prompt
                        playPrompt("now_read_sound");
                    }

                } else {

                    // It's any other word, play it! ~ ♬♪

                    // Play the word
                    playActiveWord(mMute, mSetIndex, mRowIndex, mWordIndex + 1);
                }
            } catch (Exception ex){
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.handleOnCompletion() > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    private void playActiveWord(boolean mute, int setIndex, int rowIndex, int wordIndex) {

        // Debug
        System.out.println(":: SimpleStoryActivity.playActiveWord(" + mute + ", " + setIndex +
                ", " + rowIndex + ", " + wordIndex + ") > Debug: METHOD CALLED");

        try {
            // Get sound path from sound path grid sets
            String soundPath = soundPathGridSets.get(setIndex).get(rowIndex).get(wordIndex);

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Check if media player is playing
            if (mp.isPlaying() && mActiveWordFlipped) {
                mp.stop();
                flipActiveWord(BLACK_WORD, mActiveSetIndex, mActiveRowIndex, mActiveWordIndex);
            }

            // Media player jazz ♫♪
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new ActiveWordListener(thisActivity, mute, setIndex, rowIndex, wordIndex));
            mp.setOnCompletionListener(new ActiveWordListener(thisActivity, mute, setIndex, rowIndex, wordIndex));
            mp.prepare();

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.playActiveWord(" + setIndex + ", " + rowIndex + ", " +
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

    private void flipActiveWord(int side, int setIndex, int rowIndex, int wordIndex) {
        try {
            // Initialize image (resource id)
            int image = 0;

            // Get the resource id, based on the side
            // 0: black
            // 1: red
            if (side == BLACK_WORD) {
                image = blackWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mActiveWordFlipped = false;
            } else if (side == RED_WORD) {
                image = redWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mActiveWordFlipped = true;
            }

            // Validate image
            // Simply 'return' if image resource id remains as 0
            if (image == 0) {
                mActiveWordFlipped = false;
                return;
            }

            // Update the corresponding Image View's image resource
            imageViewGridSets.get(setIndex).get(rowIndex).get(wordIndex).setImageResource(image);

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.flipActiveWord(" + side + ", " + setIndex + ", " +
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

    class SelectedWordListener implements ImageView.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mSetIndex;
        private int mRowIndex;
        private int mWordIndex;

        public SelectedWordListener(SimpleStoryActivity thisActivity, int setIndex, int rowIndex, int wordIndex) {
            mSetIndex = setIndex;
            mRowIndex = rowIndex;
            mWordIndex = wordIndex;
            mThisActivity = thisActivity;

            // Update selected Set, Row and Word Indexes for the activity
            mThisActivity.setSelectedSetIndex(setIndex);
            mThisActivity.setSelectedRowIndex(rowIndex);
            mThisActivity.setSelectedWordIndex(wordIndex);
        }

        @Override
        public void onClick(View v) {
            mThisActivity.playSelectedWord(mSetIndex, mRowIndex, mWordIndex);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            flipSelectedWord(RED_WORD, mSetIndex, mRowIndex, mWordIndex);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            flipSelectedWord(BLACK_WORD, mSetIndex, mRowIndex, mWordIndex);

        }
    }

    private void playSelectedWord(int setIndex, int rowIndex, int wordIndex) {

        // Debug
        System.out.println(":: SimpleStoryActivity.playSelectedWord(" + setIndex +
                ", " + rowIndex + ", " + wordIndex + ") > Debug: METHOD CALLED");

        // Proceed with logic only when touch words (narration via touch)
        // are enabled
        if (touchWordsEnabled) {

            // Debug
            System.out.println(":: SimpleStoryActivity.playSelectedWord(" + setIndex +
                    ", " + rowIndex + ", " + wordIndex + ") > Debug: Touch words enabled");

            try {
                // Get sound path from sound path grid sets
                String soundPath = soundPathGridSets.get(setIndex).get(rowIndex).get(wordIndex);

                // Initialize media player if need be
                if (mp == null) {
                    mp = new MediaPlayer();
                }

                // Check if media player is playing
                if (mp.isPlaying() && mSelectedWordFlipped) {
                    mp.stop();
                    flipSelectedWord(BLACK_WORD, mSelectedSetIndex, mSelectedRowIndex, mSelectedWordIndex);
                }

                // Media player jazz ♫♪
                mp.reset();
                mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
                mp.setOnPreparedListener(new SelectedWordListener(thisActivity, setIndex, rowIndex, wordIndex));
                mp.setOnCompletionListener(new SelectedWordListener(thisActivity, setIndex, rowIndex, wordIndex));
                mp.prepare();

            } catch (Exception ex) {
                System.err.println("============================================================");
                System.err.println("SimpleStoryActivity.playSelectedWord(" + setIndex + ", " + rowIndex + ", " +
                        wordIndex + ") > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }

        } else {

            // Debug
            System.out.println(":: SimpleStoryActivity.playSelectedWord(" + setIndex +
                    ", " + rowIndex + ", " + wordIndex + ") > Debug: Touch words disabled");
        }
    }

    private void flipSelectedWord(int side, int setIndex, int rowIndex, int wordIndex) {
        try {
            // Initialize image (resource id)
            int image = 0;

            // Get the resource id, based on the side
            // 0: black
            // 1: red
            if (side == BLACK_WORD) {
                image = blackWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mSelectedWordFlipped = false;
            } else if (side == RED_WORD) {
                image = redWordGridSets.get(setIndex).get(rowIndex).get(wordIndex);
                mSelectedWordFlipped = true;
            }

            // Validate image
            // Simply 'return' if image resource id remains as 0
            if (image == 0) {
                mSelectedWordFlipped = false;
                return;
            }

            // Update the corresponding Image View's image resource
            imageViewGridSets.get(setIndex).get(rowIndex).get(wordIndex).setImageResource(image);

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.flipSelectedWord(" + side + ", " + setIndex + ", " +
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

    class StoryNavListener implements ImageView.OnClickListener {

        private boolean mToNextPage;
        private int mSetIndex;
        private int mRowIndex;
        private int mWordIndex;
        private int mMaxSetIndex;
        private SimpleStoryActivity mThisActivity;

        public StoryNavListener(SimpleStoryActivity thisActivity, boolean toNextPage) {
            mThisActivity = thisActivity;
            mToNextPage = toNextPage;
        }

        @Override
        public void onClick(View v) {

            if (touchWordsEnabled) {

                // Get data from activity
                mSetIndex = mThisActivity.getActiveSetIndex();
                mRowIndex = mThisActivity.getActiveRowIndex();
                mWordIndex = mThisActivity.getActiveWordIndex();
                mMaxSetIndex = mThisActivity.getImageViewGridSets().size() - 1;

                // Check if we should navigate to next | previous page
                if (mToNextPage) {

                    // Navigate to next page

                    // Increment the active set index
                    mSetIndex++;

                    // If last page, can't go any further
                    if (mSetIndex >= mMaxSetIndex) {

                        // Reset active set index to max possible
                        // (in case it's greater than max set index for whatever reason)
                        mSetIndex = mMaxSetIndex;

                        // Update the active set index in the activity
                        mThisActivity.setActiveSetIndex(mSetIndex);

                        // Change image resource
                        // Or in this case, 'brighten' arrow to indicate a unique transition
                        mNextArrow.setColorFilter(Color.argb(125, 0, 255, 0));

                        // Set layout params
                        RelativeLayout.LayoutParams nextArrowParams = (RelativeLayout.LayoutParams) mNextArrow.getLayoutParams();
                        nextArrowParams.topMargin = 140;
                        nextArrowParams.rightMargin = 210;
                        mNextArrow.setLayoutParams(nextArrowParams);

                        // Add a new listener
                        mNextArrow.setOnClickListener(null);
                        mNextArrow.setOnClickListener(new ImageView.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Disable touch words
                                touchWordsEnabled = false;

                                // Hide arrow
                                mNextArrow.setVisibility(View.INVISIBLE);
                                mPreviousArrow.setVisibility(View.INVISIBLE);

                                // Play prompt
                                playPrompt("well_done_you_can_read_sound");
                            }
                        });

                    } else {

                        // Update the active set index in the activity
                        mThisActivity.setActiveSetIndex(mSetIndex);

                        // Ensure that previous page arrow is visible
                        mPreviousArrow.setVisibility(View.VISIBLE);

                        // Reset colour of next arrow
                        mNextArrow.setColorFilter(null);

                        // Reset listener for next arrow
                        mNextArrow.setOnClickListener(null);
                        mNextArrow.setOnClickListener(new StoryNavListener(mThisActivity, true));

                        // Reset layout params
                        RelativeLayout.LayoutParams nextArrowParams = (RelativeLayout.LayoutParams) mNextArrow.getLayoutParams();
                        nextArrowParams.topMargin = 1200;
                        nextArrowParams.rightMargin = 210;
                        mNextArrow.setLayoutParams(nextArrowParams);
                    }

                } else {

                    // Navigate to previous page

                    // Decrement the active set index
                    mSetIndex--;

                    // If page 0, can't go any backward
                    if (mSetIndex <= 0) {

                        // Reset active set index to min possible
                        // (in case it's less than 0 for some odd inexplainable reason)
                        mSetIndex = 0;

                        // Update the active set index in the activity
                        mThisActivity.setActiveSetIndex(mSetIndex);

                        // Ensure that previous page arrow is invisible
                        mPreviousArrow.setVisibility(View.INVISIBLE);

                        // Reset colour of next arrow
                        mNextArrow.setColorFilter(null);

                        // Reset listener for next arrow
                        mNextArrow.setOnClickListener(null);
                        mNextArrow.setOnClickListener(new StoryNavListener(mThisActivity, true));

                        // Reset layout params
                        RelativeLayout.LayoutParams nextArrowParams = (RelativeLayout.LayoutParams) mNextArrow.getLayoutParams();
                        nextArrowParams.topMargin = 1200;
                        nextArrowParams.rightMargin = 210;
                        mNextArrow.setLayoutParams(nextArrowParams);

                    } else {

                        // Update the active set index in the activity
                        mThisActivity.setActiveSetIndex(mSetIndex);

                        // Ensure that next page arrow is visible
                        mNextArrow.setVisibility(View.VISIBLE);

                        // Reset colour of next arrow
                        mNextArrow.setColorFilter(null);

                        // Reset listener for next arrow
                        mNextArrow.setOnClickListener(null);
                        mNextArrow.setOnClickListener(new StoryNavListener(mThisActivity, true));

                        // Reset layout params
                        RelativeLayout.LayoutParams nextArrowParams = (RelativeLayout.LayoutParams) mNextArrow.getLayoutParams();
                        nextArrowParams.topMargin = 1200;
                        nextArrowParams.rightMargin = 210;
                        mNextArrow.setLayoutParams(nextArrowParams);
                    }
                }

                // Navigate to next or previous page, based on above logic

                // Reset active row index, and active word index
                // Active set index has already been reset
                mRowIndex = 0;
                mWordIndex = 0;

                // Update activity's active row and word indexes
                mThisActivity.setActiveRowIndex(mRowIndex);
                mThisActivity.setActiveWordIndex(mWordIndex);

                // Clear all story set views
                mThisActivity.clearAllStorySetViews();

                // Get Image Views for first row of new set
                mThisActivity.showFullStorySet(mSetIndex);
            }
        }
    }

    class ComprehensionQuestionListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mQuestionIndex;

        public ComprehensionQuestionListener(SimpleStoryActivity thisActivity, int questionIndex) {
            mQuestionIndex = questionIndex;
            mThisActivity = thisActivity;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            // Reset volume to max (in case it was muted before)
            mp.setVolume(1, 1);

            try {
                // Play da beatz ♫♪
                mp.start();

            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.ComprehensionQuestionListener(class).onPrepared()." + mQuestionIndex +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            try {

                // Get the answer type
                boolean isTouchTypeQuestion = mThisActivity.getComprehensionAnswerTypes().get(mQuestionIndex);

                // Logic depending on answer type
                if (isTouchTypeQuestion) {

                    // Touch based answer required

                    // Enable touch
                    mThisActivity.setComprehensionTouchEnabled(true);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mThisActivity.playComprehensionAnswer(mThisActivity.getComprehensionQuestionIndex());
                        }
                    }, 1000);

                } else {

                    // Microphone based answer required

                    // Disable touch
                    mThisActivity.setComprehensionTouchEnabled(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mThisActivity.playComprehensionAnswer(mThisActivity.getComprehensionQuestionIndex());
                        }
                    }, 1000);

                }

            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.ComprehensionQuestionListener(class).onCompletion()." + mQuestionIndex +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    public void playComprehensionQuestion(int questionIndex) {
        try {
            // Get sound path from sound path grid sets
            String soundPath = mComprehensionQuestionSoundPaths.get(questionIndex);

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Media player jazz ♫♪
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new ComprehensionQuestionListener(thisActivity, questionIndex));
            mp.setOnCompletionListener(new ComprehensionQuestionListener(thisActivity, questionIndex));
            mp.prepare();

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.playComprehensionQuestion(" + questionIndex +
                    ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    class ComprehensionAnswerListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mAnswerIndex;

        public ComprehensionAnswerListener(SimpleStoryActivity thisActivity, int answerIndex) {
            mThisActivity = thisActivity;
            mAnswerIndex = answerIndex;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            // Reset volume to max (in case it was muted before)
            mp.setVolume(1, 1);

            try {

                // Disable touch
                mThisActivity.setComprehensionTouchEnabled(false);

                // Play da beatz ♫♪
                mp.start();
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.ComprehensionAnswerListener(class).onPrepared()." + mAnswerIndex +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            try {

                int maxQuestions = mThisActivity.getComprehensionQuestionSoundPaths().size();
                int nextQuestionIndex = mAnswerIndex + 1;

                if (nextQuestionIndex >= maxQuestions) {

                    // Time to end it

                } else {

                    // On to next question
                    mThisActivity.setComprehensionQuestionIndex(nextQuestionIndex);

                    // Clear views for comprehension
                    clearViewsForComprehension();

                    // Play question after 500 millisecond delay
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            // Prepare views and ask next comprehension question
                            prepareViewsAndAskNextComprehensionQuestion();
                        }
                    }, 700);
                }

            } catch (Exception ex) {
                System.err.println("============================================================");
                System.out.println(":: SimpleStoryActivity.ComprehensionAnswerListener(class).onCompletion()." + mAnswerIndex +
                        " > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    public void playComprehensionAnswer(int answerIndex) {
        try {
            // Get sound path from sound path grid sets
            String soundPath = mComprehensionAnswerSoundPaths.get(answerIndex);

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Media player jazz ♫♪
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new ComprehensionAnswerListener(thisActivity, answerIndex));
            mp.setOnCompletionListener(new ComprehensionAnswerListener(thisActivity, answerIndex));
            mp.prepare();

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.playComprehensionAnswer(" + answerIndex +
                    ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    class ComprehensionTouchListener implements ImageView.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mQuestionIndex;

        public ComprehensionTouchListener(SimpleStoryActivity thisActivity, int questionIndex) {
            mThisActivity = thisActivity;
            mQuestionIndex = questionIndex;

            // Update comprehension question index for the activity
            mThisActivity.setComprehensionQuestionIndex(questionIndex);
        }

        @Override
        public void onClick(View v) {
            try {
                if (mThisActivity.getComprehensionTouchEnabled()) {
                    System.out.println(":><: Fuzzy " + mQuestionIndex);
                }
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.err.println("SimpleStoryActivity.ComprehensionTouchListener(Listener).onClick." + mQuestionIndex +
                        ") > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            try {
                mp.start();
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.err.println("SimpleStoryActivity.ComprehensionTouchListener(Listener).onPrepared." + mQuestionIndex +
                        ") > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                mp.reset();
            } catch (Exception ex) {
                System.err.println("============================================================");
                System.err.println("SimpleStoryActivity.ComprehensionTouchListener(Listener).onCompletion." + mQuestionIndex +
                        ") > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("============================================================");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }

        }
    }

    public void clearAllStorySetViews() {

        // Clear all existing rows of child views
        for (int i = 0; i < col.getChildCount(); i++) {

            // Get row
            LinearLayout row = (LinearLayout) col.getChildAt(i);

            // Remove all views from row
            row.removeAllViews();
        }

        // Clear column of all child views
        col.removeAllViews();
    }

    public void addSentenceToStorySet(int setIndex, int rowIndex) {

        try {
            // Get Image Views for next row (sentence)
            ArrayList<ImageView> imageViews = thisActivity.getImageViewGridSets().get(setIndex).get(rowIndex);

            // Create new linear layout horizontal row
            LinearLayout row = new LinearLayout(getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBaselineAligned(true);

            // Create row layout params
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(rowParams);

            // Add Image Views to row
            for (int i = 0; i < imageViews.size(); i++) {
                row.addView(imageViews.get(i));
            }

            // Add row to col
            col.addView(row);

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.addSentenceToStorySet(" + setIndex +
                    ", " + rowIndex + ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void showFullStorySet(int setIndex) {

        try {
            // Check if columns have rows
            // They shouldn't
            if (col.getChildCount() == 0) {

                // No rows exist. We're good to go!

                // Get Image Views for first row of new set
                ArrayList<ArrayList<ImageView>> imageViewGrid = thisActivity.getImageViewGridSets().get(setIndex);

                for (int i = 0; i < imageViewGrid.size(); i++) {

                    // Get Image Views for the next row (sentence)
                    ArrayList<ImageView> imageViews = imageViewGrid.get(i);

                    // Create new linear layout horizontal row
                    LinearLayout row = new LinearLayout(getApplicationContext());
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

            } else {
                throw new Exception("Column still has rows. " +
                        "Ensure that all views have been cleared first");
            }
        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.showFullStorySet(" + setIndex +
                    ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void clearViewsForComprehension() {
        // Clear all existing views
        if (col.getChildCount() > 0) {

            // Fade out existing 'questions' (or any text) on screen
            Fade fadeOut = new Fade(Fade.OUT);

            // Ensure that next transition on col is a fade out
            TransitionManager.beginDelayedTransition(col, fadeOut);

            // Clear all story views
            clearAllStorySetViews();
        }
    }

    public void showViewsForComprehension(int questionIndex) {

        try {
            // Create new 'fade in' for next transition
            Fade fadeIn = new Fade(Fade.IN);

            // Ensure that next transition on col is a fade in
            TransitionManager.beginDelayedTransition(col, fadeIn);

            // Get Image Views for next row (sentence)
            ArrayList<ImageView> imageViews = thisActivity.getComprehensionQuestionImageViewSets().get(questionIndex);

            // Create new linear layout horizontal row
            LinearLayout row = new LinearLayout(getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBaselineAligned(true);

            // Create row layout params
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(rowParams);

            // Get answer type
            // 'true' = Touch type question
            // 'false' = Speech type question
            boolean isTouchAnswer = mComprehensionAnswerTypes.get(questionIndex);

            System.out.println("!!There are " + imageViews.size() + " images!!");

            // Add Image Views to row
            for (int i = 0; i < imageViews.size(); i++) {

                row.addView(imageViews.get(i));

                /*
                // Validate answer type
                if (isTouchAnswer) {

                    System.out.println(">> IS touch answer");

                    // Is touch type
                    // Show all images
                    row.addView(imageViews.get(i));

                } else {

                    System.out.println("<< IS NOT touch answer");

                    // Is not touch type
                    // Only show 'correct' image
                    boolean isCorrectAnswer = mComprehensionQuestionAnswerSets.get(setIndex).get(i);

                    // Only add if it's the correct answer
                    if (isCorrectAnswer) {
                        // Break out
                        // Only correct image view is required
                        row.addView(imageViews.get(i));
                        break;
                    }

                }
                */
            }

            System.out.println(":@#$: " + row.getChildCount());

            // Add row to col
            col.addView(row);

        } catch (Exception ex) {
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.showViewsForComprehension(" + questionIndex +
                    ") > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void prepareViewsAndAskNextComprehensionQuestion() {

        // Clear views for comprehension
        clearViewsForComprehension();

        // Show views for comprehension question
        showViewsForComprehension(mComprehensionQuestionIndex);

        // Play comprehension question after 1100 milliseconds
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                playComprehensionQuestion(mComprehensionQuestionIndex);
            }
        }, 1150);
    }

    public void setSelectedSetIndex(int selectedSetIndex) {
        mSelectedSetIndex = selectedSetIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        mSelectedRowIndex = selectedRowIndex;
    }

    public void setSelectedWordIndex(int selectedWordIndex) {
        mSelectedWordIndex = selectedWordIndex;
    }

    public void setSelectedWordFlipped(boolean selectedWordFlipped) {
        mSelectedWordFlipped = selectedWordFlipped;
    }

    public void setActiveSetIndex(int activeSetIndex) {
        mActiveSetIndex = activeSetIndex;
    }

    public void setActiveRowIndex(int activeRowIndex) {
        mActiveRowIndex = activeRowIndex;
    }

    public void setActiveWordIndex(int activeWordIndex) {
        mActiveWordIndex = activeWordIndex;
    }

    public void setActiveWordFlipped(boolean activeWordFlipped) {
        mActiveWordFlipped = activeWordFlipped;
    }

    public void setActiveWordDuration(int activeWordDuration) {
        mActiveWordDuration = activeWordDuration;
    }

    public void setComprehensionQuestionSoundPaths(ArrayList<String> comprehensionQuestionSoundPaths) {
        mComprehensionQuestionSoundPaths = comprehensionQuestionSoundPaths;
    }

    public void setComprehensionAnswerSoundPaths(ArrayList<String> comprehensionAnswerSoundPaths) {
        mComprehensionAnswerSoundPaths = comprehensionAnswerSoundPaths;
    }

    public void setComprehensionQuestionIndex(int comprehensionQuestionIndex) {
        mComprehensionQuestionIndex = comprehensionQuestionIndex;
    }

    public void setComprehensionTouchEnabled(boolean comprehensionTouchEnabled) {
        mComprehensionTouchEnabled = comprehensionTouchEnabled;
    }

    public int getSelectedSetIndex() {
        return mSelectedSetIndex;
    }

    public int getSelectedRowIndex() {
        return mSelectedRowIndex;
    }

    public int getSelectedWordIndex() {
        return mSelectedWordIndex;
    }

    public boolean getSelectedWordFlipped() {
        return mSelectedWordFlipped;
    }

    public int getActiveSetIndex() {
        return mActiveSetIndex;
    }

    public int getActiveRowIndex() {
        return mActiveRowIndex;
    }

    public int getActiveWordIndex() {
        return mActiveWordIndex;
    }

    public boolean getActiveWordFlipped() {
        return mActiveWordFlipped;
    }

    public int getActiveWordDuration() {
        return mActiveWordDuration;
    }

    public ArrayList<ArrayList<ArrayList<ImageView>>> getImageViewGridSets() {
        return imageViewGridSets;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getBlackWordGridSets() {
        return blackWordGridSets;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getRedWordGridSets() {
        return redWordGridSets;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getSoundPathGridSets() {
        return soundPathGridSets;
    }

    public ArrayList<ArrayList<ImageView>> getComprehensionQuestionImageViewSets() {
        return mComprehensionQuestionImageViewSets;
    }

    public ArrayList<ArrayList<Boolean>> getComprehensionQuestionAnswerSets() {
        return mComprehensionQuestionAnswerSets;
    }

    public ArrayList<String> getComprehensionQuestionSoundPaths() {
        return mComprehensionQuestionSoundPaths;
    }

    public ArrayList<String> getComprehensionAnswerSoundPaths() {
        return mComprehensionAnswerSoundPaths;
    }

    public ArrayList<Boolean> getComprehensionAnswerTypes() {
        return mComprehensionAnswerTypes;
    }

    public int getComprehensionQuestionIndex() {
        return mComprehensionQuestionIndex;
    }

    public boolean getComprehensionTouchEnabled() {
        return mComprehensionTouchEnabled;
    }

    private void initData(){

        // Debug
        System.out.println(":: SimpleStoryActivity.initialiseData > Debug: METHOD CALLED");

        try {
            drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            sentences = allData.getJSONArray("sentences");
            questions = allData.getJSONArray("questions");
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

    private void initStory() {

        // Initialize story
        // Loop through sentences to create Linear Layout rows
        // and populate with Image View rows
        // These rows will be added to the 'column' that holds all story-related views
        try {

            // In the DB, Set numbers start from 1 instead of 0
            int startingSetIndex = STARTING_SET + 1;

            // Initialize the current set
            int currentSet = -1;


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

                            // Add previous Image View grid to Image View grid sets
                            if (imageViewGrid != null && imageViewGrid.size() > 0) {
                                imageViewGridSets.add(imageViewGrid);
                            }

                            // Add previous black word grid to black word grid sets
                            if (blackWordGrid != null && blackWordGrid.size() > 0) {
                                blackWordGridSets.add(blackWordGrid);
                            }

                            // Add previous red word grid to red word grid sets
                            if (redWordGrid != null && redWordGrid.size() > 0) {
                                redWordGridSets.add(redWordGrid);
                            }

                            // Add previous sound path grid to sound path grid sets
                            if (soundPathGrid != null && soundPathGrid.size() > 0) {
                                soundPathGridSets.add(soundPathGrid);
                            }

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
                        imageView.setOnClickListener(new SelectedWordListener(thisActivity, soundPathGridSets.size(), soundPathGrid.size(), j));

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

                    // If this is the last row (sentence), add grids to grid sets here
                    if (i == sentences.length() - 1) {
                        // Add final Image View grid to Image View grid sets
                        if (imageViewGrid != null && imageViewGrid.size() > 0) {
                            imageViewGridSets.add(imageViewGrid);
                        }

                        // Add final black word grid to black word grid sets
                        if (blackWordGrid != null && blackWordGrid.size() > 0) {
                            blackWordGridSets.add(blackWordGrid);
                        }

                        // Add final red word grid to red word grid sets
                        if (redWordGrid != null && redWordGrid.size() > 0) {
                            redWordGridSets.add(redWordGrid);
                        }

                        // Add final sound path grid to sound path grid sets
                        if (soundPathGrid != null && soundPathGrid.size() > 0) {
                            soundPathGridSets.add(soundPathGrid);
                        }
                    }

                    // See if the current set's Image Views must be displayed
                    // Do this by checking if the current set is the 'starting set'
                    if (currentSet == startingSetIndex && imageViewGrid.size() == 1) {

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

                // Create next arrow
                mNextArrow = new ImageView(getApplicationContext());
                mNextArrow.setImageResource(R.drawable.simple_story_next);
                mNextArrow.setScaleX(0.9f);
                mNextArrow.setScaleY(0.9f);

                // Create row layout params
                RelativeLayout.LayoutParams nextArrowParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                nextArrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                nextArrowParams.topMargin = 1200;
                nextArrowParams.rightMargin = 210;
                mNextArrow.setLayoutParams(nextArrowParams);

                // Create previous arrow
                mPreviousArrow = new ImageView(getApplicationContext());
                mPreviousArrow.setImageResource(R.drawable.simple_story_next);
                mPreviousArrow.setScaleX(-0.9f);
                mPreviousArrow.setScaleY(0.9f);

                // Create row layout params
                RelativeLayout.LayoutParams previousArrowParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                previousArrowParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                previousArrowParams.topMargin = 1200;
                previousArrowParams.leftMargin = 210;
                mPreviousArrow.setLayoutParams(previousArrowParams);

                // Add listeners to next and previous arrows
                mNextArrow.setOnClickListener(new StoryNavListener(thisActivity, true));
                mPreviousArrow.setOnClickListener(new StoryNavListener(thisActivity, false));

                // Set the arrows invisible
                mNextArrow.setVisibility(View.INVISIBLE);
                mPreviousArrow.setVisibility(View.INVISIBLE);

                // Add arrows to root view
                rootView.addView(mNextArrow);
                rootView.addView(mPreviousArrow);

            } else {
                // Throw exception. Cannot work with null sentences
                throw new Exception("Sentences are null");
            }
        } catch (Exception ex) {
            // Finish the activity - it's bugged
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.initStory > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void initComprehension() {
        mComprehensionQuestionImageViewSets = new ArrayList<>();
        mComprehensionQuestionAnswerSets = new ArrayList<>();
        mComprehensionQuestionSoundPaths = new ArrayList<>();
        mComprehensionAnswerSoundPaths = new ArrayList<>();
        mComprehensionAnswerTypes = new ArrayList<>();

        try {
            // Validate questions
            if (questions != null) {

                // Declare array to hold Image Views per comprehension question set
                ArrayList<ImageView> comprehensionQuestionImageViews;

                // Declare array to hold answer sets (is it right or wrong) per comprehension question set
                ArrayList<Boolean> comprehensionQuestionAnswers;

                for (int i = 0; i < questions.length(); i++) {

                    // Extract question
                    JSONObject question = questions.getJSONObject(i);

                    // Extract question images array
                    JSONArray questionImages = question.getJSONArray("images");

                    // Initialize array to hold Image Views per comprehension question set
                    comprehensionQuestionImageViews = new ArrayList<>();

                    // Initialize array to hold answer sets (is it right or wrong) per comprehension question set
                    comprehensionQuestionAnswers = new ArrayList<>();

                    // Initialize array of image resource ids
                    // Used to validate and remove any duplicate images if existing the data String
                    ArrayList<Integer> imageResourceIds = new ArrayList<>();

                    // Loop through each question image and add to list of
                    // comprehension question Image Views
                    for (int j = 0; j < questionImages.length(); j++) {

                        // Extract question image object
                        JSONObject questionImage = questionImages.getJSONObject(j);

                        /* * * * * * * * * * *
                         * PROCESS IMAGE VIEW *
                         * * * * * * * * * * * */

                        // Get the image (resource id) of the comprehension question image
                        int imageResourceId = questionImage.getInt("image");

                        if (!imageResourceIds.contains(imageResourceId)) {

                            // Add image resource id to list of image resource ids
                            imageResourceIds.add(imageResourceId);

                            // Create Image View to hold the comprehension question image
                            ImageView imageView = new ImageView(getApplicationContext());

                            // Set comprehension question's image resource to that of the resourceId
                            imageView.setImageResource(imageResourceId);

                            // Set adjust view bounds to true
                            imageView.setAdjustViewBounds(true);

                            // Set Image View max width
                            imageView.setMaxWidth(500);

                            // Set layout params of Image View
                            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            imageViewParams.leftMargin = 28; // Some 'padding' between sentence words
                            imageViewParams.rightMargin = 28;
                            imageView.setLayoutParams(imageViewParams);

                            // Add listeners to Image View
                            imageView.setOnClickListener(new ComprehensionTouchListener(thisActivity, i));

                            // Add the Image View to list of comprehension question Image Views
                            comprehensionQuestionImageViews.add(imageView);

                        /* * * * * * * * * * * * * *
                         * PROCESS QUESTION ANSWER *
                         * * * * * * * * * * * * * */

                            int questionAnswer = questionImage.getInt("is_right");

                            // Convert question answer
                            boolean comprehensionQuestionAnswer = (questionAnswer == 1);

                            // Store comprehension question answer
                            comprehensionQuestionAnswers.add(comprehensionQuestionAnswer);
                        }
                    }

                    // Add list of comprehension question Image Views to set
                    mComprehensionQuestionImageViewSets.add(comprehensionQuestionImageViews);

                    // Add list of comprehension question answers to set
                    mComprehensionQuestionAnswerSets.add(comprehensionQuestionAnswers);

                    /* * * * * * * * * * * * * * * * *
                     * PROCESS QUESTION SOUND PATHS *
                     * * * * * * * * * * * * * * * */

                    // Extract sound path
                    String comprehensionQuestionSoundPath = FetchResource.sound(getApplicationContext(),
                            question.getString("question_sound"));

                    // Add sound path to list of comprehension question sound paths
                    mComprehensionQuestionSoundPaths.add(comprehensionQuestionSoundPath);

                    /* * * * * * * * * * * * * * *
                     * PROCESS ANSWER SOUND PATHS *
                     * * * * * * * * * * * * * * * */

                    // Extract sound path
                    String comprehensionAnswerSoundPath = FetchResource.sound(getApplicationContext(),
                            question.getString("answer_sound"));

                    // Add sound path to list of comprehension answer sound paths
                    mComprehensionAnswerSoundPaths.add(comprehensionAnswerSoundPath);

                    /* * * * * * * * * * * * *
                     * PROCESS ANSWER TYPES *
                     * * * * * * * * * * * */

                    boolean comprehensionAnswerType = (question.getInt("is_touch") == 0);

                    // Add sound path to list of comprehension answer sound paths
                    mComprehensionAnswerTypes.add(comprehensionAnswerType);
                }

            } else {
                // Throw exception. Cannot work with null questions
                throw new Exception("Comprehension questions are null");
            }
        } catch (Exception ex) {
            // Finish the activity - it's bugged
            System.err.println("============================================================");
            System.err.println("SimpleStoryActivity.initComprehension > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("============================================================");
            if (mp != null) {
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
