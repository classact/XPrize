package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFifteenActivity extends AppCompatActivity {

    private ImageView container1;
    private ImageView container2;
    private ImageView container3;
    private ImageView container4;
    private ImageView container5;
    private ImageView container6;
    private ImageView container7;
    private ImageView container8;

    private ImageView receptacle1;
    private ImageView receptacle2;
    private ImageView receptacle3;
    private ImageView receptacle4;
    private ImageView receptacle5;
    private ImageView receptacle6;
    private ImageView receptacle7;
    private ImageView receptacle8;

    private int currentItem;
    public JSONArray drills;
    private int currentDrill;
    private MediaPlayer mp;
    private int correctItems;
    private JSONObject allData;

    private ImageView[] mContainers;
    private RelativeLayout mReceptaclesParent;
    private ImageView[] mReceptacles;
    private ImageView[] mWordImageViews;
    private boolean mGameOn;
    private int[] mWordImageResourceIds;
    private int[] mWordOrder;
    private boolean[] mReceptacleEntries;
    private String[] mWordSounds;
    private boolean mDrillComplete;
    private boolean mEndDrill;
    private SoundDrillFifteenActivity mThisActivity;

    private String mDragWordToWrite;
    private String mThisIsSound;
    private String mCurrentSentenceSound;

    private final int MAX_LETTERS = 8;
    private final String DRAG_WORD_TO_WRITE = "drag_word_to_write";
    private final String THIS_IS = "this_is";
    private final String SENTENCE_SOUND = "sentence_sound";
    private final String WORD_SOUND = "sound";
    private final String YAY = "YAY_001";
    private final String NAY = "NAY_001";

    private RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Debug
        System.out.println("SDFifteenActivity.OnCreate > Debug: MC");

        setContentView(R.layout.activity_sound_drill_fifteen);

        mRootView = (RelativeLayout) findViewById(R.id.activity_sound_drill_fifteen);
        mRootView.setBackgroundResource(R.drawable.backgroundwriteletters);

        container1 = (ImageView) findViewById(R.id.container1);
        container2 = (ImageView) findViewById(R.id.container2);
        container3 = (ImageView) findViewById(R.id.container3);
        container4 = (ImageView) findViewById(R.id.container4);
        container5 = (ImageView) findViewById(R.id.container5);
        container6 = (ImageView) findViewById(R.id.container6);
        container7 = (ImageView) findViewById(R.id.container7);
        container8 = (ImageView) findViewById(R.id.container8);
        receptacle1 = (ImageView)findViewById(R.id.rloc1);
        receptacle2 = (ImageView)findViewById(R.id.rloc2);
        receptacle3 = (ImageView)findViewById(R.id.rloc3);
        receptacle4 = (ImageView)findViewById(R.id.rloc4);
        receptacle5 = (ImageView)findViewById(R.id.rloc5);
        receptacle6 = (ImageView)findViewById(R.id.rloc6);
        receptacle7 = (ImageView)findViewById(R.id.rloc7);
        receptacle8 = (ImageView)findViewById(R.id.rloc8);

        RelativeLayout.LayoutParams container1Params = (RelativeLayout.LayoutParams) container1.getLayoutParams();
        container1Params.leftMargin = 225;
        container1.setLayoutParams(container1Params);

        RelativeLayout.LayoutParams container2Params = (RelativeLayout.LayoutParams) container2.getLayoutParams();
        container2Params.leftMargin = 375;
        container2.setLayoutParams(container2Params);

        RelativeLayout.LayoutParams container3Params = (RelativeLayout.LayoutParams) container3.getLayoutParams();
        container3Params.topMargin = 60;
        container3.setLayoutParams(container3Params);

        RelativeLayout.LayoutParams container5Params = (RelativeLayout.LayoutParams) container5.getLayoutParams();
        container5Params.topMargin = 60;
        container5.setLayoutParams(container5Params);

        RelativeLayout.LayoutParams container7Params = (RelativeLayout.LayoutParams) container7.getLayoutParams();
        container7Params.topMargin = 60;
        container7.setLayoutParams(container7Params);

        mContainers = new ImageView[MAX_LETTERS];
        mContainers[0] = container4;
        mContainers[1] = container6;
        mContainers[2] = container5;
        mContainers[3] = container3;
        mContainers[4] = container2;
        mContainers[5] = container8;
        mContainers[6] = container7;
        mContainers[7] = container1;

        mReceptaclesParent = (RelativeLayout) receptacle1.getParent();

        RelativeLayout.LayoutParams receptaclesParentParams = (RelativeLayout.LayoutParams) mReceptaclesParent.getLayoutParams();
        receptaclesParentParams.topMargin = 0;
        receptaclesParentParams.leftMargin = 0;
        mReceptaclesParent.setLayoutParams(receptaclesParentParams);

        RelativeLayout.LayoutParams receptacle1Params = (RelativeLayout.LayoutParams) receptacle1.getLayoutParams();
        receptacle1Params.leftMargin = 0;
        receptacle1.setLayoutParams(receptacle1Params);

        mReceptacles = new ImageView[MAX_LETTERS];
        mReceptacles[0] = receptacle1;
        mReceptacles[1] = receptacle2;
        mReceptacles[2] = receptacle3;
        mReceptacles[3] = receptacle4;
        mReceptacles[4] = receptacle5;
        mReceptacles[5] = receptacle6;
        mReceptacles[6] = receptacle7;
        mReceptacles[7] = receptacle8;

        mReceptacleEntries = new boolean[MAX_LETTERS];
        mReceptacleEntries[0] = false;
        mReceptacleEntries[1] = false;
        mReceptacleEntries[2] = false;
        mReceptacleEntries[3] = false;
        mReceptacleEntries[4] = false;
        mReceptacleEntries[5] = false;
        mReceptacleEntries[6] = false;
        mReceptacleEntries[7] = false;

        mThisActivity = this;

        /* Container BG test **
        int maxLength = MAX_LETTERS;

        for (int i = 0; i < maxLength; i++) {
            mContainers[i].setBackgroundColor(Color.argb((255 * (i + 1) / maxLength), 255, 0, 0));
        }
        */

        /* Receptacle BG test **
        int maxLength = MAX_LETTERS;

        for (int i = 0; i < maxLength; i++) {
            mReceptacles[i].setBackgroundColor(Color.RED);
        }
        */

        String drillData = getIntent().getExtras().getString("data");
        initializeData(drillData);
        prepareDrill();
    }

    private void initializeData(String drillData){

        // Debug
        System.out.println("SDFifteenActivity.initializeData > Debug: MC");

        try{
            allData = new JSONObject(drillData);
            mDragWordToWrite = allData.getString(DRAG_WORD_TO_WRITE);
            mThisIsSound = allData.getString(THIS_IS);
            drills = allData.getJSONArray("sentences");
            currentDrill = 0;
            mGameOn = false;
            mEndDrill = false;
        }
        catch (Exception ex){
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.initializeData");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    public void prepareDrill(){
        // Debug
        System.out.println("SDFifteenActivity.prepareDrill > Debug: MC");
        try{
            mDrillComplete = false;
            JSONObject drill = drills.getJSONObject(currentDrill);
            mCurrentSentenceSound = drill.getString(SENTENCE_SOUND);
            JSONArray words = drill.getJSONArray("words");
            resetListeners();
            resetContainers();
            resetReceptacles();
            resetReceptacleEntries();

            // Reset correct items
            correctItems = 0;
            int numberOfWords = words.length();
            if (numberOfWords > MAX_LETTERS) {
                numberOfWords = MAX_LETTERS;
            }
            mWordImageViews = new ImageView[numberOfWords];
            mWordImageResourceIds = new int[numberOfWords];
            mWordSounds = new String[numberOfWords];
            mWordOrder = FisherYates.shuffle(numberOfWords);

            for (int i = 0; i < mContainers.length; i++) {
                if (i >= numberOfWords) {
                    // Make rogue containers invisible
                    mContainers[i].setVisibility(View.INVISIBLE);
                    // Remove all rogue receptacles
                    mReceptaclesParent.removeView(mReceptacles[i]);
                } else {
                    // Get the word index (a shuffled index)
                    int containerIndex = mWordOrder[i];
                    System.out.println(":: BINDING Letter " + i + " to container " + containerIndex);
                    // Extract the word from data
                    JSONObject word = words.getJSONObject(i);
                    // Get word image resource id
                    int wordImageResourceId = word.getInt("word");
                    // Create new image view
                    ImageView container = mContainers[containerIndex];
                    container.setImageResource(wordImageResourceId);
                    // Add touch listener to image view
                    container.setOnTouchListener(new SoundDrillFifteenActivity.TouchAndDragListener(mThisActivity, containerIndex, i));
                    // Add image view to list of word image views
                    mWordImageViews[i] = container;
                    // Add word image resource id to list of word image resource ids
                    mWordImageResourceIds[i] = wordImageResourceId;
                    // Get and add the word sound
                    mWordSounds[i] = word.getString(WORD_SOUND);
                    // Get receptacle
                    ImageView receptacle = mReceptacles[i];
                    // Add on drag listener to receptacle
                    receptacle.setOnDragListener(new SoundDrillFifteenActivity.TouchAndDragListener(mThisActivity, i, i));
                    // Show container
                    container.setVisibility(View.VISIBLE);
                    // Show receptacle
                    receptacle.setVisibility(View.VISIBLE);
                }
            }

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            float screenWidth = displayMetrics.widthPixels;

            int hBase = (int) (density * 150);
            for (int i = 0; i < mWordImageViews.length; i++) {
                ImageView iv = mWordImageViews[i];
                Drawable d = iv.getDrawable();
                float hRatio = 0f;
                if (d != null) {
                    int dWidth = d.getIntrinsicWidth();
                    int dHeight = d.getIntrinsicHeight();
                    hRatio = ((float) dWidth / (float) dHeight);
                    System.out.println("[" + i + "]: " + dWidth + ", " + dHeight);
                }
                MarginLayoutParams ivParams = (MarginLayoutParams) iv.getLayoutParams();
                ivParams.height = hBase;
                ivParams.width = (int) (density * 300);//(int) ((float) hBase * hRatio);
                iv.setLayoutParams(ivParams);
                iv.setBackgroundColor(Color.argb(100, 255, 0, 0));
            }

            // RECEPTACLES
            int ivWidth = (int) (density * 100);
            int ivHeight = (int) (density * 100);
            int ivMargin = (int) (density * 10);

            int n = mReceptaclesParent.getChildCount();
            int w = (n * ivWidth) + ((n-1) * ivMargin);
            int h = ivHeight;
            // int imageId = FetchResource.imageId(this, allData, "object");

            MarginLayoutParams receptaclesLayout = (MarginLayoutParams) mReceptaclesParent.getLayoutParams();
            receptaclesLayout.width = w;
            receptaclesLayout.height = h * 2;
            receptaclesLayout.leftMargin = (int) ((screenWidth - w)/2) - 75;
            receptaclesLayout.topMargin = 900 - (h/2);
            mReceptaclesParent.setLayoutParams(receptaclesLayout);
            // mReceptaclesParent.setBackgroundColor(Color.argb(100, 255, 0, 0));

            for (int i = 0; i < n; i++) {
                ImageView iv = mReceptacles[i];
                MarginLayoutParams ivParams = (MarginLayoutParams) iv.getLayoutParams();
                if (i == 0) {
                    iv.setX(0);
                } else {
                    iv.setX(i * (ivMargin + ivWidth));
                }
                ivParams.leftMargin = 0;
                ivParams.topMargin = h/2;
                ivParams.width = ivWidth;
                ivParams.height = ivHeight;
                iv.setLayoutParams(ivParams);
            }

            playSound(DRAG_WORD_TO_WRITE, mDragWordToWrite);
        }
        catch (Exception ex){
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.prepareDrill");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private class TouchAndDragListener implements View.OnTouchListener, View.OnDragListener {

        private SoundDrillFifteenActivity mThisActivity;
        private int mShuffledIndex;
        private int mActualIndex;

        private TouchAndDragListener(SoundDrillFifteenActivity thisActivity, int shuffledIndex, int actualIndex) {
            mThisActivity = thisActivity;
            mShuffledIndex = shuffledIndex;
            mActualIndex = actualIndex;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // Debug
            System.out.println("SDFifteenActivity.TouchAndDragListener(class).onTouch > Debug: MC");

            try {
                boolean gameOn = mThisActivity.getGameOn();

                if (gameOn) {
                    mThisActivity.setCurrentItem(mActualIndex);
                    mThisActivity.playSound(WORD_SOUND, mThisActivity.getWordSounds()[mActualIndex]);
                    System.out.println("Moving word: actual(" + mActualIndex + "), shuffled(" + mShuffledIndex + ")");
                }
            } catch (Exception ex) {
                System.err.println("==============================");
                System.err.println("SDFifteenActivity.TouchAndDragListener(class).onTouch");
                System.err.println("------------------------------");
                ex.printStackTrace();
                System.err.println("==============================");
            }
            return dragItem(v, event);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Debug
            // System.out.println("SDFifteenActivity.TouchAndDragListener(class).onDrag > Debug: MC");

            try {
                // Get game on
                boolean gameOn = mThisActivity.getGameOn();

                if (gameOn) {
                    // Get the action
                    int action = event.getAction();

                    // Get list of receptacle entries
                    boolean[] receptacleEntries = mThisActivity.getReceptacleEntries();

                    // Isolate the current receptacle entry
                    boolean receptacleEntered = receptacleEntries[mActualIndex];

                    /**
                     * DRAG ENTERED
                     */
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        if (!receptacleEntered) {
                            receptacleEntries[mActualIndex] = true;
                        }

                        /**
                         * DRAG EXITED
                         */
                    } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        if (receptacleEntered) {
                            receptacleEntries[mActualIndex] = false;
                        }

                        /**
                         * ACTION DROP
                         */
                    } else if (event.getAction() == DragEvent.ACTION_DROP && receptacleEntered) {

                        // Disable game interactions
                        mThisActivity.setGameOn(false);

                        // Disable receptacle entry
                        receptacleEntries[mActualIndex] = false;

                        // Get current item
                        int currentItem = mThisActivity.getCurrentItem();

                        System.out.println("Current item DROP is: " + currentItem);
                        System.out.println("Current actual index DROP is: " + mActualIndex);
                        System.out.println("Current validation DROP is: " + (currentItem == mActualIndex));

                        // Check if current item relates to the receptacle's index
                        if (currentItem == mActualIndex) {

                            System.out.println("BINGO DROP!!!");

                            // Bingo!
                            // Get receptacles
                            ImageView[] receptacles = mThisActivity.getReceptacles();

                            // Get the image view of receptacle
                            ImageView receptacle = receptacles[mActualIndex];

                            // Get the word image resource id
                            int wordImageResourceId = mThisActivity.getWordImageResourceIds()[mActualIndex];

                            // Update image resource id of receptacle
                            receptacle.setImageResource(wordImageResourceId);

                            // Get containers
                            ImageView[] containers = mThisActivity.getContainers();

                            // Get word order
                            int[] wordOrder = mThisActivity.getWordOrder();

                            System.out.println(":: Letter Order: " + wordOrder[mActualIndex]);

                            // Get container for dragged image view
                            ImageView container = containers[wordOrder[mActualIndex]];

                            // Hide container
                            container.setVisibility(View.INVISIBLE);

                            // Get number of correct items and increment by 1
                            int correctItems = mThisActivity.getCorrectItems() + 1;

                            // Get max items possible
                            int maxItems = mThisActivity.getWordOrder().length;

                            // Check if max items reached
                            if (correctItems == maxItems) {

                                // Move on to next drill
                                // Get drills
                                JSONArray drills = mThisActivity.getDrills();

                                // Get current drill no and increment by 1
                                int currentDrill = mThisActivity.getCurrentDrill() + 1;

                                // Check if max drills reached
                                if (currentDrill == drills.length()) {

                                    // Set end drill to true
                                    mEndDrill = true;

                                    // Play affirmation sound
                                    mThisActivity.playSound(YAY, YAY);

                                } else {
                                    // Otherwise update drill no
                                    mThisActivity.setCurrentDrill(currentDrill);

                                    // Set drill complete to true
                                    mThisActivity.setDrillComplete(true);

                                    // Play affirmation sound
                                    mThisActivity.playSound(YAY, YAY);
                                }
                            } else {
                                // Update correct items
                                mThisActivity.setCorrectItems(correctItems);

                                // Re-enable game interactions
                                mThisActivity.setGameOn(true);

                                // Play affirmation sound
                                playSound(YAY, YAY);
                            }
                        } else {
                            // Re-enable game interactions
                            mThisActivity.setGameOn(true);

                            playSound(NAY, NAY);
                        }

                        /**
                         * ACTION DRAG END
                         */
                    }
                    return true;
                }
                return false;
            }
            catch (Exception ex){
                System.err.println("==============================");
                System.err.println("SDFifteenActivity.TouchAndDragListener(class).onDrag");
                System.err.println("------------------------------");
                ex.printStackTrace();
                System.err.println("==============================");
            }
            return false;
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){

        // Debug
        System.out.println("SDFifteenActivity.dragItem > Debug: MC");

        // Get game on
        boolean gameOn = mThisActivity.getGameOn();

        if (gameOn) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                try {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    return true;
                } catch (Exception ex) {
                    System.err.println("==============================");
                    System.err.println("SDFifteenActivity.dragItem");
                    System.err.println("------------------------------");
                    ex.printStackTrace();
                    System.err.println("==============================");
                }
            }
        }
        return false;
    }

    public void playSound(String tag, String sound) {

        // Debug
        System.out.println("SDFifteenActivity.playSound(\"" + tag + "\", \"" + sound + "\") > Debug: MC");

        try {
            // Declare sound path
            String soundPath;

            // Determine sound path
            if (sound.equalsIgnoreCase(YAY)) {
                soundPath = "android.resource://" + getApplicationContext().getPackageName() + "/" +
                        ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
            } else if (sound.equalsIgnoreCase(NAY)) {
                soundPath = "android.resource://" + getApplicationContext().getPackageName() + "/" +
                        ResourceSelector.getNegativeAffirmationSound(getApplicationContext());;
            } else {
                soundPath = FetchResource.sound(getApplicationContext(), sound);
            }

            // Init media player if required
            if (mp == null) {
                mp = new MediaPlayer();
            }

            // Reset media player
            mp.reset();

            // Set data source of media player
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));

            // Set listeners
            mp.setOnPreparedListener(new SoundDrillFifteenActivity.SoundListener(mThisActivity, tag, sound, soundPath));
            mp.setOnCompletionListener(new SoundDrillFifteenActivity.SoundListener(mThisActivity, tag, sound, soundPath));

            // Prepare media player to Rock and Rumble ~ ♩ ♪ ♫ ♬
            mp.prepare();
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.playSound)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            (new SoundDrillFifteenActivity.SoundListener(mThisActivity, tag, sound, null)).onCompletion(null);
        }
    }

    private class SoundListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SoundDrillFifteenActivity mThisActivity;
        private String mTag;
        private String mSound;
        private String mSoundPath;

        private SoundListener(SoundDrillFifteenActivity thisActivity, String tag, String sound, String soundPath) {
            mThisActivity = thisActivity;
            mTag = tag;
            mSound = sound;
            mSoundPath = soundPath;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

            // Debug
            System.out.println("SDFifteenActivity.SoundListener(class).onPrepared > Debug: MC");

            switch (mTag) {
                case DRAG_WORD_TO_WRITE: {
                    mp.start();
                    break;
                }
                case THIS_IS: {
                    mp.start();
                    break;
                }
                case SENTENCE_SOUND: {
                    mp.start();
                    break;
                }
                case WORD_SOUND: {
                    mp.start();
                    break;
                }
                case NAY: {
                    mp.start();
                    break;
                }
                case YAY: {
                    mp.start();
                    break;
                }
                default: {
                    break;
                }
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {

            // Debug
            System.out.println("SDFifteenActivity.SoundListener(class).onCompletion > Debug: MC");

            switch (mTag) {
                case DRAG_WORD_TO_WRITE: {
                    mThisActivity.playSound(SENTENCE_SOUND, mThisActivity.getCurrentSentenceSound());
                    break;
                }
                case THIS_IS: {
                    break;
                }
                case SENTENCE_SOUND: {
                    mThisActivity.setGameOn(true);
                    break;
                }
                case WORD_SOUND: {
                    break;
                }
                case NAY: {
                    break;
                }
                case YAY: {
                    if (mThisActivity.getEndDrill()) {

                        // Release media player
                        mp.release();

                        // Finish activity
                        mThisActivity.finish();

                    } else if (mThisActivity.getDrillComplete()) {

                        // Prepare next drill
                        mThisActivity.prepareDrill();
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void resetListeners() {

        // Debug
        System.out.println("SDFifteenActivity.resetListeners > Debug: MC");

        try {
            if (mWordImageViews != null) {

                for (ImageView wordImageView: mWordImageViews) {

                    // Remove drag listener
                    wordImageView.setOnDragListener(null);

                    // Remove touch listener
                    wordImageView.setOnTouchListener(null);
                }
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.resetContainers)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetContainers() {

        // Debug
        System.out.println("SDFifteenActivity.resetContainers > Debug: MC");

        try {
            if (mContainers != null) {

                for (ImageView container: mContainers) {

                    // Make container invisible
                    container.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.resetContainers)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetReceptacles() {

        // Debug
        System.out.println("SDFifteenActivity.resetReceptacles > Debug: MC");

        try {
            if (mReceptaclesParent != null) {

                mReceptaclesParent.removeAllViews();

                if (mReceptacles != null) {

                    for (ImageView receptacle: mReceptacles) {

                        // Reset image resource
                        receptacle.setImageResource(R.drawable.line);

                        // Reset visibility of receptacle
                        receptacle.setVisibility(View.INVISIBLE);

                        // Re-add receptacle to receptacles parent
                        mReceptaclesParent.addView(receptacle);
                    }
                }
            } else {
                throw new Exception("Receptacles parent is null!");
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.resetReceptacles)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetReceptacleEntries() {

        // Debug
        System.out.println("SDFifteenActivity.resetReceptacleEntries > Debug: MC");

        try {
            for (int i = 0; i < mReceptacleEntries.length; i++) {
                mReceptacleEntries[i] = false;
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDFifteenActivity.resetReceptacleEntries)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    public void setCurrentDrill(int drill) {
        currentDrill = drill;
    }

    public void setCurrentSentenceSound(String currentSentenceSound) {
        mCurrentSentenceSound = currentSentenceSound;
    }

    public void setCurrentItem(int item) {
        currentItem = item;
    }

    public void setCorrectItems(int correctItems) {
        this.correctItems = correctItems;
    }

    public void setGameOn(boolean gameOn) {
        mGameOn = gameOn;
    }

    public void setEndDrill(boolean endDrill) {
        mEndDrill = endDrill;
    }

    public void setDrillComplete(boolean drillComplete) {
        mDrillComplete = drillComplete;
    }

    public JSONArray getDrills() {
        return drills;
    }

    public int getCurrentDrill() {
        return currentDrill;
    }

    public String getCurrentSentenceSound() {
        return mCurrentSentenceSound;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public int getCorrectItems() {
        return correctItems;
    }

    public boolean getGameOn() {
        return mGameOn;
    }

    public boolean getEndDrill() {
        return mEndDrill;
    }

    public boolean getDrillComplete() {
        return mDrillComplete;
    }

    public ImageView[] getContainers() {
        return mContainers;
    }

    public ImageView[] getReceptacles() {
        return mReceptacles;
    }

    public boolean[] getReceptacleEntries() {
        return mReceptacleEntries;
    }

    public int[] getWordOrder() {
        return mWordOrder;
    }

    public int[] getWordImageResourceIds() {
        return mWordImageResourceIds;
    }

    public String[] getWordSounds() {
        return mWordSounds;
    }

    public ImageView[] getLetterImageViews() {
        return mWordImageViews;
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
