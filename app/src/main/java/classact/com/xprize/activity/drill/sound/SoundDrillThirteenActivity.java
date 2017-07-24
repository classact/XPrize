package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.WordLetterLayout;

public class SoundDrillThirteenActivity extends AppCompatActivity {

    private LinearLayout container1;
    private LinearLayout container2;
    private LinearLayout container3;
    private LinearLayout container4;
    private LinearLayout container5;
    private LinearLayout container6;
    private LinearLayout container7;
    private LinearLayout container8;

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
    private JSONArray letters;

    private LinearLayout[] mContainers;
    private RelativeLayout mReceptaclesParent;
    private ImageView[] mReceptacles;
    private ImageView[] mLetterImageViews;
    private boolean mGameOn;
    private int[] mLetterImageResourceIds;
    private int[] mLetterOrder;
    private boolean[] mReceptacleEntries;
    private boolean mDrillComplete;
    private boolean mEndDrill;
    private SoundDrillThirteenActivity mThisActivity;

    private String mDragTheLettersToWriteSound;
    private String mYouGotSound;
    private String mCurrentWordSound;

    private final int MAX_LETTERS = 8;
    private final String DRAG_THE_LETTERS_TO_WRITE = "drag_the_letters_to_write";
    private final String YOU_GOT = "you_got";
    private final String SOUND = "sound";
    private final String YAY = "YAY_001";
    private final String NAY = "NAY_001";
    private Handler handler;

    private RelativeLayout mRootView;
    private RelativeLayout mReceptaclesView;
    private final Context THIS = this;

    private LinkedHashMap<String, List<Integer>> letterMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Debug
        System.out.println("SDThirteenActivity.OnCreate > Debug: MC");

        setContentView(R.layout.activity_sound_drill_thirteen);
        container1 = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container3 = (LinearLayout) findViewById(R.id.container3);
        container4 = (LinearLayout) findViewById(R.id.container4);
        container5 = (LinearLayout) findViewById(R.id.container5);
        container6 = (LinearLayout) findViewById(R.id.container6);
        container7 = (LinearLayout) findViewById(R.id.container7);
        container8 = (LinearLayout) findViewById(R.id.container8);
        receptacle1 = (ImageView)findViewById(R.id.loc1);
        receptacle2 = (ImageView)findViewById(R.id.loc2);
        receptacle3 = (ImageView)findViewById(R.id.loc3);
        receptacle4 = (ImageView)findViewById(R.id.loc4);
        receptacle5 = (ImageView)findViewById(R.id.loc5);
        receptacle6 = (ImageView)findViewById(R.id.loc6);
        receptacle7 = (ImageView)findViewById(R.id.loc7);
        receptacle8 = (ImageView)findViewById(R.id.loc8);

        mRootView = (RelativeLayout) container1.getParent();

        MarginLayoutParams container1Params = (MarginLayoutParams) container1.getLayoutParams();
        container1Params.leftMargin = 225;
        container1.setLayoutParams(container1Params);

        MarginLayoutParams container2Params = (MarginLayoutParams) container2.getLayoutParams();
        container2Params.leftMargin = 375;
        container2.setLayoutParams(container2Params);

        MarginLayoutParams container3Params = (MarginLayoutParams) container3.getLayoutParams();
        container3Params.topMargin = 60;
        container3.setLayoutParams(container3Params);

        MarginLayoutParams container5Params = (MarginLayoutParams) container5.getLayoutParams();
        container5Params.topMargin = 60;
        container5.setLayoutParams(container5Params);

        MarginLayoutParams container7Params = (MarginLayoutParams) container7.getLayoutParams();
        container7Params.topMargin = 60;
        container7.setLayoutParams(container7Params);

        mContainers = new LinearLayout[MAX_LETTERS];
        mContainers[0] = container4;
        mContainers[1] = container6;
        mContainers[2] = container5;
        mContainers[3] = container3;
        mContainers[4] = container2;
        mContainers[5] = container8;
        mContainers[6] = container7;
        mContainers[7] = container1;

        mReceptaclesParent = (RelativeLayout) findViewById(R.id.lineContainer);

