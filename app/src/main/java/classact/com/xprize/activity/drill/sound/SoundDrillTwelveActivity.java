package classact.com.xprize.activity.drill.sound;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTwelveActivity extends AppCompatActivity {

    private final int COUNTDOWN_TOP_MARGIN = 18;
    private final int COUNTDOWN_LEFT_MARGIN = 564;
    private final int TWO_DIGIT_OFFSET = 117;
    private final int ONE_DIGIT_OFFSET = 40;

    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private MediaPlayer mp;
    private TextView timeView;
    private int time;
    private int cancel_repeats=0;
    public JSONArray wordSets;
    final Handler timeHandler = new Handler();
    final Handler setHandler = new Handler();
    final Handler emergencyHandler = new Handler();
    final Handler buttonEnablingHandler = new Handler();
    private int currentSet = 0;
    private int correctWord = 0;
    private JSONObject params;

    private boolean gameOver;
    private Runnable mNextAction;
    private boolean emergencyRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_twelve);
        timeView = (TextView) findViewById( R.id.textViewtimer );

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
        timeViewLP.topMargin = -COUNTDOWN_TOP_MARGIN;
        timeViewLP.leftMargin = COUNTDOWN_LEFT_MARGIN - timeViewCenterOffset;

        // Update timeView layout
        timeView.setLayoutParams(timeViewLP);

        // Update timeView with new time
        timeView.setText(timeString);

        buttonWord1 = (ImageButton)findViewById(R.id.button_word1);
        buttonWord2 = (ImageButton)findViewById(R.id.button_word_2);
        buttonWord3 = (ImageButton)findViewById(R.id.button_word3);

        /* Show white background for position-checking purposes
        buttonWord1.setBackgroundResource(android.R.color.white);
        buttonWord2.setBackgroundResource(android.R.color.white);
        buttonWord3.setBackgroundResource(android.R.color.white);
        */

        buttonWord1.setImageResource(0);
        buttonWord2.setImageResource(0);
        buttonWord3.setImageResource(0);

        // Adjust button positions
        LayoutParams button1LP = (LayoutParams) buttonWord1.getLayoutParams();
        LayoutParams button2LP = (LayoutParams) buttonWord2.getLayoutParams();
        LayoutParams button3LP = (LayoutParams) buttonWord3.getLayoutParams();

        button1LP.leftMargin = 127;
        button2LP.leftMargin = 228;
        button3LP.leftMargin = 221;

        buttonWord1.setLayoutParams(button1LP);
        buttonWord2.setLayoutParams(button2LP);
        buttonWord3.setLayoutParams(button3LP);

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

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);

    } //End of protected void onCreate

    private void initialiseData(String drillData){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.initialiseData > Debug: METHOD CALLED");

        try{
            params = new JSONObject(drillData);
            wordSets = params.getJSONArray("sets");
            currentSet = 0;
            int sound = params.getInt("quick_mothers_coming");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    countDownTracker();
                    nextSet();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void playSound(int soundid){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.playSound > Debug: METHOD CALLED");

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

                    if (gameOver) {
                        setHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Update colour of text
                                timeView.setTextColor(Color.parseColor("#cc0000"));

                                startConcluding();
                            }
                        }, 350);
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (mNextAction != null) {
                        mNextAction.run();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
            setHandler.postDelayed(saySound,500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void wordClicked(int word){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.wordClicked > Debug: METHOD CALLED");

        if (!gameOver) {
            try {
                if (word == correctWord) {
                    // Disable buttons
                    setButtonsEnabled(false);

                    switch (currentSet) {
                        case 0:
                            //reward1.setImageResource(R.drawable.rewardball1colour);
                            break;
                        case 1:
                            //reward2.setImageResource(R.drawable.rewardball1colour);
                            break;
                        case 2:
                            //reward3.setImageResource(R.drawable.rewardball1colour);
                            break;
                        case 3:
                            //reward4.setImageResource(R.drawable.rewardball1colour);
                            break;
                    }

                    // Pre-color win finish hack
                    if (currentSet+1 >= wordSets.length()) {
                        // The last set has completed
                        gameOver = true;
                        // Update colour of text
                        timeView.setTextColor(Color.parseColor("#33ccff"));
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
                    playSound(ResourceSelector.getNegativeAffirmationSound(this));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
                timeViewLP.topMargin = -COUNTDOWN_TOP_MARGIN;
                timeViewLP.leftMargin = COUNTDOWN_LEFT_MARGIN - timeViewCenterOffset;

                // Update timeView with new time
                timeView.setText(timeString);

                // Update timeView layout
                timeView.setLayoutParams(timeViewLP);

                if (String.valueOf(time).equalsIgnoreCase("5")) {
                    emergencyHandler.postDelayed(countdownEmergency, 250);
                }

                // Set post delayed for next number in countdown
                timeHandler.postDelayed(countdown, 1000);// wait for 1 seconds to start activity
            }
        }
        else {
            if (!gameOver) {
                gameOver = true;

                if (mp != null && !mp.isPlaying()) {
                    setHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Update colour of text
                            timeView.setTextColor(Color.parseColor("#cc0000"));

                            startConcluding();
                        }
                    }, 1000);
                }
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

            int sound = params.getInt("you_got");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayCorrectCount();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayCorrectCount(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.sayCorrectCount > Debug: METHOD CALLED");

        try {
            int sound = params.getInt("count_0");
            switch (currentSet) {
                case 1:
                    sound = params.getInt("count_1");
                    ;
                    break;
                case 2:
                    sound = params.getInt("count_2");
                    ;
                    break;
                case 3:
                    sound = params.getInt("count_3");
                    ;
                    break;
                case 4:
                    sound = params.getInt("count_4");
                    ;
                    break;
                case 5:
                    sound = params.getInt("count_5");
                    ;
                    break;
                case 6:
                    sound = params.getInt("count_6");
                    break;
            }
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayWords();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayWords(){

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.sayWords > Debug: METHOD CALLED");

        try {
            int sound = params.getInt("words_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    setHandler.postDelayed(finishRunnable, 300);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
                    if (time == 0) {
                        if (timeView != null) {
                            timeView.setTextColor(Color.RED);
                        }
                        emergencyRed = false;
                    } else if (emergencyRed) {
                        if (timeView != null) {
                            timeView.setTextColor(Color.DKGRAY);
                            if (time > 2) {
                                emergencyHandler.postDelayed(countdownEmergency, 500);
                            } else {
                                emergencyHandler.postDelayed(countdownEmergency, 250);
                            }
                        }
                        emergencyRed = false;
                    } else {
                        timeView.setTextColor(Color.RED);
                        emergencyRed = true;
                        if (time > 2) {
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
                    int sound = wordSets.getJSONObject(currentSet).getInt("sound");

                    // Add a next action to be played after a few hundred milliseconds
                    /* Note that this is not done after completion,
                       in case the child can determine the next word
                       after hearing the first syllable. Ie. 'who' vs. 'the'
                     */
                    buttonEnablingHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Enable buttons
                            setButtonsEnabled(true);
                        }
                    }, 500);

                    // Play sound with action happening after sound completion
                    playSound(sound);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                finish();
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();

        // Debug
        System.out.println("-- SoundTrillTwelveActivity.onPause > Debug: METHOD CALLED");

        if (mp != null){
            mp.release();
        }
    }

    /**
     * Get Text View Dimensions
     * Note that this returns a point, but that's just
     * to store the width and height
     * @param textView
     * @param text
     * @return
     */
    public Point getTextViewDims(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        return new Point(bounds.width(), bounds.height());
    }

//    @Override
//    public void onBackPressed() {
//    }
}
