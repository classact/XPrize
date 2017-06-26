package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
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
import android.widget.Toast;

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

    private RelativeLayout mPlaceHoldersParent;
    private List<ImageView> mPlaceholders;
    private RelativeLayout mRootView;
    private final Context THIS = this;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Debug
        // System.out.println("SDFifteenActivity.OnCreate > Debug: MC");
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

        mContainers = new ImageView[MAX_LETTERS];
        mContainers[0] = container1;
        mContainers[1] = container2;
        mContainers[2] = container3;
        mContainers[3] = container4;
        mContainers[4] = container5;
        mContainers[5] = container6;
        mContainers[6] = container7;
        mContainers[7] = container8;

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

        mPlaceholders = new ArrayList<>();

        mPlaceHoldersParent = new RelativeLayout(THIS);
        RelativeLayout.LayoutParams mPlaceHoldersParentLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        mPlaceHoldersParent.setLayoutParams(mPlaceHoldersParentLayout);
        mRootView.addView(mPlaceHoldersParent);

        handler = new Handler();

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
        // System.out.println("SDFifteenActivity.initializeData > Debug: MC");
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
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void prepareDrill(){
        // Debug
        // System.out.println("SDFifteenActivity.prepareDrill > Debug: MC");
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
            // System.out.println("# of words: " + numberOfWords);
            mWordImageViews = new ImageView[numberOfWords];
            mWordImageResourceIds = new int[numberOfWords];
            mWordSounds = new String[numberOfWords];
            mWordOrder = FisherYates.shuffle(numberOfWords);

            mReceptaclesParent.removeAllViews();
            for (int i = 0; i < mReceptacles.length; i++) {
                ImageView iv = mReceptacles[i];
                mReceptaclesParent.addView(iv);
                RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                ivParams.topMargin = 0;
                ivParams.leftMargin = 0;
                ivParams.width = 0;
                ivParams.height = 0;
                iv.setX(0f);
                iv.setY(0f);
                iv.setScaleX(1f);
                iv.setScaleY(1f);
                iv.setLayoutParams(ivParams);
            }

            int containersLength = mContainers.length;
            for (int i = 0; i < containersLength; i++) {
                ImageView iv = mContainers[i];
                RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                ivParams.topMargin = 0;
                ivParams.leftMargin = 0;
                ivParams.width = 0;
                ivParams.height = 0;
                iv.setX(0f);
                iv.setY(0f);
                iv.setScaleX(1f);
                iv.setScaleY(1f);
                iv.setLayoutParams(ivParams);
                // int ivFraction = (int) (((float) (i+1)/containersLength) * 255);
                // iv.setBackgroundColor(Color.argb(ivFraction, 255, 0, 0));
            }

            mPlaceHoldersParent.removeAllViews();
            mPlaceholders.clear();

            for (int i = 0; i < mContainers.length; i++) {
                if (i >= numberOfWords) {
                    ImageView c = mContainers[i];
                    c.setVisibility(View.INVISIBLE);
                    c.setOnTouchListener(null);
                    mReceptaclesParent.removeView(mReceptacles[i]);
                } else {
                    // Get the word index (a shuffled index)
                    int wi = mWordOrder[i];
                    // System.out.println(":: BINDING Letter " + i + " to container " + containerIndex);
                    // Extract the word from data
                    JSONObject word = words.getJSONObject(wi);
                    int wordImageResourceId = word.getInt("word");
                    // System.out.println("WR_ID: " + wordImageResourceId);
                    // Create new image view
                    ImageView container = mContainers[i];
                    container.setImageResource(wordImageResourceId);
                    container.setOnTouchListener(new SoundDrillFifteenActivity.TouchAndDragListener(mThisActivity, wi, wi));
                    // Add image view to list of word image views
                    mWordImageViews[wi] = container;
                    mWordImageResourceIds[wi] = wordImageResourceId;
                    mWordSounds[wi] = word.getString(WORD_SOUND);
                    // Get receptacle
                    ImageView receptacle = mReceptacles[wi];
                    receptacle.setOnDragListener(new SoundDrillFifteenActivity.TouchAndDragListener(mThisActivity, wi, i));
                    // Show container
                    container.setVisibility(View.VISIBLE);
                    receptacle.setVisibility(View.VISIBLE);
                }
            }

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            float screenWidth = displayMetrics.widthPixels;

            // RECEPTACLES
            int ivWidth = (int) (density * 100);
            int ivHeight = (int) (density * 100);
            int ivMargin = (int) (density * 20);

            int n = mWordImageViews.length;
            int w = 0;
            int h = ivHeight;
            float[] wordRatios = new float[n];
            int[] wordWidths = new int[n];

            // RESIZE WORDS
            int letterCount = 0;
            int hBase = (int) (density * 150);

            for (int i = 0; i < n; i++) {
                ImageView iv = mWordImageViews[i];
                Drawable d = iv.getDrawable();
                float hRatio = 0f;
                if (d != null) {
                    int dWidth = d.getIntrinsicWidth();
                    int dHeight = d.getIntrinsicHeight();
                    hRatio = ((float) dWidth / (float) dHeight);
                    wordRatios[letterCount] = hRatio;
                    int wordWidth = (int) (hRatio * h);
                    wordWidths[letterCount] = wordWidth;
                    w += wordWidth;
                    if (letterCount > 0) {
                        w += ivMargin;
                    }
                    letterCount++;
                }
                RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                ivParams.height = hBase;
                ivParams.width = (int) ((float) hBase * hRatio);
                iv.setLayoutParams(ivParams);
            }

            RelativeLayout.LayoutParams rpParams = (RelativeLayout.LayoutParams) mReceptaclesParent.getLayoutParams();
            rpParams.topMargin = 875;
            rpParams.leftMargin = (int) (screenWidth - w)/2 - 65;
            rpParams.width = w;
            rpParams.height = ivHeight + 20;
            mReceptaclesParent.setLayoutParams(rpParams);

            RelativeLayout.LayoutParams ppParams = (RelativeLayout.LayoutParams) mPlaceHoldersParent.getLayoutParams();
            ppParams.topMargin = 885;
            ppParams.leftMargin = (int) (screenWidth - w)/2 - 65;
            ppParams.width = w;
            ppParams.height = ivHeight;
            mPlaceHoldersParent.setLayoutParams(ppParams);

            float cx = 0f;
            float rx = 0f;
            float px = 0f;
            for (int i = 0; i < n; i++) {
                ImageView p = new ImageView(THIS);
                mPlaceHoldersParent.addView(p);
                mPlaceholders.add(p);
                int ci = mWordOrder[i];
                ImageView c = mContainers[i];
                ImageView r = mReceptacles[i];

                RelativeLayout.LayoutParams cParams = (RelativeLayout.LayoutParams) c.getLayoutParams();
                RelativeLayout.LayoutParams rParams = (RelativeLayout.LayoutParams) r.getLayoutParams();
                RelativeLayout.LayoutParams pParams = (RelativeLayout.LayoutParams) p.getLayoutParams();

                int containerWidth = wordWidths[ci];
                int width = wordWidths[i];
                int height = ivHeight;

                if (i > 0) {
                    rx += wordWidths[i-1] + ivMargin;
                    px += wordWidths[i-1] + ivMargin;
                }

                cParams.topMargin = 0;
                cParams.leftMargin = 0;
                cParams.width = containerWidth;
                cParams.height = height;
                c.setLayoutParams(cParams);

                // Receptacles
                float rXDiff = (width - h)/2;

                rParams.topMargin = 0;
                rParams.leftMargin = 0;
                rParams.width = h;
                rParams.height = height + 20;
                r.setScaleX(wordRatios[i]);
                r.setX(rx + rXDiff);
                r.setLayoutParams(rParams);

                // Placeholders
                pParams.topMargin = 0;
                pParams.leftMargin = 0;
                pParams.width = width;
                pParams.height = height;
                p.setX(px);
                p.setLayoutParams(pParams);
            }

            if (n > 2) {
                int[] levels = new int[2];
                levels[0] = n / 2;
                levels[1] = n - levels[0];

                int levelOne = levels[0];
                int divisorOne = levelOne + 1;
                int sectionOne = (int) (screenWidth / divisorOne);

                int count = 0;

                int l1TM = (int) (density * 90);
                for (int i = 1; i <= levelOne; i++) {
                    ImageView iv = mContainers[count];
                    RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    iv.setX((i * sectionOne) - (ivParams.width / 2));
                    iv.setY(l1TM);
                    count++;
                }

                int levelTwo = levels[1];
                int divisorTwo = levelTwo + 1;
                int sectionTwo = (int) (screenWidth / divisorTwo);

                int l2TM = (int) (density * 220);
                for (int i = 1; i <= levelTwo; i++) {
                    ImageView iv = mContainers[count];
                    RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    iv.setX((i * sectionTwo) - (ivParams.width / 2));
                    iv.setY(l2TM);
                    count++;
                }

                for (int i = 0; i < count; i++) {
                    ImageView iv = mContainers[i];
                    RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    System.out.println("M[" + i + "," + iv.getX() + "]: " + ivParams.topMargin);
                }

            } else {
                int[] levels = new int[1];
                levels[0] = n;

                int levelOne = levels[0];
                int divisorOne = levelOne + 1;
                int sectionOne = (int) (screenWidth / divisorOne);

                int count = 0;

                for (int i = 1; i <= levelOne; i++) {
                    ImageView iv = mContainers[count];
                    RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    ivParams.topMargin = (int) (density * 90);
                    iv.setLayoutParams(ivParams);
                    iv.setX((i * sectionOne) - (ivParams.width / 2));
                    count++;
                }
            }

            playSound(DRAG_WORD_TO_WRITE, mDragWordToWrite);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
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
            // System.out.println("SDFifteenActivity.TouchAndDragListener(class).onTouch > Debug: MC");
            try {
                boolean gameOn = mThisActivity.getGameOn();
                if (gameOn) {
                    mThisActivity.setCurrentItem(mActualIndex);
                    mThisActivity.playSound(WORD_SOUND, mThisActivity.getWordSounds()[mActualIndex]);
                    // System.out.println("Moving word: actual(" + mActualIndex + "), shuffled(" + mShuffledIndex + ")");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                    boolean[] receptacleEntries = mThisActivity.getReceptacleEntries();
                    boolean receptacleEntered = receptacleEntries[mShuffledIndex];
                    /**
                     * DRAG ENTERED
                     */
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        if (!receptacleEntered) {
                            receptacleEntries[mShuffledIndex] = true;
                        }
                        /**
                         * DRAG EXITED
                         */
                    } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        if (receptacleEntered) {
                            receptacleEntries[mShuffledIndex] = false;
                        }
                        /**
                         * ACTION DROP
                         */
                    } else if (event.getAction() == DragEvent.ACTION_DROP && receptacleEntered) {
                        // Disable game interactions
                        mThisActivity.setGameOn(false);
                        // Disable receptacle entry
                        receptacleEntries[mShuffledIndex] = false;
                        // Get current item
                        int currentItem = mThisActivity.getCurrentItem();

                        // System.out.println("Current item DROP is: " + currentItem);
                        // System.out.println("Current actual index DROP is: " + mActualIndex);
                        // System.out.println("Current validation DROP is: " + (currentItem == mActualIndex));

                        // Check if current item relates to the receptacle's index
                        if (currentItem == mShuffledIndex) {
                            // System.out.println("BINGO DROP!!!");
                            // Get receptacles
                            ImageView[] receptacles = mThisActivity.getReceptacles();
                            // Get the image view of receptacle
                            ImageView receptacle = receptacles[mShuffledIndex];
                            ImageView placeHolder = mPlaceholders.get(mShuffledIndex);
                            // Get the word image resource id
                            int wordImageResourceId = mThisActivity.getWordImageResourceIds()[mShuffledIndex];
                            // Update image resource id of receptacle
                            placeHolder.setImageResource(wordImageResourceId);
                            receptacle.setImageResource(0);
                            // Get containers
                            ImageView[] containers = mThisActivity.getContainers();
                            // Get container for dragged image view
                            ImageView container = containers[mActualIndex];
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
                                    int mpDurLeft = 0;
                                    if (mp != null && mp.isPlaying()) {
                                        mpDurLeft = mp.getDuration() - mp.getCurrentPosition();
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mThisActivity.playSound(YAY, YAY);
                                        }
                                    }, 600 + mpDurLeft);
                                } else {
                                    // Otherwise update drill no
                                    mThisActivity.setCurrentDrill(currentDrill);
                                    // Set drill complete to true
                                    mThisActivity.setDrillComplete(true);
                                    // Play affirmation sound
                                    int mpDurLeft = 0;
                                    if (mp != null && mp.isPlaying()) {
                                        mpDurLeft = mp.getDuration() - mp.getCurrentPosition();
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mThisActivity.playSound(YAY, YAY);
                                        }
                                    }, 600 + mpDurLeft);
                                }
                            } else {
                                // Update correct items
                                mThisActivity.setCorrectItems(correctItems);
                                // Re-enable game interactions
                                mThisActivity.setGameOn(true);
                                // Play affirmation sound
                                // playSound(YAY, YAY);
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
                ex.printStackTrace();
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        // Debug
        // System.out.println("SDFifteenActivity.dragItem > Debug: MC");
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
                    ex.printStackTrace();
                    Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        return false;
    }

    public void playSound(String tag, String sound) {
        // Debug
        // System.out.println("SDFifteenActivity.playSound(\"" + tag + "\", \"" + sound + "\") > Debug: MC");
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
            ex.printStackTrace();
            // System.err.println("==============================");
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
            // System.out.println("SDFifteenActivity.SoundListener(class).onPrepared > Debug: MC");
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
            // System.out.println("SDFifteenActivity.SoundListener(class).onCompletion > Debug: MC");
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
                        // Release handle
                        handler = null;
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
        //mSystem.out.println("SDFifteenActivity.resetListeners > Debug: MC");
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
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void resetContainers() {
        // Debug
        // System.out.println("SDFifteenActivity.resetContainers > Debug: MC");
        try {
            if (mContainers != null) {
                for (ImageView container: mContainers) {
                    // Make container invisible
                    container.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void resetReceptacles() {
        // Debug
        // System.out.println("SDFifteenActivity.resetReceptacles > Debug: MC");
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
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void resetReceptacleEntries() {
        // Debug
        // System.out.println("SDFifteenActivity.resetReceptacleEntries > Debug: MC");
        try {
            for (int i = 0; i < mReceptacleEntries.length; i++) {
                mReceptacleEntries[i] = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
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
        handler = null;
        if (mp != null) {
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}