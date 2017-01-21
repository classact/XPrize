package classact.com.xprize.activity.drill.math;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndTwoActivity extends AppCompatActivity {
    private ImageView largeShapeOne;
    private ImageView smallShapeOne;
    private ImageView largeShapeTwo;
    private ImageView smallShapeTwo;
    private MediaPlayer mp;
    private JSONObject allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_two);
        largeShapeOne = (ImageView)findViewById(R.id.big_shape_one);
        largeShapeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("large_object_one");
            }
        });
        smallShapeOne = (ImageView)findViewById(R.id.small_shape_one);
        smallShapeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("small_object_one");
            }
        });
        largeShapeTwo = (ImageView)findViewById(R.id.big_shape_two);
        largeShapeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("large_object_two");
            }
        });
        smallShapeTwo = (ImageView)findViewById(R.id.small_shape_two);
        smallShapeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("small_object_two");
            }
        });
        initialise();
    }

    private void objectClicked(String touchedObject){
        try{
            String objectToTouch = allData.getString("object_to_touch");
            if (objectToTouch.equalsIgnoreCase(touchedObject)){
                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                    public void run(){
                        finish();
                    }
                },500);
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mp.reset();
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
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            largeShapeOne.setImageResource(allData.getInt("object_one"));
            smallShapeOne.setImageResource(allData.getInt("object_one"));
            largeShapeTwo.setImageResource(allData.getInt("object_two"));
            smallShapeTwo.setImageResource(allData.getInt("object_two"));
            int sound = allData.getInt("lets_look_at_shapes");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayTheseArea();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTheseArea(){
        try {
            int sound = allData.getInt("these_are_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectsOne();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void sayObjectsOne(){
        try {
            int sound = allData.getInt("objects_one_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayAnd();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }
    private void sayAnd(){
        try {
            int sound = allData.getInt("and_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectsTwo();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectsTwo(){
        try {
            int sound = allData.getInt("objects_two_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayRepeat();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void sayRepeat(){
        try {
            int sound = allData.getInt("repeat_afterme_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectsOneAgain();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectsOneAgain(){
        try {
            int sound = allData.getInt("objects_one_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayAndAgain();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }
    private void sayAndAgain(){
        try {
            int sound = allData.getInt("and_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectsTwoAgain();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectsTwoAgain(){
        try {
            int sound = allData.getInt("objects_two_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayTouch();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTouch(){
        try{
            int sound = allData.getInt("touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectToTouch();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectToTouch(){
        try {
            int sound = allData.getInt("object_to_touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }
}