        MarginLayoutParams receptaclesParentParams = (MarginLayoutParams) mReceptaclesParent.getLayoutParams();
        receptaclesParentParams.topMargin = 535;
        mReceptaclesParent.setLayoutParams(receptaclesParentParams);

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

        handler = new Handler();

        letterMap = new LinkedHashMap<>();

        String drillData = getIntent().getExtras().getString("data");
        initializeData(drillData);
        prepareDrill();
    }

    private void initializeData(String drillData){

        // Debug
        System.out.println("SDThirteenActivity.initializeData > Debug: MC");

        try{
            allData = new JSONObject(drillData);
            mDragTheLettersToWriteSound = allData.getString("drag_the_letters_to_write");
            mYouGotSound = allData.getString("you_got");
            drills = allData.getJSONArray("words");
            currentDrill = 0;
            mGameOn = false;
            mEndDrill = false;
        }
        catch (Exception ex){
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.initializeData");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
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

    public void prepareDrill(){

        // Debug
        System.out.println("SDThirteenActivity.prepareDrill > Debug: MC");

        try{
            mDrillComplete = false;
            JSONObject drill = drills.getJSONObject(currentDrill);
            mCurrentWordSound = drill.getString("sound");
            letters = drill.getJSONArray("letters");
            resetListeners();
            resetContainers();
            resetReceptacles();
            resetReceptacleEntries();

            // Reset correct items
            correctItems = 0;

            int numberOfLetters = letters.length();
            if (numberOfLetters > MAX_LETTERS) {
                numberOfLetters = MAX_LETTERS;
            }
            mLetterImageViews = new ImageView[numberOfLetters];
            mLetterImageResourceIds = new int[numberOfLetters];
            mLetterOrder = FisherYates.shuffle(numberOfLetters);

            for (int i = 0; i < mContainers.length; i++) {
                if (i >= numberOfLetters) {
                    // Make rogue containers invisible
                    mContainers[i].setVisibility(View.INVISIBLE);
                    // Remove all rogue receptacles
                    mReceptaclesParent.removeView(mReceptacles[i]);

                } else {
                    // Get the letter index (a shuffled index)
                    int containerIndex = mLetterOrder[i];
                    System.out.println(":: BINDING Letter " + i + " to container " + containerIndex);
                    // Extract the letter from data
                    JSONObject letter = letters.getJSONObject(i);
                    // Get letter image resource id
                    int letterImageResourceId = letter.getInt("letter");
                    // Create new image view
                    ImageView letterImageView = new ImageView(getApplicationContext());
                    letterImageView.setImageResource(letterImageResourceId);
                    // Add touch listener to image view
                    letterImageView.setOnTouchListener(new TouchAndDragListener(mThisActivity, containerIndex, i));
                    // Get container
                    LinearLayout container = mContainers[containerIndex];
                    // Add image view to container
                    container.addView(letterImageView);
                    // Add image view to list of letter image views
                    mLetterImageViews[i] = letterImageView;
                    // Add letter image resource id to list of letter image resource ids
                    mLetterImageResourceIds[i] = letterImageResourceId;
                    // Get receptacle
                    ImageView receptacle = mReceptacles[i];
                    // Add on drag listener to receptacle
                    receptacle.setOnDragListener(new TouchAndDragListener(mThisActivity, i, i));
                    // Show container
                    container.setVisibility(View.VISIBLE);
                    // Show receptacle
                    receptacle.setVisibility(View.VISIBLE);
                }
            }

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            float screenWidth = displayMetrics.widthPixels;
            int ivWidth = (int) (density * 100);
            int ivHeight = (int) (density * 100);
            int ivMargin = (int) (density * 10);

            if (mReceptaclesView == null) {
                mReceptaclesView = new RelativeLayout(THIS);
                RelativeLayout.LayoutParams receptaclesLayout = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                mReceptaclesView.setLayoutParams(receptaclesLayout);
                mRootView.addView(mReceptaclesView);
            }
            mReceptaclesView.removeAllViews();

            int n = letters.length();
            int w = (n * ivWidth) + ((n-1) * ivMargin);
            int h = ivHeight;
            // int imageId = FetchResource.imageId(this, allData, "object");

            RelativeLayout.LayoutParams receptaclesLayout = (RelativeLayout.LayoutParams) mReceptaclesView.getLayoutParams();
            receptaclesLayout = (RelativeLayout.LayoutParams) mReceptaclesView.getLayoutParams();
            receptaclesLayout.width = w;
            receptaclesLayout.height = h * 2;
            receptaclesLayout.leftMargin = (int) ((screenWidth - w)/2) - 75;
            receptaclesLayout.topMargin = 875 - (h/2);
            mReceptaclesView.setLayoutParams(receptaclesLayout);
            // mReceptaclesView.setBackgroundColor(Color.argb(100, 255, 0, 0));

            List<ImageView> lVs = new ArrayList<>();
            List<Integer> lRVs = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                lRVs.add(letters.getJSONObject(i).getInt("letter"));
            }

            for (int i = 0; i < n; i++) {
                ImageView iv = new ImageView(THIS);
                iv.setImageResource(lRVs.get(i));
                // iv.setBackgroundColor(Color.argb(100, 0, 0, 255));
                mReceptaclesView.addView(iv);
                RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                if (i == 0) {
                    iv.setX(0);
                } else {
                    iv.setX(i * (ivMargin + ivWidth));
                }
                ivParams.topMargin = h/2;
                ivParams.width = ivWidth;
                ivParams.height = ivHeight;
                iv.setLayoutParams(ivParams);
            }

            RelativeLayout.LayoutParams mRPLayout = (RelativeLayout.LayoutParams) mReceptaclesParent.getLayoutParams();
            mRPLayout.leftMargin = (int) ((screenWidth - w)/2) - 75;
            mRPLayout.topMargin = 875 - (h/2) + 25;
            mReceptaclesParent.setLayoutParams(mRPLayout);

            for (int i = 0; i < n; i++) {
                ImageView iv = mReceptacles[i];
                iv.setImageResource(R.drawable.line);
                RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                if (i == 0) {
                    iv.setX(0);
                } else {
                    iv.setX(i * (ivMargin + ivWidth));
                }
                ivParams.topMargin = h/2;
                ivParams.leftMargin = 0;
                ivParams.width = ivWidth;
                ivParams.height = ivHeight;
                iv.setLayoutParams(ivParams);
            }

            for (int i = 0; i < n; i++) {
                lVs.add((ImageView) mReceptaclesView.getChildAt(i));
            }

            String word = "";
            for (int i = 0; i < n; i++) {
                String letterString = letters.getJSONObject(i).getString("letter_string");
                word += letterString;
            }

            for (int i = 0; i < letters.length(); i++) {
                JSONObject letter = letters.getJSONObject(i);
                String letterString = letter.getString("letter_string");

                if (letterMap.containsKey(letterString)) {
                    List<Integer> letterIndexList = letterMap.get(letterString);
                    if (letterIndexList != null) {
                        letterIndexList.add(i);
                    } else {
                        letterIndexList = new ArrayList<>();
                        letterIndexList.add(i);
                    }
                } else {
                    List<Integer> letterIndexList = new ArrayList<>();
                    letterIndexList.add(i);
                    letterMap.put(letterString, letterIndexList);
                }
            }

            float letterWidth = ivWidth;
            float letterScale = 1.f;

            lVs = WordLetterLayout.level(
                    THIS,
                    lVs,
                    lRVs,
                    word,
                    displayMetrics,
                    letterWidth,
                    letterScale
            );

            for (int i = 0; i < lVs.size(); i++) {
                ImageView iv = lVs.get(i);
                iv.setImageResource(0);
            }

            playSound(DRAG_THE_LETTERS_TO_WRITE, mDragTheLettersToWriteSound);
        }
        catch (Exception ex){
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.prepareDrill");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private class TouchAndDragListener implements View.OnTouchListener, View.OnDragListener {

        private SoundDrillThirteenActivity mThisActivity;
        private int mShuffledIndex;
        private int mActualIndex;

        private TouchAndDragListener(SoundDrillThirteenActivity thisActivity, int shuffledIndex, int actualIndex) {
            mThisActivity = thisActivity;
            mShuffledIndex = shuffledIndex;
            mActualIndex = actualIndex;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            try {
                // Debug
                System.out.println("SDThirteenActivity.TouchAndDragListener(class).onTouch > Debug: MC");

                mThisActivity.setCurrentItem(mActualIndex);
                System.out.println("Moving letter: actual(" + mActualIndex + "), shuffled(" + mShuffledIndex + ")");

                boolean gameOn = mThisActivity.getGameOn();
                if (gameOn) {
                    String sound = letters.getJSONObject(mActualIndex).getString("sound");
                    Runnable r = null;
                    playSound(sound, r);
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
            // System.out.println("SDThirteenActivity.TouchAndDragListener(class).onDrag > Debug: MC");

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

                        // A
                        JSONObject letterObjectSource = letters.getJSONObject(currentItem);
                        String letterStringSource = letterObjectSource.getString("letter_string");
                        System.out.println("Selected [" + currentItem + "]: " + letterStringSource);

                        // B
                        JSONObject letterObjectDestination = letters.getJSONObject(mActualIndex);
                        String letterStringDestination = letterObjectDestination.getString("letter_string");
                        System.out.println("To [" + mActualIndex + "]: " + letterStringDestination);

                        // Check if current item relates to the receptacle's index
                        // if (currentItem == mActualIndex) {
                        if (letterStringSource.equalsIgnoreCase(letterStringDestination)) {

                            System.out.println("BINGO DROP!!!");

                            // Bingo!
                            // Get receptacles
                            ImageView[] receptacles = mThisActivity.getReceptacles();
                            receptacles[mActualIndex].setImageResource(0);

                            // Get the image view of receptacle
                            ImageView receptacle = (ImageView) mReceptaclesView.getChildAt(mActualIndex);

                            // Get the letter image resource id
                            int letterImageResourceId = mThisActivity.getLetterImageResourceIds()[mActualIndex];

                            // Update image resource id of receptacle
                            receptacle.setImageResource(letterImageResourceId);

                            // Get containers
                            LinearLayout[] containers = mThisActivity.getContainers();

                            // Get letter order
                            int[] letterOrder = mThisActivity.getLetterOrder();

                            System.out.println(":: Letter Order: " + letterOrder[currentItem]);

                            // Get container for dragged image view
                            LinearLayout container = containers[letterOrder[currentItem]];

                            // Hide container
                            container.setVisibility(View.INVISIBLE);

                            // Get number of correct items and increment by 1
                            int correctItems = mThisActivity.getCorrectItems() + 1;

                            // Get max items possible
                            int maxItems = mThisActivity.getLetterOrder().length;

                            // Check if max items reached
                            if (correctItems == maxItems) {

                                // Move on to next drill
                                // Get drills
                                JSONArray drills = mThisActivity.getDrills();

                                // Get current drill no and increment by 1
                                int currentDrill = mThisActivity.getCurrentDrill() + 1;

                                boolean affirm = false;

                                // Check if max drills reached
                                if (currentDrill == drills.length()) {

                                    // Set end drill to true
                                    mEndDrill = true;

                                    // Set affirmation truth
                                    affirm = true;

                                } else {
                                    // Otherwise update drill no
                                    mThisActivity.setCurrentDrill(currentDrill);

                                    // Set drill complete to true
                                    mThisActivity.setDrillComplete(true);

                                    // Set affirmation truth
                                    affirm = true;
                                }
                                if (affirm) {
                                    // Play full sound
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            playSound(mCurrentWordSound, new Runnable() {
                                                @Override
                                                public void run() {
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // Play affirmation sound
                                                            mThisActivity.playSound(YAY, YAY);
                                                        }
                                                    }, 250);
                                                }
                                            });
                                        }
                                    }, 250);
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
                System.err.println("==============================");
                System.err.println("SDThirteenActivity.TouchAndDragListener(class).onDrag");
                System.err.println("------------------------------");
                ex.printStackTrace();
                System.err.println("==============================");
            }
            return false;
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){

        // Debug
        System.out.println("SDThirteenActivity.dragItem > Debug: MC");

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
                    System.err.println("SDThirteenActivity.dragItem");
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
        System.out.println("SDThirteenActivity.playSound > Debug: MC");

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
            mp.setOnPreparedListener(new SoundListener(mThisActivity, tag, sound, soundPath));
            mp.setOnCompletionListener(new SoundListener(mThisActivity, tag, sound, soundPath));

            // Prepare media player to Rock and Rumble ~ ♩ ♪ ♫ ♬
            mp.prepare();
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.playSound)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            (new SoundListener(mThisActivity, tag, sound, null)).onCompletion(null);
        }
    }

    private class SoundListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SoundDrillThirteenActivity mThisActivity;
        private String mTag;
        private String mSound;
        private String mSoundPath;

        private SoundListener(SoundDrillThirteenActivity thisActivity, String tag, String sound, String soundPath) {
            mThisActivity = thisActivity;
            mTag = tag;
            mSound = sound;
            mSoundPath = soundPath;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

            // Debug
            System.out.println("SDThirteenActivity.SoundListener(class).onPrepared > Debug: MC");

            switch (mTag) {
                case DRAG_THE_LETTERS_TO_WRITE: {
                    mp.start();
                    break;
                }
                case YOU_GOT: {
                    mp.start();
                    break;
                }
                case SOUND: {
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
            System.out.println("SDThirteenActivity.SoundListener(class).onCompletion > Debug: MC");

            switch (mTag) {
                case DRAG_THE_LETTERS_TO_WRITE: {
                    mThisActivity.playSound(SOUND, mThisActivity.getCurrentWordSound());
                    break;
                }
                case YOU_GOT: {
                    break;
                }
                case SOUND: {
                    mThisActivity.setGameOn(true);
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
        System.out.println("SDThirteenActivity.resetListeners > Debug: MC");

        try {
            if (mLetterImageViews != null) {

                for (ImageView letterImageView: mLetterImageViews) {

                    // Remove drag listener
                    letterImageView.setOnDragListener(null);

                    // Remove touch listener
                    letterImageView.setOnTouchListener(null);
                }
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.resetContainers)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetContainers() {

        // Debug
        System.out.println("SDThirteenActivity.resetContainers > Debug: MC");

        try {
            if (mContainers != null) {

                for (LinearLayout container: mContainers) {

                    // Remove all views for container
                    container.removeAllViews();

                    // Make container invisible
                    container.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.resetContainers)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetReceptacles() {

        // Debug
        System.out.println("SDThirteenActivity.resetReceptacles > Debug: MC");

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
            System.err.println("SDThirteenActivity.resetReceptacles)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    private void resetReceptacleEntries() {

        // Debug
        System.out.println("SDThirteenActivity.resetReceptacleEntries > Debug: MC");

        try {
            for (int i = 0; i < mReceptacleEntries.length; i++) {
                mReceptacleEntries[i] = false;
            }
        } catch (Exception ex) {
            System.err.println("==============================");
            System.err.println("SDThirteenActivity.resetReceptacleEntries)");
            System.err.println("------------------------------");
            ex.printStackTrace();
            System.err.println("==============================");
        }
    }

    public void setCurrentDrill(int drill) {
        currentDrill = drill;
    }

    public void setCurrentWordSound(String currentWordSound) {
        mCurrentWordSound = currentWordSound;
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

    public String getCurrentWordSound() {
        return mCurrentWordSound;
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

    public LinearLayout[] getContainers() {
        return mContainers;
    }

    public ImageView[] getReceptacles() {
        return mReceptacles;
    }

    public boolean[] getReceptacleEntries() {
        return mReceptacleEntries;
    }

    public int[] getLetterOrder() {
        return mLetterOrder;
    }

    public int[] getLetterImageResourceIds() {
        return mLetterImageResourceIds;
    }

    public ImageView[] getLetterImageViews() {
        return mLetterImageViews;
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
        handler = null;
        if (mp != null) {
            mp.release();
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
