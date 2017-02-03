package classact.com.xprize.activity.drill.sound;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import classact.com.xprize.R;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.view.PathAnimationView;
import classact.com.xprize.view.PathCoordinate;
import classact.com.xprize.view.WriteView;

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
            String sound = "";
            if (repeat == 1) {
                sound = drillData.getString("lets_learn_how_to_write_upper");
            } else {
                sound = drillData.getString("lets_learn_how_to_write_lower");
            }
            animationView = new PathAnimationView(this);
            animationView.setAlpha(0.6f);
            animationView.setPaths(getPathArray());
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
                    playLetterSound();
                }
            });
            mp.prepare();
        }
        catch(Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void playLetterSound(){
        try {
            String sound = drillData.getString("letter_sound");
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
                    prepareToWrite();
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }


    public void playWatch() {
        try {
            //watch first
            String sound = drillData.getString("watch");
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
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
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
            String sound = drillData.getString("now_you_write");
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
                    handler.postDelayed(checkDone,4000);
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
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
        try {
            if (writingView.didDraw()) {
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + ResourceSelector.getPositiveAffirmationSound(this));
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                mp.reset();
                mp.setDataSource(this, myUri);
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
                        completed();
                    }
                });
                mp.prepare();
            } else {
                if (numberOfChecks % 2 == 0) {
                    playYouTry();
                } else {
                    prepareToWrite();
                }
                numberOfChecks++;

            }
        } catch (IOException ioex) {
            System.err.println("SoundDrillEightActivity.checkIsDone() > IOException: " + ioex.getMessage());
            ioex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    public void completed(){
        if (repeat == 1) {
            repeat++;
            startDrill();
        } else {
            if (mp != null) {
                mp.release();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
        }
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
                    if (mp != null) {
                        mp.release();
                    }
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private  ArrayList<ArrayList<PathCoordinate>> getPathArray(){
        ArrayList<ArrayList<PathCoordinate>> pathsArray = new  ArrayList<ArrayList<PathCoordinate>>();
        try {
            for (int i = 0; i < paths.length(); i++) {
                ArrayList<PathCoordinate> path = new ArrayList<>();
                JSONObject obj = paths.getJSONObject(i);
                JSONArray array = obj.getJSONArray("path");
                for(int k = 0; k < array.length(); k++) {
                    PathCoordinate coordinate = new PathCoordinate((float)array.getJSONObject(k).getDouble("x"),(float)array.getJSONObject(k).getDouble("y"));
                    path.add(coordinate);
                }
                pathsArray.add(path);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
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
            if (mp != null) {
                mp.release();
            }
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