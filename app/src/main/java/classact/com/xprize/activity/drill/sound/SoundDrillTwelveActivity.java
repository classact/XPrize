package classact.com.xprize.activity.drill.sound;

import android.content.Context;
import android.graphics.Color;
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
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.TextShrinker;

public class SoundDrillTwelveActivity extends AppCompatActivity {

    private final String DRILL_DATA_KEY = "DRILL_DATA";

    private final int COUNTDOWN_TOP_MARGIN = 45;
    private final int COUNTDOWN_LEFT_MARGIN = 564;
    private final int TWO_DIGIT_OFFSET = 117;
    private final int ONE_DIGIT_OFFSET = 40;
    
    private final String LOSE_RED = "#cc0000";
    private final String ERR_RED = "#ff0000";
    private final String WIN_CYAN = "#33ccff";

    private String mDrillData;

    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private MediaPlayer mp;
    private TextView timeView;
    private int time;
    public JSONArray wordSets;
    private Handler timeHandler = new Handler();
    private Handler setHandler = new Handler();
    private Handler emergencyHandler = new Handler();
    private Handler buttonEnablingHandler = new Handler();
    private int currentSet = 0;
    private int correctWord = 0;
    private JSONObject params;

    private boolean gameOver;
    private Runnable mNextAction;
    private int mLastButtonWordClicked;
    private boolean emergencyRed;

