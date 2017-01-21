package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTenActivity extends AppCompatActivity {
    private ImageButton buttonWord1;
    private ImageButton buttonWord2;
    private ImageButton buttonWord3;
    private ImageButton buttonWord4;
    private ImageButton buttonWord5;
    private ImageButton flashButton;
    private JSONArray words;
    private int currentWord;
    private MediaPlayer mp;
    private Handler handler;
    private int mode;
    private JSONObject[] objects;
    private JSONObject allData;
    private int correctWords = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_ten);
        buttonWord1 = (ImageButton)findViewById(R.id.button_word1);
        buttonWord2 = (ImageButton)findViewById(R.id.button_word2);
        buttonWord3 = (ImageButton)findViewById(R.id.button_word3);
        buttonWord4 = (ImageButton)findViewById(R.id.button_word4);
        buttonWord5 = (ImageButton)findViewById(R.id.button_word5);
        flashButton = (ImageButton)findViewById(R.id.flash_word);
        initialiseButtons();
        String drillData = getIntent().getExtras().getString("data");
        handler = new Handler();
        mode = 1;
        initialiseData(drillData);
    }

    private void initialiseData(String drillData) {
        try {
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            flashButton.setVisibility(View.INVISIBLE);
            int sound = allData.getInt("instructions");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentWord = 1;
                    flashButton.setVisibility(View.VISIBLE);
                    showWord();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }



    private void initialiseButtons(){
        buttonWord1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(1);
                    }
                }
        );
        buttonWord2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(2);
                    }
                }
        );
        buttonWord3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(3);
                    }
                }
        );
        buttonWord4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(4);
                    }
                }
        );
        buttonWord5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mode == 2)
                            checkIfWordCorrect(5);
                    }
                }
        );
    }

    private void showWord(){
        try{
            int word = words.getJSONObject(currentWord - 1).getInt("word");
            flashButton.setImageResource(word);
            int sound = words.getJSONObject(currentWord - 1).getInt("sound");
            playSoundAndShowNext(sound);

        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playSoundAndShowNext(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(showNextWord, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable showNextWord = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            currentWord++;
            if (currentWord <= 5) {
                showWord();
            }
            else{
                handler.removeCallbacks(this);
                currentWord = 1;
                mode = 2;
                handler.postDelayed(beginTouchWord,100);
            }
        }
    };

    private void playWordSound(){
        try{
            int sound = objects[currentWord - 1].getInt("sound");
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
            ex.printStackTrace();;
            finish();
        }
    }

    private void sayTouchWord(){
        try {
            int sound = allData.getInt("touch");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playWordSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable beginTouchWord = new Runnable() {
        @Override
        public void run() {
            try {
                flashButton.setVisibility(View.INVISIBLE);
                int word;
                Random rand = new Random();
                objects = new JSONObject[]{null,null,null,null,null};
                for(int i = 0; i < 5; i++) {
                    int currentWord = rand.nextInt(5);
                    if (objects[currentWord] == null){
                        objects[currentWord] = words.getJSONObject(i);
                    }
                    else{
                        boolean done = false;
                        int k = 0;
                        while(!done){
                            if (objects[k] == null){
                                currentWord = k;
                                objects[k] = words.getJSONObject(i);
                                done = true;
                            }
                            k++;
                            if (k == 5)
                                done = true;
                        }
                    }
                    word = objects[currentWord].getInt("word");
                    switch (currentWord) {
                        case 0:
                            buttonWord1.setImageResource(word);
                            break;
                        case 1:
                            buttonWord2.setImageResource(word);
                            break;
                        case 2:
                            buttonWord3.setImageResource(word);
                            break;
                        case 3:
                            buttonWord4.setImageResource(word);
                            break;
                        case 4:
                            buttonWord5.setImageResource(word);
                            break;
                    }
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                finish();
            }
            buttonWord1.setVisibility(View.VISIBLE);
            buttonWord2.setVisibility(View.VISIBLE);
            buttonWord3.setVisibility(View.VISIBLE);
            buttonWord4.setVisibility(View.VISIBLE);
            buttonWord5.setVisibility(View.VISIBLE);
            Random rand = new Random();
            currentWord = rand.nextInt(5);
            if (currentWord == 0)
                currentWord = 5;
            sayTouchWord();
        }
    };

    public Runnable sayWord = new Runnable() {
        @Override
        public void run() {
            playWordSound();
        }
    };

    private void reward(int currentWord){
        switch (currentWord){
            case 1:
                buttonWord1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                buttonWord2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                buttonWord3.setVisibility(View.INVISIBLE);
                break;
            case 4:
                buttonWord4.setVisibility(View.INVISIBLE);
                break;
            case 5:
                buttonWord5.setVisibility(View.INVISIBLE);
                break;
        }
        objects[currentWord - 1] = null;
    }

    private void playThisSound(int soundid){
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
                    Random rand = new Random();
                    currentWord = rand.nextInt(5);
                    if (currentWord == 0)
                        currentWord = 5;
                    if (objects[currentWord - 1] == null) {
                        currentWord = -1;
                        int k = 0;
                        boolean done = false;
                        while (!done) {
                            if (objects[k] != null) {
                                currentWord = k + 1;
                            }
                            k++;
                            if (k == 5)
                                done = true;
                        }
                    }
                    if (currentWord > -1) {
                        sayTouchWord();
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

    public void checkIfWordCorrect(int word){
        if (word == currentWord){
            reward(word);
            playThisSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
            correctWords++;
            if (correctWords == 5)
                finish();
        }
        else{
            try {
                int soundid = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
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
    }

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
