package classact.com.xprize.activity.sounddrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.view.PathAnimationView;
import classact.com.xprize.view.PathCoordinate;
import classact.com.xprize.view.WriteView;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillEightActivity extends AppCompatActivity implements PathAnimationView.AnimationDone{
    private RelativeLayout drawArea;
    private PathAnimationView animationView;
    private WriteView writingView;
    private JSONObject drillData;
    private JSONArray paths;
    private ImageView letter;
    private MediaPlayer mp;
    private Handler handler;
    private int numberOfChecks = 0;
    private int repeat = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_eight);
        letter = (ImageView)findViewById(R.id.item1);
        drawArea =(RelativeLayout) findViewById(R.id.draw_area);
        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        handler = new Handler(Looper.getMainLooper());
        startDrill();

    }

    private void startDrill(){
        try {
            drawArea.removeAllViews();
            drawArea.addView(letter);
            int item = drillData.getInt("big_letter");
            if (repeat == 2)
                item = drillData.getInt("small_letter");
            letter.setImageResource(item);
            getPathData();
            if (repeat == 1)
                mp = MediaPlayer.create(this,drillData.getInt("lets_learn_how_to_write_upper"));
            else {
                int soundid =  drillData.getInt("lets_learn_how_to_write_lower");;
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
                mp.setDataSource(this, myUri);
                mp.prepare();
            }
            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playLetterSound();
                }
            });
            mp.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void playLetterSound(){
        try {

            int letterSound = drillData.getInt("letter_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + letterSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    prepareToWrite();
                    //handler.postDelayed(writeRunnable,500);
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable writeRunnable = new Runnable(){
        @Override
        public void run() {
            prepareToWrite();
        }
    };

    public void prepareToWrite(){
        try{
            drawArea.removeAllViews();
            drawArea.addView(letter);
            //write the small letter
            playWatch();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    public void playWatch() {
        try {
            //watch first
            int sound = drillData.getInt("watch");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    animationView.setLayoutParams(drawArea.getLayoutParams());
                    drawArea.addView(animationView);
                    animationView.bringToFront();
                    //drawArea.bringToFront();
                    //animationView.bringToFront();
                    //drawArea.setZ(10);
                    //animationView.setZ(10);
                    animationView.animateThisPath();
                }
            });
            mp.start();
        } catch (Exception ex) {
            ex.printStackTrace();;
            finish();
        }
    }

    @Override
    public void onAnimationDone(){
        prepareWritingCanvas();
    }


    public void prepareWritingCanvas(){
        writingView = new WriteView(this,R.drawable.backgroundtrace1);
        writingView.setAlpha(0.6f);
        writingView.setLayoutParams(drawArea.getLayoutParams());
        drawArea.removeAllViews();
        drawArea.addView(letter);
        drawArea.addView(writingView);
        playYouTry();
    }

    public void playYouTry() {
        try {
            //now you try
            int sound = drillData.getInt("now_you_write");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(checkDone,4000);
                }
            });
            mp.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    public Runnable checkDone = new Runnable() {
        @Override
        public void run() {
            checkIsDone();
        }
    };

    public void checkIsDone(){
        //String letter = ImageRecognition.detectText(writingView.getBitMap(),this);
        //new ImageSaver(this).
        //        setFileName("myImage.png").
        //        setDirectoryName("images").
        //        save(writingView.getBitMap());
        if (writingView.didDraw()){
            mp = MediaPlayer.create(this, ResourceSelector.getPositiveAffirmationSound(this));
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    completed();
                }
            });
        }
        else {
            if (numberOfChecks % 2 == 0) {
                playYouTry();
            }
            else{
                prepareToWrite();
            }
            numberOfChecks++;

        }

    }

    public void completed(){
        if (repeat == 1)
        {
            repeat++;
            startDrill();
        }
        else
            this.finish();
    }

    private void getPathData(){
        try {
            int letterPathFile = drillData.getInt("big_letter_path");
            if (repeat == 2)
                letterPathFile = drillData.getInt("small_letter_path");
            InputStream is = this.getResources().openRawResource(letterPathFile);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                try {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    paths = new JSONObject(result.toString()).getJSONArray("paths");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    finish();
                } finally {
                    reader.close();
                }
            } finally {
                is.close();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    //

    private  ArrayList<ArrayList<PathCoordinate>> getPathArray(){
        ArrayList<ArrayList<PathCoordinate>> pathsArray = new  ArrayList<ArrayList<PathCoordinate>>();
        try {
            for (int i = 0; i < paths.length(); i++) {
                ArrayList<PathCoordinate> path = new ArrayList<PathCoordinate>();
                JSONObject obj = paths.getJSONObject(i);
                JSONArray array = obj.getJSONArray("path");
                for(int k = 0; k < array.length(); k++){
                    PathCoordinate coordinate = new PathCoordinate((float)array.getJSONObject(k).getDouble("x"),(float)array.getJSONObject(k).getDouble("y"));
                    path.add(coordinate);
                }
                pathsArray.add(path);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }

        return pathsArray;
    }

    private void initialiseData(String data){
        try {
            drillData = new JSONObject(data);
            //getWindow().getDecorView().getRootView().setBackgroundResource(drillData.getInt("background"));
            //int item = drillData.getInt("letter");
            //letter.setImageResource(item);
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

   // @Override
   // public void onBackPressed() {
   // }
}