    private RelativeLayout mRootView;
    private RelativeLayout mButtonView;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_twelve);
        mRootView = (RelativeLayout) findViewById(R.id.activity_sound_drill_twelve);
        timeView = (TextView) findViewById( R.id.textViewtimer);
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
        float screenWidth = displayMetrics.widthPixels;

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
        /* Show white background for position-checking purposes
        buttonWord1.setBackgroundResource(android.R.color.white);
        buttonWord2.setBackgroundResource(android.R.color.white);
        buttonWord3.setBackgroundResource(android.R.color.white);
        */

        // Set value of time
        time = 15;

        // Declare initialize timeString to be used
        String timeString = String.valueOf(time);

        // Get new center offset of timeView
        // int timeViewCenterOffset = getTextViewDims(timeView, timeString).x / 2;
        int timeViewCenterOffset = TWO_DIGIT_OFFSET;
        if (timeString.length() == 1) {
            timeViewCenterOffset = ONE_DIGIT_OFFSET;
        }

        // Create new timeView layout based on new width
        LayoutParams timeViewLP = (LayoutParams) timeView.getLayoutParams();
        timeViewLP.topMargin = COUNTDOWN_TOP_MARGIN;
        timeViewLP.leftMargin = COUNTDOWN_LEFT_MARGIN - timeViewCenterOffset;

        // Update timeView layout
        timeView.setLayoutParams(timeViewLP);

        // Update timeView text color
        timeView.setTextColor(Color.DKGRAY);

        // Update timeView with new time
        timeView.setText(timeString);

        buttonWord1.setImageResource(0);
        buttonWord2.setImageResource(0);
        buttonWord3.setImageResource(0);

        // Disable buttons
        setButtonsEnabled(false);

        // Set emergency red (text flashing when emergency) to false
        emergencyRed = false;

        // Set game over
        gameOver = false;

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onCreate > Debug: METHOD CALLED");

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

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.initialiseData > Debug: METHOD CALLED");

        try{
            params = new JSONObject(drillData);
            wordSets = params.getJSONArray("sets");
            currentSet = 0;
            String sound = params.getString("quick_mothers_coming");

            if (mp == null) {
                mp = new MediaPlayer();
            }
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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
                    countDownTracker();
                    nextSet();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
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

    private void playSound(String sound){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.playSound > Debug: METHOD CALLED");

        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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

                    if (gameOver) {
                        setHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Update colour of text
                                timeView.setTextColor(Color.parseColor(LOSE_RED));

                                startConcluding();
                            }
                        }, 350);
                    }
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            setHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timeView.setTextColor(Color.parseColor(LOSE_RED));
                    startConcluding();
                }
            }, 1150);
        }
    }

    private void playSound(int soundid){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.playSound > Debug: METHOD CALLED");

        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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

                    if (gameOver) {
                        setHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Update colour of text
                                timeView.setTextColor(Color.parseColor(LOSE_RED));

                                startConcluding();
                            }
                        }, 350);
                    }
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void playSoundWithActionAfterCompletion(int soundId){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.playSound > Debug: METHOD CALLED");

        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
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
                    if (mNextAction != null) {
                        mNextAction.run();
                    }
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void nextSet(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.nextSet > Debug: METHOD CALLED");

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

            setHandler.postDelayed(saySound,500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void wordClicked(int word){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.wordClicked > Debug: METHOD CALLED");

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
                            buttonWord1.setColorFilter(Color.parseColor(WIN_CYAN));
                            break;
                        case 1:
                            buttonWord2.setColorFilter(Color.parseColor(WIN_CYAN));
                            break;
                        case 2:
                            buttonWord3.setColorFilter(Color.parseColor(WIN_CYAN));
                            break;
                        case 3:
                            break;
                    }

                    // Pre-color win finish hack
                    if (currentSet+1 >= wordSets.length()) {
                        // The last set has completed
                        gameOver = true;
                        // Update colour of text
                        timeView.setTextColor(Color.parseColor(WIN_CYAN));
                    }

                    mNextAction = new Runnable() {
                        @Override
                        public void run() {
                            // check if the next set exists
                            if (++currentSet < wordSets.length()) {
                                // Play next set
                                setHandler.postDelayed(playNextSet, 200);
                            } else {

                                setHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startConcluding();
                                    }
                                }, 350);
                            }
                        }
                    };

                    playSoundWithActionAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
                } else {

                    // Highlight clicked word as errored
                    switch (word) {
                        case 0:
                            buttonWord1.setColorFilter(Color.parseColor(ERR_RED));
                            buttonWord2.setColorFilter(Color.TRANSPARENT);
                            buttonWord3.setColorFilter(Color.TRANSPARENT);
                            break;
                        case 1:
                            buttonWord1.setColorFilter(Color.TRANSPARENT);
                            buttonWord2.setColorFilter(Color.parseColor(ERR_RED));
                            buttonWord3.setColorFilter(Color.TRANSPARENT);
                            break;
                        case 2:
                            buttonWord1.setColorFilter(Color.TRANSPARENT);
                            buttonWord2.setColorFilter(Color.TRANSPARENT);
                            buttonWord3.setColorFilter(Color.parseColor(ERR_RED));
                            break;
                        case 3:
                            break;
                    }

                    // After completion of negative sound, ensure that
                    // all button words revert back to original colour
                    mNextAction = new Runnable() {

                        @Override
                        public void run() {
                            if (!gameOver) {
                                buttonWord1.setColorFilter(Color.TRANSPARENT);
                                buttonWord2.setColorFilter(Color.TRANSPARENT);
                                buttonWord3.setColorFilter(Color.TRANSPARENT);
                            } else {
                                setHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update colour of text
                                        timeView.setTextColor(Color.parseColor(LOSE_RED));

                                        // Update colour tint of word to LOSE_RED
                                        switch (mLastButtonWordClicked) {
                                            case 0:
                                                buttonWord1.setColorFilter(Color.parseColor(LOSE_RED));
                                                buttonWord2.setColorFilter(Color.TRANSPARENT);
                                                buttonWord3.setColorFilter(Color.TRANSPARENT);
                                                break;
                                            case 1:
                                                buttonWord1.setColorFilter(Color.TRANSPARENT);
                                                buttonWord2.setColorFilter(Color.parseColor(LOSE_RED));
                                                buttonWord3.setColorFilter(Color.TRANSPARENT);
                                                break;
                                            case 2:
                                                buttonWord1.setColorFilter(Color.TRANSPARENT);
                                                buttonWord2.setColorFilter(Color.TRANSPARENT);
                                                buttonWord3.setColorFilter(Color.parseColor(LOSE_RED));
                                                break;
                                            case 3:
                                                break;
                                        }

                                        startConcluding();
                                    }
                                }, 350);
                            }
                        }
                    };
                    if (!gameOver) {
                        playSound(FetchResource.negativeAffirmation(THIS), new Runnable() {
                            @Override
                            public void run() {
                                if (gameOver) {
                                    setHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Update colour of text
                                            timeView.setTextColor(Color.parseColor(LOSE_RED));

                                            startConcluding();
                                        }
                                    }, 350);
                                } else {
                                    if (mNextAction != null) {
                                        mNextAction.run();
                                    }
                                }
                            }
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    }

    public void countDownTracker() {

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.countDownTracker > Debug: METHOD CALLED");

        if (time > 0) {

            if (!gameOver) {
                // Update time
                time--;

                // Declare initialize timeString to be used
                String timeString = String.valueOf(time);

                // Get new center offset of timeView
                // int timeViewCenterOffset = getTextViewDims(timeView, timeString).x / 2;
                int timeViewCenterOffset = TWO_DIGIT_OFFSET;
                if (timeString.length() == 1) {
                    timeViewCenterOffset = ONE_DIGIT_OFFSET;
                }

                // Create new timeView layout based on new width
                LayoutParams timeViewLP = (LayoutParams) timeView.getLayoutParams();
                timeViewLP.topMargin = COUNTDOWN_TOP_MARGIN;
                timeViewLP.leftMargin = COUNTDOWN_LEFT_MARGIN - timeViewCenterOffset;

                // Update timeView with new time
                timeView.setText(timeString);

                // Update timeView layout
                timeView.setLayoutParams(timeViewLP);

                if (String.valueOf(time).equalsIgnoreCase("6")) {
                    emergencyHandler.postDelayed(countdownEmergency, 250);
                }

                // Set post delayed for next number in countdown
                timeHandler.postDelayed(countdown, 1000);// wait for 1 seconds to start activity
            }
        }
        else {
            gameOver = true;

            if (mp != null && !mp.isPlaying()) {
                setHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Update colour of text
                        timeView.setTextColor(Color.parseColor(LOSE_RED));

                        startConcluding();
                    }
                }, 1000);
            }
        }
    }

    public void startConcluding(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.startConcluding > Debug: METHOD CALLED");

        timeHandler.removeCallbacks(countdown);
        try {

            // Disable all buttons
            setButtonsEnabled(false);

            String sound = params.getString("you_got");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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
                    sayCorrectCount();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayCorrectCount(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.sayCorrectCount > Debug: METHOD CALLED");

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

            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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
                    sayWords();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            setHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sayWords();
                }
            }, 800);
        }
    }

    private void sayWords(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.sayWords > Debug: METHOD CALLED");

        try {
            String sound = params.getString("words_sound");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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
                    setHandler.postDelayed(finishRunnable, 300);
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
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

    Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTwelveActivity.finishRunnable.run(Runnable) > Debug: METHOD CALLED");

            if (mp != null) {
                mp.release();
            }
            finish();
        }
    };

    Runnable countdown = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTwelveActivity.countdown.run(Runnable) > Debug: METHOD CALLED");

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
                            timeView.setText("" + 0);
                            timeView.setTextColor(Color.RED);
                        }
                        emergencyRed = false;
                    } else if (emergencyRed) {
                        if (timeView != null) {
                            timeView.setTextColor(Color.DKGRAY);
                            if (time > 3) {
                                emergencyHandler.postDelayed(countdownEmergency, 500);
                            } else {
                                emergencyHandler.postDelayed(countdownEmergency, 250);
                            }
                        }
                        emergencyRed = false;
                    } else {
                        timeView.setTextColor(Color.RED);
                        emergencyRed = true;
                        if (time > 3) {
                            emergencyHandler.postDelayed(countdownEmergency, 500);
                        } else {
                            emergencyHandler.postDelayed(countdownEmergency, 250);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("SoundDrillTwelveActivity.countdownEmergency.run(Runnable) > Exception: " + ex.getMessage());
            }
        }
    };

    Runnable playNextSet = new Runnable() {
        @Override
        public void run() {

            // Change color tint back to normal
            switch (correctWord) {
                case 0:
                    buttonWord1.setColorFilter(Color.TRANSPARENT);
                    //reward1.setImageResource(R.drawable.rewardball1colour);
                    break;
                case 1:
                    buttonWord2.setColorFilter(Color.TRANSPARENT);
                    //reward2.setImageResource(R.drawable.rewardball1colour);
                    break;
                case 2:
                    buttonWord3.setColorFilter(Color.TRANSPARENT);
                    //reward3.setImageResource(R.drawable.rewardball1colour);
                    break;
                case 3:
                    //reward4.setImageResource(R.drawable.rewardball1colour);
                    break;
            }

            // Debug
            System.out.println("-- SoundTrillTwelveActivity.playNextSet.run(Runnable) > Debug: METHOD CALLED");

            nextSet();
        }
    };


    Runnable saySound = new Runnable() {
        @Override
        public void run() {

            // Debug
            System.out.println("-- SoundTrillTwelveActivity.saySound.run(Runnable) > Debug: METHOD CALLED");

            try {
                if (!gameOver) {
                    // Get sound resource
                    String sound = wordSets.getJSONObject(currentSet).getString("sound");

                    // Add a next action to be played after a few hundred milliseconds
                    /* Note that this is not done after completion,
                       in case the child can determine the next word
                       after hearing the first syllable. Ie. 'who' vs. 'the'
                     */
                    buttonEnablingHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Enable buttons
                            if (!gameOver) {
                                setButtonsEnabled(true);
                            }
                        }
                    }, 500);

                    // Play sound with action happening after sound completion
                    playSound(sound);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onSaveInstanceState > Debug: METHOD CALLED");

        // Save drill data to key
        outState.putString(DRILL_DATA_KEY, mDrillData);

        // Call the super always
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the super always
        super.onRestoreInstanceState(savedInstanceState);

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onRestoreInstanceState > Debug: METHOD CALLED");

        // Save drill data to key
        mDrillData = savedInstanceState.getString(DRILL_DATA_KEY);

        // Initialize everything to start the drill again
        initializeGUI();
        initializeData(mDrillData);
    }

    @Override
    protected void onPause() {
        // Call the super always
        super.onPause();

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onPause > Debug: METHOD CALLED");
    }

    @Override
    protected void onResume() {
        // Call the super always
        super.onResume();

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onResume > Debug: METHOD CALLED");
    }

    @Override
    protected void onDestroy() {
        // Call the super always
        super.onDestroy();

        if (mp != null) {
            mp.release();
            mp = null;
        }

        if (timeHandler != null) {
            timeHandler.removeCallbacksAndMessages(null);
            timeHandler = null;
        }

        if (setHandler != null) {
            setHandler.removeCallbacksAndMessages(null);
            setHandler = null;
        }

        if (emergencyHandler != null) {
            emergencyHandler.removeCallbacksAndMessages(null);
            emergencyHandler = null;
        }

        if (buttonEnablingHandler != null) {
            buttonEnablingHandler.removeCallbacksAndMessages(null);
            buttonEnablingHandler = null;
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
            mp.stop();
            mp.release();
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
