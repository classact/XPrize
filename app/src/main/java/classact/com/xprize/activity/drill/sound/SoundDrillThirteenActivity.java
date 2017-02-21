package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;

public class SoundDrillThirteenActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;

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

    public boolean entered1;
    public boolean entered2;
    public boolean entered3;
    public boolean entered4;
    public boolean entered5;
    public boolean entered6;
    public boolean entered7;
    public boolean entered8;
    private int currentItem;
    public JSONArray drills;
    private int currentDrill;
    JSONArray letters;
    private MediaPlayer mp;
    private int correctItems;
    private int totalPositions;
    private int filledPositions;
    private ImageView[] currentOccupants;
    private boolean[] positionIsCorrect;
    private JSONObject allData;

    private LinearLayout[] mContainers;
    private LinearLayout mReceptaclesParent;
    private ImageView[] mReceptacles;
    private ImageView[] mLetterImageViews;
    private boolean mGameOn;
    private int[] mLetterImageResourceIds;
    private int[] mLetterOrder;
    private boolean[] mReceptacleEntries;
    private SoundDrillThirteenActivity mThisActivity;

    private final int MAX_LETTERS = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mContainers = new LinearLayout[MAX_LETTERS];
        mContainers[0] = container4;
        mContainers[1] = container6;
        mContainers[2] = container5;
        mContainers[3] = container3;
        mContainers[4] = container2;
        mContainers[5] = container8;
        mContainers[6] = container7;
        mContainers[7] = container1;

        mReceptaclesParent = (LinearLayout) receptacle1.getParent();
        mReceptaclesParent.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams receptaclesParentParams = (RelativeLayout.LayoutParams) mReceptaclesParent.getLayoutParams();
        receptaclesParentParams.topMargin = 535;
        mReceptaclesParent.setLayoutParams(receptaclesParentParams);

        LinearLayout.LayoutParams receptacle1Params = (LinearLayout.LayoutParams) receptacle1.getLayoutParams();
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
        try{
            allData = new JSONObject(drillData);
            drills = allData.getJSONArray("words");
            currentDrill = 0;
            mGameOn = false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void prepareDrill(){
        try{
            JSONObject drill = drills.getJSONObject(currentDrill);

            JSONArray letters = drill.getJSONArray("letters");

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
                    int letterIndex = mLetterOrder[i];

                    // Extract the letter from data
                    JSONObject letter = letters.getJSONObject(letterIndex);

                    // Get letter image resource id
                    int letterImageResourceId = letter.getInt("letter");

                    // Create new image view
                    ImageView letterImageView = new ImageView(getApplicationContext());
                    letterImageView.setImageResource(letterImageResourceId);

                    // Add touch listener to image view
                    letterImageView.setOnTouchListener(new TouchAndDragListener(mThisActivity, i, letterIndex));

                    // Get container
                    LinearLayout container = mContainers[i];

                    // Add image view to container
                    container.addView(letterImageView);

                    // Add image view to list of letter image views
                    mLetterImageViews[letterIndex] = letterImageView;

                    // Add letter image resource id to list of letter image resource ids
                    mLetterImageResourceIds[letterIndex] = letterImageResourceId;

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
            mGameOn = true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    class TouchAndDragListener implements View.OnTouchListener, View.OnDragListener {

        private SoundDrillThirteenActivity mThisActivity;
        private int mShuffledIndex;
        private int mActualIndex;

        public TouchAndDragListener(SoundDrillThirteenActivity thisActivity, int shuffledIndex, int actualIndex) {
            mThisActivity = thisActivity;
            mShuffledIndex = shuffledIndex;
            mActualIndex = actualIndex;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mThisActivity.setCurrentItem(mActualIndex);
            System.out.println("Moving letter: actual(" + mActualIndex + "), shuffled(" + mShuffledIndex + ")");
            return dragItem(v, event);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
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

                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        receptacleEntries[mActualIndex] = true;

                    } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        receptacleEntries[mActualIndex] = false;

                    } else if (event.getAction() == DragEvent.ACTION_DROP && receptacleEntered) {
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

                            // Get the letter image resource id
                            int letterImageResourceId = mThisActivity.getLetterImageResourceIds()[mActualIndex];

                            // Update image resource id of receptacle
                            receptacle.setImageResource(letterImageResourceId);
                        }

                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && receptacleEntered) {
                        // Get current item
                        int currentItem = mThisActivity.getCurrentItem();

                        System.out.println("Current item END is: " + currentItem);
                        System.out.println("Current actual index END is: " + mActualIndex);
                        System.out.println("Current validation END is: " + (currentItem == mActualIndex));

                        // Check if current item relates to the receptacle's index
                        if (currentItem == mActualIndex) {

                            System.out.println("BINGO END!!!");

                            // Bingo!
                            // Disable game interactions
                            mThisActivity.setGameOn(false);

                            // Get containers
                            LinearLayout[] containers = mThisActivity.getContainers();

                            // Get letter image views
                            ImageView[] letterImageViews = mThisActivity.getLetterImageViews();

                            // Get letter order
                            int[] letterOrder = mThisActivity.getLetterOrder();

                            // Get container for dragged image view
                            LinearLayout container = containers[letterOrder[mActualIndex]];

                            // Get letter image view
                            ImageView letterImageView = letterImageViews[mActualIndex];

                            // Remove letter image view from container
                            container.removeView(letterImageView);

                            // Set receptacle entries to false
                            receptacleEntries[mActualIndex] = false;

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

                                // Check if max drills reached
                                if (currentDrill == drills.length()) {

                                    // End
                                    if (mp != null) {
                                        mp.release();
                                    }
                                    finish();

                                } else {
                                    // Otherwise update drill no
                                    mThisActivity.setCurrentDrill(currentDrill);

                                    // Prepare next drill
                                    mThisActivity.prepareDrill();
                                }
                            } else {
                                // Update correct items
                                mThisActivity.setCorrectItems(correctItems);

                                // Re-enable game interactions
                                mThisActivity.setGameOn(true);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
            catch (Exception ex){
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
            return false;
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        // Get game on
        boolean gameOn = mThisActivity.getGameOn();

        if (gameOn) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                resetReceptacleEntries();
                return true;
            }
        }
        return false;
    }

    private void resetContainers() {
        for (int i = 0; i < mContainers.length; i++) {
            // Get container
            LinearLayout container = mContainers[i];

            // Remove all views for container
            container.removeAllViews();

            // Make container invisible
            container.setVisibility(View.INVISIBLE);
        }
    }

    private void resetReceptacles() {
        mReceptaclesParent.removeAllViews();

        for (int i = 0; i < mReceptacles.length; i++) {
            // Get image view
            ImageView receptacle = mReceptacles[i];

            // Reset image resource
            receptacle.setImageResource(R.drawable.line);

            // Reset visibility of receptacle
            receptacle.setVisibility(View.INVISIBLE);

            // Re-add receptacle to receptacles parent
            mReceptaclesParent.addView(receptacle);
        }
    }

    private void resetReceptacleEntries(){
        for (int i = 0; i < mReceptacleEntries.length; i++) {
            mReceptacleEntries[i] = false;
        }
    }

    public void playSound(String sound) {
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new SoundListener(mThisActivity, sound, soundPath));
            mp.setOnCompletionListener(new SoundListener(mThisActivity, sound, soundPath));
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            (new SoundListener(mThisActivity, sound, null)).onCompletion(null);
        }
    }

    class SoundListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        private SoundDrillThirteenActivity mThisActivity;
        private String mSound;
        private String mSoundPath;

        public SoundListener(SoundDrillThirteenActivity thisActivity, String sound, String soundPath) {
            mThisActivity = thisActivity;
            mSound = sound;
            mSoundPath = soundPath;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            switch (mSound) {
                case "drag_the_letters_to_write":
                    break;
                case "you_got":
                    break;
                case "sound":
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            switch (mSound) {
                case "drag_the_letters_to_write":
                    break;
                case "you_got":
                    break;
                case "sound":
                    break;
                default:
                    break;
            }
        }
    }

    public void setCurrentDrill(int drill) {
        currentDrill = drill;
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

    public JSONArray getDrills() {
        return drills;
    }

    public int getCurrentDrill() {
        return currentDrill;
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

    private void sayWord(){
        try {
            JSONObject data = drills.getJSONObject(currentDrill);
            int sound = data.getInt("sound");
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
            ex.printStackTrace();
        }
    }

    private boolean placeToContainer(View view, int image){
        boolean placed = false;
        switch (image){
            case 0:
                if (container1.getChildCount() == 0){
                    container1.addView(view);
                    placed = true;
                }
                break;
            case 1:
                if (container2.getChildCount() == 0){
                    container2.addView(view);
                    placed = true;
                }
                break;
            case 2:
                if (container3.getChildCount() == 0){
                    container3.addView(view);
                    placed = true;
                }
                break;
            case 3:
                if (container4.getChildCount() == 0){
                    container4.addView(view);
                    placed = true;
                }
                break;
            case 4:
                if (container5.getChildCount() == 0){
                    container5.addView(view);
                    placed = true;
                }
                break;
            case 5:
                if (container6.getChildCount() == 0){
                    container6.addView(view);
                    placed = true;
                }
                break;
            case 6:
                if (container7.getChildCount() == 0){
                    container7.addView(view);
                    placed = true;
                }
                break;
            case 7:
                if (container8.getChildCount() == 0){
                    container8.addView(view);
                    placed = true;
                }
                break;
        }
        return placed;
    }
    private ImageView createandPlaceImage(int resource){
        ImageView view = new ImageView(this);
        view.setImageResource(resource);
        Random rand = new Random();
        int position = rand.nextInt(8);
        int i = 0;
        while(!placeToContainer(view,position)){
            i++;
            position = i;
        }
        return view;
    }
    private void initialiseHandlers(int item){
        switch (item) {
            case 0:
                item1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 0;
                        return dragItem(v, event);
                    }
                });
                break;
            case 1:
                item2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 1;
                        return dragItem(v, event);
                    }
                });
                break;
            case 2:
                item3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 2;
                        return dragItem(v, event);
                    }
                });
                break;
            case 3:
                item4.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 3;
                        return dragItem(v, event);
                    }
                });
                break;
            case 4:
                item5.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 4;
                        return dragItem(v, event);
                    }
                });
                break;
            case 5:
                item6.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 5;
                        return dragItem(v, event);
                    }
                });
                break;
            case 6:
                item7.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 6;
                        return dragItem(v, event);
                    }
                });
                break;
            case 7:
                item8.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 7;
                        return dragItem(v, event);
                    }
                });
                break;
        }
    }

    public void resetItem(int item){
        try{
            JSONObject data = drills.getJSONObject(currentDrill);
            letters = data.getJSONArray("words");
            if (item == 0)
                item1.setImageResource(letters.getJSONObject(0).getInt("letter"));
            else if (item == 1)
                item2.setImageResource(letters.getJSONObject(1).getInt("letter"));
            else if (item == 2)
                item3.setImageResource(letters.getJSONObject(2).getInt("letter"));
            else if (item == 3)
                item4.setImageResource(letters.getJSONObject(3).getInt("letter"));
            else if (item == 4)
                item5.setImageResource(letters.getJSONObject(4).getInt("letter"));
            else if (item == 5)
                item6.setImageResource(letters.getJSONObject(5).getInt("letter"));
            else if (item == 6)
                item7.setImageResource(letters.getJSONObject(6).getInt("letter"));
            else if (item == 7)
                item8.setImageResource(letters.getJSONObject(7).getInt("letter"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void resetEntered(){
        entered1 = false;
        entered2 = false;
        entered3 = false;
        entered4 = false;
        entered5 = false;
        entered6 = false;
        entered7 = false;
        entered8 = false;
    }

    private void setupReceptacleOne(){
        receptacle1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered1 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered1 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered1) {
                        setReceptacleToImage(0);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered1) {
                        hideItem(0,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleTwo(){
        receptacle2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered2 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered2 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered2) {
                        setReceptacleToImage(1);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered2) {
                        hideItem(1,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleThree(){
        receptacle3.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered3 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered3 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered3) {
                        setReceptacleToImage(2);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered3) {
                        hideItem(2,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleFour(){
        receptacle4.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered4 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered4 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered4) {
                        setReceptacleToImage(3);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered4) {
                        hideItem(3,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleFive(){
        receptacle5.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered5 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered5 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered5) {
                        setReceptacleToImage(4);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered5) {
                        hideItem(4,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleSix(){
        receptacle6.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered6 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered6 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered6) {
                        setReceptacleToImage(5);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered6) {
                        hideItem(5,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleSeven(){
        receptacle7.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered7 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered7 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered7) {
                        setReceptacleToImage(6);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered7) {
                        hideItem(6,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleEight(){
        receptacle8.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered8 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered8 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered8) {
                        setReceptacleToImage(7);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered8) {
                        hideItem(7,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }


    public boolean isCorrectMatch(int receptacle){
        boolean result = false;
        try {
            int position = 0;
            JSONArray positions = letters.getJSONObject(currentItem).getJSONArray("positions");
            for(int i = 0; i < positions.length();i++) {
                position = positions.getInt(i);
                if (position == receptacle) {
                    result = true;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
        return result;
    }

    private void hideItem(int receptacle, DragEvent event){
        try {
            ImageView view = (ImageView) event.getLocalState();
            view.setVisibility(View.INVISIBLE);
            if (currentOccupants[receptacle] != null) {
                currentOccupants[receptacle].setVisibility(View.VISIBLE);
            }
            currentOccupants[receptacle] = view;

        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void checkCompletion(){
        if (filledPositions == totalPositions  && correctItems == totalPositions) {
            playPositiveSound(R.raw.good_job);
        }
        else if (filledPositions == totalPositions ){
            playNegativeSound(R.raw.uh_oh);
        }
    }
    private void setReceptacleToImage(int receptacle){
        try {

            if (receptacle == 0)
                receptacle1.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 1)
                receptacle2.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 2)
                receptacle3.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 3)
                receptacle4.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 4)
                receptacle5.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 5)
                receptacle6.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 6)
                receptacle7.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            else if (receptacle == 7)
                receptacle8.setImageResource(letters.getJSONObject(currentItem).getInt("letter"));
            if (positionIsCorrect[receptacle]){
                positionIsCorrect[receptacle] = false;
                correctItems --;
            }
            if (isCorrectMatch(receptacle)) {
                correctItems ++;
                positionIsCorrect[receptacle] = true;
            }
            if (currentOccupants[receptacle] != null) {
                currentOccupants[receptacle].setVisibility(View.VISIBLE);
                filledPositions--;
            }
            filledPositions++;

            checkCompletion();
        }
        catch(Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playPositiveSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (currentDrill == 0) {
                        currentDrill = 1;
                        prepareDrill();
                    } else if (currentDrill == 1) {
                        currentDrill = 2;
                        prepareDrill();
                    } else {
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

    private void playThisSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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

    private void playNegativeSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    prepareDrill();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }

}
