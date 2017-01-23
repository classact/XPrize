package classact.com.xprize.activity.drill.sound;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.view.WriteView;

public class SoundDrillFourteenActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;
    private ImageView item9;
    private ImageView receptable1;
    private ImageView receptable2;
    private ImageView receptable3;
    private ImageView receptable4;
    private ImageView receptable5;
    private ImageView receptable6;
    private ImageView receptable7;
    private ImageView receptable8;
    private ImageView receptable9;
    private LinearLayout writingContainer;
    private LinearLayout displayContainer;
    private WriteView writingView;
    private JSONArray words;
    private int currentWord;
    private MediaPlayer mp;
    private int currentTries;
    private JSONObject allData;

    String textholder;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_fourteen);
        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        item5 = (ImageView) findViewById(R.id.item5);
        item6 = (ImageView) findViewById(R.id.item6);
        item7 = (ImageView) findViewById(R.id.item7);
        item8 = (ImageView) findViewById(R.id.item8);
        item9 = (ImageView) findViewById(R.id.item9);
        receptable1 = (ImageView) findViewById(R.id.loc1);
        receptable2 = (ImageView) findViewById(R.id.loc2);
        receptable3 = (ImageView) findViewById(R.id.loc3);
        receptable4 = (ImageView) findViewById(R.id.loc4);
        receptable5 = (ImageView) findViewById(R.id.loc5);
        receptable6 = (ImageView) findViewById(R.id.loc6);
        receptable7 = (ImageView) findViewById(R.id.loc7);
        receptable8 = (ImageView) findViewById(R.id.loc8);
        receptable9 = (ImageView) findViewById(R.id.loc9);
        writingContainer = (LinearLayout) findViewById(R.id.writing_canvas_container);
        displayContainer = (LinearLayout)findViewById(R.id.layout1);
        String drillData = getIntent().getExtras().getString("data");
        handler = new Handler();
        initialiseData(drillData);
    }

    private void initialiseData(String drillData) {
        try {
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            currentWord = 1;
            startDrill();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }


    private void startDrill(){
        try {
            loadWord();
            displayContainer.setVisibility(View.INVISIBLE);
            writingContainer.removeAllViews();
            writingView = new WriteView(this,0);
            writingContainer.addView(writingView);
            int sound = allData.getInt("write");
            if (mp == null)
                mp = MediaPlayer.create(this, sound);
            else{
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayWord();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayWord(){
        try {
            int sound =  words.getJSONObject(currentWord - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(showWordRunnable,4000);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable showWordRunnable = new Runnable() {
        @Override
        public void run() {
            showWord();
        }
    };

    private void loadWord(){
        try {
            currentTries = 0;

            JSONArray word = words.getJSONObject(currentWord - 1).getJSONArray("letters");
            item1.setImageResource(word.getInt(0));
            item1.setVisibility(View.VISIBLE);
            receptable1.setVisibility(View.VISIBLE);

            if (word.length() > 1) {
                item2.setImageResource(word.getInt(1));
                item2.setVisibility(View.VISIBLE);
                receptable2.setVisibility(View.VISIBLE);
            }
            else{
                item2.setVisibility(View.INVISIBLE);
                receptable2.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 2) {
                item3.setImageResource(word.getInt(2));
                item3.setVisibility(View.VISIBLE);
                receptable3.setVisibility(View.VISIBLE);
            }
            else{
                item3.setVisibility(View.INVISIBLE);
                receptable3.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 3) {
                item4.setImageResource(word.getInt(3));
                item4.setVisibility(View.VISIBLE);
                receptable4.setVisibility(View.VISIBLE);
            }
            else{
                item4.setVisibility(View.INVISIBLE);
                receptable4.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 4) {
                item5.setImageResource(word.getInt(4));
                item5.setVisibility(View.VISIBLE);
                receptable5.setVisibility(View.VISIBLE);
            }
            else{
                item5.setVisibility(View.INVISIBLE);
                receptable5.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 5) {
                item6.setImageResource(word.getInt(5));
                item6.setVisibility(View.VISIBLE);
                receptable6.setVisibility(View.VISIBLE);
            }
            else{
                item6.setVisibility(View.INVISIBLE);
                receptable6.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 6) {
                item7.setImageResource(word.getInt(6));
                item7.setVisibility(View.VISIBLE);
                receptable7.setVisibility(View.VISIBLE);
            }
            else{
                item7.setVisibility(View.INVISIBLE);
                receptable7.setVisibility(View.INVISIBLE);
            }


            if (word.length() > 7) {
                item8.setImageResource(word.getInt(7));
                item8.setVisibility(View.VISIBLE);
                receptable8.setVisibility(View.VISIBLE);
            }
            else{
                item8.setVisibility(View.INVISIBLE);
                receptable8.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 8) {
                item9.setImageResource(word.getInt(8));
                item9.setVisibility(View.VISIBLE);
                receptable9.setVisibility(View.VISIBLE);
            }
            else{
                item9.setVisibility(View.INVISIBLE);
                receptable9.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void showWord(){
        try {
            displayContainer.setVisibility(View.VISIBLE);
            int sound = allData.getInt("this_is");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    repeatWord();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void repeatWord(){
        try {
            int sound =  words.getJSONObject(currentWord - 1).getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(wereYouCorrectRunnable,100);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable wereYouCorrectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                int sound = allData.getInt("were_you_correct");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        handler.postDelayed(continueRunnable, 2000);
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
                finish();
            }
        }
    };

    public Runnable continueRunnable = new Runnable() {
        @Override
        public void run() {
            currentWord++;
            if (currentWord <= 5)
                startDrill();
            else
                finish();
        }
    };


    private void reward(){
        switch (currentWord) {
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

    private void rewardAndGoNext(){
        reward();
        playSound(R.raw.good_job);
    }

    private void playSound(int sound){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    currentWord++;
                    if (currentWord <= 5)
                        startDrill();
                    else
                        finish();
                }
            });
        }
         catch (Exception ex){
             ex.printStackTrace();
             finish();
         }
    }

    Runnable checkProgress = new Runnable() {
        @Override
        public void run() {
            currentTries ++;
            //checkWriting();
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
