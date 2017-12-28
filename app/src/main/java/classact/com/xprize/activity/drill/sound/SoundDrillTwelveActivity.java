package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.TextShrinker;

public class SoundDrillTwelveActivity extends DrillActivity {

    private final String DRILL_DATA_KEY = "DRILL_DATA";

    private final int LOSE_RED = Color.parseColor("#cc0000");
    private final int ERR_RED = Color.parseColor("#ff0000");
    private final int WIN_CYAN = Color.parseColor("#33ccff");
    private final int DISABLED_GRAY = Color.parseColor("#AAAAAA");
    private final int NADA_GRAY = Color.parseColor("#777777");

    private String mDrillData;

    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private TextView timeView;
    private int time;
    public JSONArray wordSets;
//    private Handler timeHandler = new Handler();
//    private Handler setHandler = new Handler();
//    private Handler emergencyHandler = new Handler();
//    private Handler buttonEnablingHandler = new Handler();
    private int currentSet = 0;
    private int correctWord = 0;
    private JSONObject params;

    private boolean gameOver;
    private Runnable mNextAction;
    private int mLastButtonWordClicked;
    private boolean emergencyRed;

    private RelativeLayout mRootView;
    private RelativeLayout mButtonView;

    private final float TIMER_MID_X = 580f;
    private final float TIMER_MID_Y = 215f;

    private final Context THIS = this;

    private SoundDrill12ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill12ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        setContentView(R.layout.activity_sound_drill_twelve);
        mRootView = (RelativeLayout) findViewById(R.id.activity_sound_drill_twelve);
        timeView = (TextView) findViewById( R.id.textViewtimer);

        RelativeLayout timeViewParent = (RelativeLayout) findViewById(R.id.layout1);
        RelativeLayout.LayoutParams timeViewParentLP = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        timeViewParent.setLayoutParams(timeViewParentLP);

        RelativeLayout.LayoutParams timeViewLP = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        timeView.setLayoutParams(timeViewLP);

