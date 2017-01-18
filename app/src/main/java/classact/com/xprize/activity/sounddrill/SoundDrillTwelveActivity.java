package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTwelveActivity extends AppCompatActivity {
    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private MediaPlayer mp;
    private TextView timeView;
    private int time = 15;
    private int cancel_repeats=0;
    public JSONArray wordSets;
    final Handler timeHandler = new Handler();
    final Handler setHandler = new Handler();
    private int currentSet = 0;
    private int correctWord = 0;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_twelve);
        timeView = (TextView) findViewById( R.id.textViewtimer );
        timeView.setText("15"); // Intended to display timer.

        buttonWord1 = (ImageButton)findViewById(R.id.button_word1);
        buttonWord2 = (ImageButton)findViewById(R.id.button_word_2);
        buttonWord3 = (ImageButton)findViewById(R.id.button_word3);
        buttonWord1.setImageResource(0);
        buttonWord2.setImageResource(0);
        buttonWord3.setImageResource(0);
        // Listening to touch


        buttonWord1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                wordClicked(1);
            }
        });

        buttonWord2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                wordClicked(2);
            }
        });

        buttonWord3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                wordClicked(3);
            }
        });

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);

    } //End of protected void onCreate

    private void initialiseData(String drillData){
        try{
            params = new JSONObject(drillData);
            wordSets = params.getJSONArray("sets");
            currentSet = 1;
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
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void nextSet(){
        try{

            JSONArray words = wordSets.getJSONObject(currentSet - 1).getJSONArray("words");
            buttonWord1.setImageResource(words.getJSONObject(0).getInt("word"));
            if (words.getJSONObject(0).getInt("correct") == 1)
                correctWord = 1;
            buttonWord2.setImageResource(words.getJSONObject(1).getInt("word"));
            if (words.getJSONObject(1).getInt("correct") == 1)
                correctWord = 2;
            buttonWord3.setImageResource(words.getJSONObject(2).getInt("word"));
            if (words.getJSONObject(2).getInt("correct") == 1)
                correctWord = 3;
            setHandler.postDelayed(saySound,500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void wordClicked(int word){
        try{
            if (word == correctWord){
                //playSound(ResourceSelector.getPositiveAffirmationSound(this));
                switch (currentSet) {
                    case 1:
                        //reward1.setImageResource(R.drawable.rewardball1colour);
                        break;
                    case 2:
                        //reward2.setImageResource(R.drawable.rewardball1colour);
                        break;
                    case 3:
                        //reward3.setImageResource(R.drawable.rewardball1colour);
                        break;
                    case 4:
                        //reward4.setImageResource(R.drawable.rewardball1colour);
                        break;
                    case 5:
                        //reward5.setImageResource(R.drawable.rewardball1colour);
                        break;
                }
                currentSet++;
                if (currentSet < 6)
                    setHandler.postDelayed(playNextSet,200);
                else
                   startConcluding();

            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(this));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void countDownTracker()
    {
        if (time > 0) {
            time--;
            timeView.setText(String.valueOf(time));
            timeHandler.postDelayed (countdown, 1000);// wait for 1 seconds to start activity
        }
        else{
            startConcluding();
        }
    }

    public void startConcluding(){
        timeHandler.removeCallbacks(countdown);
        try {
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
        try {
            int sound = params.getInt("no_sound");
            switch (currentSet - 1) {
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
                    setHandler.postDelayed(finishRunnable, 200);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    Runnable countdown = new Runnable() {
        @Override
        public void run() {
            countDownTracker();
        }
    };

    Runnable playNextSet = new Runnable() {
        @Override
        public void run() {
            nextSet();
        }
    };


    Runnable saySound = new Runnable() {
        @Override
        public void run() {
            try {
                int sound = wordSets.getJSONObject(currentSet - 1).getInt("sound");
                playSound(sound);
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
        if (mp != null){
            mp.release();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }
}
