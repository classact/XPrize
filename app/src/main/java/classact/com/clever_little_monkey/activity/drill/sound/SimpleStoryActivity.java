package classact.com.clever_little_monkey.activity.drill.sound;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.FisherYates;
import classact.com.clever_little_monkey.utils.ImageHelper;
import classact.com.clever_little_monkey.utils.ResourceSelector;
import classact.com.clever_little_monkey.utils.WordRect;

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

    private ImageView subBackgroundView;

    // Image View grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private List<List<List<ImageView>>> imageViewGridSets;

    // Black word grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private List<List<List<Integer>>> blackWordGridSets;

    // Black word grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private List<List<List<Integer>>> redWordGridSets;

    // Sound path grid sets
    // * Note that a 'set' refers to a 'sentence set'
    // and not java.util.Set *
    private List<List<List<String>>> soundPathGridSets;

    // Comprehension stuff
    private List<List<ImageView>> mComprehensionQuestionImageViewSets;
    private List<List<Boolean>> mComprehensionQuestionAnswerSets;
    private List<String> mComprehensionQuestionSoundPaths;
    private List<String> mComprehensionAnswerSoundPaths;
    private List<Boolean> mComprehensionAnswerTypes;

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

    private ConstraintLayout clStory;
    private ConstraintLayout clComprehension;

    private final Context THIS = this;

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

        // Retrieves the sentences from drill data
        // Do this before creating rows, in case there is an override for number of
        // sentences per set
        // * Note: NO override exists at the moment *
        initData();

        // Initialize story and corresponding objects and views
        initStory();
        initStoryViews();
        addSentenceToStorySet(0, 0);

        // Initialize comprehension and corresponding objects and views
        initComprehension();
        initComprehensionViews();

        /*
        float densithy = getResources().getDisplayMetrics().density;
        ImageView iv = new ImageView(THIS);
        iv.setBackgroundColor(Color.argb(100, 255, 0, 0));
        clStory.addView(iv);
        iv.setX(0.0f);
        iv.setY(870.0f);
        ConstraintLayout.LayoutParams ivLP = (ConstraintLayout.LayoutParams) iv.getLayoutParams();
        ivLP.width = (int) (density * 300);
        ivLP.height = 290;
        iv.setLayoutParams(ivLP);
        */

        currentState = STATE_0;

        playPrompt("read_each_sentence_after_mother_sound");
        // playPrompt("now_read_whole_story_sound");
        // playPrompt("now_answer_sound");
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
                    clearAllStorySetViews(true);
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
                            mThisActivity.clearAllStorySetViews(true);

                            // Get Image Views for first row of new set
                            mThisActivity.showFullStorySet(0, true);

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

                    clearAllStorySetViews(true);
                    Fade fadeIn = new Fade(Fade.IN);
                    Fade fadeOut = new Fade(Fade.OUT);

                    subBackgroundView = new ImageView(getApplicationContext());
                    subBackgroundView.setBackgroundResource(R.drawable.backgound_comprehension);
                    RelativeLayout.LayoutParams storyBackgroundViewParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    subBackgroundView.setLayoutParams(storyBackgroundViewParams);

                    TransitionManager.beginDelayedTransition(clStory, fadeOut);
                    TransitionManager.beginDelayedTransition(rootView, fadeIn);

                    rootView.addView(subBackgroundView, 0);
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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
                        System.out.println("XXXXXXXXXXXXXXXX");
                        Fade fadeIn = new Fade(Fade.IN);
                        Fade fadeOut = new Fade(Fade.OUT);

                        subBackgroundView = new ImageView(THIS);
                        RelativeLayout.LayoutParams storyBackgroundViewParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                        );
                        subBackgroundView.setLayoutParams(storyBackgroundViewParams);
                        String storyImage = allData.getString("story_image");
                        System.out.println("Story Image: " + storyImage);
                        int storyImageId = FetchResource.imageId(THIS, storyImage);
                        subBackgroundView.setImageResource(storyImageId);
                        subBackgroundView.setBackgroundColor(Color.BLACK);
                        rootView.addView(subBackgroundView, rootView.getChildCount());

                        TransitionManager.beginDelayedTransition(rootView, fadeIn);
                        TransitionManager.beginDelayedTransition(clStory, fadeOut);

                        clearAllStorySetViews(true);

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
                                mThisActivity.clearAllStorySetViews(true);

                                // Get Image Views for first row of new set
                                mThisActivity.showFullStorySet(0, true);

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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void playPrompt(String prompt) {
        // Debug
        System.out.println(":: SimpleStoryActivity.playPrompt(\"" + prompt + "\") > Debug: METHOD CALLED");

        String sound = "";

        try{
            sound = allData.getString(prompt);
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
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
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
                if (mp != null) {
                    mp.reset();
                }
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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
                        mThisActivity.clearSet(mActiveSetIndex);

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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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

            // Check if sound path is valid
            if (!soundPath.equalsIgnoreCase("0")) {

                // It is valid

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

            } else {
                // Fire off the completion event
                (new ActiveWordListener(thisActivity, mute, setIndex, rowIndex, wordIndex)).onCompletion(null);
            }

        } catch (Exception ex) {
            mp = null;
            new ActiveWordListener(thisActivity, mute, setIndex, rowIndex, wordIndex).handleOnCompletion();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            if (mp != null) {
                mp.reset();
            }
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

                // Check if sound path is valid
                if (!soundPath.equalsIgnoreCase("0")) {

                    // It is valid

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

                } else {
                    // Fire off onCompletion event
                    (new SelectedWordListener(thisActivity, setIndex, rowIndex, wordIndex)).onCompletion(null);
                }

            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                        // RelativeLayout.LayoutParams nextArrowParams = (RelativeLayout.LayoutParams) mNextArrow.getLayoutParams();
                        // nextArrowParams.topMargin = 140;
                        // nextArrowParams.rightMargin = 210;
                        // mNextArrow.setLayoutParams(nextArrowParams);

                        // Add a new listener
                        mNextArrow.setOnClickListener(null);
                        mNextArrow.setOnClickListener(new ImageView.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    // Disable touch words
                                    touchWordsEnabled = false;

                                    // Hide arrow
                                    mNextArrow.setVisibility(View.INVISIBLE);
                                    mPreviousArrow.setVisibility(View.INVISIBLE);

                                    // Play prompt
                                    playPrompt("well_done_you_can_read_sound");
                                } catch (Exception ex) {
                                    Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
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
                mThisActivity.clearAllStorySetViews(false);

                // Get Image Views for first row of new set
                mThisActivity.showFullStorySet(mSetIndex, false);
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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mThisActivity.setComprehensionTouchEnabled(true);
                        }
                    }, 500);

                    /*
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mThisActivity.playComprehensionAnswer(mThisActivity.getComprehensionQuestionIndex());
                        }
                    }, 1000);
                    */

                } else {

                    // Microphone based answer required

                    // Disable touch
                    mThisActivity.setComprehensionTouchEnabled(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mThisActivity.playComprehensionAnswer(mThisActivity.getComprehensionQuestionIndex());
                            } catch (Exception ex) {
                                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                        }
                    }, 2500);
                }
            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            try {

                int maxQuestions = mThisActivity.getComprehensionQuestionSoundPaths().size();
                int nextQuestionIndex = mAnswerIndex + 1;

                if (nextQuestionIndex >= maxQuestions) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                // Fade in black view (mimic fade out)
                                Fade fadeIn = new Fade(Fade.IN);

                                subBackgroundView = new ImageView(getApplicationContext());
                                subBackgroundView.setBackgroundResource(R.drawable.maths_link_bg);
                                LinearLayout.LayoutParams storyBackgroundViewParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                subBackgroundView.setLayoutParams(storyBackgroundViewParams);

                                TransitionManager.beginDelayedTransition(rootView, fadeIn);

                                rootView.addView(subBackgroundView, rootView.getChildCount());

                                // End drill after 1000 milliseconds
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            endDrill();
                                        } catch (Exception ex) {
                                            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                            ex.printStackTrace();
                                        }
                                    }
                                }, 1000);
                            } catch(Exception ex) {
                                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                        }
                    }, 1500);
                    /*
                    // Clear views for comprehension
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearViewsForComprehension();

                            // Play end drill affirmation after 500 milliseconds
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    playEndDrillAffirmation();
                                }
                            }, 750);
                        }
                    }, 500);
                    */

                } else {
                    // On to next question
                    mThisActivity.setComprehensionQuestionIndex(nextQuestionIndex);
                    // Clear views for comprehension
                    clearViewsForComprehension(true);
                    // Play question after 500 millisecond delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Prepare views and ask next comprehension question
                                prepareViewsAndAskNextComprehensionQuestion();
                            } catch (Exception ex) {
                                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                        }
                    }, 700);
                }

            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    class ComprehensionTouchListener implements ImageView.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SimpleStoryActivity mThisActivity;
        private int mQuestionIndex;
        private int mOptionIndex;

        public ComprehensionTouchListener(SimpleStoryActivity thisActivity, int questionIndex, int optionIndex) {
            mThisActivity = thisActivity;
            mQuestionIndex = questionIndex;
            mOptionIndex = optionIndex;
            // Update comprehension question index for the activity
            mThisActivity.setComprehensionQuestionIndex(questionIndex);
        }

        @Override
        public void onClick(View v) {
            try {
                if (mThisActivity.getComprehensionTouchEnabled()) {
                    // Play touch result
                    playComprehensionTouchResult(mQuestionIndex, mOptionIndex);
                }
            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            try {
                mp.start();

            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                mp.reset();

                // Check if it's the answer
                boolean isAnswer = mThisActivity.getComprehensionQuestionAnswerSets().get(mQuestionIndex).get(mOptionIndex);

                // If it's the answer
                if (isAnswer) {

                    // Play the comprehension answer after 250 milliseconds
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                playComprehensionAnswer(mQuestionIndex);
                            } catch (Exception ex) {
                                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }
                        }
                    }, 250);
                }

            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

        }
    }

    public void playComprehensionTouchResult(int questionIndex, int optionIndex) {
        try {
            // Get sound path from sound path grid sets
            boolean isAnswer = mComprehensionQuestionAnswerSets.get(questionIndex).get(optionIndex);

            // Declare sound path
            String soundPath;

            // Determine sound path
            if (isAnswer) {

                // Disable comprehension touch
                setComprehensionTouchEnabled(false);

                // Set sound path to positive affirmations
                soundPath = "android.resource://" + getApplicationContext().getPackageName() +
                        "/" + ResourceSelector.getPositiveAffirmationSound(getApplicationContext());

            } else {

                // Set sound path to negative affirmations
                soundPath = "android.resource://" + getApplicationContext().getPackageName() +
                        "/" + ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
            }

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Media player jazz ♫♪
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new ComprehensionTouchListener(thisActivity, questionIndex, optionIndex));
            mp.setOnCompletionListener(new ComprehensionTouchListener(thisActivity, questionIndex, optionIndex));
            mp.prepare();

        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playEndDrillAffirmation() {
        try {

            // Declare sound path
            String soundPath = "android.resource://" + getApplicationContext().getPackageName() +
                    "/" + ResourceSelector.getPositiveAffirmationSound(getApplicationContext());

            // Initialize media player if need be
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Media player jazz ♫♪
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    endDrill();
                                } catch (Exception ex) {
                                    Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
                            }
                        }, 500);
                    } catch (Exception ex) {
                        Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            });
            mp.prepare();

        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void clearSet(int setIndex) {
        try {
            System.out.println("Clearing set: " + setIndex);
            List<List<ImageView>> setPhrases = imageViewGridSets.get(setIndex);
            for (int i = 0; i < setPhrases.size(); i++) {
                List<ImageView> phraseWords = setPhrases.get(i);
                for (int j = 0; j < phraseWords.size(); j++) {
                    final ImageView iv = phraseWords.get(j);
                    iv.animate().alpha(0).setDuration(500).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            iv.setVisibility(View.INVISIBLE);
                        }
                    }).start();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void addSentenceToStorySet(int setIndex, int rowIndex) {
        try {
            List<ImageView> imageViews = imageViewGridSets.get(setIndex).get(rowIndex);
            for (ImageView iv : imageViews) {
                iv.setVisibility(View.VISIBLE);
                iv.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void showFullStorySet(int setIndex, boolean fadeIn) {
        try {
            System.out.println("Clearing set: " + setIndex);
            List<List<ImageView>> setPhrases = imageViewGridSets.get(setIndex);
            for (int i = 0; i < setPhrases.size(); i++) {
                List<ImageView> phraseWords = setPhrases.get(i);
                for (int j = 0; j < phraseWords.size(); j++) {
                    ImageView iv = phraseWords.get(j);
                    if (fadeIn) {
                        iv.setVisibility(View.VISIBLE);
                        iv.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
                    } else {
                        iv.setVisibility(View.VISIBLE);
                        iv.setAlpha(1.0f);
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void clearViewsForComprehension(boolean fadeOut) {
        try {
            List<List<ImageView>> comprehensionSets = mComprehensionQuestionImageViewSets;
            for (int i = 0; i < comprehensionSets.size(); i++) {
                List<ImageView> setImageViews = comprehensionSets.get(i);
                for (int j = 0; j < setImageViews.size(); j++) {
                    final ImageView iv = setImageViews.get(j);
                    if (fadeOut) {
                        iv.animate().alpha(0.0f).setDuration(500).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                iv.setVisibility(View.INVISIBLE);
                            }
                        }).start();
                    } else {
                        iv.setAlpha(0.0f);
                        iv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void clearAllStorySetViews(boolean fadeOut) {
        try {
            List<List<List<ImageView>>> storySets = imageViewGridSets;
            for (int i = 0; i < storySets.size(); i++) {
                List<List<ImageView>> setPhrases = storySets.get(i);
                for (int j = 0; j < setPhrases.size(); j++) {
                    List<ImageView> phraseWords = setPhrases.get(j);
                    for (int k = 0; k < phraseWords.size(); k++) {
                        final ImageView iv = phraseWords.get(k);
                        if (fadeOut) {
                            iv.animate().alpha(0.0f).setDuration(500).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                        } else {
                            iv.setAlpha(0.0f);
                            iv.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void showViewsForComprehension(int questionIndex, boolean fadeIn) {
        try {
            List<ImageView> setImageViews = mComprehensionQuestionImageViewSets.get(questionIndex);
            for (int i = 0; i < setImageViews.size(); i++) {
                ImageView iv = setImageViews.get(i);
                if (fadeIn) {
                    iv.setVisibility(View.VISIBLE);
                    iv.animate().alpha(1).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
                } else {
                    iv.setVisibility(View.VISIBLE);
                    iv.setAlpha(1.0f);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void prepareViewsAndAskNextComprehensionQuestion() {

        // Clear views for comprehension
        clearViewsForComprehension(true);

        // Show views for comprehension question
        showViewsForComprehension(mComprehensionQuestionIndex, true);

        // Play comprehension question after 1100 milliseconds
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                playComprehensionQuestion(mComprehensionQuestionIndex);
            }
        }, 1150);
    }

    public void endDrill() {
        if (mp != null) {
            mp.release();
        }
        finish();
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
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

    public List<List<List<ImageView>>> getImageViewGridSets() {
        return imageViewGridSets;
    }

    public List<List<List<Integer>>> getBlackWordGridSets() {
        return blackWordGridSets;
    }

    public List<List<List<Integer>>> getRedWordGridSets() {
        return redWordGridSets;
    }

    public List<List<List<String>>> getSoundPathGridSets() {
        return soundPathGridSets;
    }

    public List<List<ImageView>> getComprehensionQuestionImageViewSets() {
        return mComprehensionQuestionImageViewSets;
    }

    public List<List<Boolean>> getComprehensionQuestionAnswerSets() {
        return mComprehensionQuestionAnswerSets;
    }

    public List<String> getComprehensionQuestionSoundPaths() {
        return mComprehensionQuestionSoundPaths;
    }

    public List<String> getComprehensionAnswerSoundPaths() {
        return mComprehensionAnswerSoundPaths;
    }

    public List<Boolean> getComprehensionAnswerTypes() {
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                List<List<ImageView>> imageViewGrid = null;
                // Create a grid to store list of black word resource ids
                List<List<Integer>> blackWordGrid = null;
                // Create a grid to store list of red word resource ids
                List<List<Integer>> redWordGrid = null;
                // Create a grid to store list of sound paths
                List<List<String>> soundPathGrid = null;

                // Loopy loop ...
                for (int i = 0; i < sentences.length(); i++) {

                    // Instantiate sentence object
                    JSONArray sentence = sentences.getJSONArray(i);
                    // Validate sentence object
                    if (sentence == null) {
                        throw new Exception("Sentence (" + i + ") is null");
                    }
                    // Create list of Image Views that will hold sentence words
                    List<ImageView> imageViews = new ArrayList<>();
                    // Create list of black word resource ids per sentence word
                    List<Integer> blackWords = new ArrayList<>();
                    // Create list of red word resource ids per sentence word
                    List<Integer> redWords = new ArrayList<>();
                    // Create list of sound paths that will hold sound paths per sentence word
                    List<String> soundPaths = new ArrayList<>();

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
                        String blackWord = sentenceWord.getString("black_word");
                        int blackWordImageId = FetchResource.imageId(THIS, blackWord);
                        // Get the red word (resource id) of the sentence word
                        String redWord = sentenceWord.getString("red_word");
                        int redWordImageId = FetchResource.imageId(THIS, redWord);
                        // Add black word to black words list
                        blackWords.add(blackWordImageId);
                        // Add red word to red words list
                        redWords.add(redWordImageId);

                        // Create Image View to hold the sentence word
                        ImageView imageView = new ImageView(getApplicationContext());
                        // Set sentence word's image resource to that of the resourceId (aka 'blackword')
                        imageView.setImageResource(blackWordImageId);

                        // Set layout params of Image View
                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        imageViewParams.leftMargin = 28; // Some 'padding' between sentence words
                        imageViewParams.rightMargin = 28;
                        imageView.setLayoutParams(imageViewParams);

                        // Get the sound of the sentence word
                        String sound = sentenceWord.getString("sound");
                        // Declare sound path
                        String soundPath;

                        // If sound path is valid, get it
                        // Otherwise, assign it a value of "0"
                        if (!sound.equalsIgnoreCase("0")) {

                            // Get the sound path
                            soundPath = FetchResource.sound(getApplicationContext(), sound);
                            // Add listener to ImageView
                            imageView.setOnClickListener(new SelectedWordListener(thisActivity, soundPathGridSets.size(), soundPathGrid.size(), j));

                        } else {
                            // Otherwise, assign "0"
                            soundPath = "0";
                        }

                        // Add sound path to list of sounds paths
                        soundPaths.add(soundPath);
                        // Add Image View to list of Image Views
                        imageViews.add(imageView);
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    /**
     * STORY VIEWS ...
     */
    private void initStoryViews() {
        float dens = getResources().getDisplayMetrics().density;

        clStory = new ConstraintLayout(THIS);
        ConstraintLayout.LayoutParams clLP = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        clStory.setLayoutParams(clLP);
        rootView.addView(clStory);

        int offsetH = 75;
        int w = 1075;
        int h = 560-(offsetH/2);


        size(clStory, dens, w, h);
        position(clStory, dens, 100, 50+(offsetH/2));

        // clStory.setBackgroundColor(Color.argb(100, 0, 0, 255));

        List<List<List<ImageView>>> a = imageViewGridSets;
        List<List<List<WordRect>>> aRects = new ArrayList<>();

        for (int i = 0; i < a.size(); i++) {

            List<List<ImageView>> b = a.get(i);
            List<List<WordRect>> bRects = new ArrayList();

            for (int j = 0; j < b.size(); j++) {
                List<ImageView> c = b.get(j);
                List<WordRect> cRects = new ArrayList<>();

                for (int k = 0; k < c.size(); k++) {
                    ImageViewContainer ivc = ivc(i, j, k);
                    cRects.add(getRect(ivc, dens));
                }
                bRects.add(cRects);
            }
            aRects.add(bRects);
        }

        for (int i = 0; i < a.size(); i++) {
            List<List<ImageView>> page = imageViewGridSets.get(i);

            List<List<WordRect>> pageRects = WordRect.pack(aRects.get(i), dens, w, h, 145, 31, true, false);
            int numOfPhrases = pageRects.size();

            for (int j = 0; j < numOfPhrases; j++) {

                List<WordRect> wordRects = pageRects.get(j);
                int numOfWords = wordRects.size();

                for (int k = 0; k < numOfWords; k++) {

                    WordRect wordRect = wordRects.get(k);
                    ImageView iv = page.get(j).get(k);

                    addIV(clStory, iv);
                    resize(iv, dens, wordRect.w, wordRect.h);
                    reposition(iv, dens, wordRect.x, wordRect.y);
                    // iv.setBackgroundColor(Color.argb(100, 0, 255, 0));
                }
            }
        }
    }

    private class ImageViewContainer {

        private ImageView iv;
        private int resId;

        public ImageViewContainer(ImageView iv, int resId) {
            this.iv = iv;
            this.resId = resId;
        }

        public ImageView getImageView() {
            return iv;
        }

        public int getResourceId() {
            return resId;
        }
    }


    private WordRect getRect(ImageViewContainer ivc, float density) {
        ImageView iv = ivc.getImageView();
        Drawable d = iv.getDrawable();
        int w = 0;
        int h = 0;
        String name = null;
        if (d != null) {
            w = (int) ((float) d.getIntrinsicWidth() / density);
            h = (int) ((float) d.getIntrinsicHeight() / density);
            name = getResources().getResourceName(ivc.getResourceId()).replace("classact.com.xprize:drawable/", "");
        }
        return new WordRect(w, h, name);
    }

    private WordRect getRect(ImageView iv, float density) {
        Drawable d = iv.getDrawable();
        int w = 0;
        int h = 0;
        if (d != null) {
            w = (int) ((float) d.getIntrinsicWidth() / density);
            h = (int) ((float) d.getIntrinsicHeight() / density);
        }
        return new WordRect(w, h, null);
    }

    private void resize(ImageView iv, float density, int width, int height) {
        /* System.out.println("Resizing to " + width + ", " + height); */
        ConstraintLayout.LayoutParams ivLP = (ConstraintLayout.LayoutParams) iv.getLayoutParams();
        ivLP.width = (int) (density * (float) width);
        ivLP.height = (int) (density * (float) height);
        /* System.out.println("ImageView size: w: " + ivLP.width +
                ", h: " + ivLP.height); */
        iv.setLayoutParams(ivLP);
    }

    private void size(ConstraintLayout cl, float density, int width, int height) {
        RelativeLayout.LayoutParams clLP = (RelativeLayout.LayoutParams) cl.getLayoutParams();
        clLP.width = (int) (density * (float) width);
        clLP.height = (int) (density * (float) height);
        /* System.out.println("Layout padding: t: " + cl.getPaddingTop() +
                ", b: " + cl.getPaddingBottom() +
                ", l: " + cl.getPaddingLeft() +
                ", r: " + cl.getPaddingRight());
        System.out.println("Layout margins: t: " + clLP.topMargin +
                ", b: " + clLP.bottomMargin +
                ", l: " + clLP.leftMargin +
                ", r: " + clLP.rightMargin);
        System.out.println("Layout size: w: " + clLP.width +
                ", h: " + clLP.height); */
        cl.setLayoutParams(clLP);
    }

    private void reposition(ImageView iv, float density, int x, int y) {
        float newX = density * x;
        float newY = density * y;
        /* System.out.println("x: " + newX + ", y: " + newY); */
        iv.setX(newX);
        iv.setY(newY);
    }

    private void position(ConstraintLayout cl, float density, int x, int y) {
        cl.setX(density * x);
        cl.setY(density * y);
    }

    private ImageViewContainer ivc(int a, int b, int c) {

        ImageViewContainer ivc = null;
        try {
            int id = View.generateViewId();
            int img = blackWordGridSets.get(a).get(b).get(c);

            ImageView iv = imageViewGridSets.get(a).get(b).get(c);
            iv.setId(id);
            iv.setImageResource(img);
            iv.setVisibility(View.INVISIBLE);
            iv.setAlpha(0.0f);
            ivc = new ImageViewContainer(iv, img);
        } catch (Exception ex) {
            Toast.makeText(THIS, "Image View does not exist", Toast.LENGTH_SHORT).show();
        }
        return ivc;
    }

    private void addIV(ConstraintLayout c, ImageView iv) {
        if (iv != null) {
            LinearLayout parent = (LinearLayout) iv.getParent();
            if (parent != null) {
                parent.removeView(iv);
            }
            c.addView(iv);
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
                List<ImageView> comprehensionQuestionImageViews;
                // Declare array to hold answer sets (is it right or wrong) per comprehension question set
                List<Boolean> comprehensionQuestionAnswers;

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
                    List<Integer> imageResourceIds = new ArrayList<>();
                    // Get total number of question images
                    int noOfQuestionImages = questionImages.length();

                    // Get shuffled indexes
                    int[] shuffled = FisherYates.shuffle(noOfQuestionImages);

                    // Loop through each question image and add to list of
                    // comprehension question Image Views
                    for (int j = 0; j < noOfQuestionImages; j++) {

                        // Get shuffled index k
                        int k = shuffled[j];

                        // Extract question image object
                        JSONObject questionImage = questionImages.getJSONObject(k);

                        /* * * * * * * * * * *
                         * PROCESS IMAGE VIEW *
                         * * * * * * * * * * * */

                        // Get the image (resource id) of the comprehension question image
                        String image = questionImage.getString("image");
                        int imageResourceId = FetchResource.imageId(THIS, image);

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
                            imageView.setOnClickListener(new ComprehensionTouchListener(thisActivity, i, j));

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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void initComprehensionViews() {
        float dens = getResources().getDisplayMetrics().density;

        clComprehension = new ConstraintLayout(THIS);
        ConstraintLayout.LayoutParams clLP = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        clComprehension.setLayoutParams(clLP);
        rootView.addView(clComprehension);

        int w = 1075;
        int h = 675;
        int offsetH = 75;

        size(clComprehension, dens, w, h-(offsetH/2));
        position(clComprehension, dens, 100, 50+(offsetH/2));

        // clComprehension.setBackgroundColor(Color.argb(100, 0, 0, 255));

        List<List<ImageView>> a = mComprehensionQuestionImageViewSets;
        List<List<List<WordRect>>> aRects = new ArrayList<>();

        for (int i = 0; i < a.size(); i++) {

            List<ImageView> b = a.get(i);
            List<List<WordRect>> bRects = new ArrayList<>();
            List<WordRect> cRects = new ArrayList<>();

            for (int j = 0; j < b.size(); j++) {
                ImageView iv = a.get(i).get(j);
                iv.setId(View.generateViewId());
                iv.setAlpha(0.0f);
                iv.setVisibility(View.INVISIBLE);
                cRects.add(getRect(iv, dens));
            }
            bRects.add(cRects);
            aRects.add(bRects);
        }

        for (int i = 0; i < aRects.size(); i++) {
            List<List<WordRect>> pageRects = WordRect.pack(aRects.get(i), dens, w, h, 300, 100, true, true);
            for (int j = 0; j < pageRects.size(); j++) {
                List<WordRect> wordRects = pageRects.get(j);
                for (int k = 0; k < wordRects.size(); k++) {
                    WordRect wordRect = wordRects.get(k);
                    ImageView iv = a.get(i).get(k);

                    addIV(clComprehension, iv);
                    resize(iv, dens, wordRect.w, wordRect.h);
                    reposition(iv, dens, wordRect.x, wordRect.y);
                }
            }
        }
    }

    private void playReadEachSentenceAfterMother() {

        // Debug
        System.out.println(":: SimpleStoryActivity.playReadEachSentenceAfterMother > Debug: METHOD CALLED");


        try{
            String sound = allData.getString("read_each_sentence_after_mother_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    saySentenceWord();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                final String sound = word.getString("sound");
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playSound(sound, null);
                    }
                });
                if (i > 0)
                    item.setPadding(50,10,0,0);
                item.setMaxHeight(143);
                String blackWordImage = word.getString("black_word");
                int blackWordImageId = FetchResource.imageId(THIS, blackWordImage);
                item.setImageResource(blackWordImageId);
                width += ImageHelper.getLength(blackWordImageId,this);
                if (width > 1150){
                    container.addView(line);
                    line = getLine();
                    width = 0;
                }
                line.addView(item);
                sentenceViews.add(item);
                sounds.add(word.getString("sound"));
            }
            container.addView(line);
            currentSound = 0;
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayListenFirst() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayListenFirst > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("listen_first_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    saySentenceWord();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void saySentenceWord() {

        // Debug
        System.out.println(":: SimpleStoryActivity.saySentenceWord > Debug: METHOD CALLED");

        try{
            if (currentSound < sounds.size()) {
                turnWord("red_word");
                String sound = sounds.get(currentSound);
                playSound(sound, new Runnable() {
                    @Override
                    public void run() {
                        turnWord("black_word");
                        currentSound++;
                        saySentenceWord();
                    }
                });
            }
            else{
                sayItsYourTurn();
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayItsYourTurn() {

        // Debug
        System.out.println(":: SimpleStoryActivity.sayItsYourTurn > Debug: METHOD CALLED");

        try{
            String sound = allData.getString("now_read_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    currentSound = -1;
                    //currentSentence = 1;
                    handler.postDelayed(showSentenceWithoutSound,100);
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void nowAnswerQuestions(){

        // Debug
        System.out.println(":: SimpleStoryActivity.nowAnswerQuestions > Debug: METHOD CALLED");

        try {
            String sound = allData.getString("now_answer_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    doComprehension();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
                    trippleImageOne.setVisibility(View.VISIBLE);
                    trippleImageTwo.setVisibility(View.VISIBLE);
                    tripleImageThree.setVisibility(View.VISIBLE);

                    String imageOne = question.getJSONArray("images").getJSONObject(0).getString("image");
                    String imageTwo = question.getJSONArray("images").getJSONObject(1).getString("image");
                    String imageThree = question.getJSONArray("images").getJSONObject(2).getString("image");

                    int imageOneId = FetchResource.imageId(THIS, imageOne);
                    int imageTwoId = FetchResource.imageId(THIS, imageTwo);
                    int imageThreeId = FetchResource.imageId(THIS, imageThree);

                    trippleImageOne.setImageResource(imageOneId);
                    trippleImageTwo.setImageResource(imageTwoId);
                    tripleImageThree.setImageResource(imageThreeId);
                }
                else{
                    singleImage.setVisibility(View.VISIBLE);
                    trippleImageOne.setVisibility(View.INVISIBLE);
                    trippleImageTwo.setVisibility(View.INVISIBLE);
                    tripleImageThree.setVisibility(View.INVISIBLE);
                    String image = question.getJSONArray("images").getJSONObject(0).getString("image");
                    int imageId = FetchResource.imageId(THIS, image);
                    singleImage.setImageResource(imageId);

                }
                String sound = question.getString("question_sound");
                playSound(sound, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject question = questions.getJSONObject(currentQuestion);
                            if (question.getInt("is_touch") == 0) { //Three objects
                                handler.postDelayed(plaSingleImageAnswerRunnable, 3000);
                            }
                        } catch (Exception ex) {
                            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }
                    }
                });
            }
            else{
                if (mp != null){
                    mp.release();
                }
                finish();
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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
            String image = question.getJSONArray("images").getJSONObject(0).getString("image");
            int imageId = FetchResource.imageId(THIS, image);
            singleImage.setImageResource(imageId);
            String sound = question.getString("answer_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    currentQuestion++;
                    nextQuestion();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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

    private void turnWord(String turnString){

        // Debug
        System.out.println(":: SimpleStoryActivity.turnWord > Debug: METHOD CALLED");

        try{
            JSONArray sentence = sentences.getJSONArray(currentSentence);
            JSONObject word = sentence.getJSONObject(currentSound);
            ImageView image = sentenceViews.get(currentSound);
            String picture = word.getString(turnString);
            int pictureImageId = FetchResource.imageId(THIS, picture);
            if (pictureImageId > 0)
                image.setImageResource(pictureImageId);
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private  void imageClicked(int image){

        // Debug
        System.out.println(":: SimpleStoryActivity.imageClicked > Debug: METHOD CALLED");

        try {
            JSONObject question = questions.getJSONObject(currentQuestion);
            if (question.getJSONArray("images").getJSONObject(image).getInt("is_right") == 1){
                playSound(FetchResource.positiveAffirmation(THIS), null);
                currentQuestion++;
                nextQuestion();
            }
            else{;
                playSound(FetchResource.negativeAffirmation(THIS), null);
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playSound(String sound, final Runnable action) {
        try {
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
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
        }
        if (handler != null) {
            handler = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();

        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.release();
        }
        if (handler != null) {
            handler = null;
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}