        timeView.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()), Typeface.BOLD);
        timeView.setTextSize(110f);
        timeView.setPadding(16, 16, 16, 16);
        timeView.setTextColor(NADA_GRAY);
        // timeView.setBackgroundColor(Color.argb(100, 0, 0, 255));

        buttonWord1 = (ImageButton)findViewById(R.id.button_word1);
        buttonWord2 = (ImageButton)findViewById(R.id.button_word_2);
        buttonWord3 = (ImageButton)findViewById(R.id.button_word3);

        RelativeLayout bwLayout = (RelativeLayout) buttonWord1.getParent();
        bwLayout.removeAllViews();

        mButtonView = new RelativeLayout(THIS);
        RelativeLayout.LayoutParams mButtonViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        mButtonView.setLayoutParams(mButtonViewLayout);
        mRootView.addView(mButtonView);

        mButtonView.addView(buttonWord1);
        mButtonView.addView(buttonWord2);
        mButtonView.addView(buttonWord3);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        int buttonWidth = (int) (density * 390);

        MarginLayoutParams bw1Layout = (MarginLayoutParams) buttonWord1.getLayoutParams();
        bw1Layout.topMargin = (int) (density * 240);
        bw1Layout.leftMargin = (int) (density * 24);
        bw1Layout.width = buttonWidth;
        buttonWord1.setLayoutParams(bw1Layout);
        // buttonWord1.setBackgroundColor(Color.argb(100, 255, 0, 0));

        MarginLayoutParams bw2Layout = (MarginLayoutParams) buttonWord2.getLayoutParams();
        bw2Layout.topMargin = (int) (density * 231);
        bw2Layout.leftMargin = (int) (density * 430);
        bw2Layout.width = buttonWidth;
        buttonWord2.setLayoutParams(bw2Layout);
        // buttonWord2.setBackgroundColor(Color.argb(100, 255, 0, 0));

        MarginLayoutParams bw3Layout = (MarginLayoutParams) buttonWord3.getLayoutParams();
        bw3Layout.topMargin = (int) (density * 222);
        bw3Layout.leftMargin = (int) (density * 840);
        bw3Layout.width = buttonWidth;
        buttonWord3.setLayoutParams(bw3Layout);
        // buttonWord3.setBackgroundColor(Color.argb(100, 255, 0, 0));

        initializeGUI();
        mDrillData = getIntent().getExtras().getString("data");
        initializeData(mDrillData);

    } //End of protected void onCreate

    private void initializeGUI() {

        // Set value of time
        time = 15;

        timeView.setTextColor(NADA_GRAY);
        Point textSize = Globals.TEXT_MEASURED_SIZE(timeView, String.valueOf(time));
        timeView.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
        timeView.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

        buttonWord1.setImageResource(0);
        buttonWord2.setImageResource(0);
        buttonWord3.setImageResource(0);

        // Disable buttons
        setButtonsEnabled(false);

        // Set emergency red (text flashing when emergency) to false
        emergencyRed = false;

        // Set game over
        gameOver = false;

        // Listening to touch
        buttonWord1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                wordClicked(0);
            }
        });
        buttonWord2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                wordClicked(1);
            }
        });
        buttonWord3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                wordClicked(2);
            }
        });
    }

    private void initializeData(String drillData) {

        try{
            params = new JSONObject(drillData);
            wordSets = params.getJSONArray("sets");
            currentSet = 0;
            String sound = params.getString("quick_mothers_coming");
            playSound(sound, () -> {
                timeView.setTextColor(Color.DKGRAY);
                handler.delayed(this::countDownTracker,1000); // time handler
                nextSet();
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSound(String sound){

        try {
            playSound(sound, () -> {
                if (gameOver) {
                    // Update colour of text
                    timeView.setTextColor(LOSE_RED);

                    // Update colour tint of word to light gray
                    buttonWord1.setColorFilter(NADA_GRAY);
                    buttonWord2.setColorFilter(NADA_GRAY);
                    buttonWord3.setColorFilter(NADA_GRAY);

                    handler.delayed(this::startConcluding,350); // setHandler
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundWithActionAfterCompletion(int soundId){
        try {
            playSound(soundId, mNextAction);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void nextSet(){

        try{
            JSONArray words = wordSets.getJSONObject(currentSet).getJSONArray("words");
            buttonWord1.setImageResource(words.getJSONObject(0).getInt("image"));
            if (words.getJSONObject(0).getInt("correct") == 1)
                correctWord = 0;
            buttonWord2.setImageResource(words.getJSONObject(1).getInt("image"));
            if (words.getJSONObject(1).getInt("correct") == 1)
                correctWord = 1;
            buttonWord3.setImageResource(words.getJSONObject(2).getInt("image"));
            if (words.getJSONObject(2).getInt("correct") == 1)
                correctWord = 2;

            buttonWord1 = TextShrinker.shrink(buttonWord1, 390, 0.9f, getResources());
            buttonWord2 = TextShrinker.shrink(buttonWord2, 390, 0.9f, getResources());
            buttonWord3 = TextShrinker.shrink(buttonWord3, 390, 0.9f, getResources());

            handler.delayed(saySound,500); // setHandler
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void wordClicked(int word){

        // Update last button word clicked
        mLastButtonWordClicked = word;

        if (!gameOver) {
            try {
                if (word == correctWord) {
                    // Disable buttons
                    setButtonsEnabled(false);

                    // Reset color tints
                    buttonWord1.setColorFilter(Color.TRANSPARENT);
                    buttonWord2.setColorFilter(Color.TRANSPARENT);
                    buttonWord3.setColorFilter(Color.TRANSPARENT);

                    // Update color tint of 'winning' button
                    switch (word) {
                        case 0:
                            buttonWord1.setColorFilter(WIN_CYAN);
                            Globals.playStarWorks(THIS, buttonWord1);
                            break;
                        case 1:
                            buttonWord2.setColorFilter(WIN_CYAN);
                            Globals.playStarWorks(THIS, buttonWord2);
                            break;
                        case 2:
                            buttonWord3.setColorFilter(WIN_CYAN);
                            Globals.playStarWorks(THIS, buttonWord3);
                            break;
                        case 3:
                            break;
                    }

                    // Pre-color win finish hack
                    if (currentSet+1 >= wordSets.length()) {
                        // The last set has completed
                        gameOver = true;
                        // Update colour of text
                        timeView.setTextColor(WIN_CYAN);
                    }

                    mNextAction = new Runnable() {
                        @Override
                        public void run() {
                            // check if the next set exists
                            if (++currentSet < wordSets.length()) {
                                // Play next set
                                handler.delayed(playNextSet, 200); // setHandler
                            } else {

                                // Update colour tint of word to WIN_CYAN
                                switch (mLastButtonWordClicked) {
                                    case 0:
                                        buttonWord1.setColorFilter(WIN_CYAN);
                                        buttonWord2.setColorFilter(NADA_GRAY);
                                        buttonWord3.setColorFilter(NADA_GRAY);
                                        break;
                                    case 1:
                                        buttonWord1.setColorFilter(NADA_GRAY);
                                        buttonWord2.setColorFilter(WIN_CYAN);
                                        buttonWord3.setColorFilter(NADA_GRAY);
                                        break;
                                    case 2:
                                        buttonWord1.setColorFilter(NADA_GRAY);
                                        buttonWord2.setColorFilter(NADA_GRAY);
                                        buttonWord3.setColorFilter(WIN_CYAN);
                                        break;
                                    case 3:
                                        break;
                                }
                                handler.delayed(() -> startConcluding(), 350); // setHandler
                            }
                        }
                    };

                    playSoundWithActionAfterCompletion(ResourceSelector.getPositiveAffirmationSound(context));
                } else {

                    // Highlight clicked word as errored
                    switch (word) {
                        case 0:
                            buttonWord1.setColorFilter(ERR_RED);
                            buttonWord2.setColorFilter(Color.TRANSPARENT);
                            buttonWord3.setColorFilter(Color.TRANSPARENT);
                            break;
                        case 1:
                            buttonWord1.setColorFilter(Color.TRANSPARENT);
                            buttonWord2.setColorFilter(ERR_RED);
                            buttonWord3.setColorFilter(Color.TRANSPARENT);
                            break;
                        case 2:
                            buttonWord1.setColorFilter(Color.TRANSPARENT);
                            buttonWord2.setColorFilter(Color.TRANSPARENT);
                            buttonWord3.setColorFilter(ERR_RED);
                            break;
                        case 3:
                            break;
                    }

                    // After completion of negative sound, ensure that
                    // all button words revert back to original colour
                    mNextAction = () -> {
                            if (!gameOver) {
                                buttonWord1.setColorFilter(Color.TRANSPARENT);
                                buttonWord2.setColorFilter(Color.TRANSPARENT);
                                buttonWord3.setColorFilter(Color.TRANSPARENT);
                            } else {

                                // Update colour of text
                                timeView.setTextColor(LOSE_RED);

                                // Update colour tint of word to light gray
                                buttonWord1.setColorFilter(NADA_GRAY);
                                buttonWord2.setColorFilter(NADA_GRAY);
                                buttonWord3.setColorFilter(NADA_GRAY);

                                handler.delayed(this::startConcluding,350); // setHandler
                            }
                        };
                    if (!gameOver) {
                        playSound(FetchResource.negativeAffirmation(THIS), () -> {
                            if (gameOver) {

                                // Update colour of text
                                timeView.setTextColor(LOSE_RED);

                                // Update colour tint of word to LOSE_RED
                                switch (mLastButtonWordClicked) {
                                    case 0:
                                        buttonWord1.setColorFilter(LOSE_RED);
                                        buttonWord2.setColorFilter(NADA_GRAY);
                                        buttonWord3.setColorFilter(NADA_GRAY);
                                        break;
                                    case 1:
                                        buttonWord1.setColorFilter(NADA_GRAY);
                                        buttonWord2.setColorFilter(LOSE_RED);
                                        buttonWord3.setColorFilter(NADA_GRAY);
                                        break;
                                    case 2:
                                        buttonWord1.setColorFilter(NADA_GRAY);
                                        buttonWord2.setColorFilter(NADA_GRAY);
                                        buttonWord3.setColorFilter(LOSE_RED);
                                        break;
                                    case 3:
                                        break;
                                }

                                handler.delayed(this::startConcluding, 750); // setHandler
                            } else {
                                if (mNextAction != null) {
                                    mNextAction.run();
                                }
                            }
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void countDownTracker() {

        if (time > 0) {

            if (!gameOver) {
                // Update time
                time--;

                // Declare initialize timeString to be used
                Point textSize = Globals.TEXT_MEASURED_SIZE(timeView, String.valueOf(time));
                timeView.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
                timeView.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

                if (String.valueOf(time).equalsIgnoreCase("6")) {
                    handler.delayed(countdownEmergency, 250); // emergencyHandler
                }

                // Set post delayed for next number in countdown
                handler.delayed(countdown, 1000);// wait for 1 seconds to start activity // timerHandler
            }
        }
        else {
            gameOver = true;

            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                // Update colour of text
                timeView.setTextColor(LOSE_RED);

                // Update colour tint of word to light gray
                buttonWord1.setColorFilter(NADA_GRAY);
                buttonWord2.setColorFilter(NADA_GRAY);
                buttonWord3.setColorFilter(NADA_GRAY);

                handler.delayed(this::startConcluding,1000); // setHandler
            }
        }
    }

    public void startConcluding() {
        handler.removeCallbacks(countdown); // timeHandler
        try {
            // Disable all buttons
            setButtonsEnabled(false);

            String sound = params.getString("you_got");
            playSound(sound, this::sayCorrectCount);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayCorrectCount(){

        String sound = "";
        try {
            sound = params.getString("count_0");
            switch (currentSet) {
                case 1:
                    sound = params.getString("count_1");
                    break;
                case 2:
                    sound = params.getString("count_2");
                    break;
                case 3:
                    sound = params.getString("count_3");
                    break;
                case 4:
                    sound = params.getString("count_4");
                    break;
                case 5:
                    sound = params.getString("count_5");
                    break;
                case 6:
                    sound = params.getString("count_6");
                    break;
            }
            playSound(sound, this::sayWords);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayWords(){

        try {
            String sound = params.getString("words_sound");
            playSound(sound, () -> handler.delayed(finishRunnable,300)); // setHandler
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setButtonsEnabled(boolean enable) {
        if (buttonWord1 != null) {
            buttonWord1.setEnabled(enable);
        }
        if (buttonWord2 != null) {
            buttonWord2.setEnabled(enable);
        }
        if (buttonWord3 != null) {
            buttonWord3.setEnabled(enable);
        }
    }

    Runnable finishRunnable = () -> {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    };

    Runnable countdown = new Runnable() {
        @Override
        public void run() {
            countDownTracker();
        }
    };

    Runnable countdownEmergency = new Runnable() {
        @Override
        public void run() {
            try {
                if (!gameOver) {
                    if (time <= 0) {
                        time = 0;
                        if (timeView != null) {
                            timeView.setTextColor(Color.RED);
                            Point textSize = Globals.TEXT_MEASURED_SIZE(timeView, String.valueOf(0));
                            timeView.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
                            timeView.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));
                        }
                        emergencyRed = false;
                    } else if (emergencyRed) {
                        if (timeView != null) {
                            timeView.setTextColor(Color.DKGRAY);
                            if (time > 3) {
                                handler.delayed(countdownEmergency, 500); // emergencyHandler
                            } else {
                                handler.delayed(countdownEmergency, 250); // emergencyHandler
                            }
                        }
                        emergencyRed = false;
                    } else {
                        timeView.setTextColor(Color.RED);
                        emergencyRed = true;
                        if (time > 3) {
                            handler.delayed(countdownEmergency, 500); // emergencyHandler
                        } else {
                            handler.delayed(countdownEmergency, 250); // emergencyHandler
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    Runnable playNextSet = () -> {
        if (!gameOver) {
            // Change color tint back to normal
            switch (correctWord) {
                case 0:
                    buttonWord1.setColorFilter(Color.TRANSPARENT);
                    break;
                case 1:
                    buttonWord2.setColorFilter(Color.TRANSPARENT);
                    break;
                case 2:
                    buttonWord3.setColorFilter(Color.TRANSPARENT);
                    break;
                case 3:
                    break;
            }
            nextSet();
        } else {

            // Update colour of text
            timeView.setTextColor(LOSE_RED);

            // Update colour tint of word to LOSE_RED
            switch (mLastButtonWordClicked) {
                case 0:
                    buttonWord1.setColorFilter(WIN_CYAN);
                    buttonWord2.setColorFilter(NADA_GRAY);
                    buttonWord3.setColorFilter(NADA_GRAY);
                    break;
                case 1:
                    buttonWord1.setColorFilter(NADA_GRAY);
                    buttonWord2.setColorFilter(WIN_CYAN);
                    buttonWord3.setColorFilter(NADA_GRAY);
                    break;
                case 2:
                    buttonWord1.setColorFilter(NADA_GRAY);
                    buttonWord2.setColorFilter(NADA_GRAY);
                    buttonWord3.setColorFilter(WIN_CYAN);
                    break;
                case 3:
                    break;
            }
            handler.delayed(this::startConcluding,350); // setHandler
        }
    };


    Runnable saySound = () -> {
        try {
            if (!gameOver) {
                // Get sound resource
                String sound = wordSets.getJSONObject(currentSet).getString("sound");

                // Add a next action to be played after a few hundred milliseconds
                    /* Note that this is not done after completion,
                       in case the child can determine the next word
                       after hearing the first syllable. Ie. 'who' vs. 'the'
                     */
                handler.delayed(() -> {
                    if (!gameOver) {
                        setButtonsEnabled(true);
                    }
                }, 500); // buttonEnablingHandler

                // Play sound with action happening after sound completion
                playSound(sound);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Save drill data to key
        outState.putString(DRILL_DATA_KEY, mDrillData);

        // Call the super always
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the super always
        super.onRestoreInstanceState(savedInstanceState);

        // Save drill data to key
        mDrillData = savedInstanceState.getString(DRILL_DATA_KEY);

        // Initialize everything to start the drill again
        initializeGUI();
        initializeData(mDrillData);
    }
}
