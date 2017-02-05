package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillElevenActivity extends AppCompatActivity {
    private ImageButton ImageButtonWord1;
    private ImageButton ImageButtonWord2;
    private ImageButton ImageButtonWord3;
    private ImageButton ImageButtonWord4;
    private ImageButton ImageButtonWord5;
    private ImageButton ImageButtonWord6;
    private ImageButton ImageButtonWord7;
    private ImageButton ImageButtonWord8;
    private ImageButton ImageButtonWord9;
    private ImageButton ImageButtonWord10;
    private JSONArray words;
    private int correctSets;
    private int[] assignments;
    private int currentlyOpen;
    private int[] openPair;
    private boolean[] cardState;
    private int startPair;
    private MediaPlayer mp;
    Handler handler;
    private JSONObject allData;

    private boolean gameStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_eleven);
        ImageButtonWord1 = (ImageButton)findViewById(R.id.button_word1);
        ImageButtonWord2 = (ImageButton)findViewById(R.id.button_word2);
        ImageButtonWord3 = (ImageButton)findViewById(R.id.button_word3);
        ImageButtonWord4 = (ImageButton)findViewById(R.id.button_word4);
        ImageButtonWord5 = (ImageButton)findViewById(R.id.button_word5);
        ImageButtonWord6 = (ImageButton)findViewById(R.id.button_word6);
        ImageButtonWord7 = (ImageButton)findViewById(R.id.button_word7);
        ImageButtonWord8 = (ImageButton)findViewById(R.id.button_word8);
        ImageButtonWord9 = (ImageButton)findViewById(R.id.button_word9);
        ImageButtonWord10 = (ImageButton)findViewById(R.id.button_word10);

        /*
        ImageButtonWord1.setAlpha(0f);
        ImageButtonWord2.setAlpha(0f);
        ImageButtonWord3.setAlpha(0f);
        ImageButtonWord4.setAlpha(0f);
        ImageButtonWord5.setAlpha(0f);
        ImageButtonWord6.setAlpha(0f);
        ImageButtonWord7.setAlpha(0f);
        ImageButtonWord8.setAlpha(0f);
        ImageButtonWord9.setAlpha(0f);
        ImageButtonWord10.setAlpha(0f);
        */

        gameStarted = false;

        initialiseCards();

        enableCards(false);

        String drillData = getIntent().getExtras().getString("data");

        handler = new Handler();
        initialiseData(drillData);

        try {
            String sound = allData.getString("monkey_wants_two");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp != null) {
                mp.release();
            }
            mp = new MediaPlayer();
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
                    completeIntro();
                }
            });

            AlphaAnimation animation1 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation2 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation3 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation4 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation5 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation6 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation7 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation8 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation9 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation10 = new AlphaAnimation(0f, 1.0f);

            animation1.setDuration(1100);
            animation2.setDuration(1100);
            animation3.setDuration(1100);
            animation4.setDuration(1100);
            animation5.setDuration(1100);
            animation6.setDuration(1100);
            animation7.setDuration(1100);
            animation8.setDuration(1100);
            animation9.setDuration(1100);
            animation10.setDuration(1100);

            animation1.setStartOffset(100);
            animation2.setStartOffset(200);
            animation3.setStartOffset(300);
            animation4.setStartOffset(400);
            animation5.setStartOffset(500);
            animation6.setStartOffset(600);
            animation7.setStartOffset(700);
            animation8.setStartOffset(800);
            animation9.setStartOffset(900);
            animation10.setStartOffset(1000);

            animation1.setFillAfter(true);
            animation2.setFillAfter(true);
            animation3.setFillAfter(true);
            animation4.setFillAfter(true);
            animation5.setFillAfter(true);
            animation6.setFillAfter(true);
            animation7.setFillAfter(true);
            animation8.setFillAfter(true);
            animation9.setFillAfter(true);
            animation10.setFillAfter(true);

            ImageButtonWord1.startAnimation(animation1);
            ImageButtonWord2.startAnimation(animation2);
            ImageButtonWord3.startAnimation(animation3);
            ImageButtonWord4.startAnimation(animation4);
            ImageButtonWord5.startAnimation(animation5);
            ImageButtonWord6.startAnimation(animation6);
            ImageButtonWord7.startAnimation(animation7);
            ImageButtonWord8.startAnimation(animation8);
            ImageButtonWord9.startAnimation(animation9);
            ImageButtonWord10.startAnimation(animation10);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableCards(true);
                }
            }, 500);

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

    private void completeIntro(){
        try {
            String sound = allData.getString("can_you_match");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
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
                    gameStarted = true;
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

    private void initialiseCards(){
        ImageButtonWord1.setImageResource(0);
        ImageButtonWord1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(1);
                    }
                }
        );
        ImageButtonWord2.setImageResource(0);
        ImageButtonWord2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(2);
                    }
                }
        );
        ImageButtonWord3.setImageResource(0);
        ImageButtonWord3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(3);
                    }
                }
        );
        ImageButtonWord4.setImageResource(0);
        ImageButtonWord4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(4);
                    }
                }
        );
        ImageButtonWord5.setImageResource(0);
        ImageButtonWord5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(5);
                    }
                }
        );
        ImageButtonWord6.setImageResource(0);
        ImageButtonWord6.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(6);
                    }
                }
        );
        ImageButtonWord7.setImageResource(0);
        ImageButtonWord7.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(7);
                    }
                }
        );
        ImageButtonWord8.setImageResource(0);
        ImageButtonWord8.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(8);
                    }
                }
        );
        ImageButtonWord9.setImageResource(0);
        ImageButtonWord9.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(9);
                    }
                }
        );
        ImageButtonWord10.setImageResource(0);
        ImageButtonWord10.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnCard(10);
                    }
                }
        );
    }

    public void enableCards(boolean enable) {
        ImageButtonWord1.setEnabled(enable);
        ImageButtonWord2.setEnabled(enable);
        ImageButtonWord3.setEnabled(enable);
        ImageButtonWord4.setEnabled(enable);
        ImageButtonWord5.setEnabled(enable);
        ImageButtonWord6.setEnabled(enable);
        ImageButtonWord7.setEnabled(enable);
        ImageButtonWord8.setEnabled(enable);
        ImageButtonWord9.setEnabled(enable);
        ImageButtonWord10.setEnabled(enable);
    }

    private void initialiseData(String drillData){
        try{
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            currentlyOpen = 0;
            assignments = new int[10];
            Arrays.fill(assignments,-1);
            Random rand = new Random();
            for (int l = 1;  l < 3; l++){
                for (int i = 0; i < 5; i++) {
                    int word = rand.nextInt(10);
                    if (assignments[word] == -1) {
                        assignments[word] = i;
                    } else {
                        int j = 9;
                        boolean assigned = false;
                        while (!assigned  && j > - 1) {
                            if (assignments[j] == -1) {
                                assignments[j] = i;
                                assigned = true;
                            }
                            j--;
                        }
                    }
                }
            }
            correctSets = 0;
            startPair = 0;
            openPair = new int[2];
            Arrays.fill(openPair,0);
            cardState = new boolean[10];
            Arrays.fill(cardState,false);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private ImageButton getCard(int card){
        ImageButton ImageButton = null;
        switch (card){
            case 1:
                ImageButton = ImageButtonWord1;
                break;
            case 2:
                ImageButton = ImageButtonWord2;
                break;
            case 3:
                ImageButton = ImageButtonWord3;
                break;
            case 4:
                ImageButton = ImageButtonWord4;
                break;
            case 5:
                ImageButton = ImageButtonWord5;
                break;
            case 6:
                ImageButton = ImageButtonWord6;
                break;
            case 7:
                ImageButton = ImageButtonWord7;
                break;
            case 8:
                ImageButton = ImageButtonWord8;
                break;
            case 9:
                ImageButton = ImageButtonWord9;
                break;
            case 10:
                ImageButton = ImageButtonWord10;
                break;
        }
        return ImageButton;
    }

    private void turnCard(int card){
        if (gameStarted) {
            try {
                if (!cardState[card - 1]) {
                    ImageButton ImageButton = getCard(card);
                    processCard(card, ImageButton);
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

    private void processCard(int card, ImageButton button ){
        try {
            if (startPair < 2) {
                cardState[card - 1] = true;
                openPair[startPair] = card;
                startPair++;
                String sound = words.getJSONObject(assignments[card - 1]).getString("sound");
                int image = words.getJSONObject(assignments[card - 1]).getInt("image");
                button.setBackgroundResource(R.drawable.cardsinglesmlback);
                button.setImageResource(image);

                playThisSound(sound);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void playThisSound(String sound){
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
                    if (startPair == 2) {
                        handler.postDelayed(isCorrectPair, 500);
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

    /* private void playThisSound(int soundid){
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
                    if (startPair == 2)
                        handler.postDelayed(isCorrectPair, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    } */

    private void playSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void playSoundAndEnd(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
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
                    mp.release();
                    finish();
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
    private void resetCard(ImageButton card){
        card.setBackgroundResource(R.drawable.cardsinglesml);
        card.setImageResource(0);
    }

    private void hideCard(ImageButton card){
        card.setImageResource(0);
        card.setBackgroundResource(0);
        card.setVisibility(View.INVISIBLE);
        card.setEnabled(false);
    }

    private void reward(){
        switch (correctSets) {
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
    }

    Runnable isCorrectPair = new Runnable() {
        @Override
        public void run() {
            if (assignments[openPair[0] - 1] == assignments[openPair[1] - 1]){
                ImageButton card = getCard(openPair[0]);
                hideCard(card);
                card = getCard(openPair[1]);
                hideCard(card);
                correctSets ++;
                reward();
                if (correctSets < 5)
                    playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                else
                    playSoundAndEnd(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
            }
            else{
                ImageButton card = getCard(openPair[0]);
                resetCard(card);
                card = getCard(openPair[1]);
                resetCard(card);
                cardState[openPair[0] - 1] = false;
                cardState[openPair[1] - 1] = false;
                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
            Arrays.fill(openPair,0);
            startPair = 0;
            handler.removeCallbacks(this);
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
