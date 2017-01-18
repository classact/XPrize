package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.view.SegmetedWritingView;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillSevenActivity extends AppCompatActivity {
    //private SegmetedWritingView segmentWritingView;
    private JSONArray data;
    private LinearLayout writingContainer;
    private MediaPlayer mp;
    private int[] wordSounds ;
    private Handler handler;
    private int currentSound = 0;
    private int [] pictures;
    private int correctItem;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private int currentTripple = 0;
    private JSONObject params;
    private ArrayList<JSONObject> currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_seven);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(1);
                    }
                }
        );
        item2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(2);
                    }
                }
        );
        item3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(3);
                    }
                }
        );
        writingContainer = (LinearLayout)findViewById(R.id.writing_container);
        String drillData = getIntent().getExtras().getString("data");
        handler = new Handler(Looper.getMainLooper());
        initialiseData(drillData);
    }

    private void initialiseData(String drillData){
        try{
            params = new JSONObject(drillData);
           // getWindow().getDecorView().getRootView().setBackgroundResource(params.getInt("background"));
            data = params.getJSONArray("words");
            showTripple();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void showTripple(){
        try {
            //segmentWritingView = new SegmetedWritingView(this,R.drawable.backgroundwhite);
            //writingContainer.removeAllViews();
            //
            for (int x = 0 ; x < writingContainer.getChildCount(); x++){
                writingContainer.getChildAt(x).setVisibility(View.INVISIBLE);
            }
            currentWord = new ArrayList();
            JSONArray ourWord = data.getJSONObject(currentTripple).getJSONArray("segmeted_word_spelling");
            for (int i = 0; i < ourWord.length(); i++)
                currentWord.add(ourWord.getJSONObject(i));

            JSONArray array = data.getJSONObject(currentTripple).getJSONArray("segmeted_word_sounds");
            wordSounds = new int[array.length()];
            for (int i = 0; i < array.length(); i++) {
                wordSounds[i] = array.getJSONObject(i).getInt("sound");
            }
            int correct_picture = 0;
            JSONArray pictures = data.getJSONObject(currentTripple).getJSONArray("pictures");
            for(int i = 0; i < pictures.length();i++) {
                if (i == 0) {
                    item1.setImageResource(pictures.getJSONObject(i).getInt("picture"));
                    if (pictures.getJSONObject(i).getInt("correct") == 1) {
                        correctItem = 1;
                        correct_picture = pictures.getJSONObject(i).getInt("picture");
                    }
                }
                else if (i == 1) {
                    item2.setImageResource(pictures.getJSONObject(i).getInt("picture"));
                    if (pictures.getJSONObject(i).getInt("correct") == 1) {
                        correctItem = 2;
                        correct_picture = pictures.getJSONObject(i).getInt("picture");
                    }
                }
                else{
                    item3.setImageResource(pictures.getJSONObject(i).getInt("picture"));
                    if (pictures.getJSONObject(i).getInt("correct") == 1) {
                        correctItem = 3;
                        correct_picture = pictures.getJSONObject(i).getInt("picture");
                    }
                }
            }
            //segmentWritingView.setWord(word,correct_picture);
            currentSound = 0;
            if (mp ==null) {
                int sound = params.getInt("listen_to_word_and_touch");
                mp = MediaPlayer.create(this, sound);
            }
            else{
                int sound = params.getInt("listen_to_word_and_touch");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(this, myUri);
                mp.prepare();
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayWordSlowly();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void sayWordSlowly(){
        try{
            int sound = data.getJSONObject(currentTripple).getInt("segmeted_word_slow_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void playSound(int sound){
        if (sound > 0) {
            try {
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(this, myUri);
                mp.prepare();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        try {
                            if (currentSound <= currentWord.size()) {
                                if (currentSound > 1) {
                                    ImageView previousObject = (ImageView) writingContainer.getChildAt(currentSound - 2);
                                    previousObject.setImageResource(currentWord.get(currentSound - 2).getInt("black"));
                                }
                                ImageView currentObject = (ImageView) writingContainer.getChildAt(currentSound-1);
                                currentObject.setImageResource(currentWord.get(currentSound-1).getInt("red"));
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                            finish();
                        }
                        handler.postDelayed(sayAndShow, 100);
                    }
                });
                mp.start();
            }
            catch (Exception ex){
                ex.printStackTrace();
                finish();
            }
        }
        else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentTripple++;
                    if (currentTripple < data.length())
                        showTripple();
                    else
                        finish();
                }
            },50);
        }

    }

    Runnable sayAndShow = new Runnable() {
        @Override
        public void run() {
            if (currentSound < wordSounds.length){
                playSound(wordSounds[currentSound]);
                currentSound =  currentSound + 1;
            }
            else if (currentSound == wordSounds.length){
                playSound(0);
                currentSound = -1;
            }
        }
    };

    private void playThisSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception  ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void showWord(){
        try{
            int x = 0;
            for(JSONObject obj : currentWord){
                ((ImageView)writingContainer.getChildAt(x)).setVisibility(View.VISIBLE);
                ((ImageView)writingContainer.getChildAt(x)).setImageResource(obj.getInt("black"));
                x++;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playAffirmationSound(){
        try {
            int soundid = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    //writingContainer.addView(segmentWritingView);
                    showWord();
                    handler.postDelayed(sayAndShow, 50);
                }
            });
        }
        catch(Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void clickedItem(int item){
        try {
            if (item == correctItem) {
                playAffirmationSound();

            }
            else {
                playThisSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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

